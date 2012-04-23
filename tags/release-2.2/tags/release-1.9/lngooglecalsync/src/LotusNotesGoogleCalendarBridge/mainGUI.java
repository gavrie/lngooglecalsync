// Copyright 2009 Shin Sterneck, Dean Hill
//
// This file is part of the Lotus Notes to Google Calendar Synchronizer application.
//
//    This application is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This application is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this application.  If not, see <http://www.gnu.org/licenses/>.


package LotusNotesGoogleCalendarBridge;

import LotusNotesGoogleCalendarBridge.ProxyModule.ProxyConfigBean;
import LotusNotesGoogleCalendarBridge.LotusNotesService.LotusNotesExport;
import LotusNotesGoogleCalendarBridge.LotusNotesService.NotesCalendarEntry;
import LotusNotesGoogleCalendarBridge.GoogleService.GoogleImport;

import com.google.gdata.data.calendar.CalendarEventEntry;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Cursor;
import java.math.BigDecimal;
import javax.swing.*;
import preferences.ConfigurationBean;

public class mainGUI extends javax.swing.JFrame {

    public mainGUI() {
        preInitComponents();
        initComponents();

        // Initialize proxy bean
        proxy = new ProxyConfigBean();

        // Load configuration bean
        confBean = new ConfigurationBean();
        confBean.readConfig();

        loadSettings();

        validate();
        // Check whether the loaded configuration meets our requirements to sync.
        validateSettings();

        setDateRange();
    }

    private void preInitComponents() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Logger.getLogger(mainGUI.class.getName()).log(Level.SEVERE, "There was an error setting the window's look-and-feel.", ex);
        }
    }

    public static void main(String args[]) {
        if (args.length == 0) {
              SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                      new mainGUI().setVisible(true);
                  }
              });
        } else if (args[0].equals("-silent")) {
            // Run in "silent" command-line mode
            // Use all configuration settings from the property file
            new mainGUI().runCommandLine();
            System.exit(exitCode);
        } else {
            System.out.println("Usage: mainGUI <options>\n\tIf no options are specified, then the application starts in GUI mode.\n\t-silent  Performs synchronization with existing settings in non-GUI mode.");
            System.exit(EXIT_INVALID_PARM);
        }
    }
    
    /**
     * Runs the synchronization in silent mode using existing configuration settings.
     */
    public void runCommandLine(){
        try {
            // Make sure the GUI is hidden
            this.setVisible(false);
            isSilentMode = true;
            doSync();
        } catch (Exception ex) {
            exitCode = EXIT_EXCEPTION;
            System.out.println("\nGeneral problem: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    /**
     * Perform synchronization independent of GUI or non-GUI mode.
     */
    public void doSync() throws Exception {
        long startTime = System.currentTimeMillis();

        proxy.deactivateNow();

        statusClear();

        if (confBean.getSyncOnStartup() && jButton_Synchronize.isEnabled())
            statusAppendLine("Automatic sync-on-startup is enabled. Starting sync.");
        else
            statusAppendLine("Starting sync");
        
        if (jCheckBox_DiagnosticMode.isSelected()) {
            // Don't echo the commented out values for privacy reasons
            statusAppendLineDiag("Application Version: " + appVersion);
            statusAppendLineDiag("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"));
            statusAppendLineDiag("Java: " + System.getProperty("java.version") + " " + System.getProperty("java.vendor"));
            //statusAppendLineDiag("Lotus Username: " + jTextField_LotusNotesUsername.getText());
            statusAppendLineDiag("Local Server: " + jCheckBox_LotusNotesServerIsLocal.isSelected());
            //statusAppendLineDiag("Server: " + jTextField_LotusNotesServer.getText());
            //statusAppendLineDiag("Mail File: " + jTextField_LotusNotesMailFile.getText());
            //statusAppendLineDiag("Google Email: " + jTextField_GoogleUsername.getText());
            statusAppendLineDiag("Use Proxy: " + jCheckBox_enableProxy.isSelected());
            statusAppendLineDiag("Use SSL: " + jCheckBox_GoogleSSL.isSelected());
            statusAppendLineDiag("Sync Description: " + jCheckBox_SyncDescription.isSelected());
            statusAppendLineDiag("Sync Alarms: " + jCheckBox_SyncAlarms.isSelected());
            statusAppendLineDiag("Sync Days In Future: " + jTextField_SyncDaysInFuture.getText());
            statusAppendLineDiag("Sync Days In Past: " + jTextField_SyncDaysInPast.getText());
            statusAppendLineDiag("Java Classpath: " + System.getProperty("java.class.path"));
            statusAppendLineDiag("Java Library Path: " + System.getProperty("java.library.path"));
        }

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        statusAppendLine("Date range: " + df.format(minStartDate) + " thru " + df.format(maxEndDate));

        // === Get the Lotus Notes calendar data
        statusAppendStart("Getting Lotus Notes calendar entries");
        LotusNotesExport lotusNotesService = new LotusNotesExport();

        lotusNotesService.setRequiresAuth(true);
        lotusNotesService.setCredentials(jTextField_LotusNotesUsername.getText(), new String(jPasswordField_LotusNotesPassword.getPassword()));
        String lnServer = jTextField_LotusNotesServer.getText();
        if (jCheckBox_LotusNotesServerIsLocal.isSelected())
            lnServer = "";
        lotusNotesService.setServer(lnServer);
        lotusNotesService.setMailFile(jTextField_LotusNotesMailFile.getText());
        lotusNotesService.setMinStartDate(minStartDate);
        lotusNotesService.setMaxEndDate(maxEndDate);
        lotusNotesService.setDiagnosticMode(jCheckBox_DiagnosticMode.isSelected());
        
        ArrayList<NotesCalendarEntry> lotusCalEntries = lotusNotesService.getCalendarEntries();
        statusAppendFinished();
        statusAppendLine("  " + lotusCalEntries.size() + " entries found within date range");
        if (jCheckBox_DiagnosticMode.isSelected())
            statusAppendLineDiag("Lotus Version: " + lotusNotesService.getNotesVersion());

//if (true) {statusAppendLineDiag("DEBUG: Lotus Notes tasks finished. Stopping sync."); return;}

        // === Copy the Lotus Notes data to Google calendar

        if (jCheckBox_enableProxy.isSelected()) {
            if (! jTextField_proxyUsername.getText().isEmpty()) {
                proxy.enableProxyAuthentication(true);
                proxy.setProxyUser(jTextField_proxyUsername.getText());
                proxy.setProxyPassword(new String(jPasswordField_proxyPassword.getPassword()));
            }
            
            proxy.activateNow();
        }




        // check whether the user has deselected to use SSL when connecting to google (this is not recommended)
        boolean GoogleConnectUsingSSL = jCheckBox_GoogleSSL.isSelected();
        statusAppendStart("Logging into Google");
        GoogleImport googleService = new GoogleImport(jTextField_GoogleUsername.getText(), new String(jPasswordField_GooglePassword.getPassword()), jTextField_DestinationCalendarName.getText(), GoogleConnectUsingSSL);
        statusAppendFinished();
//if (true) {statusAppendLineDiag("DEBUG: Done logging into Google. Stopping sync."); return;}

        googleService.setDiagnosticMode(jCheckBox_DiagnosticMode.isSelected());
        googleService.setSyncDescription(jCheckBox_SyncDescription.isSelected());
        googleService.setSyncAlarms(jCheckBox_SyncAlarms.isSelected());
        googleService.setSyncMeetingAttendees(jCheckBox_SyncMeetingAttendees.isSelected());

        statusAppendStart("Getting Google calendar entries");
        ArrayList<CalendarEventEntry> googleCalEntries = googleService.getCalendarEntries();
        statusAppendFinished();
        statusAppendLine("  " + googleCalEntries.size() + " entries found within date range");

        statusAppendStart("Comparing Lotus Notes and Google calendar entries");
        googleService.compareCalendarEntries(lotusCalEntries, googleCalEntries);
        statusAppendFinished();
        statusAppendLine("  " + lotusCalEntries.size() + " entries to create. " + googleCalEntries.size() + " entries to delete.");

//googleService.createSampleGEntry();
//if (true) {statusAppendLineDiag("DEBUG: Done comparing entries. Stopping sync."); return;}

        if (googleCalEntries.size() > 0) {
            statusAppendStart("Deleting old Google calendar entries");
            int deleteCount = googleService.deleteCalendarEntries(googleCalEntries);
            statusAppendFinished();
            statusAppendLine("  " + deleteCount + " entries deleted");
        }

        if (lotusCalEntries.size() > 0) {
            statusAppendStart("Creating new Google calendar entries");
            int createdCount = 0;
            createdCount = googleService.createCalendarEntries(lotusCalEntries);
            statusAppendFinished();
            statusAppendLine("  " + createdCount + " entries created");
        }

        long elapsedMillis = System.currentTimeMillis() - startTime;    
        BigDecimal elapsedSecs = new BigDecimal(elapsedMillis / 1000.0).setScale(1, BigDecimal.ROUND_HALF_UP);
        statusAppendLine("Finished sync (" + elapsedSecs + " s total)");            	
    }
   

    class SyncSwingWorker extends SwingWorker<Void, Void>
    {
        @Override
        protected Void doInBackground()
        {
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                doSync();
            } catch (Exception ex) {
                statusAppendException("There was an error synchronizing the calendars.", ex);
            }
            finally {
                setCursor(Cursor.getDefaultCursor());
            }
            
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton_Cancel = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jButton_Synchronize = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_Status = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jCheckBox_SyncOnStart = new javax.swing.JCheckBox();
        jCheckBox_DiagnosticMode = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        jCheckBox_SyncDescription = new javax.swing.JCheckBox();
        jCheckBox_SyncAlarms = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        jTextField_DestinationCalendarName = new javax.swing.JTextField();
        jCheckBox_SyncMeetingAttendees = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        jTextField_SyncDaysInPast = new javax.swing.JFormattedTextField();
        jTextField_SyncDaysInFuture = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jCheckBox_GoogleSSL = new javax.swing.JCheckBox();
        jPasswordField_GooglePassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField_GoogleUsername = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPasswordField_LotusNotesPassword = new javax.swing.JPasswordField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField_LotusNotesUsername = new javax.swing.JTextField();
        jTextField_LotusNotesMailFile = new javax.swing.JTextField();
        jTextField_LotusNotesServer = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jCheckBox_enableProxy = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jTextField_proxyIP = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField_proxyPort = new javax.swing.JTextField();
        jCheckBox_LotusNotesServerIsLocal = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        jTextField_proxyUsername = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPasswordField_GooglePassword1 = new javax.swing.JPasswordField();
        jPasswordField_proxyPassword = new javax.swing.JPasswordField();
        jLabel16 = new javax.swing.JLabel();
        jButton_Help = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lotus Notes to Google Calendar Synchronizer (LNGS)");
        setBackground(new java.awt.Color(254, 254, 254));
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jButton_Cancel.setMnemonic('x');
        jButton_Cancel.setText("Exit");
        jButton_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelActionPerformed(evt);
            }
        });

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(105, 88));

        jLabel10.setText("After filling in the Settings, click Synchronize to begin.");

        jButton_Synchronize.setMnemonic('y');
        jButton_Synchronize.setText("Synchronize");
        jButton_Synchronize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SynchronizeActionPerformed(evt);
            }
        });

        jTextArea_Status.setColumns(20);
        jTextArea_Status.setEditable(false);
        jTextArea_Status.setRows(5);
        jTextArea_Status.setText("Status messages display here.\n");
        jScrollPane1.setViewportView(jTextArea_Status);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 308, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 77, Short.MAX_VALUE)
                        .add(jButton_Synchronize)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel10)
                    .add(jButton_Synchronize))
                .add(18, 18, 18)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Perform Sync", jPanel2);

        jLabel11.setForeground(new java.awt.Color(51, 51, 255));
        jLabel11.setText("General Settings");

        jCheckBox_SyncOnStart.setText("Synchronize On Startup");
        jCheckBox_SyncOnStart.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncOnStart.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_SyncOnStart.setPreferredSize(new java.awt.Dimension(100, 23));

        jCheckBox_DiagnosticMode.setText("Diagnostic Mode");
        jCheckBox_DiagnosticMode.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_DiagnosticMode.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_DiagnosticMode.setPreferredSize(new java.awt.Dimension(100, 23));

        jLabel17.setForeground(new java.awt.Color(51, 51, 255));
        jLabel17.setText("Data To Sync");

        jCheckBox_SyncDescription.setText("Descriptions");
        jCheckBox_SyncDescription.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncDescription.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_SyncDescription.setPreferredSize(new java.awt.Dimension(100, 23));

        jCheckBox_SyncAlarms.setText("Alarms Become Google Reminders");
        jCheckBox_SyncAlarms.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncAlarms.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_SyncAlarms.setPreferredSize(new java.awt.Dimension(100, 23));

        jLabel20.setText("Destination Calendar Name");

        jTextField_DestinationCalendarName.setToolTipText("The calendar name is case sensitive, i.e. \"my cal\" is different then \"My Cal\".");
        jTextField_DestinationCalendarName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_DestinationCalendarNameFocusLost(evt);
            }
        });

        jCheckBox_SyncMeetingAttendees.setText("Attendees are Listed at Top of Description");
        jCheckBox_SyncMeetingAttendees.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncMeetingAttendees.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_SyncMeetingAttendees.setPreferredSize(new java.awt.Dimension(100, 23));

        jLabel21.setForeground(new java.awt.Color(51, 51, 255));
        jLabel21.setText("Sync Date Range");

        jTextField_SyncDaysInPast.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat(""))));
        jTextField_SyncDaysInPast.setText("7");
        jTextField_SyncDaysInPast.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_SyncDaysInPastFocusLost(evt);
            }
        });

        jTextField_SyncDaysInFuture.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat(""))));
        jTextField_SyncDaysInFuture.setText("60");
        jTextField_SyncDaysInFuture.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_SyncDaysInFutureFocusLost(evt);
            }
        });

        jLabel22.setText("Days in the Past");

        jLabel23.setText("Days in the Future");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(16, 16, 16)
                        .add(jLabel20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 135, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextField_DestinationCalendarName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 258, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 105, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCheckBox_SyncOnStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 179, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jCheckBox_DiagnosticMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel22, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .add(jLabel23, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTextField_SyncDaysInFuture, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTextField_SyncDaysInPast, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCheckBox_SyncDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jCheckBox_SyncMeetingAttendees, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 274, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jCheckBox_SyncAlarms, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 253, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jLabel17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField_DestinationCalendarName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel20))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBox_SyncOnStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox_DiagnosticMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1, 1, 1)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel22)
                    .add(jTextField_SyncDaysInPast, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel23)
                    .add(jTextField_SyncDaysInFuture, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(14, 14, 14)
                .add(jLabel17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox_SyncDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox_SyncMeetingAttendees, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox_SyncAlarms, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(159, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Sync Settings", jPanel3);

        jPanel1.setAutoscrolls(true);

        jCheckBox_GoogleSSL.setSelected(true);
        jCheckBox_GoogleSSL.setText("Connect to Google Using SSL/TLS (recommended)");

        jLabel4.setText("Password");

        jLabel3.setText("Email Address");

        jTextField_GoogleUsername.setText("user@google.com");

        jLabel5.setForeground(new java.awt.Color(51, 51, 255));
        jLabel5.setText("Google Settings");

        jLabel13.setText("Password");

        jLabel14.setText("Server");

        jLabel15.setText("Mail File");

        jLabel12.setText("Username");

        jTextField_LotusNotesServer.setToolTipText("Leave server name blank to access a local mail file.");

        jLabel6.setForeground(new java.awt.Color(51, 51, 255));
        jLabel6.setText("Lotus Notes Settings");

        jLabel9.setForeground(new java.awt.Color(51, 51, 255));
        jLabel9.setText("Network Settings");

        jCheckBox_enableProxy.setText("Use Proxy Server to Reach the Internet");
        jCheckBox_enableProxy.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox_enableProxyStateChanged(evt);
            }
        });

        jLabel8.setText("Server IP or Name");
        jLabel8.setPreferredSize(new java.awt.Dimension(97, 14));

        jTextField_proxyIP.setEnabled(false);

        jLabel7.setText("Port Number");
        jLabel7.setPreferredSize(new java.awt.Dimension(97, 14));

        jTextField_proxyPort.setEnabled(false);

        jCheckBox_LotusNotesServerIsLocal.setText("Local Server");
        jCheckBox_LotusNotesServerIsLocal.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_LotusNotesServerIsLocal.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_LotusNotesServerIsLocal.setPreferredSize(new java.awt.Dimension(100, 23));
        jCheckBox_LotusNotesServerIsLocal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox_LotusNotesServerIsLocalStateChanged(evt);
            }
        });

        jLabel18.setText("Username (optional)");

        jTextField_proxyUsername.setEnabled(false);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText("Password (optional)");
        jLabel19.setPreferredSize(new java.awt.Dimension(97, 14));

        jPasswordField_proxyPassword.setEnabled(false);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(jLabel15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .add(70, 70, 70)
                                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField_LotusNotesUsername, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPasswordField_LotusNotesPassword)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField_GoogleUsername, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE))
                                    .add(473, 473, 473)))
                            .add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(70, 70, 70)
                                .add(jPasswordField_GooglePassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(80, 80, 80)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                .add(jTextField_LotusNotesServer)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jCheckBox_LotusNotesServerIsLocal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jTextField_LotusNotesMailFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 388, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCheckBox_enableProxy)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(21, 21, 21)
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(jTextField_proxyIP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup()
                                            .add(jLabel19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                            .add(jPasswordField_proxyPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(jPanel1Layout.createSequentialGroup()
                                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(jLabel18))
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                .add(jTextField_proxyUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .add(jTextField_proxyPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                            .add(66, 66, 66)))))
                            .add(jCheckBox_GoogleSSL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 453, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(839, Short.MAX_VALUE)
                    .add(jPasswordField_GooglePassword1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(8, 8, 8)))
        );

        jPanel1Layout.linkSize(new java.awt.Component[] {jLabel12, jLabel13, jLabel14, jLabel15, jLabel3}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.linkSize(new java.awt.Component[] {jPasswordField_LotusNotesPassword, jTextField_LotusNotesMailFile}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.linkSize(new java.awt.Component[] {jPasswordField_GooglePassword, jTextField_GoogleUsername}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel14)
                    .add(jTextField_LotusNotesServer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jCheckBox_LotusNotesServerIsLocal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel15)
                    .add(jTextField_LotusNotesMailFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel12)
                    .add(jTextField_LotusNotesUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel13)
                    .add(jPasswordField_LotusNotesPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextField_GoogleUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPasswordField_GooglePassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBox_GoogleSSL)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBox_enableProxy)
                .add(1, 1, 1)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextField_proxyIP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextField_proxyPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel18)
                    .add(jTextField_proxyUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPasswordField_proxyPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                    .add(213, 213, 213)
                    .add(jPasswordField_GooglePassword1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(212, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Connection Settings", jPanel1);

        jLabel16.setForeground(new java.awt.Color(51, 51, 255));
        jLabel16.setText("This tool synchronizes your Lotus Notes calendar to your Google calendar.");

        jButton_Help.setMnemonic('x');
        jButton_Help.setText("Help");
        jButton_Help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_HelpActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 416, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 501, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(220, 220, 220)
                        .add(jButton_Cancel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jButton_Help)
                        .add(26, 26, 26))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel16)
                .add(17, 17, 17)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_Cancel)
                    .add(jButton_Help))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelActionPerformed
        formWindowClosed(null);
}//GEN-LAST:event_jButton_CancelActionPerformed

    private void jButton_SynchronizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SynchronizeActionPerformed
        new SyncSwingWorker().execute();
}//GEN-LAST:event_jButton_SynchronizeActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // Prepare the sync completed dialog message
        // (this is not nice, but will have to do for now)
        //syncCompletedDialog = new SyncCompletedDialog(new javax.swing.JFrame(), true);

        this.setTitle(this.getTitle() + " v" + appVersion);

        if (confBean.getSyncOnStartup() && jButton_Synchronize.isEnabled()) {
            new SyncSwingWorker().execute();
        }
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        try {
            if (saveSettingsOnExit)
                saveSettings();
        }
        catch (Exception ex) {
            statusAppendException("There was an error saving settings.", ex);
            // The next time the user clicks Exit, we will exit without saving.
            saveSettingsOnExit = false;
            return;
        }

        System.exit(EXIT_SUCCESS);
    }//GEN-LAST:event_formWindowClosed

    private void jCheckBox_enableProxyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox_enableProxyStateChanged
        jTextField_proxyIP.setEnabled(jCheckBox_enableProxy.isSelected());
        jTextField_proxyPort.setEnabled(jCheckBox_enableProxy.isSelected());
        jTextField_proxyUsername.setEnabled(jCheckBox_enableProxy.isSelected());
        jPasswordField_proxyPassword.setEnabled(jCheckBox_enableProxy.isSelected());
    }//GEN-LAST:event_jCheckBox_enableProxyStateChanged

    private void jCheckBox_LotusNotesServerIsLocalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox_LotusNotesServerIsLocalStateChanged
        jTextField_LotusNotesServer.setEnabled(!jCheckBox_LotusNotesServerIsLocal.isSelected());
    }//GEN-LAST:event_jCheckBox_LotusNotesServerIsLocalStateChanged

    private void jTextField_DestinationCalendarNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_DestinationCalendarNameFocusLost
        // Trim whitespace from front and back of text
        jTextField_DestinationCalendarName.setText(jTextField_DestinationCalendarName.getText().trim());
    }//GEN-LAST:event_jTextField_DestinationCalendarNameFocusLost

    private void jButton_HelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_HelpActionPerformed
        try {
            // Get the absolute path to this app and append the help filename
            String helpFilename = new java.io.File("").getAbsolutePath() + System.getProperty("file.separator") + "HelpFile.html";

            // This works for Java 1.6+ to open an html file:
            java.awt.Desktop.getDesktop().browse(new java.io.File(helpFilename).toURI());
        } catch (Exception ex) {
            statusAppendException("There was a problem opening the help file.", ex);
        }
    }//GEN-LAST:event_jButton_HelpActionPerformed

    private void jTextField_SyncDaysInPastFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_SyncDaysInPastFocusLost
        setDateRange();
    }//GEN-LAST:event_jTextField_SyncDaysInPastFocusLost

    private void jTextField_SyncDaysInFutureFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_SyncDaysInFutureFocusLost
        setDateRange();
    }//GEN-LAST:event_jTextField_SyncDaysInFutureFocusLost

    private void validateSettings() {
        boolean complete = false;

        if (jCheckBox_enableProxy.isSelected()) {
            if (jTextField_proxyIP.getText().length() > 0 &&
                    jTextField_proxyPort.getText().length() > 0) {
                complete = true;
            } else {
                complete = false;
            }
        }
        else
            complete = true;

        jButton_Synchronize.setEnabled(complete);
    }

    @SuppressWarnings("static-access")
    private void saveSettings() throws Exception {
        confBean.setLotusNotesServer(jTextField_LotusNotesServer.getText());
        confBean.setLotusNotesServerIsLocal(jCheckBox_LotusNotesServerIsLocal.isSelected());
        confBean.setLotusNotesMailFile(jTextField_LotusNotesMailFile.getText());
        confBean.setLotusNotesUsername(jTextField_LotusNotesUsername.getText());
        confBean.setLotusNotesPassword(new String(jPasswordField_LotusNotesPassword.getPassword()));

        confBean.setGoogleUserName(jTextField_GoogleUsername.getText());
        confBean.setGooglePassword(new String(jPasswordField_GooglePassword.getPassword()));

        confBean.setGoogleEnableProxy(jCheckBox_enableProxy.isSelected());
        confBean.setGoogleProxyPort(jTextField_proxyPort.getText());
        confBean.setGoogleProxyIP(jTextField_proxyIP.getText());
        confBean.setGoogleProxyUsername(jTextField_proxyUsername.getText());
        confBean.setGoogleProxyPassword(new String(jPasswordField_proxyPassword.getPassword()));
        confBean.setGoogleCalendarName(jTextField_DestinationCalendarName.getText());

        confBean.setGoogleUseSSL(jCheckBox_GoogleSSL.isSelected());
        confBean.setSyncOnStartup(jCheckBox_SyncOnStart.isSelected());
        confBean.setDiagnosticMode(jCheckBox_DiagnosticMode.isSelected());
        confBean.setSyncDescription(jCheckBox_SyncDescription.isSelected());
        confBean.setSyncAlarms(jCheckBox_SyncAlarms.isSelected());
        confBean.setSyncDaysInFuture(Integer.parseInt(jTextField_SyncDaysInFuture.getText()));
        confBean.setSyncDaysInPast(Integer.parseInt(jTextField_SyncDaysInPast.getText()));
        confBean.setSyncMeetingAttendees(jCheckBox_SyncMeetingAttendees.isSelected());

        //save configuration to file
        confBean.writeConfig();
    }

    private void loadSettings() {
        try {
            jTextField_LotusNotesServer.setText(confBean.getLotusNotesServer());
            jCheckBox_LotusNotesServerIsLocal.setSelected(confBean.getLotusNotesServerIsLocal());
            jTextField_LotusNotesMailFile.setText(confBean.getLotusNotesMailFile());
            jTextField_LotusNotesUsername.setText(confBean.getLotusNotesUsername());
            String s = confBean.getLotusNotesPassword();
            jPasswordField_LotusNotesPassword.setText(confBean.getLotusNotesPassword());

            jTextField_GoogleUsername.setText(confBean.getGoogleUserName());
            jPasswordField_GooglePassword.setText(confBean.getGooglePassword());
            jCheckBox_GoogleSSL.setSelected(confBean.getGoogleUseSSL());
            jCheckBox_enableProxy.setSelected(confBean.getGoogleEnableProxy());
            jTextField_proxyIP.setText(confBean.getGoogleProxyIP());
            jTextField_proxyPort.setText(confBean.getGoogleProxyPort());
            jTextField_proxyUsername.setText(confBean.getGoogleProxyUsername());
            jPasswordField_proxyPassword.setText(confBean.getGoogleProxyPassword());
            jTextField_DestinationCalendarName.setText(confBean.getGoogleCalendarName());
            
            jCheckBox_SyncOnStart.setSelected(confBean.getSyncOnStartup());
            jCheckBox_DiagnosticMode.setSelected(confBean.getDiagnosticMode());
            jCheckBox_SyncDescription.setSelected(confBean.getSyncDescription());
            jCheckBox_SyncAlarms.setSelected(confBean.getSyncAlarms());
            jTextField_SyncDaysInFuture.setText(Integer.toString(confBean.getSyncDaysInFuture()));
            jTextField_SyncDaysInPast.setText(Integer.toString(confBean.getSyncDaysInPast()));
            jCheckBox_SyncMeetingAttendees.setSelected(confBean.getSyncMeetingAttendees());

            // Configure proxy settings
            proxy.setProxyHost(confBean.getGoogleProxyIP());
            proxy.setProxyPort(confBean.getGoogleProxyPort());
            proxy.setEnabled(confBean.getGoogleEnableProxy());
        } catch (Exception ex) {
            statusAppendException("Unable to read settings from the config file.", ex);
        }
    }


    protected void setDateRange() {
        // Define our min start date for entries we will process
        Calendar now = Calendar.getInstance();
        int syncDaysInPast = 0;
        if (!jTextField_SyncDaysInPast.getText().isEmpty())
            syncDaysInPast = Integer.parseInt(jTextField_SyncDaysInPast.getText()) * -1;
        now.add(Calendar.DATE, syncDaysInPast);
        // Clear out the time portion
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        minStartDate = now.getTime();

        // Define our max end date for entries we will process
        now = Calendar.getInstance();
        int syncDaysInFuture = 0;
        if (!jTextField_SyncDaysInFuture.getText().isEmpty())
            syncDaysInFuture = Integer.parseInt(jTextField_SyncDaysInFuture.getText());
        now.add(Calendar.DATE, syncDaysInFuture);
        // Set the time portion
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        maxEndDate = now.getTime();
    }

    /**
     * Clears the status text area.
     */
    protected void statusClear() {
    	if (!isSilentMode) jTextArea_Status.setText("");
    }
            
    /**
     * Adds a line to the status area.
     * @param text - The text to add.
     */
    protected void statusAppendLine(String text) {
    	if (isSilentMode) 
    		System.out.println(text);
    	else {
    		jTextArea_Status.append(text + "\n");

            // Scroll to the bottom so the new text can be seen
            jTextArea_Status.setCaretPosition(jTextArea_Status.getDocument().getLength());
      }
    }

    /**
     * Adds a line to the status area in diagnostic format.
     * @param text - The text to add.
     */
    protected void statusAppendLineDiag(String text) {
    	statusAppendLine("    " + text);
    }

    protected void statusAppendStart(String text) {
        statusStartTime = System.currentTimeMillis();
        if (isSilentMode) 
        	System.out.print(text);
        else
        	jTextArea_Status.append(text);
    }

    protected void statusAppendFinished() {
        // Convert milliseonds to seconds and round to the tenths place
        long elapsedMillis = System.currentTimeMillis() - statusStartTime;
        BigDecimal elapsedSecs = new BigDecimal(elapsedMillis / 1000.0).setScale(1, BigDecimal.ROUND_HALF_UP);
        statusAppendLine(" (" + elapsedSecs.toString() + " s)");
    }

    protected void statusAppendException(String msg, Exception ex) {
        statusAppendLine("\n\n=== ERROR ===");
        statusAppendLine(msg);

        // Add the stack trace to the status area
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        statusAppendLine(sw.toString());
    }

    ProxyConfigBean proxy;
    SyncCompletedDialog syncCompletedDialog;
    ConfigurationBean confBean;
    private boolean isUrlValid = false;
    long statusStartTime = 0;
    // An exit code of 0 is success. All other values are failure.
    final String appVersion = "1.9";
    private boolean isSilentMode = false;
    private boolean saveSettingsOnExit = true;

    // Our min and max dates for entries we will process.
    // If the calendar entry is outside this range, it is ignored.
    Date minStartDate = null;
    Date maxEndDate = null;

    static final int EXIT_SUCCESS = 0;
    static final int EXIT_INVALID_PARM = 1;
    static final int EXIT_EXCEPTION = 2;
    static int exitCode = EXIT_SUCCESS;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Help;
    private javax.swing.JButton jButton_Synchronize;
    private javax.swing.JCheckBox jCheckBox_DiagnosticMode;
    private javax.swing.JCheckBox jCheckBox_GoogleSSL;
    private javax.swing.JCheckBox jCheckBox_LotusNotesServerIsLocal;
    private javax.swing.JCheckBox jCheckBox_SyncAlarms;
    private javax.swing.JCheckBox jCheckBox_SyncDescription;
    private javax.swing.JCheckBox jCheckBox_SyncMeetingAttendees;
    private javax.swing.JCheckBox jCheckBox_SyncOnStart;
    private javax.swing.JCheckBox jCheckBox_enableProxy;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField_GooglePassword;
    private javax.swing.JPasswordField jPasswordField_GooglePassword1;
    private javax.swing.JPasswordField jPasswordField_LotusNotesPassword;
    private javax.swing.JPasswordField jPasswordField_proxyPassword;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea_Status;
    private javax.swing.JTextField jTextField_DestinationCalendarName;
    private javax.swing.JTextField jTextField_GoogleUsername;
    private javax.swing.JTextField jTextField_LotusNotesMailFile;
    private javax.swing.JTextField jTextField_LotusNotesServer;
    private javax.swing.JTextField jTextField_LotusNotesUsername;
    private javax.swing.JFormattedTextField jTextField_SyncDaysInFuture;
    private javax.swing.JFormattedTextField jTextField_SyncDaysInPast;
    private javax.swing.JTextField jTextField_proxyIP;
    private javax.swing.JTextField jTextField_proxyPort;
    private javax.swing.JTextField jTextField_proxyUsername;
    // End of variables declaration//GEN-END:variables
}

package LotusNotesGoogleCalendarBridge;

import LotusNotesGoogleCalendarBridge.ProxyModule.ProxyConfigBean;
import LotusNotesGoogleCalendarBridge.LotusNotesService.LotusNotesExport;
import LotusNotesGoogleCalendarBridge.LotusNotesService.NotesCalendarEntry;
import LotusNotesGoogleCalendarBridge.GoogleService.GoogleImport;
import com.google.gdata.data.calendar.ColorProperty;
import com.google.gdata.util.ServiceException;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

public class mainGUI extends javax.swing.JFrame {

    public mainGUI() {
        preInitComponents();
        initComponents();
        postInitComponents();
    }

    private void preInitComponents() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting window theme");
        }
    }

    private void postInitComponents() {
        // initialize proxy bean.
        proxy = new ProxyConfigBean();

        // prepare the sync completed dialog message
        // (this is not nice, but will have to do for now)
        syncCompletedDialog = new SyncCompletedDialog(new javax.swing.JFrame(), true);

        //hide date time pickers
        setDateTimeSelectorVisible(!jCheckBox_LimitDateRange.isSelected());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton_Synchronize = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPasswordField_GooglePassword = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField_GoogleURL = new javax.swing.JTextField();
        jTextField_GoogleUsername = new javax.swing.JTextField();
        jLabel_UsernameError = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextField_proxyPort = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox_enableProxy = new javax.swing.JCheckBox();
        jTextField_proxyIP = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jDatePicker_start = new net.sourceforge.jdatepicker.JDatePicker();
        jDatePicker_end = new net.sourceforge.jdatepicker.JDatePicker();
        jCheckBox_LimitDateRange = new javax.swing.JCheckBox();
        jCheckBox_uploadToMainCalendar = new javax.swing.JCheckBox();
        jCheckBox_GoogleUseSSL = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calendar Synchronizer");
        setBackground(new java.awt.Color(254, 254, 254));
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setResizable(false);

        jButton_Synchronize.setText("Synchronize");
        jButton_Synchronize.setEnabled(false);
        jButton_Synchronize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SynchronizeActionPerformed(evt);
            }
        });

        jButton_Cancel.setText("Cancel");
        jButton_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelActionPerformed(evt);
            }
        });

        jPasswordField_GooglePassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPasswordField_GooglePasswordKeyReleased(evt);
            }
        });

        jLabel3.setText("Google Account:");

        jLabel2.setText("Lotus Notes Mail File URL:");

        jLabel4.setText("Google Password:");

        jTextField_GoogleURL.setText("http://lotus.host/mail/mailfile.nsf");
        jTextField_GoogleURL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_GoogleURLKeyReleased(evt);
            }
        });

        jTextField_GoogleUsername.setText("user@google.com");
        jTextField_GoogleUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_GoogleUsernameKeyReleased(evt);
            }
        });

        jLabel_UsernameError.setForeground(new java.awt.Color(255, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel_UsernameError, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField_GoogleURL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField_GoogleUsername)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPasswordField_GooglePassword))
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(26, 26, 26)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jTextField_GoogleURL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jTextField_GoogleUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jPasswordField_GooglePassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jLabel_UsernameError, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("General", jPanel1);

        jTextField_proxyPort.setEnabled(false);
        jTextField_proxyPort.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_proxyPortFocusLost(evt);
            }
        });
        jTextField_proxyPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_proxyPortKeyReleased(evt);
            }
        });

        jLabel5.setText("Port Number:");

        jCheckBox_enableProxy.setText("Enable Proxy Support");
        jCheckBox_enableProxy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox_enableProxyMouseClicked(evt);
            }
        });
        jCheckBox_enableProxy.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox_enableProxyStateChanged(evt);
            }
        });
        jCheckBox_enableProxy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jCheckBox_enableProxyKeyReleased(evt);
            }
        });

        jTextField_proxyIP.setEnabled(false);
        jTextField_proxyIP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_proxyIPFocusLost(evt);
            }
        });
        jTextField_proxyIP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_proxyIPKeyReleased(evt);
            }
        });

        jLabel6.setText("IP Address:");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(27, 27, 27)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel5)
                            .add(jLabel6))
                        .add(40, 40, 40)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTextField_proxyPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTextField_proxyIP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jCheckBox_enableProxy))
                .addContainerGap(262, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBox_enableProxy)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(jTextField_proxyIP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField_proxyPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .addContainerGap(121, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Proxy", jPanel2);

        jLabel7.setText("Calendar Starting:");

        jLabel8.setText("Calendar Ending:");

        jCheckBox_LimitDateRange.setSelected(true);
        jCheckBox_LimitDateRange.setText("Synchronize only two weeks of entries");
        jCheckBox_LimitDateRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_LimitDateRangeActionPerformed(evt);
            }
        });

        jCheckBox_uploadToMainCalendar.setText("Upload to main Calendar, instead of the secondary");

        jCheckBox_GoogleUseSSL.setSelected(true);
        jCheckBox_GoogleUseSSL.setText("Connect to Google using SSL/TLS (recommended!)");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox_uploadToMainCalendar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox_GoogleUseSSL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3Layout.createSequentialGroup()
                        .add(jLabel7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 33, Short.MAX_VALUE)
                        .add(jDatePicker_start, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel8)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 38, Short.MAX_VALUE)
                        .add(jDatePicker_end, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox_LimitDateRange, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))
                .add(533, 533, 533))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(21, 21, 21)
                .add(jCheckBox_uploadToMainCalendar)
                .add(5, 5, 5)
                .add(jCheckBox_GoogleUseSSL)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox_LimitDateRange)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jDatePicker_start, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jDatePicker_end, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Advanced", jPanel3);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lngooglecalsync-logo.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Beta Release 0.3");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel9))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jButton_Cancel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton_Synchronize)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel9)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 258, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1, 1, 1)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_Synchronize)
                    .add(jButton_Cancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelActionPerformed
        System.exit(0);
}//GEN-LAST:event_jButton_CancelActionPerformed

    private void jTextField_GoogleURLKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_GoogleURLKeyReleased
        if (jTextField_GoogleURL.getText().startsWith("http")) {
            isUrlValid = true;
        } else {
            isUrlValid = false;
        }
        checkCompletion();
}//GEN-LAST:event_jTextField_GoogleURLKeyReleased

    private void jTextField_GoogleUsernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_GoogleUsernameKeyReleased
        if (jTextField_GoogleUsername.getText().contains("@")) {
            isValidAccount = true;
            jLabel_UsernameError.setText("");
        } else {
            isValidAccount = false;
            jLabel_UsernameError.setText("Account name does not contain \"@\" symbol");
        }
        checkCompletion();
}//GEN-LAST:event_jTextField_GoogleUsernameKeyReleased

    private void jPasswordField_GooglePasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordField_GooglePasswordKeyReleased
        checkCompletion();
}//GEN-LAST:event_jPasswordField_GooglePasswordKeyReleased

    private void jButton_SynchronizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SynchronizeActionPerformed
        try {
            proxy.deactivateNow();

            String[] dateRange;
            LotusNotesExport lotusNotesService;

            if (!jCheckBox_LimitDateRange.isSelected()) {
                dateRange = getFormattedCalendarRange();
                lotusNotesService = new LotusNotesExport(dateRange);
            } else {
                lotusNotesService = new LotusNotesExport();
            }

            List<NotesCalendarEntry> cals = lotusNotesService.start(jTextField_GoogleURL.getText());

            proxy.activateNow();
            // check whether the user has deselected to use SSL when connecting to google (this is not recommended)
            boolean GoogleConnectUsingSSL = jCheckBox_GoogleUseSSL.isSelected();
            GoogleImport googleService = new GoogleImport(jTextField_GoogleUsername.getText(), new String(jPasswordField_GooglePassword.getPassword()),GoogleConnectUsingSSL);

            // googleService.setCalendarColor(CALENDARCOLOR);
            googleService.deleteCalendar();

            if (jCheckBox_uploadToMainCalendar.isSelected()) {
                googleService.createEvent(cals, true);
            } else {
                googleService.createCalendar();
                googleService.createEvent(cals, false);
            }
            jTextField_GoogleURL.setEnabled(false);
            jTextField_GoogleUsername.setEnabled(false);
            jPasswordField_GooglePassword.setEnabled(false);
            //jButton_Synchronize.setEnabled(false);
            jButton_Cancel.setText("Exit");
            jButton_Synchronize.setText("Synch Again!");
            syncCompletedDialog.setVisible(true);
        } catch (ServiceException ex) {
            Logger.getLogger(mainGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(mainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_jButton_SynchronizeActionPerformed

    private void jTextField_proxyPortKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_proxyPortKeyReleased
        checkCompletion();
}//GEN-LAST:event_jTextField_proxyPortKeyReleased

    private void jCheckBox_enableProxyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox_enableProxyStateChanged
        jTextField_proxyIP.setEnabled(jCheckBox_enableProxy.isSelected());
        jTextField_proxyPort.setEnabled(jCheckBox_enableProxy.isSelected());
}//GEN-LAST:event_jCheckBox_enableProxyStateChanged

    private void jTextField_proxyIPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_proxyIPKeyReleased
        checkCompletion();
}//GEN-LAST:event_jTextField_proxyIPKeyReleased

    private void jCheckBox_LimitDateRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_LimitDateRangeActionPerformed
        setDateTimeSelectorVisible(!jCheckBox_LimitDateRange.isSelected());
    }//GEN-LAST:event_jCheckBox_LimitDateRangeActionPerformed

    private void jCheckBox_enableProxyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox_enableProxyMouseClicked
        proxy.setEnabled(jCheckBox_enableProxy.isSelected());
        checkCompletion();
    }//GEN-LAST:event_jCheckBox_enableProxyMouseClicked

    private void jCheckBox_enableProxyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCheckBox_enableProxyKeyReleased
        checkCompletion();
        proxy.setEnabled(jCheckBox_enableProxy.isSelected());
    }//GEN-LAST:event_jCheckBox_enableProxyKeyReleased

    private void jTextField_proxyIPFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_proxyIPFocusLost
        proxy.setProxyHost(jTextField_proxyIP.getText());
        System.out.println(proxy.getProxyHost());
    }//GEN-LAST:event_jTextField_proxyIPFocusLost

    private void jTextField_proxyPortFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_proxyPortFocusLost
        proxy.setProxyPort(jTextField_proxyPort.getText());
        System.out.println(proxy.getProxyPort());
    }//GEN-LAST:event_jTextField_proxyPortFocusLost

    public void setDateTimeSelectorVisible(boolean visible) {
        jDatePicker_start.setVisible(visible);
        jDatePicker_end.setVisible(visible);
        jLabel7.setVisible(visible);
        jLabel8.setVisible(visible);
    }

    private void checkCompletion() {
        boolean complete = false;
        if (jTextField_GoogleURL.getText().length() > 0 &&
                jTextField_GoogleUsername.getText().length() > 0 &&
                jPasswordField_GooglePassword.getPassword().length > 0 &&
                isUrlValid &&
                isValidAccount) {
            complete = true;
        }

        if (jCheckBox_enableProxy.isSelected()) {
            if (jTextField_proxyIP.getText().length() > 0 &&
                    jTextField_proxyPort.getText().length() > 0) {
                complete = true;
            } else {
                complete = false;
            }

        }

        jButton_Synchronize.setEnabled(complete);
    }

    @SuppressWarnings("static-access")
    // very ugly way of converting the date/time..
    // need to look for something better, but it works...
    private String[] getFormattedCalendarRange() {
        String[] calrange = new String[2];

        Calendar start = jDatePicker_start.getCalendarClone();
        Calendar end = jDatePicker_end.getCalendarClone();

        Integer intSday = new Integer(start.get(start.DAY_OF_MONTH));
        Integer intSmonth = new Integer(start.get(start.MONTH)) + 1;
        Integer intSyear = new Integer(start.get(start.YEAR));
        Integer intEday = new Integer(end.get(end.DAY_OF_MONTH));
        Integer intEmonth = new Integer(end.get(end.MONTH) + 1);
        Integer intEyear = new Integer(end.get(end.YEAR));

        String stringSday = intSday.toString();
        String stringSmonth = intSmonth.toString();
        String stringEday = intEday.toString();
        String stringEmonth = intEmonth.toString();

        if (stringSday.length() < 2) {
            stringSday = "0" + stringSday;
        }

        if (stringSmonth.length() < 2) {
            stringSmonth = "0" + stringSmonth;
        }

        if (stringEday.length() < 2) {
            stringEday = "0" + stringEday;
        }

        if (stringEmonth.length() < 2) {
            stringEmonth = "0" + stringEmonth;
        }

        calrange[0] = (intSyear + "" + stringSmonth + "" + stringSday);
        calrange[1] = (intEyear + "" + stringEmonth + "" + stringEday);

        return calrange;

    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                String jre_version = System.getProperty("java.version");
                if (!jre_version.startsWith("1.6")) {
                    System.out.println("\nThis application will only run with Java 1.6 or higher!!");
                    System.out.println("Your version is: " + jre_version);
                    System.out.println("Please install the required Version\n\n");
                    System.exit(1);
                }

                new mainGUI().setVisible(true);
            }
        });
    }
    private boolean isUrlValid = false;
    private boolean isValidAccount = false;
    ProxyConfigBean proxy;
    SyncCompletedDialog syncCompletedDialog;
    private String[] calcolors;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Synchronize;
    private javax.swing.JCheckBox jCheckBox_GoogleUseSSL;
    private javax.swing.JCheckBox jCheckBox_LimitDateRange;
    private javax.swing.JCheckBox jCheckBox_enableProxy;
    private javax.swing.JCheckBox jCheckBox_uploadToMainCalendar;
    private net.sourceforge.jdatepicker.JDatePicker jDatePicker_end;
    private net.sourceforge.jdatepicker.JDatePicker jDatePicker_start;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_UsernameError;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField_GooglePassword;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField_GoogleURL;
    private javax.swing.JTextField jTextField_GoogleUsername;
    private javax.swing.JTextField jTextField_proxyIP;
    private javax.swing.JTextField jTextField_proxyPort;
    // End of variables declaration//GEN-END:variables
}

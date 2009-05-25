package LotusNotesGoogleCalendarBridge;

import LotusNotesGoogleCalendarBridge.ProxyModule.ProxyConfigurationDialog;
import LotusNotesGoogleCalendarBridge.ProxyModule.ProxyConfigBean;
import LotusNotesGoogleCalendarBridge.LotusNotesService.LotusNotesExport;
import LotusNotesGoogleCalendarBridge.LotusNotesService.NotesCalendarEntry;
import LotusNotesGoogleCalendarBridge.GoogleService.GoogleImport;
import com.google.gdata.data.calendar.ColorProperty;
import com.google.gdata.util.ServiceException;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Array;
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
        proxy = new ProxyConfigBean();
        proxyDialog = new ProxyConfigurationDialog(new javax.swing.JFrame(), true, proxy);
        syncCompletedDialog = new SyncCompletedDialog(new javax.swing.JFrame(), true);

        //hide date time pickers
        setDateTimeSelectorVisible(!jCheckBox2.isSelected());

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton_Synchronize = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();
        jLabel_UsernameError = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPasswordField_GooglePassword = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField_GoogleURL = new javax.swing.JTextField();
        jTextField_GoogleUsername = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jDatePicker1 = new net.sourceforge.jdatepicker.JDatePicker();
        jDatePicker2 = new net.sourceforge.jdatepicker.JDatePicker();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jTextField_proxyPort = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jTextField_proxyIP = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calendar Synchronizer");
        setBackground(new java.awt.Color(254, 254, 254));
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Beta Release 0.3");

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

        jLabel_UsernameError.setForeground(new java.awt.Color(255, 0, 0));

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
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jTextField_GoogleURL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                    .add(jTextField_GoogleUsername)
                    .add(jPasswordField_GooglePassword))
                .addContainerGap(74, Short.MAX_VALUE))
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
                .addContainerGap(86, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("General", jPanel1);

        jLabel7.setText("Calendar Starting:");

        jLabel8.setText("Calendar Ending:");

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Synchronize all Lotus Notes Calendar entries");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jCheckBox2)
                        .addContainerGap())
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel3Layout.createSequentialGroup()
                                .add(jLabel7)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jDatePicker1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                                .add(jLabel8)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 14, Short.MAX_VALUE)
                                .add(jDatePicker2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(208, 208, 208))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(16, 16, 16)
                .add(jCheckBox2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jDatePicker1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jDatePicker2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(102, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Customize", jPanel3);

        jTextField_proxyPort.setEnabled(false);
        jTextField_proxyPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_proxyPortKeyReleased(evt);
            }
        });

        jLabel5.setText("Port Number:");

        jCheckBox1.setText("Enable Proxy Support");
        jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox1StateChanged(evt);
            }
        });

        jTextField_proxyIP.setEnabled(false);
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
                    .add(jCheckBox1))
                .addContainerGap(234, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBox1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(jTextField_proxyIP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField_proxyPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .addContainerGap(106, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Proxy", jPanel2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(jButton_Cancel)
                        .add(18, 18, 18)
                        .add(jButton_Synchronize))
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE))
                .add(18, 18, 18)
                .add(jLabel_UsernameError, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jLabel1)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(192, 192, 192)
                        .add(jLabel_UsernameError, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(8, 8, 8)
                        .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 258, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_Cancel)
                    .add(jButton_Synchronize))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            LotusNotesExport lotusNotesService = new LotusNotesExport();
            List<NotesCalendarEntry> cals = lotusNotesService.start(jTextField_GoogleURL.getText());

            proxy.activateNow();
            GoogleImport googleService = new GoogleImport(jTextField_GoogleUsername.getText(), new String(jPasswordField_GooglePassword.getPassword()));
            // googleService.setCalendarColor(CALENDARCOLOR);
            googleService.deleteCalendar();
            googleService.createCalendar();
            googleService.createEvent(cals);

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
        //jButton1.setEnabled(checkCompletion());
}//GEN-LAST:event_jTextField_proxyPortKeyReleased

    private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox1StateChanged
        jTextField_proxyIP.setEnabled(jCheckBox1.isSelected());
        jTextField_proxyPort.setEnabled(jCheckBox1.isSelected());
}//GEN-LAST:event_jCheckBox1StateChanged

    private void jTextField_proxyIPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_proxyIPKeyReleased
        //jButton1.setEnabled(checkCompletion());
}//GEN-LAST:event_jTextField_proxyIPKeyReleased

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
      setDateTimeSelectorVisible(!jCheckBox2.isSelected());
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    public void setDateTimeSelectorVisible(boolean visible) {
        jDatePicker1.setVisible(visible);
        jDatePicker2.setVisible(visible);
        jLabel7.setVisible(visible);
        jLabel8.setVisible(visible);
    }

    private void checkCompletion() {
        if (jTextField_GoogleURL.getText().length() > 0 &&
                jTextField_GoogleUsername.getText().length() > 0 &&
                jPasswordField_GooglePassword.getPassword().length > 0 &&
                isUrlValid &&
                isValidAccount) {

            jButton_Synchronize.setEnabled(true);
        } else {
            jButton_Synchronize.setEnabled(false);
        }
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
    ProxyConfigurationDialog proxyDialog;
    SyncCompletedDialog syncCompletedDialog;
    private String[] calcolors;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Synchronize;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private net.sourceforge.jdatepicker.JDatePicker jDatePicker1;
    private net.sourceforge.jdatepicker.JDatePicker jDatePicker2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
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

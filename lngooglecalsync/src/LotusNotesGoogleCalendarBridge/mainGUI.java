package LotusNotesGoogleCalendarBridge;

import LotusNotesGoogleCalendarBridge.ProxyModule.ProxyConfigurationDialog;
import LotusNotesGoogleCalendarBridge.ProxyModule.ProxyConfigBean;
import LotusNotesGoogleCalendarBridge.LotusNotesService.LotusNotesExport;
import LotusNotesGoogleCalendarBridge.LotusNotesService.NotesCalendarEntry;
import LotusNotesGoogleCalendarBridge.GoogleService.GoogleImport;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
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
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField_GoogleURL = new javax.swing.JTextField();
        jTextField_GoogleUsername = new javax.swing.JTextField();
        jPasswordField_GooglePassword = new javax.swing.JPasswordField();
        jButton_Synchronize = new javax.swing.JButton();
        jButton_Cancel = new javax.swing.JButton();
        jLabel_UsernameError = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(254, 254, 254));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Beta Release 0.2");

        jLabel2.setText("Lotus Notes Mail File URL:");

        jLabel3.setText("Google Account:");

        jLabel4.setText("Google Password (uses SSL):");

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

        jPasswordField_GooglePassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPasswordField_GooglePasswordKeyReleased(evt);
            }
        });

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

        jLabel7.setText("Please make sure to export your gmail calendar as a backup!!");

        jButton3.setText("Configure Proxy");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPasswordField_GooglePassword, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                            .add(jTextField_GoogleURL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                            .add(jTextField_GoogleUsername, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                            .add(jButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 173, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jLabel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel_UsernameError, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jButton_Cancel)
                        .add(18, 18, 18)
                        .add(jButton_Synchronize)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .add(30, 30, 30)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jTextField_GoogleURL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jTextField_GoogleUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jPasswordField_GooglePassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton3)
                .add(18, 18, 18)
                .add(jLabel_UsernameError, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel7)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 57, Short.MAX_VALUE)
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
            LotusNotesExport lotusNotesService = new LotusNotesExport();
            List<NotesCalendarEntry> cals = lotusNotesService.start(jTextField_GoogleURL.getText());

            proxy.activateNow();
            GoogleImport googleService = new GoogleImport(jTextField_GoogleUsername.getText(), new String(jPasswordField_GooglePassword.getPassword()));
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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        proxyDialog.setVisible(true);
        this.proxy = proxyDialog.getProxyConfigBean();
    }//GEN-LAST:event_jButton3ActionPerformed

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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Synchronize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel_UsernameError;
    private javax.swing.JPasswordField jPasswordField_GooglePassword;
    private javax.swing.JTextField jTextField_GoogleURL;
    private javax.swing.JTextField jTextField_GoogleUsername;
    // End of variables declaration//GEN-END:variables
}

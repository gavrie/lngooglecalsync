package LotusNotesGoogleCalendarBridge;

import LotusNotesGoogleCalendarBridge.ProxyModule.ProxyConfigBean;
import LotusNotesGoogleCalendarBridge.LotusNotesService.LotusNotesExport;
import LotusNotesGoogleCalendarBridge.LotusNotesService.NotesCalendarEntry;
import LotusNotesGoogleCalendarBridge.GoogleService.GoogleImport;
import com.google.gdata.util.ServiceException;
import java.awt.Cursor;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import preferences.ConfigurationBean;

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
        // initialize proxy bean
        proxy = new ProxyConfigBean();

        // prepare the sync completed dialog message
        // (this is not nice, but will have to do for now)
        syncCompletedDialog = new SyncCompletedDialog(new javax.swing.JFrame(), true);

        // hide date time pickers
        setDateTimeSelectorVisible(!jCheckBox_LimitDateRange.isSelected());

        // load configuration bean
        confBean = new ConfigurationBean();
        confBean.readConfig();

        activateConfiguration();

        validate();
        // check whether the loaded configuration meets our requirements to sync.
        checkCompletion();

        if (confBean.getSyncOnStartup() && jButton_Synchronize.isEnabled()) {
            System.out.println("Automatic Sync on Startup is enabled. Starting sync now!");
            SyncNow();
        }

    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                String jre_version = System.getProperty("java.version");
                if (!jre_version.startsWith("1.6")) {
                    System.out.println("\nThis application will only run with Java 1.6 or higher.");
                    System.out.println("Your version is: " + jre_version);
                    System.out.println("Please install the required version.\n\n");
                    System.exit(1);
                }

                new mainGUI().setVisible(true);
            }
        });
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
        jLabel4 = new javax.swing.JLabel();
        jTextField_GoogleUsername = new javax.swing.JTextField();
        jLabel_UsernameError = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPasswordField_LotusNotesPassword = new javax.swing.JPasswordField();
        jTextField_LotusNotesUsername = new javax.swing.JTextField();
        jTextField_LotusNotesServer = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField_LotusNotesMailFile = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextField_proxyPort = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox_enableProxy = new javax.swing.JCheckBox();
        jTextField_proxyIP = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jDatePicker_start = new net.sourceforge.jdatepicker.JDatePicker();
        jDatePicker_end = new net.sourceforge.jdatepicker.JDatePicker();
        jCheckBox_LimitDateRange = new javax.swing.JCheckBox();
        jCheckBox_uploadToMainCalendar = new javax.swing.JCheckBox();
        jCheckBox_GoogleSSL = new javax.swing.JCheckBox();
        jCheckBox_SyncOnStart = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calendar Synchronizer");
        setBackground(new java.awt.Color(254, 254, 254));
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setResizable(false);

        jButton_Synchronize.setText("Synchronize");
        jButton_Synchronize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SynchronizeActionPerformed(evt);
            }
        });

        jButton_Cancel.setText("Exit");
        jButton_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelActionPerformed(evt);
            }
        });

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(105, 88));

        jPanel1.setAutoscrolls(true);

        jPasswordField_GooglePassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPasswordField_GooglePasswordFocusLost(evt);
            }
        });

        jLabel3.setText("Google Account");

        jLabel4.setText("Google Password");

        jTextField_GoogleUsername.setText("user@google.com");
        jTextField_GoogleUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_GoogleUsernameFocusLost(evt);
            }
        });

        jLabel_UsernameError.setForeground(new java.awt.Color(255, 0, 0));

        jLabel12.setText("Lotus Notes Username");

        jLabel13.setText("Lotus Notes Password");

        jPasswordField_LotusNotesPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPasswordField_LotusNotesPasswordFocusLost(evt);
            }
        });

        jTextField_LotusNotesUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_LotusNotesUsernameFocusLost(evt);
            }
        });

        jTextField_LotusNotesServer.setToolTipText("Leave server name blank to access a local mail file.");
        jTextField_LotusNotesServer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_LotusNotesServerFocusLost(evt);
            }
        });

        jLabel14.setText("Lotus Notes Server");

        jTextField_LotusNotesMailFile.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_LotusNotesMailFileFocusLost(evt);
            }
        });

        jLabel15.setText("Lotus Notes Mail File");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(190, 190, 190)
                        .add(jLabel_UsernameError, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 279, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(405, 405, 405))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 184, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                        .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 184, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(jLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 184, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(jLabel15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 184, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel4)
                                .add(106, 106, 106)))
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jTextField_LotusNotesServer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                                    .add(jTextField_LotusNotesMailFile, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                                    .add(jTextField_LotusNotesUsername, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                                    .add(jPasswordField_LotusNotesPassword, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
                                .add(405, 405, 405))
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField_GoogleUsername)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jPasswordField_GooglePassword, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(25, 25, 25)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel14)
                    .add(jTextField_LotusNotesServer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel15)
                    .add(jTextField_LotusNotesMailFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel12)
                    .add(jTextField_LotusNotesUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel13)
                    .add(jPasswordField_LotusNotesPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(37, 37, 37)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextField_GoogleUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPasswordField_GooglePassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(5, 5, 5)
                .add(jLabel_UsernameError, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
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
        jCheckBox_enableProxy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox_enableProxyMouseClicked(evt);
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

        jLabel10.setText("In some networks, you may need to use a proxy to reach the internet.");

        jLabel11.setText("Here is the option for you to supply the proxy server and port.");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                    .add(jLabel11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
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
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel10)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel11)
                .add(18, 18, 18)
                .add(jCheckBox_enableProxy)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(jTextField_proxyIP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField_proxyPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .addContainerGap(162, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Proxy", jPanel2);

        jLabel7.setText("Calendar Starting:");

        jLabel8.setText("Calendar Ending:");

        jCheckBox_LimitDateRange.setText("Synchronize only two weeks of entries");
        jCheckBox_LimitDateRange.setEnabled(false);
        jCheckBox_LimitDateRange.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox_LimitDateRangeStateChanged(evt);
            }
        });
        jCheckBox_LimitDateRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_LimitDateRangeActionPerformed(evt);
            }
        });

        jCheckBox_uploadToMainCalendar.setText("Upload to main Calendar, instead of the secondary (Make Backup!!!)");
        jCheckBox_uploadToMainCalendar.setEnabled(false);
        jCheckBox_uploadToMainCalendar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox_uploadToMainCalendarStateChanged(evt);
            }
        });

        jCheckBox_GoogleSSL.setSelected(true);
        jCheckBox_GoogleSSL.setText("Connect to Google using SSL/TLS (recommended!)");
        jCheckBox_GoogleSSL.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox_GoogleSSLStateChanged(evt);
            }
        });

        jCheckBox_SyncOnStart.setText("Sync on Startup");
        jCheckBox_SyncOnStart.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox_SyncOnStartStateChanged(evt);
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
                        .add(jCheckBox_SyncOnStart, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                        .add(131, 131, 131))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(27, 27, 27)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jPanel3Layout.createSequentialGroup()
                                .add(jLabel8)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jDatePicker_end, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel3Layout.createSequentialGroup()
                                .add(jLabel7)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jDatePicker_start, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 205, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jCheckBox_LimitDateRange, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jCheckBox_GoogleSSL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                        .add(47, 47, 47))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jCheckBox_uploadToMainCalendar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 467, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 58, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBox_uploadToMainCalendar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox_SyncOnStart)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox_GoogleSSL)
                .add(4, 4, 4)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(2, 2, 2)
                .add(jCheckBox_LimitDateRange)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jDatePicker_start, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jDatePicker_end, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(135, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Advanced", jPanel3);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lngooglecalsync-logo.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Beta Release 0.4");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 540, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel9)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(402, Short.MAX_VALUE)
                .add(jButton_Cancel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton_Synchronize)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel9)
                .add(18, 18, 18)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 324, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_Synchronize)
                    .add(jButton_Cancel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelActionPerformed
        System.exit(0);
}//GEN-LAST:event_jButton_CancelActionPerformed

    private void jButton_SynchronizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SynchronizeActionPerformed
        SyncNow();
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
    }//GEN-LAST:event_jTextField_proxyIPFocusLost

    private void jTextField_proxyPortFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_proxyPortFocusLost
        proxy.setProxyPort(jTextField_proxyPort.getText());
    }//GEN-LAST:event_jTextField_proxyPortFocusLost

    private void jCheckBox_SyncOnStartStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox_SyncOnStartStateChanged
        updateConfiguration();
    }//GEN-LAST:event_jCheckBox_SyncOnStartStateChanged

    private void jCheckBox_uploadToMainCalendarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox_uploadToMainCalendarStateChanged
        updateConfiguration();
    }//GEN-LAST:event_jCheckBox_uploadToMainCalendarStateChanged

    private void jCheckBox_GoogleSSLStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox_GoogleSSLStateChanged
        updateConfiguration();
    }//GEN-LAST:event_jCheckBox_GoogleSSLStateChanged

    private void jCheckBox_LimitDateRangeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox_LimitDateRangeStateChanged
        updateConfiguration();
    }//GEN-LAST:event_jCheckBox_LimitDateRangeStateChanged

    private void jTextField_LotusNotesServerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_LotusNotesServerFocusLost
        updateConfiguration();
    }//GEN-LAST:event_jTextField_LotusNotesServerFocusLost

    private void jTextField_LotusNotesUsernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_LotusNotesUsernameFocusLost
        updateConfiguration();
    }//GEN-LAST:event_jTextField_LotusNotesUsernameFocusLost

    private void jPasswordField_LotusNotesPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField_LotusNotesPasswordFocusLost
        updateConfiguration();
    }//GEN-LAST:event_jPasswordField_LotusNotesPasswordFocusLost

    private void jTextField_GoogleUsernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_GoogleUsernameFocusLost
        updateConfiguration();
    }//GEN-LAST:event_jTextField_GoogleUsernameFocusLost

    private void jPasswordField_GooglePasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField_GooglePasswordFocusLost
        updateConfiguration();
    }//GEN-LAST:event_jPasswordField_GooglePasswordFocusLost

    private void jTextField_LotusNotesMailFileFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_LotusNotesMailFileFocusLost
        updateConfiguration();
    }//GEN-LAST:event_jTextField_LotusNotesMailFileFocusLost

    public void setDateTimeSelectorVisible(boolean visible) {
        jDatePicker_start.setVisible(visible);
        jDatePicker_end.setVisible(visible);
        jLabel7.setVisible(visible);
        jLabel8.setVisible(visible);
    }

    private void checkCompletion() {
        boolean complete = false;

        if (jTextField_GoogleUsername.getText().contains("@")) {
            isValidAccount = true;
            jLabel_UsernameError.setText("");
        } else {
            isValidAccount = false;
            jLabel_UsernameError.setText("Account name does not contain \"@\" symbol");
        }

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

        //write configuration
        updateConfiguration();
    }

    @SuppressWarnings("static-access")
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

    private void updateConfiguration() {
        confBean.setLotusNotesServer(jTextField_LotusNotesServer.getText());
        confBean.setLotusNotesMailFile(jTextField_LotusNotesMailFile.getText());
        confBean.setLotusNotesUsername(jTextField_LotusNotesUsername.getText());
        confBean.setLotusNotesPassword(new String(jPasswordField_LotusNotesPassword.getPassword()));

        confBean.setGoogleUserName(jTextField_GoogleUsername.getText());
        confBean.setGooglePassword(new String(jPasswordField_GooglePassword.getPassword()));

        confBean.setGoogleEnableProxy(jCheckBox_enableProxy.isSelected());
        confBean.setGoogleProxyPort(jTextField_proxyPort.getText());
        confBean.setGoogleProxyIP(jTextField_proxyIP.getText());

        confBean.setGoogleUseSSL(jCheckBox_GoogleSSL.isSelected());
        confBean.setGoogleUploadToMainCalendar(jCheckBox_uploadToMainCalendar.isSelected());
        confBean.setSyncOnStartup(jCheckBox_SyncOnStart.isSelected());

        //save configuration to file
        confBean.writeConfig();
    }

    private void activateConfiguration() {
        try {
            jTextField_LotusNotesServer.setText(confBean.getLotusNotesServer());
            jTextField_LotusNotesMailFile.setText(confBean.getLotusNotesMailFile());
            jTextField_LotusNotesUsername.setText(confBean.getLotusNotesUsername());
            jPasswordField_LotusNotesPassword.setText(new String(confBean.getLotusNotesPassword()));

            jTextField_GoogleUsername.setText(confBean.getGoogleUserName());
            jPasswordField_GooglePassword.setText(new String(confBean.getGooglePassword().toCharArray()));
            jCheckBox_uploadToMainCalendar.setSelected(confBean.getGoogleUploadToMainCalendar());
            jCheckBox_GoogleSSL.setSelected(confBean.getGoogleUseSSL());
            jCheckBox_enableProxy.setSelected(confBean.getGoogleEnableProxy());
            jTextField_proxyIP.setText(confBean.getGoogleProxyIP());
            jTextField_proxyPort.setText(confBean.getGoogleProxyPort());
            jCheckBox_SyncOnStart.setSelected(confBean.getSyncOnStartup());

            //configure proxy settings from the configuration
            proxy.setProxyHost(confBean.getGoogleProxyIP());
            proxy.setProxyPort(confBean.getGoogleProxyPort());
            proxy.setEnabled(confBean.getGoogleEnableProxy());

        } catch (Exception e) {
            System.err.println("Some preferences were not set in the configuration file," +
                    " please set them inside the GUI.\n " +
                    "They will be saved to a configuration file next time");
        }

    }


    /**
     * Synchronize the Lotus Notes calendar with the Google calendar.
     */
    public void SyncNow() {
        boolean caughtException = false;

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            proxy.deactivateNow();

            String LotusNotesUsername = jTextField_LotusNotesUsername.getText();
            String LotusNotesPassword = new String(jPasswordField_LotusNotesPassword.getPassword());

            // === Get the Lotus Notes calendar data
            LotusNotesExport lotusNotesService;
            lotusNotesService = new LotusNotesExport();
            lotusNotesService.setRequiresAuth(true);
            lotusNotesService.setCredentials(LotusNotesUsername, LotusNotesPassword);

            List<NotesCalendarEntry> cals = lotusNotesService.start(jTextField_LotusNotesServer.getText(), jTextField_LotusNotesMailFile.getText());


            // === Copy the Lotus Notes data to Google calendar
            proxy.activateNow();
            // check whether the user has deselected to use SSL when connecting to google (this is not recommended)
            boolean GoogleConnectUsingSSL = jCheckBox_GoogleSSL.isSelected();
            GoogleImport googleService = new GoogleImport(jTextField_GoogleUsername.getText(), new String(jPasswordField_GooglePassword.getPassword()), GoogleConnectUsingSSL);

            googleService.deleteCalendarEntries();

            int createdCount = 0;
            if (jCheckBox_uploadToMainCalendar.isSelected()) {
                createdCount = googleService.createCalendarEntries(cals, true);
            } else {
                createdCount = googleService.createCalendarEntries(cals, false);
            }

            jTextField_GoogleUsername.setEnabled(false);
            jPasswordField_GooglePassword.setEnabled(false);

            jButton_Cancel.setText("Exit");
            jButton_Synchronize.setText("Synch Again!");

            syncCompletedDialog.setLotusNotesEntriesRead(Integer.toString(cals.size()));
            syncCompletedDialog.setGoogleEntriesCreated(Integer.toString(createdCount));
            syncCompletedDialog.setVisible(true);
        } catch (Exception ex) {
            caughtException = true;
            Logger.getLogger(mainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            setCursor(Cursor.getDefaultCursor());
            
            if (caughtException)
                JOptionPane.showMessageDialog(null, "An error occurred.  Look at the output window to see the error information.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    ProxyConfigBean proxy;
    SyncCompletedDialog syncCompletedDialog;
    ConfigurationBean confBean;
    private boolean isUrlValid = false;
    private boolean isValidAccount = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Synchronize;
    private javax.swing.JCheckBox jCheckBox_GoogleSSL;
    private javax.swing.JCheckBox jCheckBox_LimitDateRange;
    private javax.swing.JCheckBox jCheckBox_SyncOnStart;
    private javax.swing.JCheckBox jCheckBox_enableProxy;
    private javax.swing.JCheckBox jCheckBox_uploadToMainCalendar;
    private net.sourceforge.jdatepicker.JDatePicker jDatePicker_end;
    private net.sourceforge.jdatepicker.JDatePicker jDatePicker_start;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JPasswordField jPasswordField_LotusNotesPassword;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField_GoogleUsername;
    private javax.swing.JTextField jTextField_LotusNotesMailFile;
    private javax.swing.JTextField jTextField_LotusNotesServer;
    private javax.swing.JTextField jTextField_LotusNotesUsername;
    private javax.swing.JTextField jTextField_proxyIP;
    private javax.swing.JTextField jTextField_proxyPort;
    // End of variables declaration//GEN-END:variables
}

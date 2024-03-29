package gui;

import error.CBGNException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import networking.CBGNClient;
import networking.CBGNClientListener;
import networking.CBGNServer;
import networking.CBGNServerListener;

/**
 * The ServerMainUI class is a simple UI that executed the basic functionality
 * of the CBGN API. You can start a server, connect to it with a client and then
 * message back and forth while keeping track of the ping between
 * clients/server. The goal of this class isn't to be a game or anything, but to
 * exercise the API for both ease-of-implementation and testing purposes.
 *
 * @author Chris
 */
public class ServerMainUI extends javax.swing.JFrame implements CBGNServerListener, CBGNClientListener {

    private CBGNServer server;
    private Thread serverThread, clientThread;
    private CBGNClient client;

    /**
     * Creates new form ServerMainUI
     */
    public ServerMainUI() {
        initComponents();

        this.serverPortField.setText("" + CBGNServer.DEFAULT_SERVER_PORT);
        this.serverUDPPortField.setText("" + CBGNServer.DEFAULT_SERVER_UDP_PORT);

        this.clientPortField.setText("" + CBGNClient.DEFAULT_CLIENT_TCP_PORT);
        this.targetUDPPortField.setText("" + CBGNClient.DEFAULT_CLIENT_UDP_PORT);
    }

    /**
     *
     */
    @Override
    public void onBegin() {
        log("Server started.");
    }

    /**
     *
     * @param name
     */
    @Override
    public void onConnection(String name) {
        log("Server: new connection! \"" + name + "\"");
    }

    /**
     *
     * @param data
     */
    @Override
    public void onMessage(HashMap<String, String> data) {
        log("Server received: " + data.toString());
    }

    @Override
    public void onUDPMessage(HashMap<String, String> data) {
        log("Server received UDP: " + data.toString());
    }

    /**
     *
     * @param socket
     * @param reason
     */
    @Override
    public void onConnectionClosed(Socket socket, CBGNException reason) {
        log("Connection has quit for reason: " + reason.getMessage());
    }

    /**
     *
     * @param except
     */
    @Override
    public void onStopped(CBGNException except) {
        log("Server stopped with exception: "
                + (except == null ? "None" : except.getMessage()));
    }

    // client methods
    @Override
    public void onClientConnected() {
        log("\tClient successfully connected!");
    }

    @Override
    public void onClientMessage(HashMap<String, String> data) {
        log("\tClient received TCP: " + data.toString());
    }

    @Override
    public void onClientUDPMessage(HashMap<String, String> data) {
        log("\tClient received UDP: " + data.toString());
    }

    @Override
    public void onClientConnectionClosed(CBGNException reason) {
        log("\tClient stopped with exception: "
                + (reason == null ? "None" : reason.getMessage()));
    }

    /**
     *
     * @param message
     */
    public void log(String message) {
        System.out.println(message);
        this.serverLogArea.append("\n" + message);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        startServerButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        maxConnectionCountField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        serverPortField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        serverSendButton = new javax.swing.JButton();
        serverMessageField = new javax.swing.JTextField();
        serverTCPCheck = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        serverUDPPortField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        serverLocationField = new javax.swing.JTextField();
        connectToServerButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        messageField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        clientPortField = new javax.swing.JTextField();
        clientTCPCheck = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        targetUDPPortField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        serverLogArea = new javax.swing.JTextArea();
        lockToBottomCheck = new javax.swing.JCheckBox();
        verboseCheck = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        connectionPingTable = new javax.swing.JTable();

        jLabel1.setText("jLabel1");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CBGameNetwork UI");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Server"));

        startServerButton.setText("Start Server");
        startServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startServerButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Maximum connections:");

        maxConnectionCountField.setText("16");

        jLabel6.setText("Port:");

        serverPortField.setText("1776");

        jLabel7.setText("Message:");

        serverSendButton.setText("Send");
        serverSendButton.setEnabled(false);
        serverSendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverSendButtonActionPerformed(evt);
            }
        });

        serverMessageField.setText("Hello, client!");
        serverMessageField.setEnabled(false);

        serverTCPCheck.setSelected(true);
        serverTCPCheck.setText("TCP");

        jLabel9.setText("UDP port:");

        serverUDPPortField.setText("1777");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(35, 35, 35)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(maxConnectionCountField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(serverPortField, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(serverUDPPortField, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(startServerButton))
                            .addComponent(serverTCPCheck, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(serverMessageField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(serverSendButton)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(serverPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startServerButton)
                    .addComponent(jLabel9)
                    .addComponent(serverUDPPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(maxConnectionCountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serverTCPCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(serverMessageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(serverSendButton))
                    .addComponent(jLabel7))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Client"));

        jLabel2.setText("Server address:");

        serverLocationField.setText("localhost");

        connectToServerButton.setText("Connect to Server");
        connectToServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectToServerButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Message:");

        messageField.setText("Hello, server!");
        messageField.setEnabled(false);

        sendButton.setText("Send");
        sendButton.setEnabled(false);
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Port:");

        clientPortField.setText("1776");

        clientTCPCheck.setSelected(true);
        clientTCPCheck.setText("TCP");

        jLabel8.setText("Server UDP:");

        targetUDPPortField.setText("1777");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(messageField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(serverLocationField, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(targetUDPPortField)
                            .addComponent(clientPortField, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(connectToServerButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clientTCPCheck, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(serverLocationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connectToServerButton)
                    .addComponent(jLabel5)
                    .addComponent(clientPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clientTCPCheck)
                    .addComponent(jLabel8)
                    .addComponent(targetUDPPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(messageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        serverLogArea.setColumns(20);
        serverLogArea.setLineWrap(true);
        serverLogArea.setRows(5);
        serverLogArea.setWrapStyleWord(true);
        serverLogArea.setEnabled(false);
        jScrollPane3.setViewportView(serverLogArea);

        lockToBottomCheck.setSelected(true);
        lockToBottomCheck.setText("Lock to Bottom");
        lockToBottomCheck.setEnabled(false);
        lockToBottomCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockToBottomCheckActionPerformed(evt);
            }
        });

        verboseCheck.setSelected(true);
        verboseCheck.setText("Verbose");
        verboseCheck.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(verboseCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                .addComponent(lockToBottomCheck)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lockToBottomCheck)
                    .addComponent(verboseCheck))
                .addContainerGap())
        );

        connectionPingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Connection", "Ping"
            }
        ));
        connectionPingTable.setEnabled(false);
        jScrollPane2.setViewportView(connectionPingTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startServerButtonActionPerformed
        try {
            int port = Integer.parseInt(this.serverPortField.getText());

            server = new CBGNServer(port);
            server.registerListener(this);
            serverThread = new Thread(server);
            serverThread.start();

            this.serverMessageField.setEnabled(true);
            this.serverSendButton.setEnabled(true);
            this.serverLogArea.setEnabled(true);
            this.lockToBottomCheck.setEnabled(true);
            this.verboseCheck.setEnabled(true);
            this.connectionPingTable.setEnabled(true);

            log("Server started on ports"
                    + "\n\tTCP: " + server.getTcpPort()
                    + "\n\tUDP: " + server.getUdpPort()
                    + "\n\tbUDP: " + server.getUdpBroadcastPort());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a server port that is a number.");
        }
    }//GEN-LAST:event_startServerButtonActionPerformed

    private void connectToServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectToServerButtonActionPerformed
        String serverLocation = this.serverLocationField.getText();

        if (serverLocation.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a server location.");
            return;
        }

        try {
            int port = Integer.parseInt(this.clientPortField.getText());
            int udpPort = CBGNClient.DEFAULT_CLIENT_UDP_PORT;
            int serverUDPPort = Integer.parseInt(this.targetUDPPortField.getText());

            InetAddress addr = InetAddress.getByName(serverLocation);

            client = new CBGNClient(this, addr, port, udpPort, serverUDPPort);

            log("Client starting on ports..."
                    + "\n\tTCP: " + client.getTcpPort()
                    + "\n\tUDP: " + client.getUdpPort()
                    + "\n\tbUDP: " + client.getServerUDPPort());

            clientThread = new Thread(client);
            clientThread.start();

            this.messageField.setEnabled(true);
            this.sendButton.setEnabled(true);
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(this, "Please enter a server IP address or 'localhost'.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a port that is a number.");
        }
    }//GEN-LAST:event_connectToServerButtonActionPerformed

    private void lockToBottomCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lockToBottomCheckActionPerformed

    }//GEN-LAST:event_lockToBottomCheckActionPerformed

    private void serverSendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverSendButtonActionPerformed
        if (server != null) {
            String message = this.serverMessageField.getText();
            if (!message.isEmpty()) {
                HashMap<String, String> data = new HashMap<>();
                data.put("message", message);
                try {
                    if (serverTCPCheck.isSelected()) {
                        server.broadcastMessage(data);
                    } else {
                        server.broadcastUDPMessage(data);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ServerMainUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_serverSendButtonActionPerformed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        if (client != null) {
            String message = this.messageField.getText();
            if (!message.isEmpty()) {
                HashMap<String, String> data = new HashMap<>();
                data.put("message", message);
                try {
                    if (clientTCPCheck.isSelected()) {
                        client.sendMessage(data);
                    } else {
                        client.sendUDPMessage(data);
                    }
                } catch (IOException e) {
                    log("Could not send message: " + e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_sendButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerMainUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField clientPortField;
    private javax.swing.JCheckBox clientTCPCheck;
    private javax.swing.JButton connectToServerButton;
    private javax.swing.JTable connectionPingTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JCheckBox lockToBottomCheck;
    private javax.swing.JTextField maxConnectionCountField;
    private javax.swing.JTextField messageField;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextField serverLocationField;
    private javax.swing.JTextArea serverLogArea;
    private javax.swing.JTextField serverMessageField;
    private javax.swing.JTextField serverPortField;
    private javax.swing.JButton serverSendButton;
    private javax.swing.JCheckBox serverTCPCheck;
    private javax.swing.JTextField serverUDPPortField;
    private javax.swing.JButton startServerButton;
    private javax.swing.JTextField targetUDPPortField;
    private javax.swing.JCheckBox verboseCheck;
    // End of variables declaration//GEN-END:variables
}

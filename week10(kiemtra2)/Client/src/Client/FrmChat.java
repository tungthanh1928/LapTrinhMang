package Client;

import Models.FileDownloader;
import Models.ImageUtils;
import Models.RunnableServerFile;
import Models.Session;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.nio.file.Files;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


public class FrmChat extends javax.swing.JFrame {
    private final String idRoom ; 
    private boolean isAdmin = false;
    private DefaultTableModel tableModel;
    
    private class MessageListener implements Runnable{
        private final BufferedReader reader;
        private boolean flag = true;
        private final FrmChat dis;
        
        public MessageListener(FrmChat dis, BufferedReader reader){
            this.reader = reader;
            this.dis = dis;
        }
        
        public void interrupted(){
            flag = false;
        }
        
        @Override
        public void run() {
            boolean isAfk = false;
            while(flag){
                try {
                    String messageReceive = this.reader.readLine();
                    String function = messageReceive.split("\\|")[0];
                    String data = messageReceive.split("\\|")[1];
                    switch(function){
                        case "admin":{
                            dis.isAdmin = Integer.parseInt(data) == 1 ? true : false;
                            if(!dis.isAdmin){
                                
                            }
                            break;
                        }
                        //Example: new_message|message
                        case "new_message" :{
                            addMessage(data);
                            break;
                        }
                        case "accept_leave":{
                            new FrmDashboard("").setVisible(true);
                            dis.dispose();
                            flag = false;
                            break;
                        }
                        case "ask_leave":{
                            int askOwnerDeleteroom = JOptionPane.
                                    showConfirmDialog(null, "Xác nhận rời phòng?\nThao tác này sẽ xóa phòng và kick tất cả người dùng khác trong phòng", "Xác nhận rời phòng", JOptionPane.YES_NO_CANCEL_OPTION);
                            switch (askOwnerDeleteroom) {
                                case JOptionPane.YES_OPTION:{
                                    String command = String.format("leave_room-%s-1", idRoom);
                                    Session.gI().sendMessage(command);
                                    break;
                                }
                                default:{
                                    break;
                                }
                            }
                            break;
                        }
                        case "kick":{
                            JOptionPane.showMessageDialog(null, "Bạn đã kick khỏi phòng bởi " + data);
                            new FrmDashboard("").setVisible(true);
                            dis.dispose();
                            flag = false;
                            break;
                        }
                        case "roomHasBeenDeleted" :{
                            JOptionPane.showMessageDialog(null, "Phòng đã bị xóa khỏi server bởi chủ phòng");
                            new FrmDashboard("").setVisible(true);
                            dis.dispose();
                            flag = false;
                            break;
                        }
                        case "persons":{
                            tableModel = (DefaultTableModel) tableUser.getModel();
                            tableModel.setRowCount(0);
                            String[] person_arr = data.split(",");
                            for(String person_data: person_arr){
                                String []arr = person_data.split("\\#");
                                tableModel.addRow(new Object[]{arr[0], arr[1], arr[2]});
                            }
                            break;
                        }
                        case "deny_set_password":{
                            JOptionPane.showMessageDialog(null, "Không thể đặt mật khẩu lúc này !");
                            break;
                        }
                        case "deny_upload_file":{
                            JOptionPane.showMessageDialog(null, "Có lỗi trong quá trình tải file!");
                            break;
                        }
                        case "new_attachment":{
                            Session.gI().sendMessage("get_files-null");
                            break;
                        }
                        case "files":{
                            if(data.equals("null")) break;
                            DefaultTableModel model = (DefaultTableModel)tableFiles.getModel();
                            model.setRowCount(0);
                            
                            String[] files = data.split("\\,");
                            for(String dataFile: files){
                                String[] tmp = dataFile.split("\\#");
                                model.addRow(new Object[]{tmp[0], tmp[1], tmp[2], tmp[3]});
                            }
                            break;
                        }
                        case "accept_delete_file":{
                            JOptionPane.showMessageDialog(null,
                                    "Xóa file thành công " + data);
                            break;
                        }
                        case "deny_delete_file":{
                            JOptionPane.showMessageDialog(null,
                                    "Xóa file thất bại " + data);
                            break;
                        }
                        case "file_not_found":{
                            JOptionPane.showMessageDialog(null,
                                    "Không tìm thấy file " + data);
                            break;
                        }
                        case "not_owner":{
                            JOptionPane.showMessageDialog(null,
                                    "Bạn không có quyền xóa file " + data);
                            break;
                        }
                        case "afk":{
                            isAfk = true;
                            JOptionPane.showMessageDialog(null, "Bạn đã bị đăng xuất do treo quá lâu !");
                            new FrmLogin().setVisible(true);
                            Session.gI().resetSession();
                            dis.dispose();
                            break;
                        }
                    }
                } catch (Exception e) {
                    if(!isAfk)
                        JOptionPane.showMessageDialog(null, "Server đã đóng ! Lập tức ngắt kết nối khỏi server.");
                    dis.dispose();
                    break;
                }
            }
        }
        
    }
    
    //handle GUI
    private void handleSelectionChanged(){
        ListSelectionModel selectionModel = tableFiles.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Chỉ cho phép chọn một hàng
        
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tableFiles.getSelectedRow();
                    if (selectedRow != -1) { // Đảm bảo có hàng được chọn
                        String fileType = (String)tableFiles.getValueAt(selectedRow, 2);
                        if(fileType.equals("image/jpeg")){
                            String owner = (String)tableFiles.getValueAt(selectedRow, 3);
                            String fileName = (String)tableFiles.getValueAt(selectedRow, 0);
                            new Thread(new ImageUtils.SetIconForLabel(img, Session.host, owner.replaceAll("\\ ", "") + "__" + fileName)).start();
                        }
                        else{
                            new Thread(new ImageUtils.SetIconForLabel(img, Session.host, "image_template.png")).start();
                        }
                    }
                }
            }
        });
    }
    
    private void addMessage(String message){
        String current = mainTextArea.getText();
        mainTextArea.setText(current + "\n" + message);
    }
    
    public FrmChat(String idRoom) {
        this.setLocation(300, 400);
        UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
        try {
            UIManager.setLookAndFeel(lafInfo[3].getClassName());
        } catch (Exception e) {
        }
        initComponents();
        new Thread(new MessageListener(this, Session.gI().getReader())).start();
        Session.gI().sendMessage("joinchat-"+idRoom);
        this.setTitle(String.format("User %s - %s", Session.gI().personID, Session.gI().personName));
        handleSelectionChanged();
        
        
        
        this.idRoom = idRoom;
        
        //Check admin 
        Session.gI().sendMessage("check_admin-null");
        //Load user
        Session.gI().sendMessage("get_persons-null");
        //Load file
        Session.gI().sendMessage("get_files-null");
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainTextArea = new javax.swing.JTextArea();
        btnSend = new javax.swing.JButton();
        btnSendImage = new javax.swing.JButton();
        btnSendFile = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        lbNamePhong = new javax.swing.JLabel();
        txtInput = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableUser = new javax.swing.JTable();
        img = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableFiles = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        mainTextArea.setEditable(false);
        mainTextArea.setColumns(20);
        mainTextArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mainTextArea.setRows(5);
        jScrollPane1.setViewportView(mainTextArea);

        btnSend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/send.png"))); // NOI18N
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        btnSendImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/image.png"))); // NOI18N
        btnSendImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendImageActionPerformed(evt);
            }
        });

        btnSendFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/attachment.png"))); // NOI18N
        btnSendFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendFileActionPerformed(evt);
            }
        });

        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/exit.png"))); // NOI18N
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lbNamePhong.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        lbNamePhong.setText("Chat room");

        txtInput.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInputActionPerformed(evt);
            }
        });

        tableUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID người dùng", "Tên người dùng", "Vai trò"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableUser);

        tableFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên tập tin", "Dung lượng", "Loại", "Người gửi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableFiles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableFilesMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tableFiles);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbNamePhong)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jScrollPane4)
                                    .addComponent(txtInput, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnSendImage, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSendFile, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(img, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(0, 11, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(lbNamePhong)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInput, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSendFile, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSendImage, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(img, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(67, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        //15
        String msg = txtInput.getText();
        if(msg == null || msg.strip().equals("")){
            return;
        }
        String message = Session.gI().personName + " : " + msg;
        String command = String.format("send_msg-%s-%s", Session.gI().personID, msg.replaceAll("\\-", "?"));
        Session.gI().sendMessage(command);
        addMessage(message);
        txtInput.setText("");
    }//GEN-LAST:event_btnSendActionPerformed

   
    private void txtInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInputActionPerformed
        btnSendActionPerformed(evt);
    }//GEN-LAST:event_txtInputActionPerformed

   
    private void btnSendFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendFileActionPerformed
        JFileChooser fc = new JFileChooser("./");
        fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        if(file != null){
            Session.gI().sendFile(file);
        }
    }//GEN-LAST:event_btnSendFileActionPerformed

   
    private void btnSendImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendImageActionPerformed
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image (.png, .jpg or .jpeg)", "png", "jpg", "jpeg");
        JFileChooser fc = new JFileChooser("./");
        fc.setFileFilter(filter);
        fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        if(file != null){
            new FrmPreviewImage(file).setVisible(true);
        }
    }//GEN-LAST:event_btnSendImageActionPerformed

    private void tableFilesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableFilesMouseClicked
        
    }//GEN-LAST:event_tableFilesMouseClicked

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        Session.gI().sendMessage("leave_room-" + idRoom +"-0");
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton btnSendFile;
    private javax.swing.JButton btnSendImage;
    private javax.swing.JLabel img;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbNamePhong;
    private javax.swing.JTextArea mainTextArea;
    private javax.swing.JTable tableFiles;
    private javax.swing.JTable tableUser;
    private javax.swing.JTextField txtInput;
    // End of variables declaration//GEN-END:variables
}

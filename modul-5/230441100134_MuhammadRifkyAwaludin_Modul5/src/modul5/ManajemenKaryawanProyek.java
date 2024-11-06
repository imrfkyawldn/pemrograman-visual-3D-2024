/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package modul5;

//Import 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//end Import

/**
 *
 * @author Aldinn
 */
public class ManajemenKaryawanProyek extends javax.swing.JFrame {
    //inisiasi GLobal Variable
    Connection conn;
    private DefaultTableModel model;
    private DefaultTableModel model2;
    private DefaultTableModel model3;
    private ArrayList<Integer> karyawanIds = new ArrayList<>();
    private ArrayList<Integer> proyekIds = new ArrayList<>();
    
    public ManajemenKaryawanProyek() {
        initComponents();
        conn = koneksi.getConnection();
        
        model = new DefaultTableModel();
        tbl_karyawan.setModel(model);

        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("Jabatan");
        model.addColumn("Departemen");
        
        model2 = new DefaultTableModel();
        tbl_proyek.setModel(model2);
        
        model2.addColumn("ID");
        model2.addColumn("Nama Proyek");
        model2.addColumn("Durasi Pengerjaan");
        
        model3 = new DefaultTableModel();
        tbl_transaksi.setModel(model3);

        //model3.addColumn("ID Karyawan");
        model3.addColumn("Nama Karyawan");
        //model3.addColumn("ID Proyek");
        model3.addColumn("Nama Proyek");
        model3.addColumn("Peran");
        loadData();
        loadData2();
        loadData3();
        //loadKaryawanId();
        //loadProyekId();
        loadComboProyek();
        loadComboKaryawan();
        
        tbl_karyawan.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent evt) {
        int selectedRow = tbl_karyawan.getSelectedRow(); // Mendapatkan baris
        if (selectedRow != -1) { // Pastikan ada baris yang dipilih
            tf_id_karyawan.setText(model.getValueAt(selectedRow, 0).toString()); 
            tf_nama_karyawan.setText(model.getValueAt(selectedRow, 1).toString());
            tf_jabatan_karyawan.setText(model.getValueAt(selectedRow, 2).toString()); 
            tf_departemen_karyawan.setText(model.getValueAt(selectedRow, 3).toString());
        }
    }
});
        tbl_proyek.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent evt) {
        int selectedRow = tbl_proyek.getSelectedRow();
        if (selectedRow != -1) {
            tf_id_proyek.setText(model2.getValueAt(selectedRow, 0).toString());
            tf_nama_proyek.setText(model2.getValueAt(selectedRow, 1).toString());
            tf_durasi_pengerjaan.setText(model2.getValueAt(selectedRow, 2).toString());
        }
    }
});
        tbl_transaksi.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent evt) {
        int selectedRow = tbl_transaksi.getSelectedRow();

        if (selectedRow != -1) {
            try {
                // Adjusted indices if necessary
                String selectedKaryawan = model3.getValueAt(selectedRow, 0).toString(); // First column
                System.out.println("Pilih Karyawan: " + selectedKaryawan);
                cb_namakaryawan.setSelectedItem(selectedKaryawan);
                tf_nama_karyawan.setText(selectedKaryawan);

                String selectedProyek = model3.getValueAt(selectedRow, 1).toString(); // Second column
                System.out.println("Pilih Proyek: " + selectedProyek);
                cb_namaproyek.setSelectedItem(selectedProyek);
                tf_nama_proyek.setText(selectedProyek);

                String peran = model3.getValueAt(selectedRow, 2).toString(); // Third column
                System.out.println("Peran: " + peran);
                tf_peran.setText(peran);

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Error retrieving data from the selected row.");
            }
        }
    }
});

    }
    private int getSelectedKaryawanId() {
        int selectedIndex = cb_namakaryawan.getSelectedIndex();
        return selectedIndex >= 0 ? karyawanIds.get(selectedIndex) : -1;
    }

    private int getSelectedProyekId() {
        int selectedIndex = cb_namaproyek.getSelectedIndex();
        return selectedIndex >= 0 ? proyekIds.get(selectedIndex) : -1;
    }
    
    private void loadComboKaryawan() {
        cb_namakaryawan.removeAllItems(); // Menghapus semua item di combobox
        cb_namakaryawan.addItem("- Pilih Nama Karyawan -"); // Menambahkan opsi default
    
        karyawanIds.clear(); // Membersihkan daftar proyek
        karyawanIds.add(-1); // Tambahkan ID proyek untuk opsi default
        
        try {
            String sql = "SELECT id, nama FROM karyawan";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");

                karyawanIds.add(id);
                cb_namakaryawan.addItem(nama);
            }
        } catch (SQLException e) {
            System.out.println("Error loading karyawan data: " + e.getMessage());
        }
    }

    private void loadComboProyek() {
        cb_namaproyek.removeAllItems(); // Menghapus semua item di combobox
        cb_namaproyek.addItem("- Pilih Nama Proyek -"); // Menambahkan opsi default
    
        proyekIds.clear(); // Membersihkan daftar proyek
        proyekIds.add(-1); // Tambahkan ID proyek untuk opsi default

        try {
            String sql = "SELECT id, nama_proyek FROM proyek";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String namaProyek = rs.getString("nama_proyek");

                proyekIds.add(id);
                cb_namaproyek.addItem(namaProyek);
            }
        } catch (SQLException e) {
            System.out.println("Error loading proyek data: " + e.getMessage());
        }
    }
    
    //Load Data Tabel Karyawan
    private void loadData() {
        model.setRowCount(0);

        String sql = "SELECT * FROM karyawan";
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet hasil = ps.executeQuery()) {
             
            while (hasil.next()) {
                model.addRow(new Object[]{
                    hasil.getInt("id"),
                    hasil.getString("nama"),
                    hasil.getString("jabatan"),
                    hasil.getString("departemen") 
                });
            }
        } catch (SQLException e) {
            System.out.println("Error Loading Data: " + e.getMessage());
        }
    }
    //end Load Data Tabel Karyawan
    
    //Load Data Tabel Proyek
    private void loadData2() {
        model2.setRowCount(0);

      try {
        String sql = "SELECT * FROM proyek";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
           model2.addRow(new Object[]{
           rs.getInt("id"),
           rs.getString("nama_proyek"),
           rs.getString("durasi_pengerjaan")
         });
        }
        } catch (SQLException e) {
         System.out.println("Error Save Data" + e.getMessage());
       }
    }
    
    //Load Data Tabel Transaksi
    private void loadData3() {
      model3.setRowCount(0);

      try {
        String sql = """
            SELECT t.id_karyawan, k.nama AS nama, 
                   t.id_proyek, p.nama_proyek, t.peran
            FROM transaksi t
            JOIN karyawan k ON t.id_karyawan = k.id
            JOIN proyek p ON t.id_proyek = p.id
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
           model3.addRow(new Object[]{
           //rs.getString("id_karyawan"),
           rs.getString("nama"),
           //rs.getString("id_proyek"),
           rs.getString("nama_proyek"),
           rs.getString("peran")
         });
        }
        } catch (SQLException e) {
         System.out.println("Error Save Datainij" + e.getMessage());
       }
    }
    
    //Simpan Data Karyawan
    private void TambahDataKaryawan() {
    if (!tf_id_karyawan.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Harap kosongkan ID!");
        return;
    }

    StringBuilder missingFields = new StringBuilder("Harap masukkan data:");
    boolean hasMissingFields = false; 
    if (tf_nama_karyawan.getText().isEmpty()) {
        missingFields.append("\n- Nama Karyawan");
        hasMissingFields = true; 
    }
    if (tf_jabatan_karyawan.getText().isEmpty()) {
        missingFields.append("\n- Jabatan");
        hasMissingFields = true; 
    }
    if (tf_departemen_karyawan.getText().isEmpty()) {
        missingFields.append("\n- Departemen");
        hasMissingFields = true; 
    }
    if (hasMissingFields) {
        JOptionPane.showMessageDialog(this, missingFields.toString());
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menambahkan data karyawan?", "Konfirmasi Tambah", 
            JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String sql = "INSERT INTO karyawan (nama, jabatan, departemen) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tf_nama_karyawan.getText());
            ps.setString(2, tf_jabatan_karyawan.getText());
            ps.setString(3, tf_departemen_karyawan.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan");

            loadData();
            loadComboKaryawan();
            resetForm();
        } catch (SQLException e) {
            System.out.println("Error Save Data: " + e.getMessage());
        }
    }
}
    //End Simpan Data Karyawan
    
    //Simpan Data Proyek
    private void TambahDataProyek() {
    if (!tf_id_proyek.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Harap kosongkan ID Proyek");
        return;
    }

    StringBuilder missingFields = new StringBuilder("Harap masukkan data:");
    boolean hasMissingFields = false; // Flag to check for missing fields
    if (tf_nama_proyek.getText().isEmpty()) {
        missingFields.append("\n- Nama Proyek");
        hasMissingFields = true;
    }
    if (tf_durasi_pengerjaan.getText().isEmpty()) {
        missingFields.append("\n- Durasi Pengerjaan");
        hasMissingFields = true;
    }
    if (hasMissingFields) {
        JOptionPane.showMessageDialog(this, missingFields.toString());
        return;
    }
    try {
        int durasi = Integer.parseInt(tf_durasi_pengerjaan.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Tolong masukkan Durasi berupa angka!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menambahkan data proyek?", "Konfirmasi Tambah", 
            JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String sql = "INSERT INTO proyek (nama_proyek, durasi_pengerjaan) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tf_nama_proyek.getText());
            ps.setString(2, tf_durasi_pengerjaan.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan");

            loadData2();
            loadComboProyek();
            resetForm();
        } catch (SQLException e) {
            System.out.println("Error Save Data proyek: " + e.getMessage());
        }
    }
}
    //End Simpan Data Proyek
    
    //Simpan Data Transaksi
    private void TambahDataTransaksi() {
    int karyawanId = getSelectedKaryawanId();
    int proyekId = getSelectedProyekId();
    String peran = tf_peran.getText();
    
    if (karyawanId == -1 || proyekId == -1 || peran.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Isi Terlebih Dahulu Bagian Yang Kosong!!!");
        return;
    }

    try {
        String sql = "INSERT INTO transaksi (id_karyawan, id_proyek, peran) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, karyawanId);
        ps.setInt(2, proyekId);
        ps.setString(3, peran);
        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan");
        loadData3(); 
        resetForm();
        } catch (SQLException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }
    //End Simpan Data Transaksi
    
    //Update Data Karyawan
    private void UbahDataKaryawan() {
    if (tf_nama_karyawan.getText().isEmpty() || tf_jabatan_karyawan.getText().isEmpty() || 
            tf_departemen_karyawan.getText().isEmpty() || tf_id_karyawan.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Tolong lengkapi data karyawan yang akan diubah!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin mengubah data karyawan?", "Konfirmasi Ubah", 
            JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String sql = "UPDATE karyawan SET nama = ?, jabatan = ?, departemen = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tf_nama_karyawan.getText());
            ps.setString(2, tf_jabatan_karyawan.getText());
            ps.setString(3, tf_departemen_karyawan.getText());
            ps.setInt(4, Integer.parseInt(tf_id_karyawan.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Berhasil Diubah");
            loadData();
            resetForm();
        } catch (SQLException e) {
            System.out.println("Error Save Data" + e.getMessage());
        }
    }
}
    //End Update Data Karyawan
    
    //Update Data Proyek
    private void UbahDataProyek() {
    if (tf_nama_proyek.getText().isEmpty() || tf_durasi_pengerjaan.getText().isEmpty() || 
            tf_id_proyek.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Tolong lengkapi data proyek yang akan diubah!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin mengubah data proyek?", "Konfirmasi Ubah", 
            JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String sql = "UPDATE proyek SET nama_proyek = ?, durasi_pengerjaan = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tf_nama_proyek.getText());
            ps.setString(2, tf_durasi_pengerjaan.getText());
            ps.setInt(3, Integer.parseInt(tf_id_proyek.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Berhasil Diubah");
            loadData2();
            resetForm();
        } catch (SQLException e) {
            System.out.println("Error Save Data" + e.getMessage());
        }
    }
}
    //End Update Data Proyek
    
    //Update Data Transaksi
    private void UbahDataTransaksi() {
    int karyawanId = getSelectedKaryawanId();
    int proyekId = getSelectedProyekId();
    String peran = tf_peran.getText();

    if (karyawanId == -1 || proyekId == -1 || peran.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Tolong lengkapi data transaksi yang akan diubah!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin mengubah data transaksi?", "Konfirmasi Ubah", 
            JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String sql = "UPDATE transaksi SET peran = ? WHERE id_karyawan = ? AND id_proyek = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, peran);
            ps.setInt(2, karyawanId);
            ps.setInt(3, proyekId);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Data Berhasil Diubah");
                loadData3();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Update Gagal");
            }
        } catch (SQLException e) {
            System.out.println("Error updating transaction: " + e.getMessage());
        }
    }
}
    //End Update Transaksi
    
    //Delete Data Karyawan
    private void HapusDataKaryawan() {
    if (tf_id_karyawan.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Tolong pilih karyawan yang akan dihapus!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data karyawan?", "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String sql = "DELETE FROM karyawan WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(tf_id_karyawan.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus");
            loadData();
            resetForm();
        } catch (SQLException e) {
            System.out.println("Error deleting karyawan: " + e.getMessage());
        }
    }
}
    //End Delete Data Karyawan
    
    //Delete Data Proyek
    private void HapusDataProyek() {
    if (tf_id_proyek.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Tolong pilih proyek yang akan dihapus!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data proyek?", "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String sql = "DELETE FROM proyek WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(tf_id_proyek.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus");
            loadData2();
            resetForm();
        } catch (SQLException e) {
            System.out.println("Error deleting proyek: " + e.getMessage());
        }
    }
}
    //End Delete Data Proyek
    
    //Delete Data Transaksi
    private void HapusDataTransaksi() {
    int karyawanId = getSelectedKaryawanId();
    int proyekId = getSelectedProyekId();

    if (karyawanId == -1 || proyekId == -1) {
        JOptionPane.showMessageDialog(this, "Tolong pilih transaksi yang akan dihapus!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data transaksi?", "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            String sql = "DELETE FROM transaksi WHERE id_karyawan = ? AND id_proyek = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, karyawanId);
            ps.setInt(2, proyekId);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus");
                loadData3();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Data Gagal Dihapus");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting transaction: " + e.getMessage());
        }
    }
}
    //End Delete Data Transaksi
    
    //Clear Otomatis
    private void resetForm() {
    tf_nama_karyawan.setText("");
    tf_jabatan_karyawan.setText("");
    tf_departemen_karyawan.setText("");
    tf_id_karyawan.setText("");
    tf_nama_proyek.setText("");
    tf_durasi_pengerjaan.setText("");
    tf_id_proyek.setText("");
    tf_peran.setText("");
    cb_namakaryawan.setSelectedIndex(0);
    cb_namaproyek.setSelectedIndex(0);
}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        PanelKaryawan = new javax.swing.JPanel();
        PanelBawah2 = new javax.swing.JPanel();
        btn_tambah_karyawan = new javax.swing.JButton();
        btn_edit_karyawan = new javax.swing.JButton();
        btn_hapus_karyawan = new javax.swing.JButton();
        btn_keluar_karyawan1 = new javax.swing.JButton();
        PanelTengah2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_karyawan = new javax.swing.JTable();
        PanelAtas2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        tf_id_karyawan = new javax.swing.JTextField();
        tf_nama_karyawan = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        tf_departemen_karyawan = new javax.swing.JTextField();
        tf_jabatan_karyawan = new javax.swing.JTextField();
        PanelProyek = new javax.swing.JPanel();
        PanelBawah1 = new javax.swing.JPanel();
        btn_tambah_proyek = new javax.swing.JButton();
        btn_edit_proyek = new javax.swing.JButton();
        btn_hapus_proyek = new javax.swing.JButton();
        btn_keluar_proyek = new javax.swing.JButton();
        PanelTengah1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_proyek = new javax.swing.JTable();
        PanelAtas1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tf_id_proyek = new javax.swing.JTextField();
        tf_nama_proyek = new javax.swing.JTextField();
        tf_durasi_pengerjaan = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        PanelTransaksi = new javax.swing.JPanel();
        PanelBawah3 = new javax.swing.JPanel();
        btn_tambah_transaksi = new javax.swing.JButton();
        btn_edit_trasnsaksi = new javax.swing.JButton();
        btn_hapus_transaksi = new javax.swing.JButton();
        btn_keluar_transaksi = new javax.swing.JButton();
        PanelTengah3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tbl_transaksi = new javax.swing.JTable();
        PanelAtas3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        tf_peran = new javax.swing.JTextField();
        cb_namakaryawan = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        cb_namaproyek = new javax.swing.JComboBox<>();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manajemen Karyawan Proyek");

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(30000, 32767));

        PanelKaryawan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelBawah2.setBackground(new java.awt.Color(227, 173, 84));
        PanelBawah2.setLayout(null);

        btn_tambah_karyawan.setBackground(new java.awt.Color(76, 77, 82));
        btn_tambah_karyawan.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_tambah_karyawan.setForeground(new java.awt.Color(227, 173, 84));
        btn_tambah_karyawan.setText("TAMBAH");
        btn_tambah_karyawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_karyawanActionPerformed(evt);
            }
        });
        PanelBawah2.add(btn_tambah_karyawan);
        btn_tambah_karyawan.setBounds(320, 10, 90, 27);

        btn_edit_karyawan.setBackground(new java.awt.Color(76, 77, 82));
        btn_edit_karyawan.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_edit_karyawan.setForeground(new java.awt.Color(227, 173, 84));
        btn_edit_karyawan.setText("PERBARUI");
        btn_edit_karyawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_edit_karyawanActionPerformed(evt);
            }
        });
        PanelBawah2.add(btn_edit_karyawan);
        btn_edit_karyawan.setBounds(410, 10, 90, 27);

        btn_hapus_karyawan.setBackground(new java.awt.Color(76, 77, 82));
        btn_hapus_karyawan.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_hapus_karyawan.setForeground(new java.awt.Color(227, 173, 84));
        btn_hapus_karyawan.setText("HAPUS");
        btn_hapus_karyawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapus_karyawanActionPerformed(evt);
            }
        });
        PanelBawah2.add(btn_hapus_karyawan);
        btn_hapus_karyawan.setBounds(500, 10, 76, 27);

        btn_keluar_karyawan1.setBackground(new java.awt.Color(76, 77, 82));
        btn_keluar_karyawan1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_keluar_karyawan1.setForeground(new java.awt.Color(227, 173, 84));
        btn_keluar_karyawan1.setText("KELUAR");
        btn_keluar_karyawan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_keluar_karyawan1ActionPerformed(evt);
            }
        });
        PanelBawah2.add(btn_keluar_karyawan1);
        btn_keluar_karyawan1.setBounds(20, 10, 76, 27);

        PanelKaryawan.add(PanelBawah2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 590, 50));

        PanelTengah2.setBackground(new java.awt.Color(76, 77, 82));
        PanelTengah2.setLayout(new java.awt.CardLayout());

        tbl_karyawan.setBackground(new java.awt.Color(76, 77, 82));
        tbl_karyawan.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        tbl_karyawan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "NAMA", "JABATAN", "DEPARTEMEN"
            }
        ));
        tbl_karyawan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane5.setViewportView(tbl_karyawan);

        PanelTengah2.add(jScrollPane5, "card2");

        PanelKaryawan.add(PanelTengah2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 590, 200));

        PanelAtas2.setBackground(new java.awt.Color(227, 173, 84));

        jLabel14.setFont(new java.awt.Font("Segoe UI Black", 0, 31)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(76, 77, 82));
        jLabel14.setText("KARYAWAN");

        jPanel17.setBackground(new java.awt.Color(76, 77, 82));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );

        jLabel15.setIcon(new javax.swing.ImageIcon("C:\\Users\\Toshiba\\Downloads\\karyawan5__1_-removebg-preview.png")); // NOI18N

        jLabel16.setFont(new java.awt.Font("Segoe UI Semibold", 0, 9)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(76, 77, 82));
        jLabel16.setText("PT. Ketenaga Kerjaan Madju Jaya, Jawa Timur, Indonesia.");

        jLabel17.setFont(new java.awt.Font("Segoe UI Semibold", 0, 8)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(76, 77, 82));
        jLabel17.setText("NO.88629, Telp. 0883244");

        jLabel18.setIcon(new javax.swing.ImageIcon("C:\\Users\\Toshiba\\Downloads\\logormh__1_-removebg-preview.png")); // NOI18N

        javax.swing.GroupLayout PanelAtas2Layout = new javax.swing.GroupLayout(PanelAtas2);
        PanelAtas2.setLayout(PanelAtas2Layout);
        PanelAtas2Layout.setHorizontalGroup(
            PanelAtas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PanelAtas2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addGroup(PanelAtas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelAtas2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(52, 52, 52))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelAtas2Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(124, 124, 124))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelAtas2Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(73, 73, 73)))
                .addComponent(jLabel15)
                .addGap(33, 33, 33))
        );
        PanelAtas2Layout.setVerticalGroup(
            PanelAtas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelAtas2Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(PanelAtas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelAtas2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(PanelAtas2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(PanelAtas2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(PanelAtas2Layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17)
                                .addGap(0, 13, Short.MAX_VALUE)))))
                .addContainerGap())
        );

        PanelKaryawan.add(PanelAtas2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 100));

        jPanel2.setBackground(new java.awt.Color(76, 77, 82));

        jLabel19.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("ID                        :");

        jLabel20.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Nama                  :");

        jLabel21.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Jabatan              :");

        jLabel22.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Departemen      :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tf_id_karyawan)
                    .addComponent(tf_nama_karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tf_jabatan_karyawan)
                    .addComponent(tf_departemen_karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(tf_jabatan_karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_departemen_karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(tf_id_karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_nama_karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        PanelKaryawan.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 590, 90));

        jTabbedPane1.addTab("Karyawan", PanelKaryawan);

        PanelProyek.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelBawah1.setBackground(new java.awt.Color(227, 173, 84));
        PanelBawah1.setLayout(null);

        btn_tambah_proyek.setBackground(new java.awt.Color(76, 77, 82));
        btn_tambah_proyek.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_tambah_proyek.setForeground(new java.awt.Color(227, 173, 84));
        btn_tambah_proyek.setText("TAMBAH");
        btn_tambah_proyek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_proyekActionPerformed(evt);
            }
        });
        PanelBawah1.add(btn_tambah_proyek);
        btn_tambah_proyek.setBounds(320, 10, 90, 27);

        btn_edit_proyek.setBackground(new java.awt.Color(76, 77, 82));
        btn_edit_proyek.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_edit_proyek.setForeground(new java.awt.Color(227, 173, 84));
        btn_edit_proyek.setText("PERBARUI");
        btn_edit_proyek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_edit_proyekActionPerformed(evt);
            }
        });
        PanelBawah1.add(btn_edit_proyek);
        btn_edit_proyek.setBounds(410, 10, 90, 27);

        btn_hapus_proyek.setBackground(new java.awt.Color(76, 77, 82));
        btn_hapus_proyek.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_hapus_proyek.setForeground(new java.awt.Color(227, 173, 84));
        btn_hapus_proyek.setText("HAPUS");
        btn_hapus_proyek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapus_proyekActionPerformed(evt);
            }
        });
        PanelBawah1.add(btn_hapus_proyek);
        btn_hapus_proyek.setBounds(500, 10, 76, 27);

        btn_keluar_proyek.setBackground(new java.awt.Color(76, 77, 82));
        btn_keluar_proyek.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_keluar_proyek.setForeground(new java.awt.Color(227, 173, 84));
        btn_keluar_proyek.setText("KELUAR");
        btn_keluar_proyek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_keluar_proyekActionPerformed(evt);
            }
        });
        PanelBawah1.add(btn_keluar_proyek);
        btn_keluar_proyek.setBounds(20, 10, 76, 27);

        PanelProyek.add(PanelBawah1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 590, 50));

        PanelTengah1.setBackground(new java.awt.Color(76, 77, 82));
        PanelTengah1.setLayout(new java.awt.CardLayout());

        tbl_proyek.setBackground(new java.awt.Color(76, 77, 82));
        tbl_proyek.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        tbl_proyek.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "NAMA PROYEK", "DURASI"
            }
        ));
        tbl_proyek.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane4.setViewportView(tbl_proyek);

        PanelTengah1.add(jScrollPane4, "card2");

        PanelProyek.add(PanelTengah1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 590, 200));

        PanelAtas1.setBackground(new java.awt.Color(227, 173, 84));

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 0, 31)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(76, 77, 82));
        jLabel4.setText("PROYEK");

        jPanel16.setBackground(new java.awt.Color(76, 77, 82));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );

        jLabel2.setIcon(new javax.swing.ImageIcon("C:\\Users\\Toshiba\\Downloads\\proyekp4__1_-removebg-preview.png")); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 9)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(76, 77, 82));
        jLabel8.setText("PT. Ketenaga Kerjaan Madju Jaya, Jawa Timur, Indonesia.");

        jLabel9.setFont(new java.awt.Font("Segoe UI Semibold", 0, 8)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(76, 77, 82));
        jLabel9.setText("NO.88629, Telp. 0883244");

        jLabel10.setIcon(new javax.swing.ImageIcon("C:\\Users\\Toshiba\\Downloads\\logormh__1_-removebg-preview.png")); // NOI18N

        javax.swing.GroupLayout PanelAtas1Layout = new javax.swing.GroupLayout(PanelAtas1);
        PanelAtas1.setLayout(PanelAtas1Layout);
        PanelAtas1Layout.setHorizontalGroup(
            PanelAtas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PanelAtas1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addGroup(PanelAtas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelAtas1Layout.createSequentialGroup()
                        .addGroup(PanelAtas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(PanelAtas1Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(jLabel4)))
                        .addGap(47, 47, 47))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelAtas1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(124, 124, 124)))
                .addComponent(jLabel2)
                .addGap(33, 33, 33))
        );
        PanelAtas1Layout.setVerticalGroup(
            PanelAtas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelAtas1Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(PanelAtas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelAtas1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(PanelAtas1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(PanelAtas1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)))
                        .addContainerGap(13, Short.MAX_VALUE))
                    .addGroup(PanelAtas1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        PanelProyek.add(PanelAtas1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 100));

        jPanel1.setBackground(new java.awt.Color(76, 77, 82));

        jLabel11.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("ID                        :");

        jLabel12.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Nama Proyek    :");

        jLabel13.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Durasi Pengerjaan   :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tf_id_proyek)
                    .addComponent(tf_nama_proyek, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tf_durasi_pengerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(tf_id_proyek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_nama_proyek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addContainerGap(13, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_durasi_pengerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(30, 30, 30))
        );

        PanelProyek.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 590, 90));

        jTabbedPane1.addTab("Proyek", PanelProyek);

        PanelTransaksi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelBawah3.setBackground(new java.awt.Color(227, 173, 84));
        PanelBawah3.setLayout(null);

        btn_tambah_transaksi.setBackground(new java.awt.Color(76, 77, 82));
        btn_tambah_transaksi.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_tambah_transaksi.setForeground(new java.awt.Color(227, 173, 84));
        btn_tambah_transaksi.setText("TAMBAH");
        btn_tambah_transaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tambah_transaksiActionPerformed(evt);
            }
        });
        PanelBawah3.add(btn_tambah_transaksi);
        btn_tambah_transaksi.setBounds(310, 10, 90, 27);

        btn_edit_trasnsaksi.setBackground(new java.awt.Color(76, 77, 82));
        btn_edit_trasnsaksi.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_edit_trasnsaksi.setForeground(new java.awt.Color(227, 173, 84));
        btn_edit_trasnsaksi.setText("PERBARUI");
        btn_edit_trasnsaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_edit_trasnsaksiActionPerformed(evt);
            }
        });
        PanelBawah3.add(btn_edit_trasnsaksi);
        btn_edit_trasnsaksi.setBounds(406, 10, 90, 27);

        btn_hapus_transaksi.setBackground(new java.awt.Color(76, 77, 82));
        btn_hapus_transaksi.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_hapus_transaksi.setForeground(new java.awt.Color(227, 173, 84));
        btn_hapus_transaksi.setText("HAPUS");
        btn_hapus_transaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapus_transaksiActionPerformed(evt);
            }
        });
        PanelBawah3.add(btn_hapus_transaksi);
        btn_hapus_transaksi.setBounds(500, 10, 76, 27);

        btn_keluar_transaksi.setBackground(new java.awt.Color(76, 77, 82));
        btn_keluar_transaksi.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 14)); // NOI18N
        btn_keluar_transaksi.setForeground(new java.awt.Color(227, 173, 84));
        btn_keluar_transaksi.setText("KELUAR");
        btn_keluar_transaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_keluar_transaksiActionPerformed(evt);
            }
        });
        PanelBawah3.add(btn_keluar_transaksi);
        btn_keluar_transaksi.setBounds(20, 10, 76, 27);

        PanelTransaksi.add(PanelBawah3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 590, 50));

        PanelTengah3.setBackground(new java.awt.Color(76, 77, 82));
        PanelTengah3.setLayout(new java.awt.CardLayout());

        tbl_transaksi.setBackground(new java.awt.Color(76, 77, 82));
        tbl_transaksi.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        tbl_transaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nama Karyawan", "Nama Proyek", "Peran"
            }
        ));
        tbl_transaksi.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane6.setViewportView(tbl_transaksi);

        PanelTengah3.add(jScrollPane6, "card2");

        PanelTransaksi.add(PanelTengah3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 590, 170));

        PanelAtas3.setBackground(new java.awt.Color(227, 173, 84));

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 0, 31)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(76, 77, 82));
        jLabel5.setText("TRANSAKSI");

        jPanel18.setBackground(new java.awt.Color(76, 77, 82));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 17, Short.MAX_VALUE)
        );

        jLabel3.setIcon(new javax.swing.ImageIcon("C:\\Users\\Toshiba\\Downloads\\uang__1_-removebg-preview.png")); // NOI18N

        jLabel23.setFont(new java.awt.Font("Segoe UI Semibold", 0, 9)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(76, 77, 82));
        jLabel23.setText("PT. Ketenaga Kerjaan Madju Jaya, Jawa Timur, Indonesia.");

        jLabel24.setFont(new java.awt.Font("Segoe UI Semibold", 0, 8)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(76, 77, 82));
        jLabel24.setText("NO.88629, Telp. 0883244");

        jLabel25.setIcon(new javax.swing.ImageIcon("C:\\Users\\Toshiba\\Downloads\\logormh__1_-removebg-preview.png")); // NOI18N

        javax.swing.GroupLayout PanelAtas3Layout = new javax.swing.GroupLayout(PanelAtas3);
        PanelAtas3.setLayout(PanelAtas3Layout);
        PanelAtas3Layout.setHorizontalGroup(
            PanelAtas3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PanelAtas3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(PanelAtas3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelAtas3Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(47, 47, 47))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelAtas3Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(124, 124, 124))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelAtas3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(70, 70, 70)))
                .addComponent(jLabel3)
                .addGap(33, 33, 33))
        );
        PanelAtas3Layout.setVerticalGroup(
            PanelAtas3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelAtas3Layout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(PanelAtas3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelAtas3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(PanelAtas3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(PanelAtas3Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24)))
                        .addContainerGap(13, Short.MAX_VALUE))
                    .addGroup(PanelAtas3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        PanelTransaksi.add(PanelAtas3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 100));

        jPanel3.setBackground(new java.awt.Color(76, 77, 82));

        jLabel26.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Nama Karyawan    :");

        jLabel27.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Nama Proyek         :");

        jLabel28.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Peran                      :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(125, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tf_peran)
                    .addComponent(cb_namaproyek, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cb_namakaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(125, 125, 125))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_namakaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cb_namaproyek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_peran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        PanelTransaksi.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 590, 120));

        jTabbedPane1.addTab("Transaksi", PanelTransaksi);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambah_karyawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambah_karyawanActionPerformed
        // TODO add your handling code here:
        TambahDataKaryawan();
    }//GEN-LAST:event_btn_tambah_karyawanActionPerformed

    private void btn_edit_karyawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_edit_karyawanActionPerformed
        // TODO add your handling code here:
        UbahDataKaryawan(); 
    }//GEN-LAST:event_btn_edit_karyawanActionPerformed

    private void btn_hapus_karyawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapus_karyawanActionPerformed
        // TODO add your handling code here:
        HapusDataKaryawan();
    }//GEN-LAST:event_btn_hapus_karyawanActionPerformed

    private void btn_tambah_proyekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambah_proyekActionPerformed
        // TODO add your handling code here:
        TambahDataProyek();
    }//GEN-LAST:event_btn_tambah_proyekActionPerformed

    private void btn_edit_proyekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_edit_proyekActionPerformed
        // TODO add your handling code here:
        UbahDataProyek();
    }//GEN-LAST:event_btn_edit_proyekActionPerformed

    private void btn_hapus_proyekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapus_proyekActionPerformed
        // TODO add your handling code here:
        HapusDataProyek();
    }//GEN-LAST:event_btn_hapus_proyekActionPerformed

    private void btn_tambah_transaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tambah_transaksiActionPerformed
        // TODO add your handling code here:
        TambahDataTransaksi();
    }//GEN-LAST:event_btn_tambah_transaksiActionPerformed

    private void btn_edit_trasnsaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_edit_trasnsaksiActionPerformed
        // TODO add your handling code here:
        UbahDataTransaksi();
    }//GEN-LAST:event_btn_edit_trasnsaksiActionPerformed

    private void btn_hapus_transaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapus_transaksiActionPerformed
        // TODO add your handling code here:
        HapusDataTransaksi();
    }//GEN-LAST:event_btn_hapus_transaksiActionPerformed

    private void btn_keluar_proyekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_keluar_proyekActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btn_keluar_proyekActionPerformed

    private void btn_keluar_transaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_keluar_transaksiActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btn_keluar_transaksiActionPerformed

    private void btn_keluar_karyawan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_keluar_karyawan1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btn_keluar_karyawan1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManajemenKaryawanProyek.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManajemenKaryawanProyek.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManajemenKaryawanProyek.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManajemenKaryawanProyek.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManajemenKaryawanProyek().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelAtas1;
    private javax.swing.JPanel PanelAtas2;
    private javax.swing.JPanel PanelAtas3;
    private javax.swing.JPanel PanelBawah1;
    private javax.swing.JPanel PanelBawah2;
    private javax.swing.JPanel PanelBawah3;
    private javax.swing.JPanel PanelKaryawan;
    private javax.swing.JPanel PanelProyek;
    private javax.swing.JPanel PanelTengah1;
    private javax.swing.JPanel PanelTengah2;
    private javax.swing.JPanel PanelTengah3;
    private javax.swing.JPanel PanelTransaksi;
    private javax.swing.JButton btn_edit_karyawan;
    private javax.swing.JButton btn_edit_proyek;
    private javax.swing.JButton btn_edit_trasnsaksi;
    private javax.swing.JButton btn_hapus_karyawan;
    private javax.swing.JButton btn_hapus_proyek;
    private javax.swing.JButton btn_hapus_transaksi;
    private javax.swing.JButton btn_keluar_karyawan1;
    private javax.swing.JButton btn_keluar_proyek;
    private javax.swing.JButton btn_keluar_transaksi;
    private javax.swing.JButton btn_tambah_karyawan;
    private javax.swing.JButton btn_tambah_proyek;
    private javax.swing.JButton btn_tambah_transaksi;
    private javax.swing.JComboBox<String> cb_namakaryawan;
    private javax.swing.JComboBox<String> cb_namaproyek;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbl_karyawan;
    private javax.swing.JTable tbl_proyek;
    private javax.swing.JTable tbl_transaksi;
    private javax.swing.JTextField tf_departemen_karyawan;
    private javax.swing.JTextField tf_durasi_pengerjaan;
    private javax.swing.JTextField tf_id_karyawan;
    private javax.swing.JTextField tf_id_proyek;
    private javax.swing.JTextField tf_jabatan_karyawan;
    private javax.swing.JTextField tf_nama_karyawan;
    private javax.swing.JTextField tf_nama_proyek;
    private javax.swing.JTextField tf_peran;
    // End of variables declaration//GEN-END:variables
}

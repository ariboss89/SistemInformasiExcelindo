/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Koneksi;
import Model.Login;
import Model.Pelanggan;
import Model.Pembelian;
import Model.Pengguna;
import Model.Table;
import Model.Transaksi;
import com.sun.glass.events.KeyEvent;
import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author User
 */
public class FormTransaksiPembelian extends javax.swing.JFrame {

    /**
     * Creates new form FormPengguna
     */
    private String supplier;
    private String idpembelian;
    private String iddetail;
    private String nama;
    private Date tanggal;
    private int harga;
    private int jumlah;

    Table table = new Table();
    Pembelian pbl = new Pembelian();
    private String rs[][];

    String[] namaKolom = {"Id", "Id Detail", "Nama", "Jumlah"};
    int jmlKolom = namaKolom.length;
    int[] lebar = {200, 400, 400, 400};

    public FormTransaksiPembelian() {
        initComponents();
        setLocationRelativeTo(this);
        refreshTable();
        ShowSupplier();
        ShowBarangMasuk();
        IdDetailPembelian();
        Run();
    }

    private void refreshTable() {
        IdDetailPembelian();
        IdPembelian();
        idpembelian = txtNoTransaksi.getText().trim();
        rs = pbl.ShowData(idpembelian);
        table.SetTabel(jTable1, rs, namaKolom, jmlKolom, lebar);
        cbBarang.setSelectedIndex(0);
        txtJumlah.setText("0");
    }

    private void ShowBarangMasuk() {
        java.sql.Connection conn = new Koneksi().connect();
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet res = stmt.executeQuery("select distinct(nama) from barangmasuk where stok !='0'  order by tanggal desc");
            if (res.next()) {
                cbBarang.addItem(res.getString("nama"));
            }
        } catch (SQLException ex) {

        }
    }

    private void ShowSupplier() {
        java.sql.Connection conn = new Koneksi().connect();
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet res = stmt.executeQuery("select *from supplier");
            while (res.next()) {
                cbNamaPelanggan.addItem(res.getString("nama"));
            }
        } catch (SQLException ex) {

        }
    }

    public void Run() {

        Calendar kalender = new GregorianCalendar();

        //set variabel  
        int hari = kalender.get(Calendar.DATE);
        int bulan = kalender.get(Calendar.MONTH) + 1;
        int tahun = kalender.get(Calendar.YEAR);
        String formatTanggal = tahun + "-" + bulan + "-" + hari;
        txtTanggal.setText(formatTanggal);

    }

    private void IdPembelian() {
        int byk_data;
        byk_data = 0;
        try {
            java.sql.Connection conn = new Koneksi().connect();

            String sql = "select *from pembelian order by idpembelian";
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            byk_data = rs.getRow();
            if (byk_data == 0) {
                this.txtNoTransaksi.setText("NP001");
            } else {
                String id = rs.getString(1);
                int n = Integer.parseInt(id.substring(2, 4)) + 1;
                String nomor = "";
                for (int i = 1; i <= 2 - String.valueOf(n).length(); i++) {
                    nomor = nomor + "00";
                }
                nomor = "NP" + nomor + String.valueOf(n);
                this.txtNoTransaksi.setText(nomor);
            }
        } catch (Exception e) {
            System.out.println("Error auto increment userID : " + e + "\n");
        }
    }

    private void IdDetailPembelian() {
        int byk_data;
        byk_data = 0;
        try {
            java.sql.Connection conn = new Koneksi().connect();

            String sql = "select *from detailpembelian order by iddetail";
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            byk_data = rs.getRow();
            if (byk_data == 0) {
                pbl.setIdDetail("DTB001");
            } else {
                String id = rs.getString(1);
                int n = Integer.parseInt(id.substring(3, 5)) + 1;
                String nomor = "";
                for (int i = 1; i <= 2 - String.valueOf(n).length(); i++) {
                    nomor = nomor + "00";
                }
                nomor = "DT" + nomor + String.valueOf(n);
                pbl.setIdDetail(nomor);
            }
        } catch (Exception e) {
            System.out.println("Error auto increment userID : " + e + "\n");
        }
    }

    private void SumJumlahItem() {
        java.sql.Connection conn = new Koneksi().connect();
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet res = stmt.executeQuery("select sum(jumlah) as hitung from detailpembelian where idpembelian ='" + txtNoTransaksi.getText() + "'");
            if (res.next()) {
                txtJumlahItem.setText(res.getString("hitung"));
            }
        } catch (SQLException ex) {

        }
    }
    
    private void cetak() {
        //ShowAlamat();
        java.sql.Connection conn = new Koneksi().connect();

        try {
            HashMap parameter = new HashMap();
            File file = new File("src/Report/NotaPembelianExcelindo.jasper");
            String tanggal1 = txtTanggal.getText();
            String nota = txtNoTransaksi.getText();
            String pelanggan = cbNamaPelanggan.getSelectedItem().toString();
            parameter.put("nota", nota);
            parameter.put("tanggal", tanggal1);
            parameter.put("pelanggan", pelanggan);
            JasperReport jp = (JasperReport) JRLoader.loadObject(file);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jp, parameter, conn);
            JasperViewer.viewReport(jasperPrint, false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtJumlah = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        cbBarang = new javax.swing.JComboBox();
        btnDelete2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtNoTransaksi = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTanggal = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtJumlahItem = new javax.swing.JTextField();
        btnSelesai = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtKontak = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        cbNamaPelanggan = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("NAMA BARANG");

        txtJumlah.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtJumlah.setText("0");
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJumlahKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtJumlahKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("JUMLAH");

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSave.setText("SAVE");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        cbBarang.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbBarang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PILIH BARANG" }));
        cbBarang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbBarangItemStateChanged(evt);
            }
        });

        btnDelete2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDelete2.setText("EXIT");
        btnDelete2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(txtJumlah)
                    .addComponent(cbBarang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete2, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(11, 11, 11)
                .addComponent(cbBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "TOTAL PEMBELIAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("NO. PEMBELIAN");

        txtNoTransaksi.setEditable(false);
        txtNoTransaksi.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("TANGGAL");

        txtTanggal.setEditable(false);
        txtTanggal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("JUMLAH ITEM");

        txtJumlahItem.setEditable(false);
        txtJumlahItem.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnSelesai.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSelesai.setText("SELESAI");
        btnSelesai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelesaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                        .addGap(50, 50, 50))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                    .addComponent(txtNoTransaksi)
                    .addComponent(txtJumlahItem)))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtNoTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtJumlahItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "INFORMASI SUPPLIER", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("NAMA SUPPLIER");

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("ALAMAT");

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("KONTAK");

        txtKontak.setEditable(false);
        txtKontak.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtAlamat.setEditable(false);
        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        jScrollPane2.setViewportView(txtAlamat);

        cbNamaPelanggan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbNamaPelanggan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PILIH" }));
        cbNamaPelanggan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbNamaPelangganItemStateChanged(evt);
            }
        });
        cbNamaPelanggan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbNamaPelangganKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel18)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                    .addComponent(txtKontak)
                    .addComponent(cbNamaPelanggan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(cbNamaPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtKontak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        idpembelian = txtNoTransaksi.getText().trim();
        iddetail = pbl.getIdDetail();
        nama = cbBarang.getSelectedItem().toString().trim();
        jumlah = Integer.parseInt(txtJumlah.getText().trim());

        if (nama.equals("")) {
            JOptionPane.showMessageDialog(null, "Silahkan Pilih Barang Yang Ingin di Pesan");
            cbBarang.requestFocus();
        } else if (jumlah == 0) {
            JOptionPane.showMessageDialog(null, "Tidak Boleh Kosong");
            txtJumlah.requestFocus();
        } else {
            pbl.SaveDetail(iddetail, idpembelian, nama, jumlah);
            SumJumlahItem();
            refreshTable();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int row = jTable1.getSelectedRow();
        //txtUsername.setText(jTable1.getValueAt(row, 1).toString());
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnSelesaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelesaiActionPerformed
        // TODO add your handling code here:
        Date tanggal1 = Date.valueOf(txtTanggal.getText());
        supplier = cbNamaPelanggan.getSelectedItem().toString().trim();
        idpembelian = txtNoTransaksi.getText().trim();
        jumlah = Integer.parseInt(txtJumlahItem.getText());

        if (cbNamaPelanggan.getSelectedItem().equals("PILIH")) {
            JOptionPane.showMessageDialog(null, "Silahkan Pilih Pelanggan Dahulu !!!");
            cbNamaPelanggan.requestFocus();
        } else {
            pbl.Save(idpembelian, tanggal1, jumlah, supplier);
            cetak();
            refreshTable();
            txtJumlahItem.setText("");
            txtAlamat.setText("");
            txtKontak.setText("");
            cbNamaPelanggan.setSelectedIndex(0);
        }
    }//GEN-LAST:event_btnSelesaiActionPerformed

    private void btnDelete2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnDelete2ActionPerformed

    private void cbNamaPelangganItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbNamaPelangganItemStateChanged
        // TODO add your handling code here:
        java.sql.Connection conn = new Koneksi().connect();
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet res = stmt.executeQuery("select *from supplier where nama = '" + cbNamaPelanggan.getSelectedItem() + "'");
            while (res.next()) {
                txtAlamat.setText(res.getString("alamat"));
                txtKontak.setText(res.getString("telepon"));
            }
        } catch (SQLException ex) {

        }
    }//GEN-LAST:event_cbNamaPelangganItemStateChanged

    private void txtJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyReleased
        // TODO add your handling code here:
        jumlah = Integer.parseInt(txtJumlah.getText());       
    }//GEN-LAST:event_txtJumlahKeyReleased

    private void cbBarangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbBarangItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cbBarangItemStateChanged

    private void cbNamaPelangganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbNamaPelangganKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbNamaPelangganKeyPressed

    private void txtJumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyTyped
        // TODO add your handling code here:
        char karakter = evt.getKeyChar();
        if (txtJumlah.getText().length() == 0 && karakter == java.awt.event.KeyEvent.VK_SPACE) {
            getToolkit().beep();
            evt.consume();

        }
        if ((karakter >= 'a') && (karakter <= 'z') || (karakter == java.awt.event.KeyEvent.VK_BACK_SPACE) || (karakter == java.awt.event.KeyEvent.VK_DELETE) || (karakter == java.awt.event.KeyEvent.VK_ENTER)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtJumlahKeyTyped

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
            java.util.logging.Logger.getLogger(FormTransaksiPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormTransaksiPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormTransaksiPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormTransaksiPembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormTransaksiPembelian().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete2;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSelesai;
    private javax.swing.JComboBox cbBarang;
    private javax.swing.JComboBox cbNamaPelanggan;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtJumlahItem;
    private javax.swing.JTextField txtKontak;
    private javax.swing.JTextField txtNoTransaksi;
    private javax.swing.JTextField txtTanggal;
    // End of variables declaration//GEN-END:variables
}

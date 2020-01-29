/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Koneksi;
import Model.Login;
import Model.Pelanggan;
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
public class FormTransaksi extends javax.swing.JFrame {

    /**
     * Creates new form FormPengguna
     */
    private String pelanggan;
    private String idtransaksi;
    private String iddetail;
    private String nama;
    private Date tanggal;
    private int harga;
    private int jumlah;
    private int total;
    private int bayar;
    private int kembali;
    private int stokbarang;
    private int minstok;

    Table table = new Table();
    Transaksi trc = new Transaksi();
    private String rs[][];

    String[] namaKolom = {"Id", "Id Detail", "Nama", "Harga", "Jumlah", "Total"};
    int jmlKolom = namaKolom.length;
    int[] lebar = {200, 400, 400, 400, 400, 400};

    public FormTransaksi() {
        initComponents();
        setLocationRelativeTo(this);
        refreshTable();
        ShowPelanggan();
        ShowBarangMasuk();
        IdDetailTransaksi();
        Run();
    }

    private void refreshTable() {
        IdDetailTransaksi();
        IdTransaksi();
        idtransaksi = txtNoTransaksi.getText().trim();
        rs = trc.ShowData(idtransaksi);
        table.SetTabel(jTable1, rs, namaKolom, jmlKolom, lebar);
        cbBarang.setSelectedIndex(0);
        txtHarga.setText("");
        txtJumlah.setText("0");
        txtTotal.setText("");
    }

    private void ShowBarangMasuk() {
        java.sql.Connection conn = new Koneksi().connect();
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet res = stmt.executeQuery("select distinct(nama) from barangmasuk where stok !='0'  order by tanggal desc");
            while (res.next()) {
                cbBarang.addItem(res.getString("nama"));
            }
        } catch (SQLException ex) {

        }
    }

    private void ShowStokBarangMasuk() {
        java.sql.Connection conn = new Koneksi().connect();
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet res = stmt.executeQuery("select *from barangmasuk where nama = '" + cbBarang.getSelectedItem() + "' and stok !='0' order by tanggal desc");
            while (res.next()) {
                trc.setStokBarangMasuk(res.getInt("stok"));
                trc.setTanggalMasuk(res.getDate("tanggal"));
            }
        } catch (SQLException ex) {

        }
    }

    private void ShowStokBarang() {
        java.sql.Connection conn = new Koneksi().connect();
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet res = stmt.executeQuery("select *from barang where nama = '" + cbBarang.getSelectedItem() + "' ");
            while (res.next()) {
                trc.setStokBarang(res.getInt("stok"));
                trc.setMinStokBarang(res.getInt("minstok"));
            }
        } catch (SQLException ex) {

        }
    }

    private void UpdateStokBarangMasuk() {
        nama = cbBarang.getSelectedItem().toString().trim();
        int currentstok = trc.getStokBarangMasuk();
        int stok = Integer.parseInt(txtJumlah.getText().trim());
        int stoknow = currentstok - stok;
        tanggal = trc.getTanggalMasuk();

        trc.UpdateStokBarangMasuk(nama, stoknow, tanggal);
    }

    private void UpdateStokBarang() {
        nama = cbBarang.getSelectedItem().toString().trim();
        int currentstok = trc.getStokBarang();
        int stok = Integer.parseInt(txtJumlah.getText().trim());
        int stoknow = currentstok - stok;

        trc.UpdateStokBarang(nama, stoknow);
    }

    private void ShowPelanggan() {
        java.sql.Connection conn = new Koneksi().connect();
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet res = stmt.executeQuery("select *from pelanggan");
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

    private void IdTransaksi() {
        int byk_data;
        byk_data = 0;
        try {
            java.sql.Connection conn = new Koneksi().connect();

            String sql = "select *from transaksi order by idtransaksi";
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            byk_data = rs.getRow();
            if (byk_data == 0) {
                this.txtNoTransaksi.setText("N001");
            } else {
                String id = rs.getString(1);
                int n = Integer.parseInt(id.substring(1, 4)) + 1;
                String nomor = "";
                for (int i = 1; i <= 2 - String.valueOf(n).length(); i++) {
                    nomor = nomor + "00";
                }
                nomor = "N" + nomor + String.valueOf(n);
                this.txtNoTransaksi.setText(nomor);
            }
        } catch (Exception e) {
            System.out.println("Error auto increment userID : " + e + "\n");
        }
    }

    private void IdDetailTransaksi() {
        int byk_data;
        byk_data = 0;
        try {
            java.sql.Connection conn = new Koneksi().connect();

            String sql = "select *from detailtransaksi order by iddetail";
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            byk_data = rs.getRow();
            if (byk_data == 0) {
                trc.setIdDetail("DT001");
            } else {
                String id = rs.getString(1);
                int n = Integer.parseInt(id.substring(2, 5)) + 1;
                String nomor = "";
                for (int i = 1; i <= 2 - String.valueOf(n).length(); i++) {
                    nomor = nomor + "00";
                }
                nomor = "DT" + nomor + String.valueOf(n);
                trc.setIdDetail(nomor);
            }
        } catch (Exception e) {
            System.out.println("Error auto increment userID : " + e + "\n");
        }
    }

    private void SumJumlahItem() {
        java.sql.Connection conn = new Koneksi().connect();
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet res = stmt.executeQuery("select sum(jumlah) as hitung from detailtransaksi where idtransaksi ='" + txtNoTransaksi.getText() + "'");
            if (res.next()) {
                txtJumlahItem.setText(res.getString("hitung"));
            }
        } catch (SQLException ex) {

        }
    }

    private void SumTotalBelanja() {
        java.sql.Connection conn = new Koneksi().connect();
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet res = stmt.executeQuery("select sum(total) as belanja from detailtransaksi where idtransaksi ='" + txtNoTransaksi.getText() + "'");
            if (res.next()) {
                txtTotalBelanja.setText(res.getString("belanja"));
            }
        } catch (SQLException ex) {

        }
    }

    private void SumKembali() {
        total = Integer.parseInt(txtTotalBelanja.getText());
        bayar = Integer.parseInt(txtBayar.getText());
        kembali = bayar - total;
        txtKembalian.setText(String.valueOf(kembali));
    }

    private void cetak() {
        //ShowAlamat();
        java.sql.Connection conn = new Koneksi().connect();

        try {
            HashMap parameter = new HashMap();
            File file = new File("src/Report/NotaExcelindo.jasper");
            String tanggal1 = txtTanggal.getText();
            String nota = txtNoTransaksi.getText();
            String pelanggan = cbNamaPelanggan.getSelectedItem().toString();
            int total1 = Integer.parseInt(txtTotalBelanja.getText());
            int bayar1 = Integer.parseInt(txtBayar.getText());
            int kembalian = Integer.parseInt(txtKembalian.getText());
            parameter.put("nota", nota);
            parameter.put("tanggal", tanggal1);
            parameter.put("pelanggan", pelanggan);
            parameter.put("totalbayar", total1);
            parameter.put("bayar", bayar1);
            parameter.put("kembalian", kembalian);
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
        txtHarga = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtJumlah = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
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
        jLabel14 = new javax.swing.JLabel();
        txtTotalBelanja = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtBayar = new javax.swing.JTextField();
        txtKembalian = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        btnDelete1 = new javax.swing.JButton();
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

        txtHarga.setEditable(false);
        txtHarga.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("HARGA SATUAN");

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

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("TOTAL");

        txtTotal.setEditable(false);
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);

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
                    .addComponent(txtHarga)
                    .addComponent(txtJumlah)
                    .addComponent(txtTotal)
                    .addComponent(cbBarang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete2, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)))
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
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "TOTAL BELANJAAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("NO. TRANSAKSI");

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

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("TOTAL BELANJA");

        txtTotalBelanja.setEditable(false);
        txtTotalBelanja.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("BAYAR");

        txtBayar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBayarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBayarKeyTyped(evt);
            }
        });

        txtKembalian.setEditable(false);
        txtKembalian.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("KEMBALIAN");

        btnDelete1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnDelete1.setText("SELESAI");
        btnDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtKembalian)
                    .addComponent(btnDelete1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(56, 56, 56))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(35, 35, 35))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(50, 50, 50))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTanggal, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                    .addComponent(txtNoTransaksi)
                    .addComponent(txtBayar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTotalBelanja)
                    .addComponent(txtJumlahItem)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtJumlahItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtTotalBelanja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "INFORMASI PELANGGAN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("NAMA PELANGGAN");

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
                    .addComponent(jLabel17))
                .addGap(90, 90, 90)
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
                    .addComponent(jScrollPane2))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        idtransaksi = txtNoTransaksi.getText().trim();
        iddetail = trc.getIdDetail();
        nama = cbBarang.getSelectedItem().toString().trim();
        harga = Integer.parseInt(txtHarga.getText().trim());
        jumlah = Integer.parseInt(txtJumlah.getText().trim());
        total = Integer.parseInt(txtTotal.getText().trim());
        stokbarang = trc.getStokBarang();
        minstok = trc.getStokBarang();

        if (nama.equals("")) {
            JOptionPane.showMessageDialog(null, "Silahkan Pilih Barang Yang Ingin di Pesan");
            cbBarang.requestFocus();
        } else if (harga == 0) {
            JOptionPane.showMessageDialog(null, "Tidak Boleh Kosong");
            txtHarga.requestFocus();
        } else if (jumlah == 0) {
            JOptionPane.showMessageDialog(null, "Tidak Boleh Kosong");
            txtJumlah.requestFocus();
        } else if (total == 0) {
            JOptionPane.showMessageDialog(null, "Tidak Boleh Kosong");
            txtTotal.requestFocus();
        } else if (jumlah > stokbarang) {
            JOptionPane.showMessageDialog(null, "Transaski Tidak dapat Dilanjutkan dikarenakan Stok Kurang");
            txtJumlah.requestFocus();

        } else if (stokbarang < minstok) {
            JOptionPane.showMessageDialog(null, "Transaski Tidak dapat Dilanjutkan dikarenakan Stok Kurang");
            txtJumlah.requestFocus();

        } else {
            trc.SaveDetail(iddetail, idtransaksi, nama, harga, jumlah, total);
            UpdateStokBarang();
            UpdateStokBarangMasuk();
            SumJumlahItem();
            SumTotalBelanja();
            refreshTable();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int row = jTable1.getSelectedRow();
        //txtUsername.setText(jTable1.getValueAt(row, 1).toString());
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed
        // TODO add your handling code here:
        Date tanggal1 = Date.valueOf(txtTanggal.getText());
        pelanggan = cbNamaPelanggan.getSelectedItem().toString().trim();
        idtransaksi = txtNoTransaksi.getText().trim();
        jumlah = Integer.parseInt(txtJumlahItem.getText());
        total = Integer.parseInt(txtTotalBelanja.getText());
        bayar = Integer.parseInt(txtBayar.getText());
        kembali = Integer.parseInt(txtKembalian.getText());

        if (bayar < total) {
            JOptionPane.showMessageDialog(null, "Silahkan Input Total Bayar Dengan Angka Yang Benar !!!");
            txtBayar.setText("");
            txtKembalian.setText("");
            txtBayar.requestFocus();
        } else if (cbNamaPelanggan.getSelectedItem().equals("PILIH")) {
            JOptionPane.showMessageDialog(null, "Silahkan Pilih Pelanggan Dahulu !!!");
            cbNamaPelanggan.requestFocus();
        } else {
            trc.Save(idtransaksi, tanggal1, jumlah, total, bayar, kembali, pelanggan);
            cetak();
            refreshTable();
            txtJumlahItem.setText("");
            txtTotalBelanja.setText("");
            txtBayar.setText("");
            txtKembalian.setText("");
            txtAlamat.setText("");
            txtKontak.setText("");
            cbNamaPelanggan.setSelectedIndex(0);
        }
    }//GEN-LAST:event_btnDelete1ActionPerformed

    private void btnDelete2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete2ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnDelete2ActionPerformed

    private void cbNamaPelangganItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbNamaPelangganItemStateChanged
        // TODO add your handling code here:
        java.sql.Connection conn = new Koneksi().connect();
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet res = stmt.executeQuery("select *from pelanggan where nama = '" + cbNamaPelanggan.getSelectedItem() + "'");
            while (res.next()) {
                txtAlamat.setText(res.getString("alamat"));
                txtKontak.setText(res.getString("kontak"));
                txtBayar.requestFocus();
            }
        } catch (SQLException ex) {

        }
    }//GEN-LAST:event_cbNamaPelangganItemStateChanged

    private void txtJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyReleased
        // TODO add your handling code here:
        jumlah = Integer.parseInt(txtJumlah.getText());
        harga = Integer.parseInt(txtHarga.getText());
        total = jumlah * harga;
        txtTotal.setText(String.valueOf(total));
    }//GEN-LAST:event_txtJumlahKeyReleased

    private void cbBarangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbBarangItemStateChanged
        // TODO add your handling code here:
        java.sql.Connection conn = new Koneksi().connect();
        try {
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet res = stmt.executeQuery("select *from barang where nama = '" + cbBarang.getSelectedItem() + "'");
            while (res.next()) {
                txtHarga.setText(res.getString("hargabeli"));
                trc.setStokBarang(res.getInt("stok"));
                trc.setMinStokBarang(res.getInt("minstok"));
                ShowStokBarangMasuk();
            }
        } catch (SQLException ex) {

        }
    }//GEN-LAST:event_cbBarangItemStateChanged

    private void txtBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyReleased
        // TODO add your handling code here:

        SumKembali();


    }//GEN-LAST:event_txtBayarKeyReleased

    private void cbNamaPelangganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbNamaPelangganKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()== KeyEvent.VK_ENTER){
            txtBayar.requestFocus();
        }
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

    private void txtBayarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyTyped
        // TODO add your handling code here:
        char karakter = evt.getKeyChar();
        if (txtBayar.getText().length() == 0 && karakter == java.awt.event.KeyEvent.VK_SPACE) {
            getToolkit().beep();
            evt.consume();

        }
        if ((karakter >= 'a') && (karakter <= 'z') || (karakter == java.awt.event.KeyEvent.VK_BACK_SPACE) || (karakter == java.awt.event.KeyEvent.VK_DELETE) || (karakter == java.awt.event.KeyEvent.VK_ENTER)) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtBayarKeyTyped

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
            java.util.logging.Logger.getLogger(FormTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormTransaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete1;
    private javax.swing.JButton btnDelete2;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cbBarang;
    private javax.swing.JComboBox cbNamaPelanggan;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtJumlahItem;
    private javax.swing.JTextField txtKembalian;
    private javax.swing.JTextField txtKontak;
    private javax.swing.JTextField txtNoTransaksi;
    private javax.swing.JTextField txtTanggal;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotalBelanja;
    // End of variables declaration//GEN-END:variables
}

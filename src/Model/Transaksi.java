/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.Koneksi;
import com.sun.jndi.ldap.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class Transaksi {

    private Koneksi con;
    private Statement st;
    private String query;
    private ResultSet res;
    private String IdDetail;
    private Date tanggalMasuk;
    private int stokBarangMasuk;
    private int stokBarang;
    private int minStokBarang;
    private Date tanggal;
    private int Penampung;
    private String namaBarang;

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public int getPenampung() {
        return Penampung;
    }

    public void setPenampung(int Penampung) {
        this.Penampung = Penampung;
    }
    
    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public Date getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(Date tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }
    
    public String getIdDetail() {
        return IdDetail;
    }

    public void setIdDetail(String IdDetail) {
        this.IdDetail = IdDetail;
    }

    public int getStokBarang() {
        return stokBarang;
    }

    public void setStokBarang(int stokBarang) {
        this.stokBarang = stokBarang;
    }

    public int getStokBarangMasuk() {
        return stokBarangMasuk;
    }

    public void setStokBarangMasuk(int stokBarangMasuk) {
        this.stokBarangMasuk = stokBarangMasuk;
    }

    public int getMinStokBarang() {
        return minStokBarang;
    }

    public void setMinStokBarang(int minStokBarang) {
        this.minStokBarang = minStokBarang;
    }
    
    public void UpdateStokBarangMasuk(String nama, int stok, Date tanggal) {
        con = new Koneksi();
        con.connect();
        try {
            st = con.conn.createStatement();
            query = "update barangmasuk set stok='" + stok + "' where nama = '" + nama + "' and tanggal = '" + tanggal + "'";
            st.executeUpdate(query);
            st.close();
            con.conn.close();
        } catch (SQLException e) {

        }
    }
    
    public void UpdateStokBarang(String nama, int stok) {
        con = new Koneksi();
        con.connect();
        try {
            st = con.conn.createStatement();
            query = "update barang set stok='" + stok + "' where nama = '" + nama + "'";
            st.executeUpdate(query);
            st.close();
            con.conn.close();
        } catch (SQLException e) {

        }
    }
    
    public void Save(String Id, Date tanggal, int jumlah, int total, int bayar, int kembali, String pelanggan) {
        con = new Koneksi();
        con.connect();
        try {
            st = con.conn.createStatement();
            query = "insert into transaksi(idtransaksi, tanggal, jumlah, total, bayar, kembalian, pelanggan)values('" + Id + "','" + tanggal + "','" + jumlah + "','" + total + "','" + bayar + "','" + kembali + "','" + pelanggan + "')";
            st.executeUpdate(query);
            st.close();
            con.conn.close();
            JOptionPane.showMessageDialog(null, "Tunggu Sebentar ... , Struk Akan di Cetak");
        } catch (SQLException e) {
        }
    }
    
    public void SaveDetail(String Id, String idtransaksi, String nama, int harga, int jumlah, int total) {
        con = new Koneksi();
        con.connect();
        try {
            st = con.conn.createStatement();
            query = "insert into detailtransaksi(iddetail, idtransaksi, nama, harga, jumlah, total)values('" + Id + "','" + idtransaksi + "','" + nama + "','" + harga + "','" + jumlah + "','" + total + "')";
            st.executeUpdate(query);
            st.close();
            con.conn.close();
            JOptionPane.showMessageDialog(null, "Barang di Tambahkan");
        } catch (SQLException e) {
        }
    }
    
    public void Delete(String Id) {
        con = new Koneksi();
        con.connect();
        try {
            st = con.conn.createStatement();
            query = "delete from supplier where Id = '" + Id + "'";
            st.executeUpdate(query);
            st.close();
            con.conn.close();
            JOptionPane.showMessageDialog(null, "Data di Hapus");
        } catch (SQLException e) {

        }
    }

    public String[][] ShowData(String idtransaksi) {

        res = null;
        String[][] data = null;
        con = new Koneksi();
        con.connect();
        int jumlahBaris = 0;
        try {
            st = con.conn.createStatement();
            query = "SELECT COUNT(iddetail) AS Jumlah FROM detailtransaksi;";
            res = st.executeQuery(query);
            if (res.next()) {
                jumlahBaris = res.getInt("Jumlah");
            }
            query = "select *from detailtransaksi where idtransaksi = '"+idtransaksi+"'";
            res = st.executeQuery(query);
            data = new String[jumlahBaris][6];
            int r = 0;
            while (res.next()) {
                data[r][0] = res.getString("iddetail");
                data[r][1] = res.getString("idtransaksi");
                data[r][2] = res.getString("nama");
                data[r][3] = res.getString("harga");
                data[r][4] = res.getString("jumlah");
                data[r][5] = res.getString("total");
                r++;
            }
            int jmlBaris = r;
            String[][] tmpArray = data;
            data = new String[jmlBaris][6];
            for (r = 0; r < jmlBaris; r++) {
                for (int c = 0; c < 6; c++) {
                    data[r][c] = tmpArray[r][c];
                }
            }
            st.close();
            con.conn.close();
        } catch (SQLException e) {
            System.err.println("SQLException : " + e.getMessage());
        }
        return data;
    }

    public String[][] SearchData(String Id) {

        res = null;
        String[][] data = null;
        con = new Koneksi();
        con.connect();
        int jumlahBaris = 0;
        try {
            st = con.conn.createStatement();
            query = "SELECT COUNT(Id) AS Jumlah FROM transaksi";
            res = st.executeQuery(query);
            if (res.next()) {
                jumlahBaris = res.getInt("Jumlah");
            }
            query = "select *from transaksi where Id like '%" + Id + "%'";
            res = st.executeQuery(query);
            data = new String[jumlahBaris][5];
            int r = 0;
            while (res.next()) {
                data[r][0] = res.getString("Id");
                data[r][1] = res.getString("tanggal");
                data[r][2] = res.getString("jumlah");
                data[r][3] = res.getString("total");
                data[r][4] = res.getString("bayar");
                data[r][4] = res.getString("kembalian");
                r++;
            }
            int jmlBaris = r;
            String[][] tmpArray = data;
            data = new String[jmlBaris][6];
            for (r = 0; r < jmlBaris; r++) {
                for (int c = 0; c < 6; c++) {
                    data[r][c] = tmpArray[r][c];
                }
            }
            st.close();
            con.conn.close();
        } catch (SQLException e) {
            System.err.println("SQLException : " + e.getMessage());
        }
        return data;
    }
}

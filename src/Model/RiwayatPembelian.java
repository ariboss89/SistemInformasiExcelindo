/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import Controller.Koneksi;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author User
 */
public class RiwayatPembelian extends Riwayat{
    
    private Koneksi con;
    private Statement st;
    private String query;
    private ResultSet res;
    private String supplier;

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    public String[][] ShowDataPembelian() {

        res = null;
        String[][] data = null;
        con = new Koneksi();
        con.connect();
        int jumlahBaris = 0;
        try {
            st = con.conn.createStatement();
            query = "SELECT COUNT(idpembelian) AS Jumlah FROM pembelian;";
            res = st.executeQuery(query);
            if (res.next()) {
                jumlahBaris = res.getInt("Jumlah");
            }
            query = "select *from pembelian";
            res = st.executeQuery(query);
            data = new String[jumlahBaris][4];
            int r = 0;
            while (res.next()) {
                data[r][0] = res.getString("idpembelian");
                data[r][1] = res.getString("tanggal");
                data[r][2] = res.getString("jumlah");
                data[r][3] = res.getString("supplier");
                r++;
            }
            int jmlBaris = r;
            String[][] tmpArray = data;
            data = new String[jmlBaris][4];
            for (r = 0; r < jmlBaris; r++) {
                for (int c = 0; c < 4; c++) {
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

    public String[][] SearchData(String idtransaksi) {

        res = null;
        String[][] data = null;
        con = new Koneksi();
        con.connect();
        int jumlahBaris = 0;
        try {
            st = con.conn.createStatement();
            query = "SELECT COUNT(idtransaksi) AS Jumlah FROM transaksi";
            res = st.executeQuery(query);
            if (res.next()) {
                jumlahBaris = res.getInt("Jumlah");
            }
            query = "select *from transaksi where idtransaksi like '%" + idtransaksi + "%'";
            res = st.executeQuery(query);
            data = new String[jumlahBaris][7];
            int r = 0;
            while (res.next()) {
                data[r][0] = res.getString("idtransaksi");
                data[r][1] = res.getString("tanggal");
                data[r][2] = res.getString("jumlah");
                data[r][3] = res.getString("total");
                data[r][4] = res.getString("bayar");
                data[r][5] = res.getString("kembalian");
                data[r][6] = res.getString("pelanggan");
                
                r++;
            }
            int jmlBaris = r;
            String[][] tmpArray = data;
            data = new String[jmlBaris][7];
            for (r = 0; r < jmlBaris; r++) {
                for (int c = 0; c < 7; c++) {
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

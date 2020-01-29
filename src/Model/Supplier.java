/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.Koneksi;
import com.sun.jndi.ldap.Connection;
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
public class Supplier {

    private Koneksi con;
    private Statement st;
    private String query;
    private ResultSet res;
    private String IdSupplier;

    public void Save(String Id, String nama, String alamat, String email, String telepon) {
        con = new Koneksi();
        con.connect();
        try {
            st = con.conn.createStatement();
            query = "insert into supplier(Id, nama, alamat,email, telepon)values('" + Id + "','" + nama + "','" + alamat + "','" + email + "','" + telepon + "')";
            st.executeUpdate(query);
            st.close();
            con.conn.close();
            JOptionPane.showMessageDialog(null, "Data di Simpan");
        } catch (SQLException e) {
        }
    }
    
    public void Update(String Id, String nama, String alamat, String email, String telepon) {
        con = new Koneksi();
        con.connect();
        try {
            st = con.conn.createStatement();
            query = "update supplier set nama='" + nama + "', alamat='" + alamat + "', email='" + email + "', telepon='" + telepon + "' where Id = '" + Id + "'";
            st.executeUpdate(query);
            st.close();
            con.conn.close();
            JOptionPane.showMessageDialog(null, "Data di Update");
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

    public String[][] ShowData() {

        res = null;
        String[][] data = null;
        con = new Koneksi();
        con.connect();
        int jumlahBaris = 0;
        try {
            st = con.conn.createStatement();
            query = "SELECT COUNT(Id) AS Jumlah FROM supplier;";
            res = st.executeQuery(query);
            if (res.next()) {
                jumlahBaris = res.getInt("Jumlah");
            }
            query = "select *from supplier";
            res = st.executeQuery(query);
            data = new String[jumlahBaris][5];
            int r = 0;
            while (res.next()) {
                data[r][0] = res.getString("Id");
                data[r][1] = res.getString("nama");
                data[r][2] = res.getString("alamat");
                data[r][3] = res.getString("email");
                data[r][4] = res.getString("telepon");
                r++;
            }
            int jmlBaris = r;
            String[][] tmpArray = data;
            data = new String[jmlBaris][5];
            for (r = 0; r < jmlBaris; r++) {
                for (int c = 0; c < 5; c++) {
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
            query = "SELECT COUNT(Id) AS Jumlah FROM supplier";
            res = st.executeQuery(query);
            if (res.next()) {
                jumlahBaris = res.getInt("Jumlah");
            }
            query = "select *from supplier where Id like '%" + Id + "%'";
            res = st.executeQuery(query);
            data = new String[jumlahBaris][5];
            int r = 0;
            while (res.next()) {
                data[r][0] = res.getString("Id");
                data[r][1] = res.getString("nama");
                data[r][2] = res.getString("alamat");
                data[r][3] = res.getString("email");
                data[r][4] = res.getString("telepon");
                r++;
            }
            int jmlBaris = r;
            String[][] tmpArray = data;
            data = new String[jmlBaris][5];
            for (r = 0; r < jmlBaris; r++) {
                for (int c = 0; c < 5; c++) {
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

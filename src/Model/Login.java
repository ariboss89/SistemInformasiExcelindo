/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import Controller.Koneksi;
import View.FormLogin;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import View.FormMenuUtama;
/**
 *
 * @author User
 */
public class Login {
    private String nama;
    private String alamat;
    private static String username;
    private String password;
    private static String status;
    private Koneksi con;
    private Statement st;
    private ResultSet res;
    private String query;
    private static String kategori;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String aUsername) {
        username = aUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String aStatus) {
        status = aStatus;
    }

    public static String getKategori() {
        return kategori;
    }

    public static void setKategori(String kategori) {
        Login.kategori = kategori;
    }
    
    public void Login(String username, String password) {
        con = new Koneksi();
        con.connect();
        try {
            Login login = new Login();
            st = con.connect().createStatement();
            res = st.executeQuery("select *from admin where username ='" + username + "' And password = '" + password + "'");
            if (res.next()) {
                JOptionPane.showMessageDialog(null, "Welcome " + username);
              
                new FormMenuUtama().setVisible(true);
               
            } 
            else{
                JOptionPane.showMessageDialog(null, "Username or Password is Wrong");
            }
        } catch (SQLException e) {

        }
    }

    public void CekUsername(String username){
        con = new Koneksi();
        con.connect();        
        try{
            Statement st = con.conn.createStatement();
            res = st.executeQuery("select *from user where username = '"+username+"' and password "); 
            if(!res.next()){
                JOptionPane.showMessageDialog(null, "Username Is Not Valid !!");
            }
            
        }catch(SQLException ex){
            
        }
    }
}

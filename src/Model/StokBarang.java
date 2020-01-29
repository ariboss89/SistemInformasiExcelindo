/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import java.sql.Date;

/**
 *
 * @author User
 */
public class StokBarang {
   private static String Id;
   private static String nama;
   private Date tanggal;
   private int stok;

    public static String getId() {
        return Id;
    }

    public static void setId(String aId) {
        Id = aId;
    }

    public static String getNama() {
        return nama;
    }

    public static void setNama(String Nama) {
        nama = Nama;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}

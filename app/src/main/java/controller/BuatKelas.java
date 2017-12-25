package controller;

import database.Kelas;
import database.LokasiKelas;
import database.User;

/**
 * Created by root on 13/12/17.
 */

public class BuatKelas {
    public String destinasi;
    public String judul;
    public String mapel;
    public String kapasitas;
    public String hari;
    public String bulan;
    public String tahun;
    public String jamMulai;
    public String menitMulai;
    public String jamSelesai;
    public String menitSelesai;
    public String longtitude;
    public String langtitude;
    public Kelas x;
    public LokasiKelas y;
    public User z;

    public BuatKelas () {}

    public BuatKelas (String destinasi, String judul, String mapel, String kapasitas,
                      String hari, String bulan, String tahun, String jamMulai, String menitMulai,
                      String jamSelesai, String menitSelesai, String langtitude,
                      String longtitude){
        this.destinasi = destinasi;
        this.judul = judul;
        this.mapel = mapel;
        this.kapasitas = kapasitas;
        this.hari = hari;
        this.bulan = bulan;
        this.tahun = tahun;
        this.jamMulai = jamMulai;
        this.menitMulai = menitMulai;
        this.jamSelesai = jamSelesai;
        this.menitSelesai = menitSelesai;
        this.longtitude = longtitude;
        this.langtitude = langtitude;
    }

    public boolean cekForm(){
        return destinasi.isEmpty() || judul.isEmpty() || mapel.isEmpty() || kapasitas.isEmpty()
                || hari.isEmpty() || bulan.isEmpty() || tahun.isEmpty() || jamMulai.isEmpty() ||
                menitMulai.isEmpty() || jamSelesai.isEmpty() || menitSelesai.isEmpty() ||
                longtitude.isEmpty() || langtitude.isEmpty();
    }

    public boolean validasiWaktu(){
        return true;
    }

    public void saveData (String idUser){
        x = new Kelas(destinasi, judul, mapel, kapasitas, hari, bulan, tahun, jamMulai, menitMulai,
                jamSelesai, menitSelesai, langtitude, longtitude);
        y = new LokasiKelas(Double.parseDouble(langtitude),Double.parseDouble(longtitude));
        z = new User();

        String key = x.insetKelas(x, z.getValue(idUser), idUser);
        x.insertUser(idUser, key);

        y.insertData(key);
        z.InsertKelas(x, key, idUser);
    }
//
//    public User getValue(String key){
//
//    }
}

package controller;

/**
 * Created by root on 14/12/17.
 */

public class List {
    private String lokasi;
    private String judul;
    private String tanggal;
    private String jamMulai;
    private String jamSelesai;

    public List(){}

    public List(String lokasi, String judul, String tanggal, String jamMulai, String jamSelesai){
        this.lokasi = lokasi;
        this.judul = judul;
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getJudul() {
        return judul;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJamMulai() {
        return jamMulai;
    }

    public String getJamSelesai() {
        return jamSelesai;
    }
}

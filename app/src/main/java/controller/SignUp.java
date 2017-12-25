package controller;

import database.User;

/**
 * Created by root on 03/11/17.
 */

public class SignUp{
    private User x;
    String nama;
    String jenisKelamin;
    String alamat;
    String jenjang;
    String noHp;
    String email;
    String password;

    public SignUp(){}

    public SignUp(String nama, String jenisKelamin, String alamat, String jenjang, String noHp,
                 String email, String password){
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.jenjang = jenjang;
        this.noHp = noHp;
        this.email = email;
        this.password = password;
    }

    public boolean validate(){
        return nama.equals("") || jenisKelamin.equals("") || alamat.equals("") || jenjang.equals("")
                || noHp.equals("") && noHp.equals("") || email.equals("") || password.equals("");
    }

    public void saveData(String id){
        x = new User(nama, jenisKelamin, alamat, jenjang, noHp, email, password);
        x.InsertUser(x, id);
    }
}

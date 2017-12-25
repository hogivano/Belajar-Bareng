package database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by root on 07/11/17.
 */

public class User {
    String nama;
    String jenisKelamin;
    String alamat;
    String jenjang;
    String noHp;
    String email;
    String password;
    User x;
    String key;

    private FirebaseDatabase db;
    private DatabaseReference tableUser;

    public User(){}

    public User (String nama, String jenisKelamin, String alamat, String jenjang, String noHp,
                 String email, String password){
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.jenjang = jenjang;
        this.noHp = noHp;
        this.email = email;
        this.password = password;
    }

    public  String getNama(){
        return nama;
    }

    public String getJenisKelamin (){
        return jenisKelamin;
    }

    public String getAlamat(){
        return alamat;
    }

    public String getJenjang(){
        return jenjang;
    }

    public String getNoHp(){
        return noHp;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public void setNama(String nama){
        this.nama = nama;
    }

    public void setUsername (String username) {
        this.jenisKelamin = username;
    }

    public void setAlamat (String alamat){
        this.alamat = alamat;
    }

    public void setJenjang (String jenjang){
        this.jenjang = jenjang;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public void setPassword (String password){
        this.password = password;
    }

    public void InsertUser (User x, String id){
//        x.setId(mFirebaseInstance.getReference().child("user").push().getKey());
//        db.getInstance().getReference().child("user").child(x.getEmail()).setValue(x);
        db.getInstance().getReference().child("user").child(id).setValue(x);
//        tableUser = db.getReference("user");
//        tableUser.child(x.getEmail()).setValue(x);
    }

    public void InsertKelas(Kelas x, String key, String idUser){
        db.getInstance().getReference().child("user").child(idUser).child("kelas").child(key).setValue(x);
    }

    public User getValue(String key){
        this.key = key;
        DatabaseReference tableUser = FirebaseDatabase.getInstance().getReference();

        tableUser.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.getKey().toString().equals(getKey())){
                        x = ds.getValue(User.class);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return x;
    }

    public String getKey(){
        return key;
    }
}
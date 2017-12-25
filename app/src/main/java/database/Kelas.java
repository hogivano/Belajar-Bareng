package database;

import com.firebase.geofire.GeoFire;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by root on 13/12/17.
 */

public class Kelas {
    private FirebaseDatabase db;
    private FirebaseDatabase db2;

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

    public Kelas (){}

    public Kelas (String destinasi, String judul, String mapel, String kapasitas,
                  String hari, String bulan, String tahun, String jamMulai, String menitMulai,
                  String jamSelesai, String menitSelesai, String langtitude,
                  String longtitude) {
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

    public String getDestinasi(){
        return destinasi;
    }

    public String getJudul(){
        return judul;
    }

    public String getMapel(){
        return mapel;
    }

    public String getKapasitas(){
        return kapasitas;
    }

    public String getHari(){
        return hari;
    }

    public String getBulan(){
        return bulan;
    }

    public String getTahun(){
        return tahun;
    }

    public String getJamMulai(){
        return jamMulai;
    }

    public String getMenitMulai(){
        return menitMulai;
    }

    public String getJamSelesai(){
        return jamSelesai;
    }

    public String getMenitSelesai(){
        return menitSelesai;
    }

    public String getLongtitude(){
        return longtitude;
    }

    public String getLangtitude(){
        return langtitude;
    }

    public String insetKelas(Kelas x, User y, String idUser){
        String key = db.getInstance().getReference().child("kelas").push().getKey();
        db.getInstance().getReference().child("kelas").child(key).setValue(x);

//        db2.getInstance().getReference().child("kelas").child(key).child("user")
//                .child(idUser).setValue(y);
//        insertLokasiKelas(key);
        return key;
//        db.getInstance().getReference().child("kelas").child(key).child("idUser");
    }

    public void insertUser(final String keyUser, final String keyKelas){
        DatabaseReference tableUser = FirebaseDatabase.getInstance().getReference();
        tableUser.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.getKey().equals(keyUser)){
                        User y = ds.getValue(User.class);
                        FirebaseDatabase.getInstance().getReference().child("kelas").child(keyKelas).child("user")
                                .child(keyUser).setValue(y);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    public void insertLokasiKelas(String key){
//
//    }
}

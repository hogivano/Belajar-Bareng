package database;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by root on 13/12/17.
 */

public class LokasiKelas {
    public double langtitude;
    public double longtitude;
    public DatabaseReference ref;
    public GeoFire geoFire;

    public LokasiKelas(){}

    public LokasiKelas (double lat, double lng){
        langtitude = lat;
        longtitude = lng;
//        ref = FirebaseDatabase.getInstance().getReference("lokasiKelas");
//        geoFire = new GeoFire(ref);
    }

    public double getLangtitude(){
        return langtitude;
    }

    public double getLongtitude(){
        return longtitude;
    }

    public void insertData(String key){
//        geoFire.setLocation(key, new GeoLocation(langtitude, longtitude));
        FirebaseDatabase.getInstance().getReference().child("lokasiKelas").child(key).setValue(new LokasiKelas(langtitude, longtitude));
    }
}

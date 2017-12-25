package com.trydev.belajarbareng;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import controller.List;
import database.Kelas;

public class ListKelas extends AppCompatActivity {
    DatabaseReference tableUser;
    ArrayList<List> list;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kelas);
        list = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listKelas);

        loadKelas();
    }

    public void loadKelas(){
        tableUser = FirebaseDatabase.getInstance().getReference();
        tableUser.child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("kelas")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Kelas x;
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            x = ds.getValue(Kelas.class);
                            list.add(new List(x.getDestinasi(), x.getJudul(), x.getHari() + "/" + x.getBulan()
                            + "/" + x.getTahun(), x.getJamMulai() + ":" + x.getMenitMulai(), x.getJamSelesai()
                            + ":" + x.getMenitSelesai()));
                        }
                        setAdapter(list);
                        Log.e("LisKelasInloop", String.valueOf(list.size()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        Log.e("ListKelas", String.valueOf(list.size()));
    }

    public void setAdapter(ArrayList<List> x){

        KelasAdapter adapter = new KelasAdapter(this, x);
        lv.setAdapter(adapter);
    }

    public class KelasAdapter extends ArrayAdapter<List> {

        public KelasAdapter(@NonNull Context context, ArrayList<List> kelas) {
            super(context,0, kelas);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            List list = getItem(position);

            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
            }

            TextView lokasi = (TextView) convertView.findViewById(R.id.lokasi);
            TextView judul = (TextView) convertView.findViewById(R.id.judul);
            TextView tanggal = (TextView) convertView.findViewById(R.id.tanggal);
            TextView jamMulai = (TextView) convertView.findViewById(R.id.jamMulai);
            TextView jamSelesai = (TextView) convertView.findViewById(R.id.jamSelesai);

            lokasi.setText(list.getLokasi());
            judul.setText(list.getJudul());
            tanggal.setText(list.getTanggal());
            jamMulai.setText(list.getJamMulai());
            jamSelesai.setText(list.getJamSelesai());

            return convertView;
        }
    }
}

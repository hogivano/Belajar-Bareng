package com.trydev.belajarbareng;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import database.User;

public class MenuActivity extends AppCompatActivity {
    ImageView cari, buat, list;
    Button keluar;
    TextView namaUser;
    Intent intent;
    String uId;
    FirebaseDatabase db;
    DatabaseReference tableUser;
    ArrayList<User> kl;
    ArrayList<String> listKey;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tableUser = db.getInstance().getReference();
        uId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        kl = new ArrayList<>();
        listKey = new ArrayList<>();

        cari = (ImageView) findViewById(R.id.cari);
        buat = (ImageView) findViewById(R.id.buat);
        list = (ImageView) findViewById(R.id.list);
        keluar = (Button) findViewById(R.id.keluar);
        namaUser = (TextView) findViewById(R.id.namaUser);

        getName();

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, ListKelas.class));
            }
        });

        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MenuActivity.this, CariActivity.class);
                startActivity(intent);
            }
        });

        buat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MenuActivity.this, LocationActivity.class);
                startActivity(intent);
//                Toast.makeText(MenuActivity.this, String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()), Toast.LENGTH_SHORT).show();
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                MenuActivity.super.onBackPressed();
//                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        setAlertDialog().show();
    }

    public AlertDialog setAlertDialog(){
        builder = new AlertDialog.Builder(this);

        builder.setMessage("Anda yakin ingin keluar?")
                .setTitle("Keluar Aplikasi");

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void getName(){
        tableUser.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    kl.add(ds.getValue(User.class));
                    listKey.add(ds.getKey());
                }

//                Log.e("Cobaa", "====" + kl.get(0).getUsername());
//                Log.e("Key", "====" + listKey.get(3));
                User usr = dataSnapshot.child(uId).getValue(User.class);
                namaUser.setText(usr.getNama());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TryDev", databaseError.getMessage());
            }
        });
    }
}
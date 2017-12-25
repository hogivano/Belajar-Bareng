package com.trydev.belajarbareng;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VerifikasiActivity extends AppCompatActivity {
    Button masuk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);
        masuk = (Button) findViewById(R.id.masuk);

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                VerifikasiActivity.super.onBackPressed();
                startActivity(new Intent(VerifikasiActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        startActivity(new Intent(VerifikasiActivity.this, MainActivity.class));
    }
}
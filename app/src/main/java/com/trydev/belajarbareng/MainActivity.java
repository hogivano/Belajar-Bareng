package com.trydev.belajarbareng;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import controller.Login;
import database.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView daftar;
    private EditText email, password;
    private Button masuk;
    private ProgressDialog progressDialog;
//    private DatabaseReference db;
    private DatabaseReference mDb;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Login login;
    private AlertDialog.Builder builder;

    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Firebase.setAndroidContext(this);
//        mDb = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        TAG = "TryDev";

        progressDialog = new ProgressDialog(this);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        daftar = (TextView) findViewById(R.id.daftar);
        masuk = (Button) findViewById(R.id.masuk);
        daftar.setText(Html.fromHtml("<u>Daftar</u>"));

        daftar.setOnClickListener(this);
        masuk.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        if (view == daftar){
            Intent intent = new Intent(MainActivity.this,DaftarActivity.class);
            startActivity(intent);
        } else {
//            User baru = new User("101", "hendriyan", "op");
//                mDb = baru.getmFirebaseDatabase();
//                baru.setId(mDb.getKey());
//                baru.InsertUser(baru);
            login = new Login (email.getText().toString(), password.getText().toString());

            if (!login.validate()){
                progressDialog.setMessage("Harap tunggu . . .");
                progressDialog.show();

                auth.signInWithEmailAndPassword(login.getEmail(), login.getPassword())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    user = auth.getCurrentUser();
                                    if (user.isEmailVerified()) {
                                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Cek verifikasi email", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                } else {
//                                    Toast.makeText(MainActivity.this, "Email atau password salah", Toast.LENGTH_SHORT).show();
                                    try
                                    {
                                        throw task.getException();
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthInvalidUserException invalidEmail)
                                    {
                                        // TODO: take your actions!
                                        Log.d(TAG, "onComplete: invalid_email");
                                        Toast.makeText(MainActivity.this, "Email salah atau tidak terdaftar", Toast.LENGTH_SHORT).show();
                                    }
                                    // if user enters wrong password.
                                    catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                                    {
                                        Log.d(TAG, "onComplete: wrong_password");
                                        Toast.makeText(MainActivity.this, "Password salah", Toast.LENGTH_SHORT).show();
                                        // TODO: Take your action
                                    }
                                    catch (Exception e)
                                    {
                                        Log.d(TAG, "onComplete: " + e.getMessage());
                                    }
                                    progressDialog.dismiss();
                                }
                            }
                        });
            } else {
                Toast.makeText(MainActivity.this, "Semua harus diisi!!", Toast.LENGTH_SHORT).show();
            }
        }
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
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
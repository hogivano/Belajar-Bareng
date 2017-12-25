package com.trydev.belajarbareng;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import controller.SignUp;

public class DaftarActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView masuk;
    private Button daftar;
    private EditText nama, username, alamat, jenjang, noHp, email, password, rePassword;
    private Intent intent;
    private SignUp signUp;

    private String TAG;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        TAG = "TryDev";
        auth =  FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        nama = (EditText) findViewById(R.id.nama);
        username = (EditText) findViewById(R.id.username);
        alamat = (EditText) findViewById(R.id.alamat);
        jenjang = (EditText) findViewById(R.id.jenjang);
        noHp = (EditText) findViewById(R.id.noHp);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        rePassword = (EditText) findViewById(R.id.rePassword);

        daftar = (Button) findViewById(R.id.daftar);
        masuk = (TextView) findViewById(R.id.masuk);
        masuk.setText(Html.fromHtml("<u>Masuk</u>"));

        masuk.setOnClickListener(this);
        daftar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == masuk){
            super.onBackPressed();
//            intent = new Intent(DaftarActivity.this, MainActivity.class);
//            startActivity(intent);
        } else {
            signUp = new SignUp(nama.getText().toString(), username.getText().toString(),
                    alamat.getText().toString(), jenjang.getText().toString(),
                    noHp.getText().toString(), email.getText().toString(),
                    password.getText().toString());
            if(!password.getText().toString().equals(rePassword.getText().toString())){
                Toast.makeText(this, "Re-password tidak sama", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!signUp.validate()){
                progressDialog.setMessage("Harap tunggu . . .");
                progressDialog.show();

                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    verifikasiEmail();
                                    saveData(user.getUid());
                                    intent = new Intent(DaftarActivity.this, VerifikasiActivity.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                } else {
//                                    if (!cekUser()){
//                                        Toast.makeText(DaftarActivity.this, "Cek email atau password", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(DaftarActivity.this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show();
//                                    }
                                    try
                                    {
                                        throw task.getException();
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthWeakPasswordException weakPassword)
                                    {
                                        Toast.makeText(DaftarActivity.this, "Password min 6 digit", Toast.LENGTH_SHORT).show();
                                        // TODO: take your actions!
                                    }
                                    // if user enters wrong password.
                                    catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                    {
                                        Log.d(TAG, "onComplete: malformed_email");

                                        // TODO: Take your action
                                    }
                                    catch (FirebaseAuthUserCollisionException existEmail)
                                    {
                                        Log.d(TAG, "onComplete: exist_email");
                                        Toast.makeText(DaftarActivity.this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DaftarActivity.this, "Semua harus diisi !!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveData(String id){
        signUp.saveData(id);
    }

    public void verifikasiEmail(){
        user= FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DaftarActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean cekUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return false;
        } else {
            return true;
        }
    }
//
//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(DaftarActivity.this, MainActivity.class));
//    }

    //
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = auth.getCurrentUser();
//        Toast.makeText(this, currentUser.toString(), Toast.LENGTH_SHORT).show();
//    }
}

package controller;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.trydev.belajarbareng.DaftarActivity;

import database.User;

/**
 * Created by root on 03/11/17.
 */

public class Login {
    private String email;
    private String password;
    private FirebaseAuth auth;

    public Login(){}

    public Login (String email, String password){
        this.email = email;
        this.password = password;
    }

    public boolean validate(){
        return email.equals("") || password.equals("");
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

}

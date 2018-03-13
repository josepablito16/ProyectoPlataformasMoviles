package com.cracks.proyectoplataformasmoviles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener listener;

     TextView loginTV = findViewById(R.id.login_id);
     TextView usernameTV = findViewById(R.id.username_id);
     TextView passwordTV = findViewById(R.id.password_id);
     TextView welcomeTV = findViewById(R.id.welcome_tv);
     TextView mensajeTV = findViewById(R.id.mensaje_tv);
     TextView roomText = findViewById(R.id.room_text);

     EditText usernameText = findViewById(R.id.username_text);
     EditText passwordText = findViewById(R.id.password_text);

     Button entrarBtn = findViewById(R.id.entrar_btn);
     Button continueBtn = findViewById(R.id.continue_btn);//Ingresar sesion

     ConstraintLayout layoutLogin = findViewById(R.id.loginLayout);
     ConstraintLayout layoutPrincipal = findViewById(R.id.principalLayout);

     CheckBox checkBox = findViewById(R.id.checkBox);






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        listener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mAuth.getCurrentUser();
                if (user==null)
                {// no esta logeado
                    Toast.makeText(getApplicationContext(),"NO LOGEADO",Toast.LENGTH_LONG).show();


                }
                else
                {
                    //esta logeado :)
                    Toast.makeText(getApplicationContext(),"CORRECTO Y LOGEADO",Toast.LENGTH_LONG).show();
                }

            }
        };




        Switch sw = findViewById(R.id.switch_login);


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    layoutLogin.setVisibility(View.VISIBLE);
                    layoutPrincipal.setVisibility(View.GONE);


                } else {

                    layoutLogin.setVisibility(View.GONE);
                    layoutPrincipal.setVisibility(View.VISIBLE);

                }

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {

                    continueBtn.setText("Register");
                    loginTV.setText("Create Account");

                } else {

                    continueBtn.setText("Continue");
                    loginTV.setText("Login");

                }

            }


        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingresar();

            }
        });

    }

    private void ingresar()
    {
        String email=usernameText.getText().toString();
        String contra=passwordText.getText().toString();

        if (!email.isEmpty() && !contra.isEmpty())
        {
            mAuth.signInWithEmailAndPassword(email,contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"CORRECTO",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"INCORRECTO",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listener  != null)
        {
            mAuth.removeAuthStateListener(listener);

        }
    }
}


package com.cracks.proyectoplataformasmoviles;

import android.content.Intent;
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

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        //Variables / objetos que no se usan.
        TextView usernameTV = findViewById(R.id.username_id);
        TextView passwordTV = findViewById(R.id.password_id);
        TextView welcomeTV = findViewById(R.id.welcome_tv);
        TextView mensajeTV = findViewById(R.id.mensaje_tv);
        TextView roomText = findViewById(R.id.room_text);
        Button entrarBtn = findViewById(R.id.entrar_btn);

        //Variables que se usan, por ello se declaran final.
        final TextView loginTV = findViewById(R.id.login_id);
        final Button continueBtn = findViewById(R.id.continue_btn);//Ingresar sesion
        final ConstraintLayout layoutLogin = findViewById(R.id.loginLayout);
        final ConstraintLayout layoutPrincipal = findViewById(R.id.principalLayout);

        //Listener de la base de datos
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

        //Inicia la implmementacion si el usuario desea hostear una sesion.
        CheckBox checkBox = findViewById(R.id.checkBox);

        Switch sw = findViewById(R.id.switch_login);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    //Si el boton de switch esta activado
                    layoutLogin.setVisibility(View.VISIBLE);
                    layoutPrincipal.setVisibility(View.GONE);

                } else {

                    //Si no esta activado -> Siguiente pantalla
                    layoutLogin.setVisibility(View.GONE);
                    layoutPrincipal.setVisibility(View.VISIBLE);

                }
            }
        });

        //Inicia la implementacion si el usuario quiere hacer una cuenta
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {

                    //Si la casilla esta marcada
                    continueBtn.setText("Register");
                    loginTV.setText("Create Account");

                    //PONER AQUI LA IMPLEMENTACION PARA REGISTRAR UN USUARIO

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

        //Se declaran las variables que se van a usar en los metodos
        EditText usernameText = findViewById(R.id.username_text);
        EditText passwordText = findViewById(R.id.password_text);

        final String email = usernameText.getText().toString();
        final String contra = passwordText.getText().toString();

        if (!email.isEmpty() && !contra.isEmpty())
        {
            mAuth.signInWithEmailAndPassword(email,contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //Si la informacion de la BD es correcta
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"CORRECTO",Toast.LENGTH_LONG).show();

                        //Se crea un nuevo intent y se inicia otra pantalla
                        Intent nuevoIntent = new Intent(Login.this, Configuracion.class);
                        nuevoIntent.putExtra("usuario",email);
                        startActivityForResult(nuevoIntent, 1);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"INCORRECTO",Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            Toast.makeText(getApplicationContext(),"Por favor llene todos los campos",Toast.LENGTH_LONG).show();
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


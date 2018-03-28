package com.cracks.proyectoplataformasmoviles;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener listener;
    View view;

    int columna;
    int fila;
    String img;
    int posX;
    int posY;

    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hola
        view = this.getCurrentFocus();
        mAuth=FirebaseAuth.getInstance();
        //Variables / objetos que no se usan.
        TextView usernameTV = findViewById(R.id.username_id);
        TextView passwordTV = findViewById(R.id.password_id);
        TextView welcomeTV = findViewById(R.id.welcome_tv);
        TextView mensajeTV = findViewById(R.id.mensaje_tv);
        final EditText roomText = findViewById(R.id.room_text);

        //Variables que se usan, por ello se declaran final.
        final TextView loginTV = findViewById(R.id.login_id);
        final Button continueBtn = findViewById(R.id.continue_btn);//Ingresar sesion
        final ConstraintLayout layoutLogin = findViewById(R.id.loginLayout);
        final ConstraintLayout layoutPrincipal = findViewById(R.id.principalLayout);
        final Button nextBtn = findViewById(R.id.entrar_btn);
        final Switch sw = findViewById(R.id.switch_login);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference mDatabase;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                mDatabase = database.getReference("Cuartos");

                try{
                    mDatabase.child(roomText.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            try{
                                Cuarto cuarto = dataSnapshot.getValue(Cuarto.class);

                                columna = cuarto.getColumnasT();
                                fila = cuarto.getFilasT();
                                img = cuarto.getImagen();
                                posX = cuarto.getPosicionX();
                                posY = cuarto.getPosicionY();
                                Intent intent = new Intent(Login.this, Display.class);
                                intent.putExtra("columna",(int)columna);
                                intent.putExtra("fila",(int) fila);
                                intent.putExtra("img", img.toString());
                                intent.putExtra("posX",(int) posX );
                                intent.putExtra("posY",(int) posY);
                                intent.putExtra("cuarto",roomText.getText().toString());

                                startActivityForResult(intent, 1);
                            }catch(Exception e){
                                Toast.makeText(getApplicationContext(),"There's no room: " + roomText.getText().toString(),Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"There's no room: " + roomText.getText().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });


        //Listener de la base de datos
        listener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user=mAuth.getCurrentUser();
            }
        };

        //Inicia la implmementacion si el usuario desea hostear una sesion.
        final CheckBox checkBox = findViewById(R.id.checkBox);

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

                } else {

                    continueBtn.setText("Continue");
                    loginTV.setText("Login");

                }
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked())
                {
                    EditText usernameText = findViewById(R.id.username_text);
                    EditText passwordText = findViewById(R.id.password_text);

                    final String email = usernameText.getText().toString() + "@gmail.com";
                    final String contra = passwordText.getText().toString();
                    //no tiene usuario
                    if (!email.isEmpty() && !contra.isEmpty())
                    {
                        //si no estan vacios
                        mAuth.createUserWithEmailAndPassword(email,contra);
                        checkBox.setChecked(false);

                    }

                }
                else
                {
                    ingresar();

                }
            }
        });

        roomText.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

    }

    private void ingresar()
    {

        //Se declaran las variables que se van a usar en los metodos
        final EditText usernameText = findViewById(R.id.username_text);
        EditText passwordText = findViewById(R.id.password_text);

        //Boton de carga invisible hasta interaccion
        final ProgressBar carga = findViewById(R.id.cargaInicio);

        final Button continueBtn = findViewById(R.id.continue_btn);

        final String email = usernameText.getText().toString() + "@gmail.com";
        final String contra = passwordText.getText().toString();

        if (!email.isEmpty() && !contra.isEmpty())
        {
            AsyncTask<String, String, String> cargaLogin = new AsyncTask<String, String, String>() {

                String estado = "";

                @Override
                protected void onPreExecute() {

                    continueBtn.setClickable(false);
                    carga.setVisibility(View.VISIBLE);
                }

                @Override
                protected String doInBackground(String... strings) {

                    try {

                        Thread.sleep(3000);

                        mAuth.signInWithEmailAndPassword(email, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //Si la informacion de la BD es correcta
                                if (task.isSuccessful()) {

                                    estado = "listo";
                                    //Se crea un nuevo intent y se inicia otra pantalla

                                    Intent nuevoIntent = new Intent(Login.this, Configuracion.class);
                                    nuevoIntent.putExtra("usuario",usernameText.getText().toString());
                                    startActivityForResult(nuevoIntent, 1);


                                } else {

                                    estado = "fallo";
                                    Toast.makeText(getApplicationContext(), "USERNAME OR PASWORD IS INCORRECT", Toast.LENGTH_LONG).show();

                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return estado;
                }

                @Override
                protected void onPostExecute(String s) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    carga.setVisibility(View.GONE);
                    continueBtn.setClickable(true);

                }

            };

            cargaLogin.execute();

        } else {
            Toast.makeText(getApplicationContext(), "Please complete all of the fields", Toast.LENGTH_LONG).show();
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


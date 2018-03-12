package com.cracks.proyectoplataformasmoviles;

import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView loginTV = findViewById(R.id.login_id);
        final TextView usernameTV = findViewById(R.id.username_id);
        final TextView passwordTV = findViewById(R.id.password_id);
        final TextView welcomeTV = findViewById(R.id.welcome_tv);
        final TextView mensajeTV = findViewById(R.id.mensaje_tv);
        final TextView roomText = findViewById(R.id.room_text);

        final EditText usernameText = findViewById(R.id.username_text);
        final EditText passwordText = findViewById(R.id.password_text);

        final Button entrarBtn = findViewById(R.id.entrar_btn);
        final Button continueBtn = findViewById(R.id.continue_btn);

        final ConstraintLayout layoutLogin = findViewById(R.id.loginLayout);
        final ConstraintLayout layoutPrincipal = findViewById(R.id.principalLayout);

        final CheckBox checkBox = findViewById(R.id.checkBox);

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

    }

}

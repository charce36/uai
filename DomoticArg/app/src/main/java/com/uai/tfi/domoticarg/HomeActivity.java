package com.uai.tfi.domoticarg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button btnDispositivos;
    Button btnAcercaDe;
    Button btnSalir;
    Button btnOpciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnDispositivos = (Button) findViewById(R.id.btnPantallaDispositivos);
        btnSalir = (Button) findViewById(R.id.btnSalir);
        btnAcercaDe = (Button) findViewById(R.id.btnPantallaNosotros);
        btnOpciones = (Button) findViewById(R.id.btnPantallaOpciones);

        btnDispositivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
                System.exit(0);
            }
        });

        btnAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        btnOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeActivity.this, OpcionesActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.uai.tfi.domoticarg;


import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.uai.tfi.domoticarg.dal.DatabaseHandler;
import com.uai.tfi.domoticarg.model.Device;

public class MainActivity extends AppCompatActivity {

    ToggleButton[] botones;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Button btnVolver, btnAdd;
    private String m_Text = "";
    private Context cont = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        botones = new ToggleButton[10];

        btnVolver = (Button) findViewById(R.id.btnVolver);
        btnAdd = (Button) findViewById(R.id.btnnewdevice);


        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                // Obtengo codigo
                AlertDialog.Builder builder = new AlertDialog.Builder(cont);
                builder.setTitle("Nombre");

                final EditText input = new EditText(cont);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        new IntentIntegrator(MainActivity.this).initiateScan();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        DatabaseHandler db = new DatabaseHandler(this);

        List<Device> contacts = db.getAllDevices();

        for (Device cn : contacts) {
            int btnID = cn.getID();
            String name = cn.getName();
            final String onlineID = cn.getOnlineID();

            if ( name.charAt(name.length() - 1) == '-' ) {
                name = name.substring(0, name.length() - 1);
                AgregarDispositivo(btnID,name+"(2)",onlineID,"luzPin2");
            }else {
                AgregarDispositivo(btnID,name,onlineID,"luzPin");
            }

        }
    }


    public void AgregarDispositivo(int id, String nombre, final String webID, final String devID){
        // Agrego boton a la lista
        TextView mytext = new TextView (this);
        final ToggleButton myButton = new ToggleButton(this);
        int i = id;
        myButton.setId(i);

        mytext.setText(nombre);
        LinearLayout ll = (LinearLayout)findViewById(R.id.granscroll);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(1);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mytext.setTypeface(null, Typeface.BOLD);
        mytext.setTextColor(0xFF000000);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 40;
        lp.rightMargin = 20;

        layout.addView(mytext, lp);
        lp.leftMargin = 20;
        layout.addView(myButton, lp);
        ll.addView(layout);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DatabaseReference myRef = database.getReference(webID+"//"+devID);
                if (myButton.isChecked()) {
                    myRef.setValue(1);
                }else {
                    myRef.setValue(0);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            handleResult(scanResult);
        } catch (Exception e) {

        }
    }

    // Metodo para validar si existe

    private boolean existeDispositivo(String valor) {

        DatabaseHandler db = new DatabaseHandler(this);
        List<Device> misdeviceslocales = db.getAllDevices();

        for (Device cn : misdeviceslocales) {
            String onlineID = cn.getOnlineID();
            if (onlineID.equals(valor)) {
                return true;
            }
        }
        return false;
    }


    // Metodo con la logica del agregar boton luego de leer el codigo.
    private void handleResult(IntentResult scanResult) {

        String datos = scanResult.getContents();
        //descomentar la linea que sigue para las pruebas en emulador pc.
        //datos = "1disp1";

        if ((datos != null) && (!existeDispositivo(datos.substring(1, datos.length())))) {
            //saco el primer caracter por que es el codigo de tipo de dispositivo
            //1: 1 luz; 2: 2 luces; 3: cortina; 4: motor ventilador...
            char codigo = datos.charAt(0);
            datos = datos.substring(1, datos.length());

            updateUITextViews(datos);

            // Verifico Validez

            if ( codigo == '1' ) {
                //Es Valido

                DatabaseReference myRef = database.getReference(datos+"//luzPin");
                // Agrego a la BBDD
                myRef.setValue(0);

                AgregarDispositivo(1,m_Text,datos,"luzPin");
                // Agregado a la vista.


                DatabaseHandler db = new DatabaseHandler(this);
                db.addDevice(new Device(m_Text, datos));
                //agregado a la DB
            } else if ( codigo == '2' ) {
                DatabaseReference myRef = database.getReference(datos+"//luzPin");
                // Agrego a la BBDD
                myRef.setValue(0);

                myRef = database.getReference(datos+"//luzPin2");
                // Agrego a la BBDD
                myRef.setValue(0);


                AgregarDispositivo(1,m_Text,datos,"luzPin");
                AgregarDispositivo(1,m_Text+"(2)",datos,"luzPin2");
                // Agregado a la vista.


                DatabaseHandler db = new DatabaseHandler(this);
                db.addDevice(new Device(m_Text, datos));
                db.addDevice(new Device(m_Text+"-", datos));
                //agregado a la DB
            }

        } else {
            if (datos == null) {
                updateUITextViews("No se ha escaneado nada. No se agrego dispositivo.");
            } else {
                updateUITextViews("El codigo ingresado ya existe. No se agrego nada.");
            }
        }
    }

    // Alert Box
    private void updateUITextViews(String scan_result) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Agregado el dispositivo!");
        //siguiente linea comentada por si el usuario va a saber el ID del dispositivo (solo debug por ahora)
        alertDialog.setMessage(scan_result);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}

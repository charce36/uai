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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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

        mytext.setTypeface(null, Typeface.BOLD);
        mytext.setTextColor(0xFFFFFFFF);
        mytext.setTextAppearance(this, android.R.style.TextAppearance_Holo_Medium);

        //busco el Scroll que es el principal contenedor
        LinearLayout scroll = (LinearLayout)findViewById(R.id.granscroll);

        //creo layout para la linea horizontal
        LinearLayout layoutancha = new LinearLayout(this);
        layoutancha.setId(10000+i);
        layoutancha.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parametros.setMargins(20,10,20,10);
        layoutancha.setLayoutParams(parametros);

        //creo la layout del texto (izq)
        LinearLayout textoLayout = new LinearLayout(this);
        textoLayout.setGravity(Gravity.CENTER_VERTICAL);
        textoLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, (float) 2.0 ));

        //creo la layout del boton (der)
        LinearLayout btnLayout = new LinearLayout(this);
        btnLayout.setGravity(Gravity.RIGHT);
        btnLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT , (float) 2.0 ));

        // meto las layouts dentro de cada container.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textoLayout.addView(mytext);
        btnLayout.addView(myButton);
        layoutancha.addView(textoLayout);
        layoutancha.addView(btnLayout);
        scroll.addView(layoutancha);

        //hago un get del valor para poder actualizar los valores de los botones
        DatabaseReference myRef = database.getReference(webID).child(devID);
        //hago un get del valor para poder actualizar los valores de los botones
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int valor = dataSnapshot.getValue(int.class);
                if (valor == 1) {
                    myButton.setChecked(true);
                } else {
                    myButton.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //do nothing
            }
        });


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

            //La siguiente linea se descomenta solo a modo de debug.
            //updateUITextViews("Agregado!",datos);

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
                updateUITextViews("Error","No se ha escaneado nada. No se agrego dispositivo.");
            } else {
                updateUITextViews("Error","El codigo ingresado ya existe. No se agrego nada.");
            }
        }
    }

    // Alert Box
    private void updateUITextViews(String Titulo, String scan_result) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(Titulo);
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

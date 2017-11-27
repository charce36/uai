package com.uai.tfi.domoticarg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.uai.tfi.domoticarg.dal.DatabaseHandler;
import com.uai.tfi.domoticarg.model.Device;

import java.util.List;

public class OpcionesActivity extends AppCompatActivity {

    Button btnVolver, btnAdd;
    private Context cont = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        btnVolver = (Button) findViewById(R.id.btnVolverOpc);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //cargo lista de dispositivos en la vista segun la SQLite

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


    public void AgregarDispositivo(final int id, final String nombre, final String webID, final String devID){
        // Agrego boton a la lista
        TextView mytext = new TextView (this);
        final Button myButton = new Button(this);
        int i = id;
        myButton.setId(i);
        myButton.setText("Eliminar");
        mytext.setText(nombre);
        LinearLayout ll = (LinearLayout)findViewById(R.id.granscroll);
        LinearLayout layout = new LinearLayout(this);
        layout.setId(10000+i);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(1);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 50;
        lp.rightMargin = 20;
        layout.addView(mytext, lp);
        lp.leftMargin = 20;
        layout.addView(myButton, lp);
        ll.addView(layout);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                AlertDialog.Builder builder = new AlertDialog.Builder(cont);
                builder.setCancelable(true);
                builder.setTitle("Esta seguro?");
                builder.setMessage("Eliminar dispositivo "+ nombre);
                builder.setPositiveButton("Confirmar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //elimino de la db
                                DatabaseHandler db = new DatabaseHandler(cont);
                                Device temp = new Device(id, nombre, webID);
                                db.deleteDevice(temp);
                                //elimino de la vista
                                findViewById(10000+id).setVisibility(View.GONE);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

    }

}

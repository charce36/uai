package com.uai.tfi.domoticarg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uai.tfi.domoticarg.rest.MyApiEndpointInterface;
import com.uai.tfi.domoticarg.rest.Respuesta;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AboutActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://domoticarg-webapi.azurewebsites.net/api/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Button btnVolver, btnCosto;
    ImageButton btnFb, btnTwt, btnGp, btnMsg;
    private Context cont = this;
    private String mi_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnVolver = (Button) findViewById(R.id.btnVolverdeAbout);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnFb = (ImageButton) findViewById(R.id.imgFb);
        btnTwt = (ImageButton) findViewById(R.id.imgTwt);
        btnGp = (ImageButton) findViewById(R.id.imgGp);
        btnMsg = (ImageButton) findViewById(R.id.imgMsg);

        btnCosto = (Button) findViewById(R.id.btnCosto);

        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://www.facebook.com/diego.leonian");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        btnTwt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://twitter.com/DiegoLeonian");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        btnGp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriUrl = Uri.parse("https://plus.google.com/108737692807438777620");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
//                sendIntent.setType("text/plain");
//                sendIntent.setData(Uri.parse("d.leonian@gmail.com"));
//                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
//                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "d.leonian@gmail.com" });
//                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Contacto desde AppMobile DomoticArg");
//                startActivity(sendIntent);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","d.leonian@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contacto desde AppMobile DomoticArg");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });



        btnCosto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtengo codigo
                AlertDialog.Builder builder = new AlertDialog.Builder(cont);
                builder.setTitle("Codigo Postal");

                final EditText input = new EditText(cont);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mi_Text = input.getText().toString();
                        int myNum = 0;

                        try {
                            myNum = Integer.parseInt(mi_Text.toString());
                        } catch(NumberFormatException nfe) {
                            TextView tb = (TextView) findViewById(R.id.tbCosto);
                            tb.setText("Formato de numero inválido");
                        }
                        if (myNum > 0) {
                            // llamo a la api

                            MyApiEndpointInterface apiService =
                                    retrofit.create(MyApiEndpointInterface.class);

                            Call<Respuesta> call = apiService.getResponse(myNum);
                            call.enqueue(new Callback<Respuesta>() {
                                @Override
                                public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                                    int statusCode = response.code();
                                    Respuesta respuesta = response.body();
                                    String mi_Costo = String.valueOf(respuesta.getCosto());
                                    String mi_Destino = respuesta.getDestino();
                                    String mi_distancia = respuesta.getDistancia();
                                    String miTexto = "";

                                    if (mi_Destino.equals("ERROR")){
                                        miTexto = "Error: El Código Postal ingresado no está registrado en nuestra base de datos.";
                                    } else {
                                        if (respuesta.getCosto() > 0) {
                                            miTexto = "El costo de su envio es de: $" + mi_Costo + " llegando a " + mi_Destino + " ( " + mi_distancia + " )";
                                        } else {
                                            miTexto = "Ha habido un error con el codigo ingresado";
                                        }
                                    }
                                    TextView tb = (TextView) findViewById(R.id.tbCosto);
                                    tb.setText(miTexto);
                                }

                                @Override
                                public void onFailure(Call<Respuesta> call, Throwable t) {
                                    TextView tb = (TextView) findViewById(R.id.tbCosto);
                                    tb.setText("Ha habido un error en el codigo ingresado");
                                }
                            });
                            // proceso devolucion
                        }
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
    }

}




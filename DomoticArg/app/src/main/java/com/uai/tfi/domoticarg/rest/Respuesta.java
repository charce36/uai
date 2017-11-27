
package com.uai.tfi.domoticarg.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Respuesta {

    @SerializedName("Origen")
    @Expose
    private String origen;
    @SerializedName("Destino")
    @Expose
    private String destino;
    @SerializedName("Distancia")
    @Expose
    private String distancia;
    @SerializedName("Costo")
    @Expose
    private double costo;

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

}
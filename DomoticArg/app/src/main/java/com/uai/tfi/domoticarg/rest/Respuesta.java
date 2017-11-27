package com.uai.tfi.domoticarg.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Respuesta {

    @SerializedName("Origen")
    @Expose
    private Origen origen;
    @SerializedName("Destino")
    @Expose
    private Destino destino;
    @SerializedName("Distancia")
    @Expose
    private Distancia distancia;
    @SerializedName("Costo")
    @Expose
    private Costo costo;
    @SerializedName("Fecha")
    @Expose
    private Fecha fecha;

    public Origen getOrigen() {
        return origen;
    }

    public void setOrigen(Origen origen) {
        this.origen = origen;
    }

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }

    public Distancia getDistancia() {
        return distancia;
    }

    public void setDistancia(Distancia distancia) {
        this.distancia = distancia;
    }

    public Costo getCosto() {
        return costo;
    }

    public void setCosto(Costo costo) {
        this.costo = costo;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

}
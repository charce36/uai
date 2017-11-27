package com.uai.tfi.domoticarg.rest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MyApiEndpointInterface {
    // Request method and URL specified in the annotation
    @GET("Costo/{id}")
    Call<Respuesta> getResponse(@Path("id") int id);
}
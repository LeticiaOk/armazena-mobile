package com.example.armazena.retrofit;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

// ApiService.kt
interface ApiService {
    @POST("/armazena_api/login.php")
    @FormUrlEncoded
    fun login(
            @Field("usuario") usuario: String,
            @Field("senha") senha: String
    ): Call<LoginResponse>

    @GET("/armazena_api/produto.php")
    fun getProdutos(): Call<List<Produto>>
}
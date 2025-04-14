package com.example.armazena.entities

import com.google.gson.annotations.SerializedName

public data class Usuario (
    @SerializedName("USUARIO_ID") val idUsuario: Int,
    @SerializedName("USUARIO_NOME") val nomeUsuario: String,
    @SerializedName("USUARIO_EMAIL") val emailUsuario: String,
    @SerializedName("USUARIO_SENHA") val senhaUsuario: String,
    @SerializedName("USUARIO_EMPRESA") val empresaUsuario: String
)
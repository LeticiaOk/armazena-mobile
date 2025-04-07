package com.example.armazena.entities

import com.google.gson.annotations.SerializedName

public data class Categoria (
    @SerializedName("CATEGORIA_ID") val idCategoria: Int,
    @SerializedName("CATEGORIA_NOME") val nomeCategoria: String,
)
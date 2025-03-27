package com.example.armazena.entities

import com.google.gson.annotations.SerializedName

public data class Produto (
    val PRODUTO_ID: Int,
    val PRODUTO_NOME: String,
    val CATEGORIA_ID: Int,
    val PRODUTO_PRECO: Double,
    val PRODUTO_DESC: String
)


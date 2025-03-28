package com.example.armazena.entities

import com.google.gson.annotations.SerializedName

public data class Produto (
    @SerializedName("PRODUTO_NOME") val nomeProduto: String,
    @SerializedName("CATEGORIA_ID") val idCategoria: Int,
    @SerializedName("PRODUTO_PRECO") val precoProduto: Double,
    @SerializedName("PRODUTO_DESC") val descricaoProduto: String
)
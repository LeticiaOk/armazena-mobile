package com.example.armazena.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

public data class Produto (
    @SerializedName("PRODUTO_ID") val idProduto: Int,
    @SerializedName("PRODUTO_NOME") val nomeProduto: String,
    @SerializedName("CATEGORIA_ID") val idCategoria: Int,
    @SerializedName("PRODUTO_PRECO") val precoProduto: Double,
    @SerializedName("PRODUTO_DESC") val descricaoProduto: String
)
package com.example.apparmazena

data class Produto(
    val PRODUTO_ID: Int,
    val PRODUTO_NOME: String,
    val PRODUTO_DESC: String,
    val PRODUTO_PRECO: String,
    val PRODUTO_IMAGEM_URL: String,
    val CATEGORIA_ID: Int,
    val PRODUTO_QTD: Int // <- Adicione essa linha
)
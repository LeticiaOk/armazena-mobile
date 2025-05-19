package com.example.armazena.entities

import com.google.gson.annotations.SerializedName

public class Produto() {

    data class Produto (
        @SerializedName("PRODUTO_ID") val idProduto: Int,
        @SerializedName("PRODUTO_NOME") val nomeProduto: String,
        @SerializedName("CATEGORIA_ID") val idCategoria: Int,
        @SerializedName("PRODUTO_PRECO") val precoProduto: Double,
        @SerializedName("PRODUTO_DESC") val descricaoProduto: String
    )

    data class ProdutoCadastroRequest(
        val nome_produto: String,
        val id_categoria: Int,
        val preco_produto: Double,
        val descricao_produto: String
    )

    data class ProdutoCadastroResponse(
        val sucesso: Boolean,
        val mensagem: String?,
        val id_produto: Int?
    )

    data class ProdutoUpdateRequest(
        val id_produto: Int,
        val nome_produto: String,
        val id_categoria: Int,
        val preco_produto: Double,
        val descricao_produto: String
    )

    data class ProdutoUpdateResponse(
        val sucesso: Boolean,
        val mensagem: String?,
        val id_produto: Int?
    )
}
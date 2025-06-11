package com.example.apparmazena

import com.example.apparmazena.LoginActivity.LoginResponse
import retrofit2.Call
import com.example.apparmazena.Usuario.UsuarioCadastroRequest
import com.example.apparmazena.Usuario.UsuarioCadastroResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("/armazena_api/login.php")
    fun login(
        // Passando argumentos para url
        @Query("usuario") usuario: String,
        @Query("senha") senha: String
    ): Call<List<LoginResponse>> // Recebe no formato lista de LoginResponse

    @POST("/armazena_api/usuario_cadastro.php")
    fun cadastrarUsuario(@Body requestBody: UsuarioCadastroRequest): Call<UsuarioCadastroResponse>

    @GET("/armazena_api/categoria.php")
    fun getCategorias(): Call<List<Categoria>>

    @GET("/armazena_api/produto.php")
    fun getProdutos(): Call<List<Produto>>

    @FormUrlEncoded
    @POST("/armazena_api/incluir_produto.php")
    fun incluirProduto(
        @Field("PRODUTO_NOME") nome: String,
        @Field("PRODUTO_DESC") descricao: String,
        @Field("PRODUTO_PRECO") preco: String,
        @Field("PRODUTO_IMAGEM_URL") imagem: String,
        @Field("CATEGORIA_ID") categoriaId: Int,
        @Field("PRODUTO_QTD") quantidade: Int // <- novo campo

    ): Call<Void>

    @FormUrlEncoded
    @POST("/armazena_api/editar_produto.php")
    fun editarProduto(
        @Field("PRODUTO_ID") id: Int,
        @Field("PRODUTO_NOME") nome: String,
        @Field("PRODUTO_DESC") descricao: String,
        @Field("PRODUTO_PRECO") preco: String,
        @Field("PRODUTO_IMAGEM_URL") imagem: String,
        @Field("CATEGORIA_ID") categoriaId: Int,
        @Field("PRODUTO_QTD") quantidade: Int // <- novo campo
    ): Call<Void>

    @FormUrlEncoded
    @POST("/armazena_api/deletar_produto.php")
    fun deletarProduto(
        @Field("PRODUTO_ID") id: Int
    ): Call<Void>
}
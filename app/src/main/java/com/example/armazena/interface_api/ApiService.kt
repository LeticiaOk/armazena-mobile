package com.example.armazena.interface_api

import com.example.armazena.activities.login.LoginActivity.LoginResponse
import com.example.armazena.activities.produto.ProdutoEditarActivity.ProdutoUpdateRequest
import com.example.armazena.activities.produto.ProdutoEditarActivity.ProdutoUpdateResponse
import com.example.armazena.entities.Produto.Produto
import com.example.armazena.entities.Produto.ProdutoCadastroRequest
import com.example.armazena.entities.Produto.ProdutoCadastroResponse
import com.example.armazena.entities.Usuario.UsuarioCadastroRequest
import com.example.armazena.entities.Usuario.UsuarioCadastroResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("/armazena_api/login.php")
    fun login(
        @Query("usuario") usuario: String,
        @Query("senha") senha: String
    ): Call<List<LoginResponse>>

    @POST("/armazena_api/usuario_cadastro.php")
    fun cadastrarUsuario(@Body requestBody: UsuarioCadastroRequest): Call<UsuarioCadastroResponse>

    @GET("/armazena_api/produto.php")
    fun getProdutos(): Call<List<Produto>>

    @POST("/armazena_api/produto_cadastro.php")
    fun cadastrarProduto(@Body requestBody: ProdutoCadastroRequest): Call<ProdutoCadastroResponse>

    @POST("/armazena_api/produto_editar.php")
    fun atualizarProduto(@Body requestBody: ProdutoUpdateRequest): Call<ProdutoUpdateResponse>

    @FormUrlEncoded
    @POST("/armazena_api/produto_delete.php")
    fun deletarProduto(
        @Field("PRODUTO_ID") id: Int
    ): Call<Void>
}
package com.example.armazena

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

class ProdutoEditarActivity : AppCompatActivity() {
    private lateinit var nomeProdutoEditText: EditText
    private lateinit var categoriaProdutoEditText: EditText
    private lateinit var precoProdutoEditText: EditText
    private lateinit var descProdutoEditText: EditText

    data class ProdutoUpdateRequest(
        val id_produto: Int,
        val nome_produto: String,
        val id_categoria: Int,
        val preco_produto: Double,
        val desc_produto: String
    )

    data class ProdutoUpdateResponse(
        val sucesso: Boolean,
        val mensagem: String?,
        val id_produto: Int?
    )

    interface ProdutoEditarApiService {
        @PUT("/armazena_api/produto_editar.php/{id}")
        fun atualizarProduto(
            @Path("id") id: Int,
            @Body requestBody: ProdutoUpdateRequest
        ): Call<ProdutoUpdateResponse>
    }

    object RetrofitClient {
        private const val BASE_URL = "http://192.168.0.43/"

        val instance: ProdutoEditarApiService by lazy {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(ProdutoEditarApiService::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_produto_editar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nomeProdutoEditText = findViewById(R.id.nomeProdutoEditText)
        categoriaProdutoEditText = findViewById(R.id.categoriaProdutoEditText)
        precoProdutoEditText = findViewById(R.id.precoProdutoEditText)
        descProdutoEditText = findViewById(R.id.descProdutoEditText)

        val nomeProduto = intent.getStringExtra("PRODUTO_NOME")
        val categoriaId = intent.getIntExtra("CATEGORIA_ID", -1)
        val precoProduto = intent.getDoubleExtra("PRODUTO_PRECO", -1.0)
        val descProduto = intent.getStringExtra("PRODUTO_DESC")

        nomeProdutoEditText.setText(nomeProduto)
        categoriaProdutoEditText.setText(categoriaId.toString())
        precoProdutoEditText.setText(precoProduto.toString())
        descProdutoEditText.setText(descProduto)

        val EditProdutoButton: Button = findViewById(R.id.EditProdutoButton)
        EditProdutoButton.setOnClickListener {
            atualizarProduto()
        }
    }

    private fun atualizarProduto() {
        val idProduto = intent.getIntExtra("PRODUTO_ID", -1)
        if (idProduto == -1) {
            Log.e("ProdutoEditar", "ID do produto não encontrado!")
            return
        }

        val nomeProduto = nomeProdutoEditText.text.toString()
        val categoriaId = categoriaProdutoEditText.text.toString().toInt()
        val precoProduto = precoProdutoEditText.text.toString().toDouble()
        val descProduto = descProdutoEditText.text.toString()

        if (nomeProduto.isEmpty()) {
            Log.e("ProdutoEditar", "Nome do produto não pode ser vazio")
            return
        }

        if (categoriaId == null || categoriaId == -1) {
            Log.e("ProdutoEditar", "Categoria inválida")
            return
        }

        if (precoProduto == null || precoProduto <= 0) {
            Log.e("ProdutoEditar", "Preço inválido")
            return
        }

        if (descProduto.isEmpty()) {
            Log.e("ProdutoEditar", "A descrição não pode ser vazia")
            return
        }

        val produtoParaAtualizar = ProdutoUpdateRequest(
            id_produto = idProduto,
            nome_produto = nomeProduto,
            id_categoria = categoriaId,
            preco_produto = precoProduto,
            desc_produto = descProduto
        )

        val call = RetrofitClient.instance.atualizarProduto(idProduto, produtoParaAtualizar)

        call.enqueue(object : Callback<ProdutoUpdateResponse> {
            override fun onResponse(
                call: Call<ProdutoUpdateResponse>,
                response: Response<ProdutoUpdateResponse>
            ) {
                if (response.isSuccessful) {
                    val produtoAtualizado = response.body()
                    Log.d("ProdutoEditar", "Produto atualizado: $produtoAtualizado")
                    finish()
                } else {
                    Log.e("ProdutoEditar", "Erro ao atualizar produto: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ProdutoUpdateResponse>, t: Throwable) {
                Log.e("ProdutoEditar", "Falha na requisição: ${t.message}")
            }
        })
    }
}
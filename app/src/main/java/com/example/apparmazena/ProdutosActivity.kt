package com.example.apparmazena

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.http.GET

class ProdutosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProdutoAdapter

    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_produtos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerViewProdutos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnLogout = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logout()
        }

        // ConfiguraÇÃo do Logging Interceptor
        val logging = HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // ConfiguraÇÃo do OkHttpClient com o interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        // Configuração do Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.4/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getProdutos().enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    val produtos = response.body() ?: emptyList()
                    adapter = ProdutoAdapter(produtos)
                    recyclerView.adapter = adapter
                } else {
                    Log.e("API Error", "Response not successful. Code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Log.e("API Failure", "Error fetching products", t)
            }
        })

    }

    private fun logout() {
        getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().clear().apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    interface ApiService {
        @GET("/armazena_api/produto.php")
        fun getProdutos(): Call<List<Produto>>

    }

    data class Produto(
        val PRODUTO_ID: Int,
        val PRODUTO_NOME: String,
        val PRODUTO_DESC: String,
        val PRODUTO_PRECO: String,
        val CATEGORIA_ID: Int,
    )

}
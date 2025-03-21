package com.example.armazena

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.armazena.entities.Produto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class ProdutoActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProdutoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produto)

        // Configura a RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProdutos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }

        // Configuração do Retrofit e chamada da API
        val logging = HttpLoggingInterceptor { message -> Log.d("OkHttp", message) }
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.205/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiService = retrofit.create(ProdutoApiService::class.java)
        apiService.getProdutos().enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    val produtos = response.body() ?: emptyList()
                    recyclerView.adapter = ProdutoAdapter(produtos)
                    for (produto in produtos) {
                        System.out.println(produto)
                    }
                } else {
                    Log.e("API Error", "Response not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Log.e("API Failure", "Error fetching products", t)
            }
        })
    }

    interface ProdutoApiService {
        @GET("/armazena_api/produto.php")
        fun getProdutos(): Call<List<Produto>>
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply() // Remove os dados do usuário

        // Redireciona para a tela de login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finaliza a ProdutosActivity
    }
}
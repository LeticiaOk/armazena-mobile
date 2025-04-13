package com.example.armazena.activities.produto

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.armazena.activities.login.LoginActivity
import com.example.armazena.adapters.ProdutoAdapter
import com.example.armazena.R
import com.example.armazena.entities.Produto
import com.example.armazena.interface_api.ApiService
import com.example.armazena.retrofit.RetrofitClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

class ProdutoActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view_produtos)

        val cadastroButton = findViewById<Button>(R.id.cadastroProdutoButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // Configura a RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProdutos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        listagemProdutos();
        cadastroButton.setOnClickListener {
            cadastroPage()
        }
        logoutButton.setOnClickListener {
            logout()
        }
    }
    private fun listagemProdutos() {
        val call = RetrofitClient.instance.getProdutos()
        call.enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    val produtos = response.body()?.toMutableList() ?: mutableListOf()
                    recyclerView.adapter = ProdutoAdapter(produtos, this@ProdutoActivity)
                } else {
                    Log.e("API Error", "Response not successful. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Log.e("API Failure", "Error fetching products", t)
            }
        })
    }
    private fun cadastroPage() {
        val intent = Intent(this, ProdutoCadastroActivity::class.java)
        startActivity(intent)
    }
    private fun logout() {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply() // Remove os dados do usuário

        // Redireciona para a tela de login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
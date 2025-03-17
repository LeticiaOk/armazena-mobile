package com.example.armazena

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.armazena.LoginActivity.LoginResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.DecimalFormat

class ProdutosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_produtos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    interface ApiService {
        @GET("/armazena_api/login.php")
        fun login(
            @Query("usuario") usuario: String,
            @Query("senha") senha: String
        ): Call<List<LoginResponse>>
    }

    data class ProdutoResponse (
        val PRODUTO_ID: Int,
        val PRODUTO_NOME: String,
        val CATEGORIA_ID: Int,
        val PRODUTO_PRECO: DecimalFormat
    )

    data class CategoriaResponse (
        val CATEGORIA_ID: Int,
        val CATEGORIA_NOME: String
    )

}
package com.example.armazena

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.armazena.LoginActivity
import com.example.armazena.entities.Produto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class ProdutoCadastro : AppCompatActivity() {
    private lateinit var nomeProdutoEditText: EditText
    private lateinit var categoriaProdutoEditText: EditText
    private lateinit var precoProdutoEditText: EditText
    private lateinit var descProdutoEditText: EditText

    interface ProdutoCadastroApiService {
        @POST("/armazena_api/produto_cadastro.php")
        fun cadastroProduto(@Body produtoCadastroRequest: Produto): Call<Produto>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_produto_cadastro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nomeProdutoEditText = findViewById(R.id.nomeProdutoEditText)
        categoriaProdutoEditText = findViewById(R.id.categoriaProdutoEditText)
        precoProdutoEditText = findViewById(R.id.precoProdutoEditText)
        descProdutoEditText = findViewById(R.id.descProdutoEditText)
        val cadastroProdutoButton: Button = findViewById(R.id.cadastroProdutoButton)
        cadastroProdutoButton.setOnClickListener {
            cadastrarProduto()
        }
    }
    private fun cadastrarProduto() {
        val nomeProduto = nomeProdutoEditText.text.toString().trim();
        val categoriaId = categoriaProdutoEditText.text.toString().trim();
        val precoProduto = precoProdutoEditText.text.toString().trim();
        val descProduto = descProdutoEditText.text.toString().trim();

        if (nomeProduto.isEmpty() || categoriaId == null || precoProduto == null || descProduto.isEmpty()) {
            // Mostrar mensagem de erro ao usuário (campos inválidos)
            Log.e("CadastroProduto", "Campos inválidos")
            return
        }

        val categoriaIdInt = try { categoriaId.toInt() } catch (e: NumberFormatException) { -1 }
        val precoProdutoDouble = try { precoProduto.toDouble() } catch (e: NumberFormatException) { -1.0 }

        val produtoCadastroRequest = Produto(nomeProduto, categoriaId.toInt(), precoProduto.toDouble(), descProduto)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.43/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ProdutoCadastroApiService::class.java)
        val call = apiService.cadastroProduto(produtoCadastroRequest)
        call.enqueue(object : Callback<Produto> {
            override fun onResponse(
                call: Call<Produto>,
                response: Response<Produto>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val produtoCadastroResponses = response.body()
                    if(produtoCadastroResponses != null && response.isSuccessful) {
                        Log.d("CadastroProduto", "Produto cadastrado com sucesso!")
                        val intent = Intent(this@ProdutoCadastro, ProdutoActivity::class.java)
                        startActivity(intent)
                        finish()
                        //TODO: Mostrar feedback de sucesso ao usuário
                        limparCampos()
                        //TODO: Navegar para outra tela ou atualizar a lista de produtos
                    } else {
                        Log.e("API Error", "Response not successful. Code: ${response.code()}")
                    }
                } else {
                    Log.e("cadastroProduto", "Erro HTTP: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<Produto>, t: Throwable) {
                Log.e("CadastroProduto", "Falha na requisição: ${t.message}")
                runOnUiThread {
                    Toast.makeText(this@ProdutoCadastro, "Erro na conexão. Tente novamente.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun limparCampos() {
        nomeProdutoEditText.text.clear()
        categoriaProdutoEditText.text.clear()
        precoProdutoEditText.text.clear()
        descProdutoEditText.text.clear()
    }
}
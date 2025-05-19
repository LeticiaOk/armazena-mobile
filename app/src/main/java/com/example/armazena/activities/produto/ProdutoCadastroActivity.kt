package com.example.armazena.activities.produto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.armazena.R
import com.example.armazena.entities.Produto.ProdutoCadastroRequest
import com.example.armazena.entities.Produto.ProdutoCadastroResponse
import com.example.armazena.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdutoCadastroActivity : AppCompatActivity() {
    private lateinit var nomeProdutoEditText: EditText
    private lateinit var categoriaProdutoSpinner: Spinner
    private lateinit var precoProdutoEditText: EditText
    private lateinit var descProdutoEditText: EditText

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
        categoriaProdutoSpinner = findViewById(R.id.categoriaProdutoSpinner)
        val opcoes = listOf("Categoria do produto", "Alimentos e bebidas", "Informática", "Eletrônicos", "Roupas")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcoes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoriaProdutoSpinner.adapter = adapter
        precoProdutoEditText = findViewById(R.id.precoProdutoEditText)
        descProdutoEditText = findViewById(R.id.descProdutoEditText)
        val cadastroProdutoButton: Button = findViewById(R.id.cadastroProdutoButton)
        cadastroProdutoButton.setOnClickListener {
            cadastrarProduto()
        }
    }
    private fun cadastrarProduto() {
        val nomeProduto = nomeProdutoEditText.text.toString().trim();
        var categoriaProduto = categoriaProdutoSpinner.selectedItem.toString().trim();
        val precoProduto = precoProdutoEditText.text.toString().trim();
        val descProduto = descProdutoEditText.text.toString().trim();

        if (nomeProduto.isEmpty() || categoriaProduto.isEmpty() || precoProduto == null || descProduto.isEmpty()) {
            // Mostrar mensagem de erro ao usuário (campos inválidos)
            Log.e("CadastroProduto", "Campos inválidos")
            return
        }

        if(categoriaProduto == "Alimentos e bebidas") {
            categoriaProduto = "1"
        } else if(categoriaProduto == "Informática") {
            categoriaProduto = "2"
        } else if(categoriaProduto == "Eletrônicos") {
            categoriaProduto = "3"
        } else if(categoriaProduto == "Roupas") {
            categoriaProduto = "4"
        }

        val precoProdutoDouble = try { precoProduto.toDouble() } catch (e: NumberFormatException) { -1.0 }

        val produtoCadastroRequest = ProdutoCadastroRequest(
            nome_produto = nomeProduto,
            id_categoria = categoriaProduto.toInt(),
            preco_produto = precoProduto.toDouble(),
            descricao_produto = descProduto
        )

        val call = RetrofitClient.instance.cadastrarProduto(produtoCadastroRequest)
        call.enqueue(object : Callback<ProdutoCadastroResponse> {
            override fun onResponse(
                call: Call<ProdutoCadastroResponse>,
                response: Response<ProdutoCadastroResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val produtoCadastroResponses = response.body()
                    if(produtoCadastroResponses != null && response.isSuccessful) {
                        Log.d("CadastroProduto", "Produto cadastrado com sucesso!")
                        val intent = Intent(this@ProdutoCadastroActivity, ProdutoActivity::class.java)
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
            override fun onFailure(call: Call<ProdutoCadastroResponse>, t: Throwable) {
                Log.e("CadastroProduto", "Falha na requisição: ${t.message}")
                runOnUiThread {
                    Toast.makeText(this@ProdutoCadastroActivity, "Erro na conexão. Tente novamente.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun limparCampos() {
        nomeProdutoEditText.text.clear()
        categoriaProdutoSpinner.setSelection(0)
        precoProdutoEditText.text.clear()
        descProdutoEditText.text.clear()
    }
}
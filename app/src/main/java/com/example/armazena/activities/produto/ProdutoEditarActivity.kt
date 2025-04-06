package com.example.armazena.activities.produto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.armazena.entities.Produto.ProdutoUpdateRequest
import com.example.armazena.entities.Produto.ProdutoUpdateResponse
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.armazena.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdutoEditarActivity : AppCompatActivity() {
    private lateinit var nomeProdutoEditText: EditText
    private lateinit var categoriaProdutoEditText: EditText
    private lateinit var precoProdutoEditText: EditText
    private lateinit var descProdutoEditText: EditText

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

        val getNomeProduto = intent.getStringExtra("PRODUTO_NOME")
        val getCategoriaId = intent.getIntExtra("CATEGORIA_ID", 0)
        val getPrecoProduto = intent.getDoubleExtra("PRODUTO_PRECO", 0.0).toString()
        val getDescProduto = intent.getStringExtra("PRODUTO_DESC")

        nomeProdutoEditText.setText(getNomeProduto)
        categoriaProdutoEditText.setText(getCategoriaId.toString())
        precoProdutoEditText.setText(getPrecoProduto)
        descProdutoEditText.setText(getDescProduto)

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

        val nomeProduto = nomeProdutoEditText.text.trim().toString()
        val categoriaId = categoriaProdutoEditText.text.trim().toString().toInt()
        val precoProduto = precoProdutoEditText.text.trim().toString().toDouble()
        val descProduto = descProdutoEditText.text.trim().toString()

        val produtoUpdateRequest = ProdutoUpdateRequest(
            id_produto = idProduto,
            nome_produto = nomeProduto,
            id_categoria = categoriaId,
            preco_produto = precoProduto,
            desc_produto = descProduto
        )

        if (produtoUpdateRequest.nome_produto.isEmpty() || produtoUpdateRequest.id_categoria == -1 || produtoUpdateRequest.preco_produto <= 0 || produtoUpdateRequest.desc_produto.isEmpty()) {
            Log.e("CadastroProduto", "Campos inválidos")
            return
        }

        val call = RetrofitClient.instance.atualizarProduto(produtoUpdateRequest)
        call.enqueue(object : Callback<ProdutoUpdateResponse> {
            override fun onResponse(
                call: Call<ProdutoUpdateResponse>,
                response: Response<ProdutoUpdateResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val produtoEditarResponses = response.body()
                    if (produtoEditarResponses != null && response.isSuccessful) {
                        Log.d("ProdutoEditar", "Produto atualizado com sucesso!")
                        val intent = Intent(this@ProdutoEditarActivity, ProdutoActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("API Error", "Response not successful. Code: ${response.code()}")
                    }
                } else {
                    Log.e("cadastroProduto", "Erro HTTP: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ProdutoUpdateResponse>, t: Throwable) {
                Log.e("CadastroProduto", "Falha na requisição: ${t.message}")
                runOnUiThread {
                    Toast.makeText(
                        this@ProdutoEditarActivity,
                        "Erro na conexão. Tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
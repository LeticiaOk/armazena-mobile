package com.example.apparmazena

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class EditarProdutoActivity : AppCompatActivity() {

    private lateinit var nomeEditText: EditText
    private lateinit var descricaoEditText: EditText
    private lateinit var precoEditText: EditText
    private lateinit var imagemEditText: EditText
    private lateinit var salvarButton: Button

    private var produtoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_produto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        nomeEditText = findViewById(R.id.nomeEditText)
        descricaoEditText = findViewById(R.id.descricaoEditText)
        precoEditText = findViewById(R.id.precoEditText)
        imagemEditText = findViewById(R.id.imagemEditText)
        salvarButton = findViewById(R.id.salvarButton)

        // Resgatar os dados passados pela Intent
        produtoId = intent.getIntExtra("PRODUTO_ID", 0)
        nomeEditText.setText(intent.getStringExtra("PRODUTO_NOME"))
        descricaoEditText.setText(intent.getStringExtra("PRODUTO_DESC"))
        precoEditText.setText(intent.getStringExtra("PRODUTO_PRECO"))
        imagemEditText.setText(intent.getStringExtra("PRODUTO_IMAGEM_URL"))

        // Configuração do Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.3/") // Substitua pelo seu endereÃƒÂ§o base
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        salvarButton.setOnClickListener {
            // Atualizar produto via API
            val produtoAtualizado = Produto(
                produtoId,
                nomeEditText.text.toString(),
                descricaoEditText.text.toString(),
                precoEditText.text.toString(),
                imagemEditText.text.toString()
            )

            apiService.editarProduto(
                produtoAtualizado.PRODUTO_ID,
                produtoAtualizado.PRODUTO_NOME,
                produtoAtualizado.PRODUTO_DESC,
                produtoAtualizado.PRODUTO_PRECO,
                produtoAtualizado.PRODUTO_IMAGEM_URL
            ).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditarProdutoActivity, "Produto atualizado com sucesso!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@EditarProdutoActivity, ProdutosActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@EditarProdutoActivity, "Erro na atualização", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@EditarProdutoActivity, "Erro ao atualizar o produto", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
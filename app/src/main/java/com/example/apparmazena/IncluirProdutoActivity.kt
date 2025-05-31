package com.example.apparmazena

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IncluirProdutoActivity : AppCompatActivity() {

    private lateinit var nomeEditText: EditText
    private lateinit var descricaoEditText: EditText
    private lateinit var precoEditText: EditText
    private lateinit var imagemEditText: EditText
    private lateinit var salvarButton: Button
    //+
    private lateinit var spinnerCategoria: Spinner
    private var listaCategorias: List<Categoria> = listOf()
    //-

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_incluir_produto)
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
        //+
        spinnerCategoria = findViewById(R.id.spinnerCategoria)
        //-

        // Configuração do Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.12.153/") // Substitua pelo seu endereÃƒÂ§o base
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        //+
        apiService.getCategorias().enqueue(object : Callback<List<Categoria>> {
            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
                if (response.isSuccessful) {
                    listaCategorias = response.body() ?: listOf()
                    val nomes = listaCategorias.map { it.CATEGORIA_NOME }
                    val adapter = ArrayAdapter(this@IncluirProdutoActivity, android.R.layout.simple_spinner_item, nomes)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCategoria.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Categoria>>, t: Throwable) {
                Toast.makeText(this@IncluirProdutoActivity, "Erro ao carregar categorias", Toast.LENGTH_SHORT).show()
            }
        }
        )
        //-
        salvarButton.setOnClickListener {
            //+
            val categoriaSelecionada = listaCategorias[spinnerCategoria.selectedItemPosition].CATEGORIA_ID
            //-

            val novoProduto = Produto(
                0,  // O ID serÃƒÂ¡ gerado automaticamente no banco de dados
                nomeEditText.text.toString(),
                descricaoEditText.text.toString(),
                precoEditText.text.toString(),
                imagemEditText.text.toString(),
                //+
                categoriaSelecionada
                //-
            )

            // Fazer a requisição para incluir o produto
            apiService.incluirProduto(
                nomeEditText.text.toString(),
                descricaoEditText.text.toString(),
                precoEditText.text.toString(),
                imagemEditText.text.toString(),
                //+
                categoriaSelecionada
                //-

            ).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@IncluirProdutoActivity, "Produto incluído com sucesso!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@IncluirProdutoActivity, ProdutosActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@IncluirProdutoActivity, "Erro na inclusão", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@IncluirProdutoActivity, "Erro ao incluir o produto", Toast.LENGTH_LONG).show()
                }
            })
        }

    }
}
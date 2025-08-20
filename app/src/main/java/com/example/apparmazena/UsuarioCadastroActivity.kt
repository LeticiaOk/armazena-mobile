package com.example.apparmazena

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
import com.example.apparmazena.Usuario.UsuarioCadastroRequest
import com.example.apparmazena.Usuario.UsuarioCadastroResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class UsuarioCadastroActivity : AppCompatActivity() {
    private lateinit var nomeUsuarioEditText: EditText
    private lateinit var emailUsuarioEditText: EditText
    private lateinit var senhaUsuarioEditText: EditText
    private lateinit var empresaUsuarioEditText: EditText
    private lateinit var usuarioTipoSpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_usuario_cadastro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        usuarioTipoSpinner = findViewById(R.id.usuarioTipoSpinner)
        nomeUsuarioEditText = findViewById(R.id.nomeUsuarioEditText)
        emailUsuarioEditText = findViewById(R.id.emailUsuarioEditText)
        senhaUsuarioEditText = findViewById(R.id.senhaUsuarioEditText)
        empresaUsuarioEditText = findViewById(R.id.empresaUsuarioEditText)
        usuarioTipoSpinner = findViewById(R.id.usuarioTipoSpinner)
        val opcoes = listOf("Tipo de usuario", "Administrador", "Cliente")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcoes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        usuarioTipoSpinner.adapter = adapter
        val cadastroUsuarioButton: Button = findViewById(R.id.cadastroUsuarioButton)
        cadastroUsuarioButton.setOnClickListener {
            cadastrarUsuario()
        }
    }

    private fun cadastrarUsuario() {
        val nomeUsuario = nomeUsuarioEditText.text.toString().trim()
        val emailUsuario = emailUsuarioEditText.text.toString().trim()
        val senhaUsuario = senhaUsuarioEditText.text.toString().trim()
        val empresaUsuario = empresaUsuarioEditText.text.toString().trim()
        var usuarioTipo = usuarioTipoSpinner.selectedItem.toString().trim()

        if (nomeUsuario.isEmpty() || emailUsuario.isEmpty() || senhaUsuario.isEmpty() || empresaUsuario.isEmpty() || usuarioTipo.isEmpty()) {
            Log.e("CadastroProduto", "Campos inválidos")
            return
        }

        if(usuarioTipo == "Administrador") {
            usuarioTipo = "1"
        } else if(usuarioTipo == "Cliente") {
            usuarioTipo = "2"
        }

        val usuarioCadastroRequest = UsuarioCadastroRequest(
            nome_usuario = nomeUsuario,
            email_usuario = emailUsuario,
            senha_usuario = senhaUsuario,
            empresa_usuario = empresaUsuario,
            usuario_tipo_id = usuarioTipo.toInt()
        )

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
            .baseUrl("http://0.0.0.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.cadastrarUsuario(usuarioCadastroRequest)

        call.enqueue(object : Callback<UsuarioCadastroResponse> {
            override fun onResponse(
                call: Call<UsuarioCadastroResponse>,
                response: Response<UsuarioCadastroResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val usuarioCadastroResponses = response.body()
                    if (usuarioCadastroResponses != null && response.isSuccessful) {
                        Log.d("CadastroUsuario", "Usuario cadastrado com sucesso!")
                        val intent = Intent(this@UsuarioCadastroActivity, ProdutosActivity::class.java)
                        startActivity(intent)
                        finish()
                        limparCampos()
                    } else {
                        Log.e("API Error", "Response not successful. Code: ${response.code()}")
                    }
                } else {
                    Log.e("CadastroUsuario", "Erro HTTP: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UsuarioCadastroResponse>, t: Throwable) {
                Log.e("CadastroUsuario", "Falha na requisição: ${t.message}")
                runOnUiThread {
                    Toast.makeText(this@UsuarioCadastroActivity, "Erro na conexão. Tente novamente.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun limparCampos() {
        nomeUsuarioEditText.text.clear()
        emailUsuarioEditText.text.clear()
        senhaUsuarioEditText.text.clear()
        empresaUsuarioEditText.text.clear()
    }
}

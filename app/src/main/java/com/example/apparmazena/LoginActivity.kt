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
import retrofit2.http.GET
import retrofit2.http.Query

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // SHARED PREFERENCES
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("USUARIO_ID", -1)

        if (usuarioId != -1) {
            val intent = Intent(this, ProdutosActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            blockLogin()
        }
    }

    private fun blockLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        // Momento do "build" da retrofit, passando a URL base.
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.login(email, password)
        call.enqueue(object : Callback<List<LoginResponse>> {
            override fun onResponse(
                call: Call<List<LoginResponse>>,
                response: Response<List<LoginResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponses = response.body()!!
                    if (loginResponses.isNotEmpty()) {
                        val user = loginResponses[0] // Pegando o primeiro usuário retornado

                        // 🔹 Salvando os dados no SharedPreferences
                        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        sharedPreferences.edit().apply {
                            putInt("USUARIO_ID", user.USUARIO_ID)
                            putString("USUARIO_NOME", user.USUARIO_NOME)
                            putString("USUARIO_EMAIL", user.USUARIO_EMAIL)
                            putString("USUARIO_EMPRESA", user.USUARIO_EMPRESA)
                            apply()
                        }

                        val intent = Intent(this@LoginActivity, ProdutosActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Usuário ou senha inválidos",
                            Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Erro no login",
                        Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<List<LoginResponse>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Erro: ${t.message}",
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    interface ApiService {
        @GET("/armazena_api/login.php")
        fun login(
            // Passando argumentos para url
            @Query("usuario") usuario: String,
            @Query("senha") senha: String
        ): Call<List<LoginResponse>> // Recebe no formato lista de LoginResponse
    }

    data class LoginResponse(
        val USUARIO_ID: Int,
        val USUARIO_NOME: String,
        val USUARIO_EMAIL: String,
        val USUARIO_EMPRESA: String
    )
}
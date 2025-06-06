package com.example.apparmazena

import android.content.Intent
import android.os.Bundle
import android.widget.Button

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // SHARED PREFERENCES
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("USUARIO_ID", -1)

        if (usuarioId != -1) {
            val intent = Intent(this, ProdutosActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val cadastroButton = findViewById<Button>(R.id.cadastroChooseButton)
        val loginButton = findViewById<Button>(R.id.loginChooseButton)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loginButton.setOnClickListener {
            redirectLoginUsuario()
        }
        cadastroButton.setOnClickListener {
            redirectCadastroUsuario()
        }
    }

    fun redirectLoginUsuario() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    fun redirectCadastroUsuario() {
        val intent = Intent(this, UsuarioCadastroActivity::class.java)
        startActivity(intent)
    }
}
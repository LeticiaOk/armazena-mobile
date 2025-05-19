package com.example.armazena.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.armazena.R
import com.example.armazena.activities.login.LoginActivity
import com.example.armazena.activities.usuario.UsuarioCadastroActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
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
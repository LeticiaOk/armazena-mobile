package com.example.apparmazena

import com.google.gson.annotations.SerializedName

class Usuario() {
    data class Usuario (
        @SerializedName("USUARIO_ID") val idUsuario: Int,
        @SerializedName("USUARIO_NOME") val nomeUsuario: String,
        @SerializedName("USUARIO_EMAIL") val emailUsuario: String,
        @SerializedName("USUARIO_SENHA") val senhaUsuario: String,
        @SerializedName("USUARIO_EMPRESA") val empresaUsuario: String,
        @SerializedName("USUARIO_TIPO_ID") val usuarioTipoId: Int
    )

    data class UsuarioCadastroRequest(
        val nome_usuario: String,
        val email_usuario: String,
        val senha_usuario: String,
        val empresa_usuario: String,
        val usuario_tipo_id: Int
    )

    data class UsuarioCadastroResponse(
        val sucesso: Boolean,
        val mensagem: String?,
        val idUsuario: Int?
    )
}
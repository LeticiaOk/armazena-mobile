package com.example.armazena.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.armazena.activities.produto.ProdutoEditarActivity
import com.example.armazena.R
import com.example.armazena.entities.Usuario.Usuario
import com.example.armazena.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsuarioAdapter(
    private val usuarios: MutableList<Usuario>,
    private val context: Context
) : RecyclerView.Adapter<UsuarioAdapter.ViewHolder>() {

    // ViewHolder para os itens da lista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.nomeUsuarioEditText)
        val email: TextView = itemView.findViewById(R.id.emailUsuarioEditText)
        val senha: TextView = itemView.findViewById(R.id.senhaUsuarioEditText)
        val empresa: TextView = itemView.findViewById(R.id.empresaUsuarioEditText)
        val editarButton: Button = itemView.findViewById(R.id.idProdutoEditarButton)
        val deletarButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    // Infla o layout do item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_usuario_cadastro, parent, false)
        return ViewHolder(view)
    }

    // Retorna o número de itens na lista
    override fun getItemCount(): Int {
        return usuarios.size
    }

    // Vincula os dados ao ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.nome.text = usuario.nomeUsuario
        holder.email.text = usuario.emailUsuario
        holder.senha.text = usuario.senhaUsuario
        holder.empresa.text = usuario.empresaUsuario
        holder.editarButton.setOnClickListener {
            val intent = Intent(context, ProdutoEditarActivity::class.java)
            intent.putExtra("USUARIO_ID", usuario.idUsuario)
            intent.putExtra("USUARIO_NOME", usuario.nomeUsuario)
            intent.putExtra("USUARIO_EMAIL", usuario.emailUsuario)
            intent.putExtra("USUARIO_SENHA", usuario.senhaUsuario)
            intent.putExtra("USUARIO_EMPRESA", usuario.empresaUsuario)
            context.startActivity(intent)
        }
        holder.deletarButton.setOnClickListener {
            deleteProduto(position)
        }
    }

    private fun deleteProduto(position: Int) {
        val call = RetrofitClient.instance.deletarProduto(usuarios[position].idUsuario)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                try {
                    if (response.isSuccessful) {
                        usuarios.removeAt(position)
                        notifyItemRemoved(position)
                        Toast.makeText(context, "Produto excluído: ${response.code()}", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("API Error", "Erro ao excluir produto: ${response.code()} - ${response.errorBody()?.string()}")
                        Toast.makeText(context, "Erro ao excluir produto: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("API Error", "Erro ao processar resposta", e)
                    Toast.makeText(context, "Erro ao processar resposta.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API Failure", "Erro na requisição: ${t.message}")
                Toast.makeText(context, "Erro na requisição: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
package com.example.armazena

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.armazena.entities.Produto
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Path

class ProdutoAdapter(
    private val produtos: List<Produto>,
    private val context: Context) : RecyclerView.Adapter<ProdutoAdapter.ViewHolder>() {

    // ViewHolder para os itens da lista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.nomeProduto)
        val descricao: TextView = itemView.findViewById(R.id.descricaoProduto)
        val preco: TextView = itemView.findViewById(R.id.valorProduto)
        val editarButton: Button = itemView.findViewById(R.id.idProdutoEditarButton)
    }
    // Infla o layout do item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_produto, parent, false)
        return ViewHolder(view)
    }
    // Retorna o número de itens na lista
    override fun getItemCount(): Int {
        return produtos.size
    }

    // Vincula os dados ao ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]
        holder.nome.text = produto.nomeProduto
        holder.descricao.text = produto.descricaoProduto
        holder.preco.text = "R$ ${produto.precoProduto.toString()}"
        holder.editarButton.setOnClickListener {
            val intent = Intent(context, ProdutoEditarActivity::class.java)
            intent.putExtra("PRODUTO_ID", produto.idProduto)
            intent.putExtra("PRODUTO_NOME", produto.nomeProduto)
            intent.putExtra("CATEGORIA_ID", produto.idCategoria.toString())
            intent.putExtra("PRODUTO_DESC", produto.descricaoProduto)
            intent.putExtra("PRODUTO_PRECO", produto.precoProduto)
            context.startActivity(intent)
        }
    }
}
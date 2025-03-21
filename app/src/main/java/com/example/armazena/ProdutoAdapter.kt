package com.example.armazena

import com.example.armazena.entities.Produto
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdutoAdapter(
    private val produtos: List<Produto>) : RecyclerView.Adapter<ProdutoAdapter.ViewHolder>() {

    // ViewHolder para os itens da lista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.nomeProduto)
        val descricao: TextView = itemView.findViewById(R.id.descricaoProduto)
        val preco: TextView = itemView.findViewById(R.id.valorProduto)
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
    }
}
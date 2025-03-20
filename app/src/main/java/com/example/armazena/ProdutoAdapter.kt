package com.example.armazena

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdutoAdapter(private val dataSet: List<Produto>) :
    RecyclerView.Adapter<ProdutoAdapter.ViewHolder>() {    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.nomeProduto)
        val descricao: TextView = view.findViewById(R.id.descricaoProduto)
        val valor: TextView = view.findViewById(R.id.valorProduto)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.activity_produto_item, viewGroup, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val produto = dataSet[position]
        viewHolder.nome.text = produto.PRODUTO_NOME
        viewHolder.descricao.text = produto.PRODUTO_DESC
        viewHolder.valor.text = "R$ %.2f".format(produto.PRODUTO_PRECO)

    }

    override fun getItemCount() : Int
    {
        return dataSet.size
    }
}

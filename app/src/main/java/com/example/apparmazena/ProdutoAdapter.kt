package com.example.apparmazena

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdutoAdapter(private val dataSet: List<Produto>, private val apiService: ApiService) :
    RecyclerView.Adapter<ProdutoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.nomeProduto)
        //val descricao: TextView = view.findViewById(R.id.descricaoProduto)
        val preco: TextView = view.findViewById(R.id.precoProduto)
        val imagem: ImageView = view.findViewById(R.id.imagemProduto)
        val editarButton: Button = view.findViewById(R.id.editarButton)
        val deletarButton: Button = view.findViewById(R.id.deletarButton)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.detalhe_produto, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val produto = dataSet[position]
        viewHolder.nome.text = produto.PRODUTO_NOME
        //viewHolder.descricao.text = produto.PRODUTO_DESC
        viewHolder.preco.text = "R$ ${produto.PRODUTO_PRECO}"
        Picasso.get().load(produto.PRODUTO_IMAGEM_URL).into(viewHolder.imagem)

        // Passar os dados do produto para a Activity de edição
        viewHolder.editarButton.setOnClickListener {
            val intent = Intent(it.context, EditarProdutoActivity::class.java)
            intent.putExtra("PRODUTO_ID", produto.PRODUTO_ID)
            intent.putExtra("PRODUTO_NOME", produto.PRODUTO_NOME)
            intent.putExtra("PRODUTO_DESC", produto.PRODUTO_DESC)
            intent.putExtra("PRODUTO_PRECO", produto.PRODUTO_PRECO)
            intent.putExtra("PRODUTO_IMAGEM_URL", produto.PRODUTO_IMAGEM_URL)
            //+
            intent.putExtra("CATEGORIA_ID", produto.CATEGORIA_ID)
            //-
            it.context.startActivity(intent)
        }

        // Deletar produto ao clicar no botÃƒÂ£o
        viewHolder.deletarButton.setOnClickListener {
            apiService.deletarProduto(produto.PRODUTO_ID).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Toast.makeText(it.context, "Produto deletado com sucesso!", Toast.LENGTH_LONG).show()
                    val context = it.context
                    val intent = Intent(context, ProdutosActivity::class.java)
                    context.startActivity(intent)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(it.context, "Erro ao deletar o produto", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun getItemCount() = dataSet.size
}

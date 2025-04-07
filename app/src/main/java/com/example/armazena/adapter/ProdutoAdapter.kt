package com.example.armazena.adapter

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
import com.example.armazena.entities.Produto
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class ProdutoAdapter(
    private val produtos: MutableList<Produto>,
    private val context: Context
) : RecyclerView.Adapter<ProdutoAdapter.ViewHolder>() {

    // ViewHolder para os itens da lista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.nomeProduto)
        val descricao: TextView = itemView.findViewById(R.id.descricaoProduto)
        val preco: TextView = itemView.findViewById(R.id.valorProduto)
        val editarButton: Button = itemView.findViewById(R.id.idProdutoEditarButton)
        val deletarButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    object RetrofitClient {
        private const val BASE_URL = "http://192.168.0.43/"

        val instance: ProdutoDeleteApiService by lazy {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val gson: Gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()


            retrofit.create(ProdutoDeleteApiService::class.java)
        }
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
        holder.deletarButton.setOnClickListener {
            deleteProduto(position)
        }
    }

    interface ProdutoDeleteApiService {
        @FormUrlEncoded
        @POST("/armazena_api/produto_delete.php")
        fun deletarProduto(
            @Field("PRODUTO_ID") id: Int
        ): Call<Void>
    }

    private fun deleteProduto(position: Int) {
        val call = RetrofitClient.instance.deletarProduto(produtos[position].idProduto)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                try {
                    if (response.isSuccessful) {
                        produtos.removeAt(position)
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
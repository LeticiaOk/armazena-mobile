package com.example.armazena.retrofit;


ApiClient.apiService.login("seu_email", "sua_senha")
    .enqueue(object : Callback<LoginResponse> {
    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
        if (response.isSuccessful) {
            val loginResponse = response.body()
            // Tratar resposta bem-sucedida
        }
    }

    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
        // Tratar erro
    }
})
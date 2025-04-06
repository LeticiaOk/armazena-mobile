package com.example.armazena.retrofit;


object RetrofitClient {
private const val BASE_URL = "http://192.168.1.4/armazena_api/"

private val gson = GsonBuilder()
        .setLenient()
        .create()

private val logging = HttpLoggingInterceptor { message ->
        Log.d("OkHttp", message)
    }.apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

val retrofit: Retrofit by lazy {
    Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
}
}
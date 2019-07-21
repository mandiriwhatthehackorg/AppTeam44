package id.co.bankmandiri.common.api

import id.co.bankmandiri.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {
    private val baseUrl = "http://api.themoviedb.org/3/"
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyAdderInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        })
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient.build())
    val retrofit = retrofitBuilder.build()

    inline fun <reified T> createService(): T {
        return retrofit.create(T::class.java)
    }
}
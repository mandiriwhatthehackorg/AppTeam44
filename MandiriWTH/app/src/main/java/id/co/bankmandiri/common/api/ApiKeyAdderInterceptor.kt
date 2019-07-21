package id.co.bankmandiri.common.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ApiKeyAdderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request()
            .url
            .newBuilder()
                // TODO add auth mechanism
            // .addQueryParameter("api_key", BuildConfig.API_THE_MOVIE_DB)
            .build()
        val request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }
}
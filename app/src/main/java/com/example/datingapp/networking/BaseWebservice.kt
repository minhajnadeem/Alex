package com.example.datingapp.networking

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseWebservice {

    private const val BASE_URL = "http://18.222.150.210:8007/"
    private val clientBuilder = OkHttpClient.Builder()
    private val okHttpClient = buildClient()
    private val retrofitBuilder = Retrofit.Builder()
    private val retrofit = buildRetrofit()

    /**
     * build and return okHttpClient
     */
    private fun buildClient(): OkHttpClient {
        clientBuilder.addInterceptor(getLogginInterceptor())
        return clientBuilder.build()
    }

    private fun getLogginInterceptor(): Interceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    /**
     * build and return retrofit instance
     */
    private fun buildRetrofit(): Retrofit {
        return retrofitBuilder
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    /**
     * return the implementation of webservice interface
     */
    fun getApiEndpointImpl(): AlexWebservice {
        return retrofit.create(AlexWebservice::class.java)
    }

    fun <T> executeApi(call: Call<T>, apiListener: ApiListener<T>) {
        val callback = object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                if (call.isCanceled)
                    apiListener.onCancel()
                else
                    apiListener.onFailure(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (call.isCanceled) {
                    apiListener.onCancel()
                    return
                }
                if (response.isSuccessful) {
                    apiListener.onSuccess(response.body())

                } else {
                    val reason = DefaultErrorHandler(response).handleError()
                    apiListener.onFailure(reason)
                }
            }
        }

        call.enqueue(callback)
    }
}
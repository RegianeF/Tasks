package com.example.tasks.service.repository.remote

import com.example.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {

        private lateinit var retrofit: Retrofit
        private val baseUrl = "http://devmasterteam.com/CursoAndroidAPI/"

        private var personKey = ""
        private var tokenKey = ""

        private fun getRetrofitInstance(): Retrofit {
            val httpClient = OkHttpClient.Builder()

            //addInterceptor intercepta a requisição(antes de fazer a chamada, assim que criado) e executa
            httpClient.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val resquest = chain
                        .request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, tokenKey)
                        .build()
                    return chain.proceed(resquest)
                }
                //antes da chamada o addInterceptor adiciona person e token/headers/ e retorna o processo para seguir em frente
            })

            if (!::retrofit.isInitialized) {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
            }
            return retrofit
        }

        //preenchimento do header
        fun addHeader(token: String, personKey: String) {
            this.personKey = personKey
            this.tokenKey = token
        }

        fun <S> createService(serviceClass: Class<S>): S {
            return getRetrofitInstance().create(serviceClass)
        }

    }
}
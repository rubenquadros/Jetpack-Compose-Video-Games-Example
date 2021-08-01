package com.ruben.epicworld.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ruben.epicworld.remote.rest.RestApi
import com.ruben.epicworld.remote.rest.RestApiImpl
import com.ruben.epicworld.remote.retrofit.RetrofitApi
import com.ruben.epicworld.remote.retrofit.RetrofitInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

/**
 * Created by Ruben Quadros on 01/08/21
 **/
abstract class BaseTest {

    private val server: MockWebServer = MockWebServer()
    protected lateinit var restApi: RestApi

    @Before
    fun setup() {
        this.configureServer()
        this.configureRestApi()
    }

    private fun configureServer() {
        server.start(8080)
    }

    private fun configureRestApi() {
        restApi = RestApiImpl(configureRetrofitApi())
    }

    fun <T>getExpectedResponse(path: String, response: Class<T>) : T {
        val gson = Gson()
        return gson.fromJson(getJson(path), response)
    }

    fun getResponse(path: String, responseCode: Int) {
        server.enqueue(MockResponse().setBody(getJson(path)).setResponseCode(responseCode))
    }

    fun getTimeout() {
        val mockResponse = MockResponse()
        mockResponse.socketPolicy = SocketPolicy.NO_RESPONSE
        server.enqueue(mockResponse)
    }

    private fun getJson(path: String): String {
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(path))
        val content = reader.readText()
        reader.close()
        return content
    }

    private fun configureRetrofitApi(): RetrofitApi =
        Retrofit.Builder()
            .baseUrl(RemoteTestConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .client(getOkHttpClient())
            .build()
            .create(RetrofitApi::class.java)

    private fun getGson() = GsonBuilder().create()

    private fun getOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.readTimeout(RemoteTestConstants.TIMEOUT, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(RemoteTestConstants.TIMEOUT, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(RemoteTestConstants.TIMEOUT, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(RetrofitInterceptor())
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient.addNetworkInterceptor(loggingInterceptor)
        return okHttpClient.build()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}
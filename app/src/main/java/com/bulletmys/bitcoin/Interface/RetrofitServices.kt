package com.bulletmys.bitcoin.Interface

import com.bulletmys.bitcoin.models.Response
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface RetrofitServices {

    @GET("histoday")
    fun search(
        @Query("fsym") queryFrom: String,
        @Query("tsym") queryTo: String,
        @Query("limit") limit: Int
    ): Observable<Response>

    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        fun create(): RetrofitServices {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl("https://min-api.cryptocompare.com/data/v2/")
                .build()

            return retrofit.create(RetrofitServices::class.java);
        }
    }
}
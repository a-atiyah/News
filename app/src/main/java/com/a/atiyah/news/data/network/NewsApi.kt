package com.a.atiyah.news.data.network

import com.a.atiyah.news.data.model.NewsList
import com.a.atiyah.news.utils.Constant.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {
    @Headers("x-rapidapi-key: $API_KEY")
    @GET("search")
    fun getData(@Query("q") query: String, @Query("lang") lang: String? = "en") : Call<NewsList>
}
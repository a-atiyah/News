package com.a.atiyah.news.data.network

import android.content.Context
import androidx.lifecycle.LiveData
import com.a.atiyah.news.data.database.NewsDao
import com.a.atiyah.news.data.model.News
import com.a.atiyah.news.data.model.NewsList
import com.a.atiyah.news.utils.Constant.LANG_ENGLISH
import com.a.atiyah.news.utils.HelperClass
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class RetroRepo
@Inject constructor(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
){

    fun getAllNews() : LiveData<List<News>>{
        return newsDao.getAllNonBookmarkNews()
    }

    fun getBookmarkedNews(): LiveData<List<News>> {
        return newsDao.getAllBookmarkNews()
    }

    fun insertNews(news: News) {
        GlobalScope.launch() {
            if(news.bookmark == null)
                news.bookmark = 0

            newsDao.insertNews(news)
        }
    }

    fun deleteAllNews() {
        GlobalScope.launch {
            newsDao.deleteAllNews()
        }
    }

    // Api Call

    fun callNewsApi(word: String, lang: String? = LANG_ENGLISH, sync: Boolean,callback: HelperClass.Companion.ProgBarCallback){

        val call: Call<NewsList> = newsApi.getData(word, lang)
        call.enqueue(object: Callback<NewsList>{
            override fun onResponse(call: Call<NewsList>, response: Response<NewsList>) {
               // PB
                callback.onResponse(response.message())

                if (!response.isSuccessful) {
                    //TODO: Show a Toast
                    return
                }
                // New Records -> Clear resent search news
                if(!sync)
                    if (response.body()?.articles?.size!! > 0)
                        deleteAllNews()

                response.body()?.articles?.forEach {
                    insertNews(it)
                }
            }

            override fun onFailure(call: Call<NewsList>, t: Throwable) {
                callback.onFailure(t.message.toString())
                t.printStackTrace()
            }
        })

    }
}
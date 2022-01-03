package com.a.atiyah.news.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.a.atiyah.news.data.model.News
import com.a.atiyah.news.data.model.TABLE_NAME_NEWS

@Dao
interface NewsDao {

    @Query("SELECT * FROM $TABLE_NAME_NEWS")
    fun getAllNews(): LiveData<List<News>>

    @Query("SELECT * FROM $TABLE_NAME_NEWS  WHERE bookmark = 0")
    fun getAllNonBookmarkNews(): LiveData<List<News>>

    @Query("SELECT * FROM $TABLE_NAME_NEWS WHERE _id IN (:newsIds)")
    fun getNewsByIds(newsIds: IntArray): LiveData<List<News>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: News)

    @Delete
    fun deleteNews(news: News)

    @Query("DELETE FROM $TABLE_NAME_NEWS WHERE bookmark = 0")
    fun deleteAllNews()

    @Query("SELECT * FROM $TABLE_NAME_NEWS WHERE bookmark = 1")
    fun getAllBookmarkNews(): LiveData<List<News>>
}
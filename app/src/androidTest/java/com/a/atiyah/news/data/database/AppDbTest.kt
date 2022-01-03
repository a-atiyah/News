package com.a.atiyah.news.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.a.atiyah.news.data.model.News
import com.a.atiyah.news.getValueOrAwait
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDbTest: TestCase(){
    private lateinit var appDb: AppDb
    private lateinit var dbDao: NewsDao

    @Before
    public override fun setUp() {
        super.setUp()
        val ctx: Context = ApplicationProvider.getApplicationContext()
         appDb = Room.inMemoryDatabaseBuilder(ctx, AppDb::class.java)
             .allowMainThreadQueries()
            .build()
        dbDao = appDb.newsDao()
    }

    @After
    fun closeAppDb(){
        appDb.close()
    }

    @Test
    fun insertDataAndReadIt(){
        val testNews = News("123", "a.atiyah",
            "www.a.atiyah.com",
            "SA", "AR",
            "https://www.a.atiyah.sa", null,
            "", "a.atiyah",
            "This is a test articale!",
            "Test Title", "News", 1)

        dbDao.insertNews(testNews)

        val news = dbDao.getAllBookmarkNews().getValueOrAwait()

        assertThat(news.contains(testNews)).isTrue()
    }
}
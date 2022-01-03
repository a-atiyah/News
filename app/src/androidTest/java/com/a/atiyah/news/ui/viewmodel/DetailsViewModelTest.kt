package com.a.atiyah.news.ui.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.a.atiyah.news.data.database.AppDb
import com.a.atiyah.news.data.dependencyInjection.AppModule
import com.a.atiyah.news.data.model.News
import com.a.atiyah.news.data.network.RetroRepo
import com.a.atiyah.news.getValueOrAwait
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsViewModelTest: TestCase() {
    private lateinit var mDetailsViewModel: DetailsViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        super.setUp()
        val ctx: Context = ApplicationProvider.getApplicationContext()
        val appModule = AppModule
        val appDb = Room.inMemoryDatabaseBuilder(ctx, AppDb::class.java)
            .allowMainThreadQueries()
            .build()
        val repo = RetroRepo( appModule.retrofitInstance(appModule.retroInstance()) ,appDb.newsDao())
        mDetailsViewModel = DetailsViewModel(repo)
    }

    @Test
    fun testInsertBookmarkNews() {

        val testNews = News("123", "a.atiyah",
            "www.a.atiyah.com",
            "SA", "AR",
            "https://www.a.atiyah.sa", null,
        "", "a.atiyah",
            "This is a test articale!",
            "Test Title", "News", 1)

        mDetailsViewModel.addToRoomDb(testNews)

        val news = mDetailsViewModel.retrieveFromRoomDb().getValueOrAwait()

        // Please Note: the addToRoomDb is the function that responsible for
        // Insert And Delete (Override) items
        // That why you should handle the logic here otherwise the test will be
        // success one time and failed another - Normal
        assertThat(news.contains(testNews)).isTrue()
    }
}
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
class HomeViewModelTest : TestCase(){
    private lateinit var mHomeViewModel: HomeViewModel

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
        mHomeViewModel = HomeViewModel(repo)
    }

    // Test retrieve Bookmark News
    @Test
    fun testBookmarkNews() {
        // Bookmark 1 - main Bookmarked
        val test = News("1", null, null, null, null, null, null, null, null, null, null, null, 1)
        mHomeViewModel.addToRoomDb(test)
        val bookmarkedList = mHomeViewModel.getBookmarked().getValueOrAwait()

        assertThat(bookmarkedList.contains(test)).isTrue()
    }

    // Test retrieve recent search News
    @Test
    fun testRetrieveNews() {
        // Bookmark 1 - main Bookmarked
        val test = News("2", null, null, null, null, null, null, null, null, null, null, null, 0)
        mHomeViewModel.addToRoomDb(test)
        val news = mHomeViewModel.getAllNews().getValueOrAwait()

        assertThat(news.contains(test)).isTrue()
    }
}
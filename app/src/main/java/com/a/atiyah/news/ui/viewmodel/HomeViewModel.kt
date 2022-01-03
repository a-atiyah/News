package com.a.atiyah.news.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.a.atiyah.news.data.model.News
import com.a.atiyah.news.data.network.RetroRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val news: RetroRepo)
    : ViewModel() {

    fun getAllNews(): LiveData<List<News>>{
        return news.getAllNews()
    }

    fun getBookmarked(): LiveData<List<News>>{
        return news.getBookmarkedNews()
    }

    // For Unit test only!!!
    fun addToRoomDb(newsItem: News) {
        news.insertNews(newsItem)
    }
}
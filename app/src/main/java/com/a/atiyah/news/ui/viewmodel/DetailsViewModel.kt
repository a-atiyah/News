package com.a.atiyah.news.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.a.atiyah.news.data.model.News
import com.a.atiyah.news.data.network.RetroRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val news: RetroRepo) : ViewModel() {

    fun addToRoomDb(newsItem: News) {
        news.insertNews(newsItem)
    }

    // For Unit test only!!!
    fun retrieveFromRoomDb(): LiveData<List<News>> {
        return news.getBookmarkedNews()
    }
}
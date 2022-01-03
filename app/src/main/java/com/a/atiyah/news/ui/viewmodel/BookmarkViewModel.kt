package com.a.atiyah.news.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a.atiyah.news.data.model.News
import com.a.atiyah.news.data.network.RetroRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class BookmarkViewModel @Inject constructor(private val news: RetroRepo): ViewModel() {

    fun getBookmarked(): LiveData<List<News>>{
        return news.getBookmarkedNews()
    }
}
package com.a.atiyah.news.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a.atiyah.news.data.model.News
import com.a.atiyah.news.data.network.RetroRepo
import com.a.atiyah.news.utils.Constant.LANG_ARABIC
import com.a.atiyah.news.utils.Constant.LANG_ENGLISH
import com.a.atiyah.news.utils.State
import com.a.atiyah.news.utils.HelperClass
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentNewsViewModel @Inject constructor(private val news: RetroRepo)
    : ViewModel(){

    // Variables
    // State
    var state =  MutableLiveData<State>()

    // retrieve news
    fun getAllNews(): LiveData<List<News>>{
        return news.getAllNews()
    }

    fun apiCall(word:String, sync: Boolean){
        state.value = State.LOADING
        var uc:Int = word.codePointAt(0)

        if (uc in 0x0600..0x06E0)
            news.callNewsApi(word, LANG_ARABIC , sync, object : HelperClass.Companion.ProgBarCallback{
                override fun onResponse(msg: String) {
                    state.value = State.RESPONSE
                }

                override fun onFailure(msg: String) {
                    state.value = State.FAILURE
                }

            })
        else
            news.callNewsApi(word, LANG_ENGLISH , sync, object : HelperClass.Companion.ProgBarCallback{
                override fun onResponse(msg: String) {
                    state.value = State.RESPONSE
                }

                override fun onFailure(msg: String) {
                    state.value = State.FAILURE
                }
            })
    }
}
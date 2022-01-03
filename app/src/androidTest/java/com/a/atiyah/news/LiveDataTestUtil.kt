package com.a.atiyah.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> LiveData<T>.getValueOrAwait(): T {
    var data: T? = null

    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(t: T) {
            data = t
            this@getValueOrAwait.removeObserver(this)
            latch.countDown()
        }
    }

    GlobalScope.launch(Dispatchers.Main) { observeForever(observer) }

    latch.await(2, TimeUnit.SECONDS)

    try {
        if (data == null)
            throw TimeoutException("Time out!")
    } finally {

    }

    return data as T
}
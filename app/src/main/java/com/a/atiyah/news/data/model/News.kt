package com.a.atiyah.news.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

const val TABLE_NAME_NEWS = "news"

@Entity(tableName = TABLE_NAME_NEWS)
data class News(
    @PrimaryKey @NonNull
    val _id:String,
    val author:String?,
    val clean_url:String?,
    val country:String?,
    val language:String?,
    val link:String?,
    val media:String?,
    val published_date:String?,
    val rights:String?,
    val summary:String?,
    val title:String?,
    val topic:String?,
    var bookmark: Int? = 0
): Serializable
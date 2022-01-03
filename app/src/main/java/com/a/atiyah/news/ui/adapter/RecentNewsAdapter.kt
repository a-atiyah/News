package com.a.atiyah.news.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a.atiyah.news.R
import com.a.atiyah.news.data.model.News
import com.squareup.picasso.Picasso
import java.lang.Exception

class RecentNewsAdapter(private val listener: BtnClickListener)
    : RecyclerView.Adapter<RecentNewsAdapter.NewsViewHolder>() {

    private var newsList = emptyList<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recent_news, parent, false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position], position, listener)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun setNewsList(news: List<News>) {
        this.newsList = news
    }

    fun getNewsList() : List<News>{
        return newsList
    }

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var mIVNews:ImageView = view.findViewById(R.id.iv_news)
        var mTVNewsTitle:TextView = view.findViewById(R.id.tv_news_title)
        var mTVAuthor:TextView = view.findViewById(R.id.tv_author)
        var mTVDateTime:TextView = view.findViewById(R.id.tv_date_time)
        var mTVTagTopic:TextView = view.findViewById(R.id.tv_tag_topic)
        var mTVTagCountry:TextView = view.findViewById(R.id.tv_tag_country)
        var mTVTagLanguage:TextView = view.findViewById(R.id.tv_tag_language)

        companion object {
            var mClickListener: BtnClickListener? = null
        }

        fun bind(news: News, position: Int, listener: BtnClickListener) {
            mClickListener = listener

            // Set Data
            mTVNewsTitle.text = news.title
            mTVAuthor.text = news.author
            mTVDateTime.text = news.published_date
            mTVTagTopic.text = news.topic
            mTVTagCountry.text = news.country
            mTVTagLanguage.text = news.language
            // Set Image
            try {
                Picasso.get().load(news.media).placeholder(R.mipmap.ic_launcher).into(mIVNews);
            }catch (e: Exception){
                Picasso.get().load(R.mipmap.ic_launcher).into(mIVNews);
            }

            // Item Click
            itemView.setOnClickListener {
                if (mClickListener != null)
                    mClickListener?.onBtnClick(position)
            }
        }
    }

    interface BtnClickListener {
        fun onBtnClick(position: Int)
    }
}
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

class BookmarkHomeAdapter(private val listener: BtnClickListener) : RecyclerView.Adapter<BookmarkHomeAdapter.BookmarkViewHolder>(){

    private var newsList = emptyList<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_bookmark, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(newsList[position], position, listener)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun setNewsList(news: List<News>) {
        this.newsList = news
    }

    fun getNewsList(): List<News>{
        return newsList
    }

    class BookmarkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mIVNews: ImageView = view.findViewById(R.id.iv_home_bookmark)
        var mTVNewsTitle: TextView = view.findViewById(R.id.tv_home_bookmark)

        companion object {
            var mClickListener: BtnClickListener? = null
        }

        fun bind(news: News, position: Int, listener: BtnClickListener) {
            mClickListener = listener

            mTVNewsTitle.text = news.title
            // Set Image
            try {
                Picasso.get().load(news.media).placeholder(R.mipmap.ic_launcher).into(mIVNews);
            }catch (e: Exception){
                Picasso.get().load(R.mipmap.ic_launcher).into(mIVNews);
            }

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
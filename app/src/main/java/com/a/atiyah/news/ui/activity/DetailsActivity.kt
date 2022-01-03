package com.a.atiyah.news.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.a.atiyah.news.R
import com.a.atiyah.news.data.model.News
import com.a.atiyah.news.databinding.ActivityDetailsBinding
import com.a.atiyah.news.ui.viewmodel.DetailsViewModel
import com.a.atiyah.news.utils.Constant.EXTRA_KEY_NEWS
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var mNews: News
    private lateinit var mViewMode: DetailsViewModel

    // Declare UI Components
    private var mTVTitle: TextView? = null
    private var mTVTagTopic: TextView? = null
    private var mTVTagCountry: TextView? = null
    private var mTVTagLanguage: TextView? = null
    private var mTVAuth: TextView? = null
    private var mTVDetails: TextView? = null
    private var mTVGotoSource: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mNews = intent.getSerializableExtra(EXTRA_KEY_NEWS) as News

        if (mNews.title?.length!! > 10)
            binding.toolbarLayout.title = mNews.title?.substring(0..10) + "..."
        else
            binding.toolbarLayout.title = mNews.title

        displayFabIcon()

        binding.fab.setOnClickListener { view ->
            //Add to book mark or delete
            changeBookmarkState(view)
        }
        initViewModel()
        initUIComponents()
        setData()
        eventListener()
    }

    private fun changeBookmarkState(view: View) {
        if (mNews.bookmark == 0 || mNews.bookmark == null) {
            mNews.bookmark = 1
            mViewMode.addToRoomDb(mNews)
            binding.fab.setImageResource(R.drawable.ic_baseline_bookmark_added_24)
            Snackbar.make(view, getString(R.string.add_to_bookmark), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        } else {
            mNews.bookmark = 0
            mViewMode.addToRoomDb(mNews)
            binding.fab.setImageResource(R.drawable.ic_baseline_bookmark_add_24)
            Snackbar.make(
                view,getString(R.string.removed_from_bookmark),Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun displayFabIcon() {
        if (mNews.bookmark == 0 || mNews.bookmark == null)
            binding.fab.setImageResource(R.drawable.ic_baseline_bookmark_add_24)
        else
            binding.fab.setImageResource(R.drawable.ic_baseline_bookmark_added_24)

    }

    private fun initUIComponents() {
        mTVTitle = findViewById(R.id.tv_details_activity_news_title)
        mTVTagTopic = findViewById(R.id.tv_tag_topic)
        mTVTagCountry = findViewById(R.id.tv_tag_country)
        mTVTagLanguage = findViewById(R.id.tv_tag_language)
        mTVAuth = findViewById(R.id.tv_details_auth)
        mTVDetails = findViewById(R.id.tv_details)
        mTVGotoSource = findViewById(R.id.tv_goto_source)
    }

    private fun setData() {
        // Set Image
        try {
            Picasso.get().load(mNews.media).placeholder(R.color.link).into(binding.ivHeader)
        } catch (e:Exception) {
            e.stackTrace
            Picasso.get().load(R.color.link)
        }

        // Set Fields
        mTVTitle?.text = mNews.title
        mTVAuth?.text = mNews.author
        mTVTagCountry?.text = mNews.country
        mTVTagTopic?.text = mNews.topic
        mTVTagLanguage?.text = mNews.language
        mTVDetails?.text = mNews.summary
    }

    private fun eventListener() {
        mTVGotoSource?.setOnClickListener {
            gotoSource(mNews.link.toString())
        }
    }

    private fun initViewModel() {
        mViewMode = ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    fun gotoSource(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
package com.a.atiyah.news.ui.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.a.atiyah.news.R
import com.a.atiyah.news.ui.activity.DetailsActivity
import com.a.atiyah.news.ui.adapter.BookmarkHomeAdapter
import com.a.atiyah.news.ui.adapter.RecentNewsAdapter
import com.a.atiyah.news.ui.viewmodel.HomeViewModel
import com.a.atiyah.news.utils.Constant.EXTRA_KEY_NEWS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    // UI Elements
    private lateinit var mRVRecentNews: RecyclerView
    private lateinit var mRVBookmark: RecyclerView
    private lateinit var mRNAdapter: RecentNewsAdapter
    private lateinit var mBAdapter: BookmarkHomeAdapter
    private lateinit var mTVAllBookmark: TextView
    private lateinit var mTVEmptyBookmark: TextView
    private lateinit var mTVEmptySearchResult: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIComponents(view)
        initViewModel()
        eventListener(view)
    }

    private fun eventListener(view: View) {
        mTVAllBookmark.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.home_to_bookmark)
        }
    }

    private fun initAdapter() {
        mRNAdapter = RecentNewsAdapter(object : RecentNewsAdapter.BtnClickListener{
            override fun onBtnClick(position: Int) {
                var intent = Intent(requireContext(), DetailsActivity::class.java)
                intent.putExtra(EXTRA_KEY_NEWS, mRNAdapter.getNewsList()[position])
                startActivity(intent)
            }
        })
        mBAdapter = BookmarkHomeAdapter(object : BookmarkHomeAdapter.BtnClickListener{
            override fun onBtnClick(position: Int) {
                var intent = Intent(requireContext(), DetailsActivity::class.java)
                intent.putExtra("news", mBAdapter.getNewsList()[position])
                startActivity(intent)
            }
        })
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        viewModel.getAllNews().observe(requireActivity(), Observer {
            // Recent news adapter
            mRNAdapter.setNewsList(it)
            mRNAdapter.notifyDataSetChanged()
            rvItems()
        })

        viewModel.getBookmarked().observe(requireActivity(), {
            // Bookmark adapter
            mBAdapter.setNewsList(it)
            mBAdapter.notifyDataSetChanged()
            rvItems()
        })
    }

    private fun initUIComponents(view: View) {
        mRVRecentNews = view.findViewById(R.id.rv_recent_news_home)
        mRVBookmark = view.findViewById(R.id.rv_bookmark_home)
        mTVAllBookmark = view.findViewById(R.id.tv_all_bookmark_link)
        mTVEmptyBookmark = view.findViewById(R.id.tv_empty_bookmark)
        mTVEmptySearchResult = view.findViewById(R.id.tv_empty_rn)
        initAdapter()
        initRecyclerView()
        // Check no of items
        rvItems()
    }

    private fun rvItems() {
        // Bookmark
        if(mBAdapter.itemCount > 0) {
            mTVEmptyBookmark.visibility = View.GONE
            mRVBookmark.visibility = View.VISIBLE
            mTVAllBookmark.visibility = View.VISIBLE
        } else {
            mTVEmptyBookmark.visibility = View.VISIBLE
            mRVBookmark.visibility = View.GONE
            mTVAllBookmark.visibility = View.GONE
        }
        //Recent Search
        if(mRNAdapter.itemCount > 0) {
            mTVEmptySearchResult.visibility = View.GONE
            mRVRecentNews.visibility = View.VISIBLE
        } else {
            mTVEmptySearchResult.visibility = View.VISIBLE
            mRVRecentNews.visibility = View.GONE
        }
    }

    private fun initRecyclerView() {
        // /Layout manager prop - VERTICAL
        // Handle From - XML
//
//        // Recent News RV
//        mRVRecentNews.layoutManager = llManager
        mRVRecentNews.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        mRVRecentNews.setHasFixedSize(true)
        mRVRecentNews.adapter = mRNAdapter


        // Bookmark RV
        mRVBookmark.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        //mRVBookmark.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL))
        mRVBookmark.setHasFixedSize(true)
        mRVBookmark.adapter = mBAdapter
    }
}
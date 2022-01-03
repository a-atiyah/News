package com.a.atiyah.news.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a.atiyah.news.R
import com.a.atiyah.news.ui.activity.DetailsActivity
import com.a.atiyah.news.ui.adapter.RecentNewsAdapter
import com.a.atiyah.news.ui.viewmodel.BookmarkViewModel
import com.a.atiyah.news.utils.Constant.EXTRA_KEY_NEWS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    // View Model
    private lateinit var mBookmarkViewModel: BookmarkViewModel

    // UI Components
    private var mRVBookmark: RecyclerView? = null
    private lateinit var mAdapter: RecentNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIComponents(view)
        initViewModel()
    }



    private fun initViewModel() {
        mBookmarkViewModel =
            ViewModelProvider(this).get(BookmarkViewModel::class.java)
        mBookmarkViewModel.getBookmarked().observe(requireActivity(), {
            // Refresh adapter
            mAdapter.setNewsList(it)
            mAdapter.notifyDataSetChanged()
        })
    }

    private fun initUIComponents(view: View) {
        mRVBookmark = view.findViewById(R.id.rv_bookmark_news)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = RecentNewsAdapter(object : RecentNewsAdapter.BtnClickListener {
            override fun onBtnClick(position: Int) {
                var intent = Intent(requireContext(), DetailsActivity::class.java)
                intent.putExtra(EXTRA_KEY_NEWS, mAdapter.getNewsList()[position])
                startActivity(intent)
            }
        })

        // Layout manager Handle By XML


        //val llManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        llManager.reverseLayout = true
//        llManager.stackFromEnd = true
//
//        mRVBookmark?.layoutManager =llManager
        mRVBookmark?.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        mRVBookmark?.setHasFixedSize(true)

        mRVBookmark?.adapter = mAdapter
    }
}
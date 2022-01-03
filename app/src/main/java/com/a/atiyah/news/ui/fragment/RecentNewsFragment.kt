package com.a.atiyah.news.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a.atiyah.news.R
import com.a.atiyah.news.ui.activity.DetailsActivity
import com.a.atiyah.news.ui.adapter.RecentNewsAdapter
import com.a.atiyah.news.ui.viewmodel.RecentNewsViewModel
import com.a.atiyah.news.utils.Constant.EXTRA_KEY_NEWS
import com.a.atiyah.news.utils.Constant.SHORT_TIME
import com.a.atiyah.news.utils.State
import com.a.atiyah.news.utils.HelperClass
import com.a.atiyah.news.utils.HelperClass.Companion.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentNewsFragment : Fragment() {

    //UI Components
    private var mRVRecentNews: RecyclerView? = null
    private var mETSearch: EditText? = null
    private var mIBFilter: ImageButton? = null
    private var mPb: ProgressBar? = null
    private lateinit var mAdapter: RecentNewsAdapter
    private lateinit var viewModel: RecentNewsViewModel

    private var syncType = false

    private lateinit var sharedPreferences: SharedPreferences

    override fun onStart() {
        super.onStart()
        sharedPreferences = HelperClass.getPreferences(requireContext())
        syncType = sharedPreferences.getBoolean("sync_type", false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIComponents(view)
        initViewModel()
        eventListeners()
    }

    private fun eventListeners() {
        // Search Key
        mETSearch?.setOnKeyListener(object : View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    search()
                    // Hide Soft Keyboard
                    hideKeyboard()
                    mETSearch!!.clearFocus()
                    mETSearch!!.isCursorVisible = false

                    return true
                }
                return false
            }
        })

        // Filter elements
        mIBFilter?.setOnClickListener {
            openBottomSheetDialog()
        }
    }

    private fun openBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val view=layoutInflater.inflate(R.layout.bottom_sheet,null)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun search() {
        var search: String = mETSearch?.text.toString().trim()
        if (TextUtils.isEmpty(search)) {
            HelperClass.showToast(
                requireContext(),
                getString(R.string.type_somthing),
                SHORT_TIME
            )
            return
        }
        syncType = sharedPreferences.getBoolean("sync_type", false)

        viewModel.apiCall(search, syncType)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(RecentNewsViewModel::class.java)
        viewModel.getAllNews().observe(requireActivity(), Observer {
            mAdapter.setNewsList(it)
            mAdapter.notifyDataSetChanged()
        })

        // Search state
        viewModel.state.observe(requireActivity(), Observer {
            if (it == State.LOADING) {
                mPb?.visibility = View.VISIBLE
                mRVRecentNews?.visibility = View.GONE
            } else {
                mPb?.visibility = View.GONE
                mRVRecentNews?.visibility = View.VISIBLE
            }
        })
    }

    private fun initUIComponents(view: View) {
        mRVRecentNews = view.findViewById(R.id.rv_recent_news)
        mETSearch = view.findViewById(R.id.et_search)
        mIBFilter = view.findViewById(R.id.ib_filter)
        mPb = view.findViewById(R.id.pb_loading)
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
        // /Layout manager prop
        // Handle From - XML


//        val llManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        //llManager.reverseLayout = true
//        llManager.stackFromEnd = true
//        mRVRecentNews?.layoutManager = llManager
        mRVRecentNews?.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        mRVRecentNews?.setHasFixedSize(true)
        mRVRecentNews?.adapter = mAdapter
    }
}
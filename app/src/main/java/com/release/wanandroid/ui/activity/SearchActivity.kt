package com.release.wanandroid.ui.activity

import android.content.Intent
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseMvpSwipeBackActivity
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.mvp.contract.SearchContract
import com.release.wanandroid.mvp.model.bean.HotSearchBean
import com.release.wanandroid.mvp.model.bean.SearchHistoryBean
import com.release.wanandroid.mvp.presenter.SearchPresenter
import com.release.wanandroid.ui.adapter.SearchHistoryAdapter
import com.release.wanandroid.utils.CommonUtil
import com.release.wanandroid.utils.DisplayManager
import com.release.wanandroid.widget.RecyclerViewItemDecoration
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_search.*

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
@Suppress("DEPRECATION")
class SearchActivity : BaseMvpSwipeBackActivity<SearchContract.View, SearchContract.Presenter>(), SearchContract.View {

    override fun createPresenter(): SearchContract.Presenter = SearchPresenter()

    /**
     * 热搜数据
     */
    private var mHotSearchDatas = mutableListOf<HotSearchBean>()

    /**
     * datas
     */
    private val datas = mutableListOf<SearchHistoryBean>()

    /**
     * SearchHistoryAdapter
     */
    private val searchHistoryAdapter by lazy {
        SearchHistoryAdapter(this, datas)
    }

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        RecyclerViewItemDecoration(this)
    }

    override fun initLayoutID(): Int = R.layout.activity_search

    override fun initData() {
    }

    override fun initView() {
        super.initView()
        tv_hot_search.setTextColor(mThemeColor)
        tv_search_history.setTextColor(mThemeColor)

        toolbar.run {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        hot_search_flow_layout.run {
            setOnTagClickListener { _, position, _ ->
                if (mHotSearchDatas.size > 0) {
                    val hotSearchBean = mHotSearchDatas[position]
                    goToSearchList(hotSearchBean.name)
                    true
                }
                false
            }
        }

        rv_history_search.run {
            layoutManager = linearLayoutManager
            adapter = searchHistoryAdapter
            itemAnimator = DefaultItemAnimator()
        }

        searchHistoryAdapter.run {
            bindToRecyclerView(rv_history_search)
            onItemClickListener = this@SearchActivity.onItemClickListener
            onItemChildClickListener = this@SearchActivity.onItemChildClickListener
            setEmptyView(R.layout.search_empty_view)
        }

        search_history_clear_all_tv.setOnClickListener {
            datas.clear()
            searchHistoryAdapter.replaceData(datas)
            mPresenter?.clearAllHistory()
        }

        mPresenter?.getHotSearchData()
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.queryHistory()
    }

    private fun goToSearchList(key: String) {
        mPresenter?.saveSearchKey(key)
        Intent(this, CommonActivity::class.java).run {
            putExtra(Constant.TYPE_KEY, Constant.Type.SEARCH_TYPE_KEY)
            putExtra(Constant.SEARCH_KEY, key)
            startActivity(this)
        }
    }

    override fun showHistoryData(historyBeans: MutableList<SearchHistoryBean>) {
        searchHistoryAdapter.replaceData(historyBeans)
    }

    override fun showHotSearchData(hotSearchDatas: MutableList<HotSearchBean>) {
        this.mHotSearchDatas.addAll(hotSearchDatas)
        hot_search_flow_layout.adapter = object : TagAdapter<HotSearchBean>(hotSearchDatas) {
            override fun getView(parent: FlowLayout?, position: Int, hotSearchBean: HotSearchBean?): View {
                val tv: TextView = LayoutInflater.from(parent?.context).inflate(
                    R.layout.flow_layout_tv,
                    hot_search_flow_layout, false
                ) as TextView
                val padding: Int = DisplayManager.dip2px(10F)!!
                tv.setPadding(padding, padding, padding, padding)
                tv.text = hotSearchBean?.name
                tv.setTextColor(CommonUtil.randomColor())
                return tv
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        //设置搜索框直接展开显示。左侧有无放大镜  右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        searchView.onActionViewExpanded()
        //设置输入框提示语
        searchView.queryHint = getString(R.string.search_tint)
        //设置是否显示搜索框展开时的提交按钮
        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(queryTextListener)
        try {
            val field = searchView.javaClass.getDeclaredField("mGoButton")
            field.isAccessible = true
            val mGoButton = field.get(searchView) as ImageView
            mGoButton.setImageResource(R.drawable.ic_search_white_24dp)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * OnQueryTextListener
     */
    private val queryTextListener = object : OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            goToSearchList(query.toString())
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (searchHistoryAdapter.data.size != 0) {
            val item = searchHistoryAdapter.data[position]
            goToSearchList(item.key)
        }
    }

    /**
     * ItemChildClickListener
     */
    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
            if (searchHistoryAdapter.data.size != 0) {
                val item = searchHistoryAdapter.data[position]
                when (view.id) {
                    R.id.iv_clear -> {
                        mPresenter?.deleteById(item.id)
                        searchHistoryAdapter.remove(position)
                    }
                }
            }
        }

}

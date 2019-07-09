package com.release.wanandroid.ui.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.release.wanandroid.App
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseMvpFragment
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.event.ColorEvent
import com.release.wanandroid.ext.showSnackMsg
import com.release.wanandroid.ext.showToast
import com.release.wanandroid.mvp.contract.SearchListContract
import com.release.wanandroid.mvp.model.bean.Article
import com.release.wanandroid.mvp.model.bean.ArticleResponseBody
import com.release.wanandroid.mvp.presenter.SearchListPresenter
import com.release.wanandroid.ui.activity.WebActivity
import com.release.wanandroid.ui.adapter.HomeAdapter
import com.release.wanandroid.ui.login.LoginActivity
import com.release.wanandroid.utils.NetWorkUtil
import com.release.wanandroid.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.fragment_search_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class SearchListFragment : BaseMvpFragment<SearchListContract.View, SearchListContract.Presenter>(), SearchListContract.View {

    private var mKey = ""

    companion object {
        fun getInstance(bundle: Bundle): SearchListFragment {
            val fragment = SearchListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initLayoutID(): Int = R.layout.fragment_search_list

    override fun createPresenter(): SearchListContract.Presenter = SearchListPresenter()

    override fun useEventBus(): Boolean = true

    /**
     * datas
     */
    private val datas = mutableListOf<Article>()

    /**
     * Adapter
     */
    private val searchListAdapter: HomeAdapter by lazy {
        HomeAdapter(activity, datas)
    }

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    /**
     * is Refresh
     */
    private var isRefresh = true

    override fun showLoading() {
        // swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh) {
            searchListAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
        searchListAdapter.run {
            if (isRefresh)
                setEnableLoadMore(true)
            else
                loadMoreFail()
        }
    }



    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view
        mKey = arguments?.getString(Constant.SEARCH_KEY, "") ?: ""

        swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = searchListAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }

        searchListAdapter.run {
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@SearchListFragment.onItemClickListener
            onItemChildClickListener = this@SearchListFragment.onItemChildClickListener
            // setEmptyView(R.layout.fragment_empty_layout)
        }

        floating_action_btn.setOnClickListener {
            scrollToTop()
        }

    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.queryBySearchKey(0, mKey)
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.cancel_collect_success))
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.collect_success))
        }
    }

    override fun showArticles(articles: ArticleResponseBody) {
        articles.datas.let {
            searchListAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                val size = it.size
                if (size < articles.size) {
                    loadMoreEnd(isRefresh)
                } else {
                    loadMoreComplete()
                }
            }
        }
        if (searchListAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            floating_action_btn.backgroundTintList = ColorStateList.valueOf(event.color)
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        searchListAdapter.setEnableLoadMore(false)
        mPresenter?.queryBySearchKey(0, mKey)
    }

    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = searchListAdapter.data.size / 20
        mPresenter?.queryBySearchKey(page, mKey)
    }

    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.size != 0) {
            val data = datas[position]
            Intent(activity, WebActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, data.link)
                putExtra(Constant.CONTENT_TITLE_KEY, data.title)
                putExtra(Constant.CONTENT_ID_KEY, data.id)
                startActivity(this)
            }
        }
    }

    /**
     * ItemChildClickListener
     */
    private val onItemChildClickListener =
            BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
                if (datas.size != 0) {
                    val data = datas[position]
                    when (view.id) {
                        R.id.iv_like -> {
                            if (isLogin) {
                                if (!NetWorkUtil.isNetworkAvailable(App.context)) {
                                    showSnackMsg(resources.getString(R.string.no_network))
                                    return@OnItemChildClickListener
                                }
                                val collect = data.collect
                                data.collect = !collect
                                searchListAdapter.setData(position, data)
                                if (collect) {
                                    mPresenter?.cancelCollectArticle(data.id)
                                } else {
                                    mPresenter?.addCollectArticle(data.id)
                                }
                            } else {
                                Intent(activity, LoginActivity::class.java).run {
                                    startActivity(this)
                                }
                                showToast(resources.getString(R.string.login_tint))
                            }
                        }
                    }
                }
            }

}
package com.release.wanandroid.ui.fragment

import android.content.Intent
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
import com.release.wanandroid.ext.showSnackMsg
import com.release.wanandroid.ext.showToast
import com.release.wanandroid.mvp.contract.KnowledgeContract
import com.release.wanandroid.mvp.model.bean.Article
import com.release.wanandroid.mvp.model.bean.ArticleResponseBody
import com.release.wanandroid.mvp.presenter.KnowledgePresenter
import com.release.wanandroid.ui.activity.WebActivity
import com.release.wanandroid.ui.adapter.KnowledgeAdapter
import com.release.wanandroid.ui.login.LoginActivity
import com.release.wanandroid.utils.NetWorkUtil
import com.release.wanandroid.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class KnowledgeFragment : BaseMvpFragment<KnowledgeContract.View, KnowledgeContract.Presenter>(), KnowledgeContract.View {

    companion object {
        fun getInstance(cid: Int): KnowledgeFragment {
            val fragment = KnowledgeFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    override fun createPresenter(): KnowledgeContract.Presenter = KnowledgePresenter()

    /**
     * cid
     */
    private var cid: Int = 0

    /**
     * datas
     */
    private val datas = mutableListOf<Article>()

    /**
     * RecyclerView Divider
     */
    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            SpaceItemDecoration(it)
        }
    }

    /**
     * Knowledge Adapter
     */
    private val knowledgeAdapter: KnowledgeAdapter by lazy {
        KnowledgeAdapter(activity, datas)
    }

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
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
            knowledgeAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
        knowledgeAdapter.run {
            if (isRefresh)
                setEnableLoadMore(true)
            else
                loadMoreFail()
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

    override fun initLayoutID():  Int = R.layout.fragment_refresh_layout

    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view
        cid = arguments?.getInt(Constant.CONTENT_CID_KEY) ?: 0
        swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }
        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = knowledgeAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }

        knowledgeAdapter.run {
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@KnowledgeFragment.onItemClickListener
            onItemChildClickListener = this@KnowledgeFragment.onItemChildClickListener
            // setEmptyView(R.layout.fragment_empty_layout)
        }
    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.requestKnowledgeList(0, cid)
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

    override fun setKnowledgeList(articles: ArticleResponseBody) {
        articles.datas.let {
            knowledgeAdapter.run {
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
        if (knowledgeAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    /**
     * RefreshListener
     */
    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        knowledgeAdapter.setEnableLoadMore(false)
        mPresenter?.requestKnowledgeList(0, cid)
    }

    /**
     * LoadMoreListener
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = knowledgeAdapter.data.size / 20
        mPresenter?.requestKnowledgeList(page, cid)
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
                                knowledgeAdapter.setData(position, data)
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
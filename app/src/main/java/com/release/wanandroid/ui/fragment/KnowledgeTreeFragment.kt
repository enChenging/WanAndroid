package com.release.wanandroid.ui.fragment

import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseMvpFragment
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.mvp.contract.KnowledgeTreeContract
import com.release.wanandroid.mvp.model.bean.KnowledgeTreeBody
import com.release.wanandroid.mvp.presenter.KnowledgeTreePresenter
import com.release.wanandroid.ui.activity.KnowledgeActivity
import com.release.wanandroid.ui.adapter.KnowledgeTreeAdapter
import com.release.wanandroid.widget.RecyclerViewItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 * @author Mr.release
 * @create 2019/6/24
 * @Describe
 */
class KnowledgeTreeFragment : BaseMvpFragment<KnowledgeTreeContract.View, KnowledgeTreeContract.Presenter>(),
    KnowledgeTreeContract.View {

    companion object {

        fun getInstance(): KnowledgeTreeFragment = KnowledgeTreeFragment()
    }

    override fun createPresenter(): KnowledgeTreeContract.Presenter = KnowledgeTreePresenter()

    override fun initLayoutID(): Int = R.layout.fragment_refresh_layout

    private val datas = mutableListOf<KnowledgeTreeBody>()


    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition()>20){
                scrollToPosition(0)
            }else{
                smoothScrollToPosition(0)
            }
        }
    }

    override fun setKnowledgeTree(lists: List<KnowledgeTreeBody>) {
        lists.let {
            knowledgeTreeAdapter.run {
                replaceData(lists)
            }
        }

        if(knowledgeTreeAdapter.data.isEmpty()){
            mLayoutStatusView?.showEmpty()
        }else{
            mLayoutStatusView?.showContent()
        }
    }

    private val knowledgeTreeAdapter: KnowledgeTreeAdapter by lazy {
        KnowledgeTreeAdapter(activity, datas)
    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val recyclerViewItemDecoration by lazy {
        activity?.let {
            RecyclerViewItemDecoration(it, LinearLayoutManager.VERTICAL)
        }
    }

    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view
        swipeRefreshLayout.run {
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = knowledgeTreeAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let {
                addItemDecoration(it)
            }
        }

        knowledgeTreeAdapter.run {
            bindToRecyclerView(recyclerView)
            setEnableLoadMore(false)
            onItemClickListener = this@KnowledgeTreeFragment.onItemClickListener
        }
    }

    override fun lazyLoad() {

        mLayoutStatusView?.showLoading()
        mPresenter?.requestKnowledgeTree()
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
        knowledgeTreeAdapter.run {
            loadMoreComplete()
        }
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
        knowledgeTreeAdapter.run {
            loadMoreFail()
        }
    }


    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        mPresenter?.requestKnowledgeTree()
    }

    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.size != 0) {
            val data = datas[position]
            Intent(activity, KnowledgeActivity::class.java).run {
                putExtra(Constant.CONTENT_TITLE_KEY, data.name)
                putExtra(Constant.CONTENT_DATA_KEY, data)
                startActivity(this)
            }
        }
    }
}

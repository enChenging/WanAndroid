package com.release.wanandroid.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseActivity
import com.release.wanandroid.base.BaseMvpFragment
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.mvp.contract.KnowledgeTreeContract
import com.release.wanandroid.mvp.model.bean.KnowledgeTreeBody
import com.release.wanandroid.mvp.presenter.KnowledgeTreePresenter
import com.release.wanandroid.ui.activity.KnowledgeActivity
import com.release.wanandroid.ui.adapter.KnowledgeTreeAdapter
import com.release.wanandroid.widget.RecyclerViewItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.fragment_refresh_layout.multiple_status_view
import kotlinx.android.synthetic.main.fragment_refresh_layout.recyclerView
import kotlinx.android.synthetic.main.fragment_todo.*

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


        refresh_layout.run {
            setOnRefreshListener {
                mPresenter?.requestKnowledgeTree()
                finishRefresh(1000)
            }
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

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        refresh_layout.RefreshKernelImpl().refreshLayout.refreshHeader?.setPrimaryColors((activity as BaseActivity).mThemeColor)
    }


    override fun lazyLoad() {

        mLayoutStatusView?.showLoading()
        mPresenter?.requestKnowledgeTree()
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

    override fun setKnowledgeTree(lists: List<KnowledgeTreeBody>) {
        lists.let {
            knowledgeTreeAdapter.run {
                replaceData(lists)
            }
        }

        if (knowledgeTreeAdapter.data.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
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

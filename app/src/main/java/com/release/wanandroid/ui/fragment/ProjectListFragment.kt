package com.release.wanandroid.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.release.wanandroid.App
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseActivity
import com.release.wanandroid.base.BaseMvpFragment
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.ext.showSnackMsg
import com.release.wanandroid.ext.showToast
import com.release.wanandroid.mvp.contract.ProjectListContract
import com.release.wanandroid.mvp.model.bean.Article
import com.release.wanandroid.mvp.model.bean.ArticleResponseBody
import com.release.wanandroid.mvp.presenter.ProjectListPresenter
import com.release.wanandroid.ui.activity.WebActivity
import com.release.wanandroid.ui.adapter.ProjectAdapter
import com.release.wanandroid.ui.login.LoginActivity
import com.release.wanandroid.utils.NetWorkUtil
import com.release.wanandroid.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*
import kotlinx.android.synthetic.main.fragment_refresh_layout.multiple_status_view
import kotlinx.android.synthetic.main.fragment_refresh_layout.recyclerView
import kotlinx.android.synthetic.main.fragment_todo.*

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class ProjectListFragment : BaseMvpFragment<ProjectListContract.View, ProjectListContract.Presenter>(),
    ProjectListContract.View {

    companion object {
        fun getInstance(cid: Int): ProjectListFragment {
            val fragment = ProjectListFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    override fun createPresenter(): ProjectListContract.Presenter = ProjectListPresenter()

    /**
     * cid
     */
    private var cid: Int = -1

    /**
     * Article datas
     */
    private val datas = mutableListOf<Article>()

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
        activity?.let { SpaceItemDecoration(it) }
    }

    /**
     * ProjectAdapter
     */
    private val projectAdapter: ProjectAdapter by lazy {
        ProjectAdapter(activity, datas)
    }

    /**
     * is Refresh
     */
    private var isRefresh = true

    override fun initLayoutID(): Int = R.layout.fragment_refresh_layout

    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view
        cid = arguments!!.getInt(Constant.CONTENT_CID_KEY)


        refresh_layout.run {
            setOnRefreshListener {
                isRefresh = true
                projectAdapter.setEnableLoadMore(false)
                mPresenter?.requestProjectList(1, cid)
                finishRefresh(1000)
            }

            setOnLoadMoreListener {
                isRefresh = false
                val page = projectAdapter.data.size / 15 + 1
                mPresenter?.requestProjectList(page, cid)
                finishLoadMore(1000)
            }
        }


        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = projectAdapter
            itemAnimator = DefaultItemAnimator()
            recyclerViewItemDecoration?.let { addItemDecoration(it) }
        }

        projectAdapter.run {
            onItemClickListener = this@ProjectListFragment.onItemClickListener
            onItemChildClickListener = this@ProjectListFragment.onItemChildClickListener
            // setEmptyView(R.layout.fragment_empty_layout)
        }

    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        refresh_layout.RefreshKernelImpl().refreshLayout.refreshHeader?.setPrimaryColors((activity as BaseActivity).mThemeColor)
    }

    override fun lazyLoad() {
        mLayoutStatusView?.showLoading()
        mPresenter?.requestProjectList(1, cid)
    }

    override fun setProjectList(articles: ArticleResponseBody) {
        articles.datas.let {
            projectAdapter.run {
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
        if (projectAdapter.data.isEmpty()) {
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

    override fun showLoading() {
    }

    override fun hideLoading() {
        if (isRefresh) {
            projectAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
        projectAdapter.run {
            if (isRefresh)
                setEnableLoadMore(true)
            else
                loadMoreFail()
        }
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
                    R.id.item_project_list_like_iv -> {
                        if (isLogin) {
                            if (!NetWorkUtil.isNetworkAvailable(App.context)) {
                                showSnackMsg(resources.getString(R.string.no_network))
                                return@OnItemChildClickListener
                            }
                            val collect = data.collect
                            data.collect = !collect
                            projectAdapter.setData(position, data)
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
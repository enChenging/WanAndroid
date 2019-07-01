package com.release.wanandroid.mvp.contract

import com.release.wanandroid.base.IModel
import com.release.wanandroid.base.IPresenter
import com.release.wanandroid.base.IView
import com.release.wanandroid.mvp.model.bean.HttpResult
import com.release.wanandroid.mvp.model.bean.ProjectTreeBean
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
interface ProjectContract {

    interface View : IView {

        fun scrollToTop()

        fun setProjectTree(list: List<ProjectTreeBean>)

    }

    interface Presenter : IPresenter<View> {

        fun requestProjectTree()

    }

    interface Model : IModel {
        fun requestProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>>
    }

}
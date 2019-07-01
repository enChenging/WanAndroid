package com.release.wanandroid.mvp.presenter

import com.release.wanandroid.base.BasePresenter
import com.release.wanandroid.ext.ss
import com.release.wanandroid.mvp.contract.NavigationContract
import com.release.wanandroid.mvp.model.NavigationModel

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class NavigationPresenter : BasePresenter<NavigationContract.Model, NavigationContract.View>(), NavigationContract.Presenter {

    override fun createModel(): NavigationContract.Model? = NavigationModel()

    override fun requestNavigationList() {
        mModel?.requestNavigationList()?.ss(mModel, mView) {
            mView?.setNavigationData(it.data)
        }
    }

}
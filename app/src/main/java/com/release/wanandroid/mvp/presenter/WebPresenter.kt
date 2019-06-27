package com.release.wanandroid.mvp.presenter

import com.release.wanandroid.mvp.contract.WebContracnt
import com.release.wanandroid.mvp.model.WebModel

/**
 * @author Mr.release
 * @create 2019/6/25
 * @Describe
 */
class WebPresenter :CommonPresenter<WebContracnt.Model,WebContracnt.View>(),WebContracnt.Presenter {

    override fun createModel(): WebContracnt.Model?  = WebModel()
}
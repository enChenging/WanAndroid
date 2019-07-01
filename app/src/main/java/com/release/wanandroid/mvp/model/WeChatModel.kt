package com.release.wanandroid.mvp.model

import com.release.wanandroid.base.BaseModel
import com.release.wanandroid.http.RetrofitHelper
import com.release.wanandroid.mvp.contract.WeChatContract
import com.release.wanandroid.mvp.model.bean.HttpResult
import com.release.wanandroid.mvp.model.bean.WXChapterBean
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class WeChatModel : BaseModel(), WeChatContract.Model {

    override fun getWXChapters(): Observable<HttpResult<MutableList<WXChapterBean>>> {
        return RetrofitHelper.service.getWXChapters()
    }

}
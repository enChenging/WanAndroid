package com.release.wanandroid.ext

import com.orhanobut.logger.Logger
import com.release.wanandroid.App
import com.release.wanandroid.MainActivity
import com.release.wanandroid.R
import com.release.wanandroid.base.IModel
import com.release.wanandroid.base.IView
import com.release.wanandroid.http.exception.ErrorStatus
import com.release.wanandroid.http.exception.ExceptionHandle
import com.release.wanandroid.http.function.RetryWithDelay
import com.release.wanandroid.mvp.model.bean.BaseBean
import com.release.wanandroid.rx.SchedulerUtils
import com.release.wanandroid.utils.NetWorkUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @author Mr.release
 * @create 2019/6/25
 * @Describe
 */
fun <T : BaseBean> Observable<T>.ss(
    model: IModel?,
    view: IView?,
    isShowLoading: Boolean = true,
    onSuccess: (T) -> Unit
) {
    this.compose(SchedulerUtils.ioToMain())
        .retryWhen(RetryWithDelay())
        .subscribe(object : Observer<T> {

            override fun onSubscribe(d: Disposable) {

                if (isShowLoading) view?.showLoading()

                model?.addDisposable(d)

                if (!NetWorkUtil.isNetworkConnected(App.instance)) {
                    view?.showDefaultMsg(App.instance.resources.getString(R.string.network_unavailable_tip))
                    onComplete()
                }
            }

            override fun onNext(t: T) {
                Logger.i("onNext: ${t.errorCode}")
                when {
                    t.errorCode == ErrorStatus.SUCCESS -> onSuccess.invoke(t)
                    t.errorCode == ErrorStatus.TOKEN_INVALID -> {
                        view?.showDefaultMsg("Token 过期，重新登录")
                    }
                    else -> view?.showDefaultMsg(t.errorMsg)
                }
            }

            override fun onError(e: Throwable) {
                Logger.i("onError: ${e.message}")
                view?.hideLoading()
                view?.showError(ExceptionHandle.handleException(e))
            }

            override fun onComplete() {
                Logger.i("onComplete:")
                view?.hideLoading()
            }

        })
}

fun <T : BaseBean> Observable<T>.sss(
    view: IView?,
    isShowLoading: Boolean = true,
    onSuccess: (T) -> Unit
): Disposable {
    if (isShowLoading) view?.showLoading()
    return this.compose(SchedulerUtils.ioToMain())
        .retryWhen(RetryWithDelay())
        .subscribe({
            when {
                it.errorCode == ErrorStatus.SUCCESS -> onSuccess.invoke(it)
                it.errorCode == ErrorStatus.TOKEN_INVALID -> {
                    // Token 过期，重新登录
                }
                else -> view?.showDefaultMsg(it.errorMsg)
            }
            view?.hideLoading()
        }, {
            view?.hideLoading()
            view?.showError(ExceptionHandle.handleException(it))
        })
}
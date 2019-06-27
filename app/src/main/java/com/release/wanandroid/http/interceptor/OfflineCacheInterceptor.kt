package com.release.wanandroid.http.interceptor

import com.release.wanandroid.App
import com.release.wanandroid.utils.NetWorkUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author Mr.release
 * @create 2019/6/26
 * @Describe
 */
class OfflineCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (!NetWorkUtil.isNetworkAvailable(App.context)) {
            // 无网络时，设置超时为4周  只对get有用,post没有缓冲
            val maxStale = 60 * 60 * 24 * 28
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("nyn")
                    .build()
        }

        return response
    }
}
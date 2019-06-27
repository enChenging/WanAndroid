package com.release.wanandroid.http.exception

/**
 * @author Mr.release
 * @create 2019/6/26
 * @Describe
 */
class ApiException : RuntimeException {

    private var code: Int? = null

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))
}
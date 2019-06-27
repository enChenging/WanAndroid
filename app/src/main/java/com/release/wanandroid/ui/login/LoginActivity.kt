package com.release.wanandroid.ui.login

import android.content.Intent
import android.view.View
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseMvpActivity
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.ext.showToast
import com.release.wanandroid.mvp.contract.LoginContract
import com.release.wanandroid.mvp.model.bean.LoginData
import com.release.wanandroid.mvp.presenter.LoginPresenter
import com.release.wanandroid.utils.Preference
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author Mr.release
 * @create 2019/6/25
 * @Describe
 */
class LoginActivity :BaseMvpActivity<LoginContract.View,LoginContract.Presenter>(),LoginContract.View{

    /**
     * local username
     */
    private var user: String by Preference(Constant.USERNAME_KEY, "")

    /**
     * local password
     */
    private var pwd: String by Preference(Constant.PASSWORD_KEY, "")

    /**
     * token
     */
    private var token: String by Preference(Constant.TOKEN_KEY, "")

    override fun createPresenter(): LoginContract.Presenter = LoginPresenter()

    override fun initLayoutID(): Int  = R.layout.activity_login

    override fun initView() {
        super.initView()
        et_username.setText(user)
        btn_login.setOnClickListener(onClickListener)
        tv_sign_up.setOnClickListener(onClickListener)
    }

    override fun loginSuccess(data: LoginData) {
        showToast(getString(R.string.login_success))
        isLogin = true
        user = data.username
        pwd = data.password
        token = data.token
        finish()
    }

    override fun loginFail() {

    }

    private val onClickListener = View.OnClickListener {view->
        when(view.id){
            R.id.btn_login ->{
                login()
            }
            R.id.tv_sign_up ->{
//                val intent = Intent(this,RegisterActivity::class.java)
//                startActivity(intent)
//                finish()
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

    private fun login() {
        if(validate()){
            mPresenter?.loginWanAndroid(et_username.text.toString(),et_password.text.toString())
        }
    }

    private fun validate():Boolean{
        var valid = true
        val username:String = et_username.text.toString()
        val password:String  = et_password.text.toString()
        if (username.isEmpty()){
            et_username.error=getString(R.string.username_not_empty)
            valid = false
        }
        if (password.isEmpty()) {
            et_password.error = getString(R.string.password_not_empty)
            valid = false
        }
        return valid
    }
}
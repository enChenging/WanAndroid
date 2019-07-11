package com.release.wanandroid.ui.fragment

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
@Suppress("DEPRECATION")
class AboutFragment : BaseFragment() {

    companion object {
        fun getInstance(bundle: Bundle): AboutFragment {
            val fragment = AboutFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initLayoutID(): Int = R.layout.fragment_about

    override fun initView(view: View) {
        about_content.run {
            text = Html.fromHtml(getString(R.string.about_content))
            movementMethod = LinkMovementMethod.getInstance()
        }

        val versionStr = getString(R.string.app_name) + " V" + activity?.packageManager?.getPackageInfo(activity?.packageName, 0)?.versionName
        about_version.text = versionStr
    }


    override fun lazyLoad() {
    }
}
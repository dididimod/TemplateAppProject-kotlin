/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.xuexiang.templateproject.core.webview

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xuexiang.templateproject.R
import com.xuexiang.xui.utils.ResUtils
import com.xuexiang.xui.utils.XToastUtils
import com.xuexiang.xui.widget.dialog.DialogLoader
import com.xuexiang.xutil.XUtil
import com.xuexiang.xutil.app.ActivityUtils
import java.net.URISyntaxException

/**
 * WebView拦截提示
 *
 * @author xuexiang
 * @since 2019-10-21 9:51
 */
class WebViewInterceptDialog : AppCompatActivity(), DialogInterface.OnDismissListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra(KEY_INTERCEPT_URL)
        DialogLoader.getInstance().showConfirmDialog(
            this,
            getOpenTitle(url),
            ResUtils.getString(R.string.lab_yes),
            { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                if (isAppLink(url)) {
                    openAppLink(this, url)
                } else {
                    openApp(url)
                }
            },
            ResUtils.getString(R.string.lab_no)
        ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }.setOnDismissListener(this)
    }

    private fun getOpenTitle(url: String): String {
        val scheme = getScheme(url)
        return if ("mqqopensdkapi" == scheme) {
            "是否允许页面打开\"QQ\"?"
        } else {
            ResUtils.getString(R.string.lab_open_third_app)
        }
    }

    private fun getScheme(url: String): String? {
        try {
            val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
            return intent.scheme
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun isAppLink(url: String): Boolean {
        val uri = Uri.parse(url)
        return uri != null && APP_LINK_HOST == uri.host && (url.startsWith("http") || url.startsWith(
            "https"
        ))
    }

    private fun openApp(url: String) {
        val intent: Intent
        try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            XUtil.getContext().startActivity(intent)
        } catch (e: Exception) {
            XToastUtils.error("您所打开的第三方App未安装！")
        }
    }

    private fun openAppLink(context: Context, url: String) {
        try {
            val intent = Intent(APP_LINK_ACTION)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        } catch (e: Exception) {
            XToastUtils.error("您所打开的第三方App未安装！")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        finish()
    }

    companion object {
        private const val KEY_INTERCEPT_URL = "key_intercept_url"

        // TODO: 2019-10-30 这里修改你的applink
        const val APP_LINK_HOST = "xuexiangjys.club"
        const val APP_LINK_ACTION = "com.xuexiang.xui.applink"

        /**
         * 显示WebView拦截提示
         *
         * @param url 需要拦截处理的url
         */
        @JvmStatic
        fun show(url: String?) {
            ActivityUtils.startActivity(WebViewInterceptDialog::class.java, KEY_INTERCEPT_URL, url)
        }
    }
}
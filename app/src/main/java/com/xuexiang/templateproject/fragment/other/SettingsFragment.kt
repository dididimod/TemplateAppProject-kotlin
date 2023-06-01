/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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
package com.xuexiang.templateproject.fragment.other

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import com.xuexiang.templateproject.R
import com.xuexiang.templateproject.core.BaseFragment
import com.xuexiang.templateproject.databinding.FragmentSettingsBinding
import com.xuexiang.templateproject.utils.TokenUtils.handleLogoutSuccess
import com.xuexiang.xaop.annotation.SingleClick
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xui.utils.XToastUtils
import com.xuexiang.xui.widget.dialog.DialogLoader
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView.OnSuperTextViewClickListener
import com.xuexiang.xutil.XUtil

/**
 * @author xuexiang
 * @since 2019-10-15 22:38
 */
@Page(name = "设置")
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(), OnSuperTextViewClickListener {

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initViews() {
        binding?.menuCommon?.setOnSuperTextViewClickListener(this)
        binding?.menuPrivacy?.setOnSuperTextViewClickListener(this)
        binding?.menuPush?.setOnSuperTextViewClickListener(this)
        binding?.menuHelper?.setOnSuperTextViewClickListener(this)
        binding?.menuChangeAccount?.setOnSuperTextViewClickListener(this)
        binding?.menuLogout?.setOnSuperTextViewClickListener(this)
    }

    @SingleClick
    override fun onClick(superTextView: SuperTextView) {
        val id = superTextView.id
        if (id == R.id.menu_common || id == R.id.menu_privacy || id == R.id.menu_push || id == R.id.menu_helper) {
            XToastUtils.toast(superTextView.leftString)
        } else if (id == R.id.menu_change_account) {
            XToastUtils.toast(superTextView.centerString)
        } else if (id == R.id.menu_logout) {
            DialogLoader.getInstance().showConfirmDialog(
                context,
                getString(R.string.lab_logout_confirm),
                getString(R.string.lab_yes),
                { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                    XUtil.getActivityLifecycleHelper().exit()
                    handleLogoutSuccess()
                },
                getString(R.string.lab_no)
            ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        }
    }
}
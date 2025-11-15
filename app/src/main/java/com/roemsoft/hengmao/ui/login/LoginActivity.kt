package com.roemsoft.hengmao.ui.login

import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.roemsoft.hengmao.R
import com.roemsoft.hengmao.base.DataBindingAppCompatActivity
import com.roemsoft.hengmao.databinding.ActivityLoginBinding
import com.roemsoft.hengmao.dialog.ProgressBarDialog
import com.roemsoft.hengmao.hideSoftKeyboard
import com.roemsoft.hengmao.isShouldHideKeyboard
import com.roemsoft.hengmao.onSingleClick
import com.roemsoft.hengmao.ui.part_in.PartInActivity

class LoginActivity : DataBindingAppCompatActivity() {

    private val binding: ActivityLoginBinding by binding(R.layout.activity_login)

    override val viewModel: LoginViewModel by viewModels()

    private val dialog by lazy {
        ProgressBarDialog(this).build()
    }

    private var isOpenEye = false

    override fun bindingView() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    override fun initView() {
        val width = resources.displayMetrics.widthPixels
        binding.loginBg.post {
            binding.loginBg.layoutParams.width = width
            binding.loginBg.layoutParams.height = (width * 0.81f).toInt()
        }

        binding.loginUsernameView.doOnTextChanged { text, _, _, _ ->
            binding.loginUserDeleteIcon.visibility =
                if (text?.trim().isNullOrEmpty())
                    View.INVISIBLE
                else
                    View.VISIBLE
        }

        binding.loginPwView.doOnTextChanged { text, _, _, _ ->
            binding.loginPwDeleteIcon.visibility =
                if (text?.trim().isNullOrEmpty())
                    View.INVISIBLE
                else
                    View.VISIBLE
        }

        binding.loginUserDeleteIcon.onSingleClick {
            viewModel.username.value = ""
            binding.loginUsernameView.requestFocus()
        }
        binding.loginPwDeleteIcon.onSingleClick {
            viewModel.password.value = ""
            binding.loginPwView.requestFocus()
        }

        binding.loginPwTransformationIcon.onSingleClick {
            if (!isOpenEye) {
                isOpenEye = true
                binding.loginPwView.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                isOpenEye = false
                binding.loginPwView.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    override fun setupEvent() {
        super.setupEvent()

        viewModel.loading.observe(this) {
            if (it) {
                dialog.show()
            } else {
                dialog.hide()
            }
        }

        viewModel.login.observe(this) {
            gotoMain()
            finish()
        }
    }

    private fun gotoMain() {
    //    startActivity(Intent(this, MainActivity::class.java))
        startActivity(Intent(this, PartInActivity::class.java))
    }

    /*override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //如果是点击事件，获取点击的view，并判断是否要收起键盘
        if (ev.action == MotionEvent.ACTION_DOWN) {
            //获取目前得到焦点的view
            val v = currentFocus
            //判断是否要收起并进行处理
            if (v != null && v.isShouldHideKeyboard(ev)) {
                this.hideSoftKeyboard(v.windowToken)
                v.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }*/
}
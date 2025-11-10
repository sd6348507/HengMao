package com.roemsoft.hengmao.ui.pjrk

import android.content.Intent
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.roemsoft.hengmao.R
import com.roemsoft.hengmao.base.DataBindingAppCompatActivity
import com.roemsoft.hengmao.dialog.ProgressBarDialog
import com.roemsoft.hengmao.hideSoftKeyboard
import com.roemsoft.hengmao.onSingleClick
import com.roemsoft.hengmao.widget.CustomerToast
import com.roemsoft.hengmao.App
import com.roemsoft.hengmao.databinding.ActivityPdrkBinding
import com.roemsoft.hengmao.showSoftKeyboard
import com.roemsoft.hengmao.ui.search.StorageSearchActivity
import com.roemsoft.hengmao.ui.search.customer.CustomerSearchActivity
import com.roemsoft.hengmao.ui.search.hw.HwSearchActivity
import com.roemsoft.hengmao.ui.search.spec.SpecSearchActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

class PjRkActivity : DataBindingAppCompatActivity() {

    private val mBinding: ActivityPdrkBinding by binding(R.layout.activity_pdrk)

    override val viewModel: PdRkViewModel by viewModels()

    private lateinit var searchLauncher: ActivityResultLauncher<Intent>

    private val dialog by lazy {
        ProgressBarDialog(this).build()
    }

    override fun bindingView() {
        mBinding.vm = viewModel
        mBinding.lifecycleOwner = this
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun initView() {
        searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                StorageSearchActivity.ACTION_SEARCH_STORAGE -> {
                    viewModel.storage.value = result.data?.getParcelableExtra(
                        StorageSearchActivity.RESULT_SELECTED)
                }
                HwSearchActivity.ACTION_SEARCH_HW -> {
                    viewModel.hw.value = result.data?.getParcelableExtra(HwSearchActivity.RESULT_SELECTED)
                }
                CustomerSearchActivity.ACTION_SEARCH_CUSTOMER -> {
                    viewModel.customer.value = result.data?.getStringExtra(SpecSearchActivity.RESULT_SELECTED)
                }
            }
        }

        mBinding.storage.onSingleClick {
            searchLauncher.launch(Intent(this, StorageSearchActivity::class.java))
        }
        mBinding.hw.onSingleClick {
            if (viewModel.storage.value == null) {
                CustomerToast(this, "先选择仓库", Toast.LENGTH_SHORT).show()
                searchLauncher.launch(Intent(this, StorageSearchActivity::class.java))
                return@onSingleClick
            }
            searchLauncher.launch(Intent(this, HwSearchActivity::class.java).putExtra(
                HwSearchActivity.EXTRA_STORAGE_ID, viewModel.storage.value?.id ?: ""
            ))
        }
        mBinding.customer.onSingleClick {
            searchLauncher.launch(Intent(this, CustomerSearchActivity::class.java))
        }

        mBinding.submitBtn.onSingleClick {
            viewModel.submit()
        }

        mBinding.codeSearchBtn.onSingleClick {
            viewModel.fetchCodeInfo()
        }

        mBinding.codeEt.setOnEditorActionListener { v, actionId, event ->
            Timber.tag("Key Listener").i("===> edit: $actionId, $event <===")
            if (actionId == EditorInfo.IME_ACTION_DONE && event != null &&
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER) {
                viewModel.fetchCodeInfo()
                Timber.tag("Key Listener").i("===> code: ${mBinding.codeEt.text} <===")
            //    viewModel.code.value = ""
            //    mBinding.codeEt.setText("")
            }
            true
        }

        mBinding.customerEditable.onSingleClick {
            mBinding.customerEditable.isSelected = !mBinding.customerEditable.isSelected
            if (mBinding.customerEditable.isSelected) {
                mBinding.customer.visibility = View.INVISIBLE
                mBinding.customerEt.visibility = View.VISIBLE
                mBinding.customerEt.apply {
                    requestFocus()
                    setSelection(mBinding.customerEt.length())
                    showSoftKeyboard()
                }
            } else {
                mBinding.customer.visibility = View.VISIBLE
                mBinding.customerEt.visibility = View.INVISIBLE
                mBinding.customerEt.apply {
                    clearFocus()
                }
            }
        }
    }

    override fun getToolbar(): Toolbar {
        return mBinding.toolbar
    }

    override fun setToolTitle() {
        mBinding.toolbarTitle.setText(R.string.label_pdrk)
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

        viewModel.searchCompleted.observe(this) {
            if (it) {
                mBinding.codeEt.clearFocus()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == App.KEY_SCAN_LEFT || keyCode == App.KEY_SCAN_RIGHT) {
            mBinding.codeEt.requestFocus()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //如果是点击事件，获取点击的view，并判断是否要收起键盘
        if (ev.action == MotionEvent.ACTION_DOWN) {
            //获取目前得到焦点的view
            val v = currentFocus
            //判断是否要收起并进行处理
            if (v != null && isShouldHideKeyboard(v, ev)) {
                this.hideSoftKeyboard(v.windowToken)
                v.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    //判断是否要收起键盘
    private fun isShouldHideKeyboard(v: View, event: MotionEvent): Boolean {
        //如果目前得到焦点的这个view是editText的话进行判断点击的位置
        if (v is EditText) {
            val point = intArrayOf(0, 0)
            v.getLocationInWindow(point)
            val left = point[0]
            val top = point[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            // 点击EditText的事件，忽略它。
            return event.x <= left || event.x >= right || event.y <= top || event.y >= bottom
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上
        return false
    }
}

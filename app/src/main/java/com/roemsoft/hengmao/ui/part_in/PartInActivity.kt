package com.roemsoft.hengmao.ui.part_in

import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.roemsoft.hengmao.R
import com.roemsoft.hengmao.base.DataBindingAppCompatActivity
import com.roemsoft.hengmao.dialog.ProgressBarDialog
import com.roemsoft.hengmao.hideSoftKeyboard
import com.roemsoft.hengmao.onSingleClick
import com.roemsoft.hengmao.App
import com.roemsoft.hengmao.databinding.ActivityPartInBinding
import com.roemsoft.hengmao.databinding.DialogPickStorageBinding
import com.roemsoft.hengmao.dialog.BottomContainerDialog
import com.roemsoft.hengmao.dialog.bottomContainerDialog
import com.roemsoft.hengmao.dialog.showAlertDialog
import com.roemsoft.hengmao.dialog.showInfoDialog
import com.roemsoft.hengmao.dpToPx
import com.roemsoft.hengmao.showSoftKeyboard
import com.roemsoft.hengmao.widget.MarginItemDecoration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

class PartInActivity : DataBindingAppCompatActivity() {

    private val mBinding: ActivityPartInBinding by binding(R.layout.activity_part_in)

    override val viewModel: PartInViewModel by viewModels()

    private val dialog by lazy {
        ProgressBarDialog(this).build()
    }

    private val bottomDialogBinding: DialogPickStorageBinding by lazy {
        DataBindingUtil.inflate(layoutInflater, R.layout.dialog_pick_storage, null, false)
    }
    private lateinit var storageDialog: BottomContainerDialog

    override fun bindingView() {
        mBinding.vm = viewModel
        mBinding.lifecycleOwner = this
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun initView() {
        onBackPressedDispatcher.addCallback {
            if (viewModel.needSave()) {
                showInfoDialog {
                    title = "保存提示"
                    content = "数据还未保存，系统将自动保存!"
                    onConfirm = {
                        viewModel.save()
                    }
                }
            } else {
                finish()
            }
        }

        storageDialog = bottomContainerDialog {
            view = {
                bottomDialogBinding.list.apply {
                    adapter = viewModel.storageAdapter
                    layoutManager = LinearLayoutManager(this@PartInActivity)
                    addItemDecoration(MarginItemDecoration(dpToPx(1)))
                }
                bottomDialogBinding.cancel.onSingleClick {
                    dismiss()
                }
                bottomDialogBinding.confirm.onSingleClick {
                    viewModel.pickStorage()
                    dismiss()
                }
                bottomDialogBinding.root
            }
        }

        mBinding.list.apply {
            adapter = viewModel.adapter
            layoutManager = LinearLayoutManager(this@PartInActivity)
            addItemDecoration(DividerItemDecoration(this@PartInActivity, OrientationHelper.VERTICAL))
        }

        mBinding.layoutStorage.onSingleClick {
            viewModel.loadStorage()
        }

        mBinding.labelSave.onSingleClick {
            viewModel.save()
        }

        mBinding.codeSearchBtn.onSingleClick {
            viewModel.fetchCodeInfoAndSubmit()
        }

        mBinding.codeEt.setOnEditorActionListener { v, actionId, event ->
            Timber.tag("Key Listener").i("===> edit: $actionId, $event <===")
            if ((actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) &&
                event != null &&
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER) {
                viewModel.fetchCodeInfoAndSubmit()
                Timber.tag("Key Listener").i("===> code: ${mBinding.codeEt.text} <===")
            //    viewModel.code.value = ""
            //    mBinding.codeEt.setText("")
            }
            true
        }
    }

    override fun initData() {
        viewModel.loadStorage()
    }

    override fun getToolbar(): Toolbar {
        return mBinding.toolbar
    }

    override fun setToolTitle() {
        mBinding.toolbarTitle.setText(R.string.label_part_in)
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

        viewModel.showStorageDialog.observe(this) {
            if (storageDialog.isShowing()) {
                storageDialog.dismiss()
            } else {
                storageDialog.show()
            }
        }

        viewModel.showInfoDialog.observe(this) { msg ->
            showInfoDialog {
                title = "提示"
                content = msg
            }
        }

        viewModel.showDeleteDialog.observe(this) { code ->
            showAlertDialog {
                title = "删除"
                content = "是否删除该条码所有数据？"
                onConfirm = {
                    viewModel.deleteCode(code)
                }
            }
        }

        viewModel.searchCompleted.observe(this) {
            if (it) {
                mBinding.codeEt.clearFocus()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Timber.tag("Key Listener").i("===> key code: $keyCode, $event <===")
        if (keyCode == App.KEY_SCAN_LEFT || keyCode == App.KEY_SCAN_RIGHT) {
            viewModel.code.value = ""
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

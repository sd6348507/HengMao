package com.roemsoft.hengmao.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.roemsoft.hengmao.R
import com.roemsoft.hengmao.databinding.DialogProgressbarBinding

class ProgressBarDialog(context: Context) {

    private val mDialog by lazy { Dialog(context, R.style.Theme_App_Dialog_Alert) }
    private val mDialogViewBinding by lazy { DialogProgressbarBinding.inflate(LayoutInflater.from(context), null, false) }
 //   private val mDialogView by lazy { LayoutInflater.from(context).inflate(R.layout.dialog_progressbar, null, false) }

    fun build(): ProgressBarDialog {
        mDialog.apply {
            setContentView(mDialogViewBinding.root)
            setCancelable(false)
            setCanceledOnTouchOutside(false)

            window?.let {
                it.attributes.apply {
                    gravity = Gravity.CENTER
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = WindowManager.LayoutParams.MATCH_PARENT
                }
            }
        }

        return this
    }

    fun show() {
        mDialog.show()
    }

    fun hide() {
        mDialog.dismiss()
    }
}
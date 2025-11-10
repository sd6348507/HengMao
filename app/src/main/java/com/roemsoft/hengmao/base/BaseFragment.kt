package com.roemsoft.hengmao.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private var hasLoadData = false

    abstract fun dataBinding()

    abstract fun initView()

    abstract fun lazyLoad()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding()

        setupEvent()

        initView()
    }

    override fun onResume() {
        super.onResume()
        if (!hasLoadData) {
            hasLoadData = true
            lazyLoad()
        }
    }

    open fun setupEvent() {  }
}
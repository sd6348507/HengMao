package com.roemsoft.hengmao.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.roemsoft.hengmao.di.RepositoryModule
import com.roemsoft.hengmao.repository.AppRepository

typealias Retry = () -> Unit

open class BaseViewModel : ViewModel() {

    val repo: AppRepository = RepositoryModule.repository

    private val _toastResId = MutableLiveData<Int>()
    val toastResId: LiveData<Int> = _toastResId
    private val _toastText = MutableLiveData<String>()
    val toastText: LiveData<String> = _toastText

    private val _snackbarText = MutableLiveData<Pair<String, Retry>>()
    val snackbarText: LiveData<Pair<String, Retry>> = _snackbarText

    fun showToast(resId: Int) {
        _toastResId.value = resId
    }
    fun showToast(text: String) {
        _toastText.value = text
    }
    fun postToast(text: String) {
        _toastText.postValue(text)
    }

    fun showRetrySnackBar(text: String, retry: Retry) {
        _snackbarText.value = Pair(text, retry)
    }
}
package com.roemsoft.hengmao.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jeremyliao.liveeventbus.BuildConfig
import com.roemsoft.hengmao.App
import com.roemsoft.hengmao.base.BaseViewModel
import com.roemsoft.hengmao.bean.doError
import com.roemsoft.hengmao.bean.doFailure
import com.roemsoft.hengmao.bean.doSuccess
import com.roemsoft.hengmao.preference.Preference
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {

    private var user: String by Preference(Preference.USERNAME_KEY, "")
    private var pw: String by Preference(Preference.PASSWORD_KEY, "")

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

  //  val username = MutableLiveData(if (BuildConfig.DEBUG) "administrator" else user)
  //  val password = MutableLiveData(if (BuildConfig.DEBUG) "zderp.." else pw)
    val username = MutableLiveData(user)
    val password = MutableLiveData(pw)

    private val _login = MutableLiveData<Unit?>()
    val login: LiveData<Unit?> = _login

    fun login() {
    //    _login.value = null
    //    return
        if (!validate()) {
            return
        }

        viewModelScope.launch {
            repo.login(username.value.toString().trim(), password.value.toString().trim())
                .onStart { _loading.value = true }
                .catch { _loading.value = false }
                .onCompletion { _loading.value = false }
                .collectLatest { result ->
                    result.doSuccess { name ->
                        user = username.value!!
                        pw = password.value!!

                        App.userCode = name
                        _login.value = null
                    }
                    result.doFailure { showToast(it) }
                    result.doError { showToast(it) }
                }
        }
    }

    private fun validate(): Boolean {
        var valid = true
        if (username.value.toString().trim().isEmpty()) {
            showToast("账号不能为空！")
            valid = false
        }
        /*if (password.value.isNullOrEmpty()) {
            showToast("密码不能为空！")
            valid = false
        }*/
        return valid
    }
}
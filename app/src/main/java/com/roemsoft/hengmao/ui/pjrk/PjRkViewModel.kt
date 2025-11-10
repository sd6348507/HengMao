package com.roemsoft.hengmao.ui.pjrk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.roemsoft.zd.App
import com.roemsoft.zd.base.BaseViewModel
import com.roemsoft.zd.bean.Category
import com.roemsoft.zd.bean.CkData
import com.roemsoft.zd.bean.HwData
import com.roemsoft.zd.bean.ItemData
import com.roemsoft.zd.bean.PrinterData
import com.roemsoft.zd.bean.Storage
import com.roemsoft.zd.bean.doError
import com.roemsoft.zd.bean.doFailure
import com.roemsoft.zd.bean.doSuccess
import com.roemsoft.zd.today2
import com.roemsoft.zd.until.LPAPIManager
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class PjRkViewModel : BaseViewModel() {

    val code = MutableLiveData("")
    val itemData = MutableLiveData<ItemData>()
    val storage = MutableLiveData<Storage>()
    val hw = MutableLiveData<HwData>()
    val customer = MutableLiveData("")
    val brand = MutableLiveData("")
    val season = MutableLiveData("")
    val model = MutableLiveData("")
    /*val chNo = MutableLiveData("")
    val chName = MutableLiveData("")
    val spec = MutableLiveData("")
    val color = MutableLiveData("")
    val unit = MutableLiveData("Y")*/
    val qty = MutableLiveData("")
    val jm = MutableLiveData("")

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _searchCompleted = MutableLiveData(false)
    val searchCompleted: LiveData<Boolean> = _searchCompleted

    private var isSubmit: Boolean = false

    private var submitJob: Job? = null

    private var submitCode: String = ""

    fun fetchCodeInfo() {
        if (code.value.isNullOrEmpty()) {
            showToast("扫描或输入二维码")
            return
        }
        viewModelScope.launch {
            repo.fetchCodeInfo(code.value!!)
                .onStart { _loading.value = true }
                .catch { _loading.value = false }
                .onCompletion {
                    _loading.value = false
                    code.value = ""
                    _searchCompleted.value = true
                }
                .collectLatest { result ->
                    result.doSuccess {
                        if (it.data.isEmpty()) {
                            showToast("没有数据!")
                        } else {
                            itemData.value = it.data[0]
                        }
                        submitCode = code.value!!
                    }
                    result.doFailure { showToast(it) }
                    result.doError { showToast(it) }
                }
        }
    }

    @Synchronized
    fun submit() {
        if (!validate()) {
            return
        }

        isSubmit = true

        submitJob = viewModelScope.launch {
            repo.submitRk(storage.value!!.id, hw.value!!.no, customer.value ?: "",
                brand.value ?: "", season.value ?: "",
                model.value ?: "", submitCode, qty.value!!, jm.value ?: "", App.userCode!!)
                .onStart { _loading.value = true }
                .catch {
                    isSubmit = false
                    _loading.value = false
                }
                .onCompletion {
                    isSubmit = false
                    _loading.value = false
                }
                .collectLatest { result ->
                    result.doSuccess {
                        showToast("提交成功")
                        reset()
                    }
                    result.doFailure { showToast(it) }
                    result.doError { showToast(it) }
                }
        }
    }

    private fun validate(): Boolean {
        if (submitJob != null && submitJob!!.isActive) {
            showToast("请等待上个条码提交完成！")
            return false
        }
        if (App.userCode.isNullOrEmpty()) {
            showToast("请先登录")
            return false
        }
        if (storage.value?.id.isNullOrEmpty()) {
            showToast("请选择仓库")
            return false
        }
        if (hw.value?.no.isNullOrEmpty()) {
            showToast("请选择货位")
            return false
        }
        if (itemData.value == null || itemData.value?.id.isNullOrEmpty()) {
            showToast("请先获取二维码信息")
            return false
        }
        if (qty.value.isNullOrEmpty()) {
            showToast("请输入数量")
            return false
        }
        return true
    }

    private fun reset() {
        /*storage.value = Storage()
        hw.value = HwData()
        brand.value = ""
        season.value = ""
        model.value = ""
        category.value = Category()
        wlPre.value = ""
        wlName.value = ""
        wlNameNo = null
        productName.value = ""
        chName.value = ""
        spec.value = ""
        color.value = ""
        unit.value = ""*/
        itemData.value = ItemData()
        brand.value = ""
        season.value = ""
        model.value = ""
        customer.value = ""
        qty.value = ""
        jm.value = ""

        isSubmit = false
        submitCode = ""
    }
}
package com.roemsoft.hengmao.ui.part_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.roemsoft.hengmao.App
import com.roemsoft.hengmao.base.BaseViewModel
import com.roemsoft.hengmao.bean.CodeData
import com.roemsoft.hengmao.bean.HwData
import com.roemsoft.hengmao.bean.doError
import com.roemsoft.hengmao.bean.doFailure
import com.roemsoft.hengmao.bean.doSuccess
import com.roemsoft.hengmao.ui.StorageListAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PartInViewModel : BaseViewModel() {

    val code = MutableLiveData("M25111132000001")
    val storage = MutableLiveData<String>()
    val num = MutableLiveData("")
    val qty = MutableLiveData("")

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _searchCompleted = MutableLiveData(false)
    val searchCompleted: LiveData<Boolean> = _searchCompleted

    private var storageJob: Job? = null
    val storageAdapter = StorageListAdapter()
    private val _showStorageDialog = MutableLiveData<Boolean>()
    val showStorageDialog: LiveData<Boolean> = _showStorageDialog

    private val _showDeleteDialog = MutableLiveData<String>()
    val showDeleteDialog: LiveData<String> = _showDeleteDialog

    val adapter = PartInAdapter().apply {
        onDeleteClick { data ->
            _showDeleteDialog.value = data
        }
    }

    private var isSubmit: Boolean = false

    private var submitJob: Job? = null

    private var submitCode: String = ""

    fun loadStorage() {
        if (storageJob != null && storageJob!!.isActive) {
            return
        }
        if (storageAdapter.list.isNotEmpty()) {
            _showStorageDialog.value = true
            return
        }
        storageJob = viewModelScope.launch {
            repo.loadStorage()
                .onStart { _loading.value = true }
                .catch { _loading.value = false }
                .onCompletion { _loading.value = false }
                .collectLatest { result ->
                    result.doSuccess {
                        if (it.data.isEmpty()) {
                            showToast("没有数据!")
                        } else {
                            storageAdapter.setData(it.data)
                            _showStorageDialog.value = true
                        }
                    }
                    result.doFailure { showToast(it) }
                    result.doError { showToast(it) }
                }
        }
    }

    fun pickStorage() {
        storage.value = storageAdapter.getSelectedStorage()
    }

    fun fetchCodeInfo() {
        if (code.value.isNullOrEmpty()) {
            showToast("扫描或输入条码")
            return
        }
        if (adapter.isContainer(code.value!!)) {
            showToast("当前对应的条码号已扫描!!!")
            return
        }
        viewModelScope.launch {
            repo.fetchCodeInfo(code.value!!)
                .onStart { _loading.value = true }
                .catch { _loading.value = false }
                .onCompletion {
                    _loading.value = false
                //    code.value = ""
                    _searchCompleted.value = true
                }
                .collectLatest { result ->
                    result.doSuccess {
                        if (it.data.isEmpty()) {
                            showToast("没有数据!")
                        } else {
                            adapter.addData(it.data[0])
                            updateNumAndQty()
                        }
                    //    submitCode = code.value!!
                    }
                    result.doFailure { showToast(it) }
                    result.doError { showToast(it) }
                }
        }
    }

    @Synchronized
    fun deleteCode(code: String) {

    }

    @Synchronized
    fun submit() {
        if (!validate()) {
            return
        }

        /*isSubmit = true

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
        }*/
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
        /*if (storage.value?.id.isNullOrEmpty()) {
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
        }*/
        return true
    }

    private fun updateNumAndQty() {
        if (adapter.list.isEmpty()) {
            num.value = ""
            qty.value = ""
        } else {
            num.value = "${adapter.list.size}"
            var qtyInt = 0
            adapter.list.forEach {
                qtyInt += try {
                    it.ctn.toInt()
                } catch (e: Exception) {
                    0
                }
            }
            qty.value = "$qtyInt"
        }
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

        isSubmit = false
        submitCode = ""
    }
}
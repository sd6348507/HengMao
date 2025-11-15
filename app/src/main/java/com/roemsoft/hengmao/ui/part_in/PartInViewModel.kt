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
import java.util.UUID

class PartInViewModel : BaseViewModel() {

    val code = MutableLiveData("")
    val storage = MutableLiveData<String>()
    val num = MutableLiveData("")
    val qty = MutableLiveData("")

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _showInfoDialog = MutableLiveData<String>()
    val showInfoDialog: LiveData<String> = _showInfoDialog

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

    private var deleteJob: Job? = null

    private var isSubmit: Boolean = false
    private var submitJob: Job? = null

    private var saveJob: Job? = null

 //   private var submitCode: String = ""

    var mUUID: String? = null

    private fun generateUUID() {
        mUUID = UUID.randomUUID().toString()
    }

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

    fun fetchCodeInfoAndSubmit() {
        if (storage.value.isNullOrEmpty()) {
            _showStorageDialog.value = true
            return
        }
        if (submitJob != null && submitJob!!.isActive) {
            return
        }
        if (App.userCode.isNullOrEmpty()) {
            _showInfoDialog.value = "操作失败：登入超时，请退出重新登入!"
            return
        }
        if (code.value.isNullOrEmpty()) {
            showToast("扫描或输入条码")
            return
        }
        if (adapter.isContainer(code.value!!)) {
            _showInfoDialog.value = "当前对应的条码号已扫描!!!"
            return
        }

        submitJob = viewModelScope.launch {
            isSubmit = true
            _loading.value = true

            Timber.tag("PartInViewModel").i("===> load data start $isSubmit")

            val codeJob = async(Dispatchers.IO, CoroutineStart.LAZY) {
                repo.fetchCodeInfo(code.value!!)
            }
            val inJob = async(Dispatchers.IO, CoroutineStart.LAZY) {
                Timber.tag("PartInViewModel").i("===> uuid $mUUID")
                if (mUUID.isNullOrEmpty()) {
                    generateUUID()
                }
                repo.partInSubmit(mUUID!!, storage.value!!, code.value!!, App.userCode!!)
            }

            var codeInfo: CodeData? = null
            codeJob.await()
                .catch {
                    _loading.value = false
                    isSubmit = false
                }
                .collectLatest { result ->
                    result.doSuccess {
                        if (it.data.isEmpty()) {
                            _showInfoDialog.value = "找不到条码号对应的信息,条码号内容为:${code.value}!!!"
                            inJob.cancelAndJoin()
                        } else {
                            Timber.tag("PartInViewModel").i("===> 获取条码信息成功 <===")
                            codeInfo = it.data[0]
                        }
                    }
                    result.doFailure {
                        _showInfoDialog.value = it
                        Timber.tag("PartInViewModel").i("===> 获取条码信息失败 <===")
                        inJob.cancelAndJoin()
                    }
                    result.doError {
                        _showInfoDialog.value = it
                        Timber.tag("PartInViewModel").i("===> 获取条码信息失败 <===")
                        inJob.cancelAndJoin()
                    }
                }

            Timber.tag("PartInViewModel").i("===> 配件入库提交判断 <===")
            if (codeInfo != null) {
                inJob.await().collectLatest { result ->
                    result.doSuccess {
                        adapter.addData(codeInfo!!)
                        updateNumAndQty()
                    }
                    result.doFailure { _showInfoDialog.value = it }
                    result.doError { _showInfoDialog.value = it }
                }
            }

            _loading.value = false
            isSubmit = false
            _searchCompleted.value = true
            Timber.tag("PartInViewModel").i("===> load data end:$isSubmit")
        }
    }

    /*fun fetchCodeInfo() {
        if (code.value.isNullOrEmpty()) {
            showToast("扫描或输入条码")
            return
        }
        if (adapter.isContainer(code.value!!)) {
            _showInfoDialog.value = "当前对应的条码号已扫描!!!"
            return
        }
        if (mUUID.isNullOrEmpty()) {
            generateUUID()
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
                            submit(it.data[0])
                        }
                    }
                    result.doFailure { _showInfoDialog.value = it }
                    result.doError { _showInfoDialog.value = it }
                }
        }
    }*/

    @Synchronized
    fun deleteCode(code: String) {
        if (deleteJob != null && deleteJob!!.isActive) {
            showToast("等待上个条码删除完成！")
            return
        }
        if (App.userCode.isNullOrEmpty()) {
            _showInfoDialog.value = "操作失败：登入超时，请退出重新登入!"
            return
        }
        if (storage.value.isNullOrEmpty()) {
            _showStorageDialog.value = true
            return
        }
        if (mUUID.isNullOrEmpty()) {
            generateUUID()
        }
        deleteJob = viewModelScope.launch {
            repo.partInDelete(mUUID!!, storage.value!!, code, App.userCode!!)
                .onStart { _loading.value = true }
                .catch { _loading.value = false }
                .onCompletion { _loading.value = false }
                .collectLatest { result ->
                    result.doSuccess {
                        adapter.deleteCode(code)
                        updateNumAndQty()
                    }
                    result.doFailure { _showInfoDialog.value = it }
                    result.doError { _showInfoDialog.value = it }
                }
        }
    }

    fun needSave(): Boolean {
        return adapter.list.isNotEmpty()
    }

    @Synchronized
    fun save() {
        if (mUUID.isNullOrEmpty()) {
            return
        }
        if (App.userCode.isNullOrEmpty()) {
            _showInfoDialog.value = "操作失败：登入超时，请退出重新登入!"
            return
        }
        saveJob = viewModelScope.launch {
            repo.partInSave(mUUID!!, App.userCode!!)
                .onStart { _loading.value = true }
                .catch { _loading.value = false }
                .onCompletion { _loading.value = false }
                .collectLatest { result ->
                    result.doSuccess {
                        showToast("保存成功")
                        reset()
                    }
                    result.doFailure { _showInfoDialog.value = it }
                    result.doError { _showInfoDialog.value = it }
                }
        }
    }

    /*@Synchronized
    fun submit(data: CodeData) {
        if (!validate()) {
            return
        }
        if (mUUID.isNullOrEmpty()) {
            generateUUID()
        }

        isSubmit = true

        submitJob = viewModelScope.launch {
            repo.partInSubmit(mUUID!!, storage.value!!, data.barCollectNo, "马荣")
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
                        adapter.addData(data)
                        updateNumAndQty()
                    }
                    result.doFailure { showToast(it) }
                    result.doError { showToast(it) }
                }
        }
    }*/

    private fun validate(): Boolean {
        if (submitJob != null && submitJob!!.isActive) {
            showToast("请等待上个条码提交完成！")
            return false
        }
        /*if (App.userCode.isNullOrEmpty()) {
            _showInfoDialog.value = "操作失败：登入超时，请退出重新登入!"
            return
        }*/
        if (storage.value.isNullOrEmpty()) {
            _showStorageDialog.value = true
            return false
        }
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
        mUUID = null
        code.value = ""
        storage.value = ""
        num.value = ""
        qty.value = ""
        adapter.clear()
    }
}
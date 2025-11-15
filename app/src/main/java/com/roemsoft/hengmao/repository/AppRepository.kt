package com.roemsoft.hengmao.repository

import com.roemsoft.hengmao.api.ApiService
import com.roemsoft.hengmao.api.HttpConfig
import com.roemsoft.hengmao.bean.*
import com.roemsoft.hengmao.exception.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.lang.Exception

class AppRepository(private val api: ApiService) {

    private val io = Dispatchers.IO

    fun login(username: String, password: String): Flow<RespResult<String>> {
        return flow {
            try {
                val reqStr = "${HttpConfig.REQ_STR_USER_NO}=$username;${HttpConfig.REQ_STR_USER_PW}=$password"
                val resp = api.login(reqStr = reqStr)
                if (resp.result == 1) {
                    emit(RespResult.Success(resp.msg))
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(io)
    }

    /**
     * 获取产品类别
     */
    fun fetchCategory(): Flow<RespResult<DataSet<Category>>> {
        return flow {
            try {
                val resp = api.fetchCategory()
                if (resp.result == 1) {
                    if (resp.dataSet != null) {
                        emit(RespResult.Success(resp.dataSet))
                    } else {
                        emit(RespResult.Error(ApiException("缺少必要的[DataSet]参数")))
                    }
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }


    /**
     * 获取纹路前缀
     */
    fun fetchWLQZ(): Flow<RespResult<DataSet<WLQZData>>> {
        return flow {
            try {
                val resp = api.fetchWLQZ()
                if (resp.result == 1) {
                    if (resp.dataSet != null) {
                        emit(RespResult.Success(resp.dataSet))
                    } else {
                        emit(RespResult.Error(ApiException("缺少必要的[DataSet]参数")))
                    }
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取纹路
     */
    fun fetchWL(): Flow<RespResult<DataSet<WLData>>> {
        return flow {
            try {
                val resp = api.fetchWL()
                if (resp.result == 1) {
                    if (resp.dataSet != null) {
                        emit(RespResult.Success(resp.dataSet))
                    } else {
                        emit(RespResult.Error(ApiException("缺少必要的[DataSet]参数")))
                    }
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取产品名称
     */
    fun fetchProduct(): Flow<RespResult<DataSet<Product>>> {
        return flow {
            try {
                val resp = api.fetchProduct()
                if (resp.result == 1) {
                    if (resp.dataSet != null) {
                        emit(RespResult.Success(resp.dataSet))
                    } else {
                        emit(RespResult.Error(ApiException("缺少必要的[DataSet]参数")))
                    }
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取规格
     */
    fun fetchSpec(): Flow<RespResult<DataSet<Spec>>> {
        return flow {
            try {
                val resp = api.fetchSpec()
                if (resp.result == 1) {
                    if (resp.dataSet != null) {
                        emit(RespResult.Success(resp.dataSet))
                    } else {
                        emit(RespResult.Error(ApiException("缺少必要的[DataSet]参数")))
                    }
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取颜色
     */
    fun fetchColor(): Flow<RespResult<DataSet<ColorData>>> {
        return flow {
            try {
                val resp = api.fetchColor()
                if (resp.result == 1) {
                    if (resp.dataSet != null) {
                        emit(RespResult.Success(resp.dataSet))
                    } else {
                        emit(RespResult.Error(ApiException("缺少必要的[DataSet]参数")))
                    }
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }


    /**
     * 获取仓库
     */
    fun fetchStorage(): Flow<RespResult<DataSet<Storage>>> {
        return flow {
            try {
                val resp = api.fetchStorage()
                if (resp.result == 1) {
                    if (resp.dataSet != null) {
                        emit(RespResult.Success(resp.dataSet))
                    } else {
                        emit(RespResult.Error(ApiException("缺少必要的[DataSet]参数")))
                    }
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取货位
     * ReqStr=StorageId=7EC44E205CE236BE0E6D80946B886388
     */
    fun fetchHw(storageId: String): Flow<RespResult<DataSet<HwData>>> {
        return flow {
            try {
                val reqStr = "${HttpConfig.REQ_STR_STORAGE_ID}=$storageId"
                val resp = api.fetchHw(reqStr = reqStr)
                if (resp.result == 1) {
                    if (resp.dataSet != null) {
                        emit(RespResult.Success(resp.dataSet))
                    } else {
                        emit(RespResult.Error(ApiException("缺少必要的[DataSet]参数")))
                    }
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取客户
     */
    fun fetchCustomer(): Flow<RespResult<DataSet<Customer>>> {
        return flow {
            try {
                val resp = api.fetchCustomer()
                if (resp.result == 1) {
                    if (resp.dataSet != null) {
                        emit(RespResult.Success(resp.dataSet))
                    } else {
                        emit(RespResult.Error(ApiException("缺少必要的[DataSet]参数")))
                    }
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 配件入库 删除
     * ReqStr=PDAId=;StorageName=MD仓;CodeNo=M25111132000001;UserName=马荣
     */
    fun partInDelete(pdaId: String, storageName: String, code: String, user: String): Flow<RespResult<Any?>> {
        return flow {
            try {
                val reqStr = "${HttpConfig.REQ_STR_PDA_ID}=$pdaId;" +
                        "${HttpConfig.REQ_STR_STORAGE_NAME}=$storageName;" +
                        "${HttpConfig.REQ_STR_CODE}=$code;" +
                        "${HttpConfig.REQ_STR_USER_NAME}=$user"
                val resp = api.partInDelete(reqStr= reqStr)
                if (resp.result == 1) {
                    emit(RespResult.Success(Unit))
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 配件入库 提交
     * ReqStr=PDAId=;StorageName=MD仓;CodeNo=M25111132000001;UserName=马荣
     */
    fun partInSubmit(pdaId: String, storage: String, code: String, user: String): Flow<RespResult<Any?>> {
        return flow {
            try {
                val reqStr = "${HttpConfig.REQ_STR_PDA_ID}=$pdaId;" +
                        "${HttpConfig.REQ_STR_STORAGE_NAME}=$storage;" +
                        "${HttpConfig.REQ_STR_CODE}=$code;" +
                        "${HttpConfig.REQ_STR_USER_NAME}=$user"
                val resp = api.partInSubmit(reqStr = reqStr)
                if (resp.result == 1) {
                    emit(RespResult.Success(Unit))
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 配件入库 保存
     * ReqStr=PDAId=;UserName=马荣
     */
    fun partInSave(pdaId: String, user: String): Flow<RespResult<Any?>> {
        return flow {
            try {
                val reqStr = "${HttpConfig.REQ_STR_PDA_ID}=$pdaId;" +
                        "${HttpConfig.REQ_STR_USER_NAME}=$user"
                val resp = api.partInSave(reqStr = reqStr)
                if (resp.result == 1) {
                    emit(RespResult.Success(Unit))
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取条码信息
     * ReqStr=CodeNo=M25111132000001
     */
    fun fetchCodeInfo(code: String): Flow<RespResult<DataSet<CodeData>>> {
        return flow {
            try {
                val reqStr =
                        "${HttpConfig.REQ_STR_CODE}=$code"
                val resp = api.fetchCodeInfo(reqStr = reqStr)
                if (resp.result == 1) {
                    if (resp.dataSet != null) {
                        emit(RespResult.Success(resp.dataSet))
                    } else {
                        emit(RespResult.Error(ApiException("缺少必要的[DataSet]参数")))
                    }
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    /**
     * 获取仓库档案
     */
    fun loadStorage(): Flow<RespResult<DataSet<Storage>>> {
        return flow {
            try {
                val resp = api.loadStorage()
                if (resp.result == 1) {
                    if (resp.dataSet != null) {
                        emit(RespResult.Success(resp.dataSet))
                    } else {
                        emit(RespResult.Error(ApiException("缺少必要的[DataSet]参数")))
                    }
                } else {
                    emit(RespResult.Failure(resp.msg))
                }
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }


    fun checkVersion(): Flow<RespResult<UpdateBean>> {
        return flow {
            try {
                val resp = api.getVersion()
                emit(RespResult.Success(resp))
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(io)
    }

    // 开始下载
    fun download(rangeStr: String): Flow<RespResult<ResponseBody>> {
        return flow {
            try {
                val range = "${HttpConfig.REQ_HEADER_RANGE}=$rangeStr"
                val resp = api.downloadApk(range)
                emit(RespResult.Success(resp))
            } catch (e: Exception) {
                emit(RespResult.Error(e))
            }
        }.flowOn(io)
    }
}
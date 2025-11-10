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

    fun login(username: String, password: String): Flow<RespResult<Any>> {
        return flow {
            try {
                val reqStr = "${HttpConfig.REQ_STR_USER_NO}=$username;${HttpConfig.REQ_STR_USER_PW}=$password"
                val resp = api.login(reqStr = reqStr)
                if (resp.result == 1) {
                    emit(RespResult.Success(Unit))
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
     * 产品建档 提交
     * ReqStr=ClassId=;ClassNo=;WLQZName=;WLNo=;WLName=;ProductName=;
     * ItemName=;SpecName=;ColorName=;UnitName=;Maker=
     */
    fun submitCpjd(categoryId: String, categoryNo: String, wlqz: String, wlNo: String, wlName: String, product: String,
                           name: String, spec: String, color: String, unit: String, maker: String): Flow<RespResult<Any?>> {
        return flow {
            try {
                val reqStr = "${HttpConfig.REQ_STR_CATEGORY_ID}=$categoryId;" +
                        "${HttpConfig.REQ_STR_CATEGORY_NO}=$categoryNo;" +
                        "${HttpConfig.REQ_STR_WLQZ}=$wlqz;" +
                        "${HttpConfig.REQ_STR_WL_NO}=$wlNo;" +
                        "${HttpConfig.REQ_STR_WL_NAME}=$wlName;" +
                        "${HttpConfig.REQ_STR_PRODUCT_NAME}=$product;" +
                        "${HttpConfig.REQ_STR_NAME}=$name;" +
                        "${HttpConfig.REQ_STR_SPEC}=$spec;" +
                        "${HttpConfig.REQ_STR_COLOR}=$color;" +
                        "${HttpConfig.REQ_STR_UNIT}=$unit;" +
                        "${HttpConfig.REQ_STR_MAKER}=$maker"
                val resp = api.submitCpjd(reqStr = reqStr)
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
     * 生成存货档案跟条码
     * ReqStr=ClassId=;ClassNo=;WLQZName=;WLNo=;WLName=;ProductName=;
     * ItemName=;SpecName=;ColorName=;UnitName=;CodeNo=;CodeQty=;Maker=
     */
    fun fetchRkCode(categoryId: String, categoryNo: String, wlqz: String, wlNo: String, wlName: String, product: String,
                    name: String, spec: String, color: String, unit: String, code: String, qty: String, maker: String): Flow<RespResult<DataSet<CodeData>>> {
        return flow {
            try {
                val reqStr = "${HttpConfig.REQ_STR_CATEGORY_ID}=$categoryId;" +
                        "${HttpConfig.REQ_STR_CATEGORY_NO}=$categoryNo;" +
                        "${HttpConfig.REQ_STR_WLQZ}=$wlqz;" +
                        "${HttpConfig.REQ_STR_WL_NO}=$wlNo;" +
                        "${HttpConfig.REQ_STR_WL_NAME}=$wlName;" +
                        "${HttpConfig.REQ_STR_PRODUCT_NAME}=$product;" +
                        "${HttpConfig.REQ_STR_NAME}=$name;" +
                        "${HttpConfig.REQ_STR_SPEC}=$spec;" +
                        "${HttpConfig.REQ_STR_COLOR}=$color;" +
                        "${HttpConfig.REQ_STR_UNIT}=$unit;" +
                        "${HttpConfig.REQ_STR_CODE}=$code;" +
                        "${HttpConfig.REQ_STR_QTY}=$qty;" +
                        "${HttpConfig.REQ_STR_MAKER}=$maker"
                val resp = api.fetchRkCode(reqStr= reqStr)
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
     * 入库 提交
     * ReqStr=StorageId=;HWNo=;CustomerName=;Label=;SeasonName=季度;
     * CustShoeNo=;CodeNo=QI202508270006;CodeQty=32;Maker=administrator
     */
    fun submitRk(storageId: String, hwNo: String, customer: String, brand: String, session: String,
                 model: String, code: String, qty: String, jm: String, maker: String): Flow<RespResult<Any?>> {
        return flow {
            try {
                val reqStr = "${HttpConfig.REQ_STR_STORAGE_ID}=$storageId;" +
                        "${HttpConfig.REQ_STR_HW_NO}=$hwNo;" +
                        "${HttpConfig.REQ_STR_CUSTOMER}=$customer;" +
                        "${HttpConfig.REQ_STR_BRAND}=$brand;" +
                        "${HttpConfig.REQ_STR_SEASON}=$session;" +
                        "${HttpConfig.REQ_STR_MODEL}=$model;" +
                        "${HttpConfig.REQ_STR_CODE}=$code;" +
                        "${HttpConfig.REQ_STR_QTY}=$qty;" +
                        "${HttpConfig.REQ_STR_JM}=$jm;" +
                        "${HttpConfig.REQ_STR_MAKER}=$maker"
                val resp = api.submitRk(reqStr = reqStr)
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
     * 盘点入库获取条码信息
     * ReqStr=CodeNo=202509020001
     */
    fun fetchCodeInfo(code: String): Flow<RespResult<DataSet<ItemData>>> {
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
     * 获取存货档案
     * ReqStr=ItemNo=;ItemName=;SpecName=;ColorName=
     */
    fun fetchItem(no: String, name: String, spec: String, color: String): Flow<RespResult<DataSet<ItemData>>> {
        return flow {
            try {
                val reqStr = "${HttpConfig.REQ_STR_ITEM_NO}=$no;" +
                        "${HttpConfig.REQ_STR_NAME}=$name;" +
                        "${HttpConfig.REQ_STR_SPEC}=$spec;" +
                        "${HttpConfig.REQ_STR_COLOR}=$color"
                val resp = api.fetchItem(reqStr = reqStr)
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
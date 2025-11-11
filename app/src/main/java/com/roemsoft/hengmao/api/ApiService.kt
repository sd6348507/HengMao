package com.roemsoft.hengmao.api

import com.roemsoft.hengmao.bean.*
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {

    // 添加 suspend 可以不使用 Call
    // Retrofit2.6.0 支持接口suspend函数配合协程使用
    // 原理是解析判断是否为挂起函数
    // 如果是挂起函数 则使用协程CallAdapted 源码中使用 suspendCancellableCoroutine<> {  } 方法 当协程取消，Call也会取消请求
    // 因此，当接口为挂起函数时，协程被取消，请求也会取消
    // suspendCancellableCoroutine<> {  }  不是创建新的携程而是主动挂起当前携程 执行方块体内容 最后方块体调用resume恢复携程
    // 链接 https://www.jianshu.com/p/c9f123c21d82

    // 登录
    // ReqType=AppUserLogin&ReqStr=UserNo=administrator;UserPwd=roemsoft..,..
    @FormUrlEncoded
    @POST(HttpConfig.URL_PATH)
    suspend fun login(
        @Field(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_LOGIN,
        @Field(HttpConfig.REQ_STR) reqStr: String
    ): HttpResult<DataSet<User>>


    // 获取产品类别
    // ReqType=AppGetClass
    @GET(HttpConfig.URL_PATH)
    suspend fun fetchCategory(
        @Query(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_CATEGORY_SEARCH
    ): HttpResult<DataSet<Category>>

    // 获取纹路前缀
    // ReqType=AppGetWLQZ
    @GET(HttpConfig.URL_PATH)
    suspend fun fetchWLQZ(
        @Query(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_WLQZ_SEARCH
    ): HttpResult<DataSet<WLQZData>>

    // 获取纹路名称
    // ReqType=AppGetWL
    @GET(HttpConfig.URL_PATH)
    suspend fun fetchWL(
        @Query(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_WL_SEARCH
    ): HttpResult<DataSet<WLData>>

    // 获取产品信息
    // ReqType=AppGetProduct
    @GET(HttpConfig.URL_PATH)
    suspend fun fetchProduct(
        @Query(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_PRODUCT_SEARCH
    ): HttpResult<DataSet<Product>>

    // 获取规格
    // ReqType=AppGetSpec
    @GET(HttpConfig.URL_PATH)
    suspend fun fetchSpec(
        @Query(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_SPEC_SEARCH
    ): HttpResult<DataSet<Spec>>

    // 获取颜色
    // ReqType=AppGetColor
    @FormUrlEncoded
    @POST(HttpConfig.URL_PATH)
    suspend fun fetchColor(
        @Field(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_COLOR_SEARCH
    ): HttpResult<DataSet<ColorData>>

    // 产品建档 提交
    // ReqType=AppBuildItem&ReqStr=ClassId=;ClassNo=;WLQZName=;WLNo=;
    // WLName=;ProductName=;ItemName=;SpecName=;ColorName=;UnitName=;Maker=
    @FormUrlEncoded
    @POST(HttpConfig.URL_PATH)
    suspend fun submitCpjd(
        @Field(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_CPJD_SUBMIT,
        @Field(HttpConfig.REQ_STR) reqStr: String
    ): HttpResult<Any?>

    // 获取仓库
    // ReqType=AppGetStorage
    @GET(HttpConfig.URL_PATH)
    suspend fun fetchStorage(
        @Query(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_STORAGE_SEARCH
    ): HttpResult<DataSet<Storage>>

    // 获取货位
    // ReqType=AppGetPostion&ReqStr=StorageId=7EC44E205CE236BE0E6D80946B886388
    @GET(HttpConfig.URL_PATH)
    suspend fun fetchHw(
        @Query(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_HW_SEARCH,
        @Query(HttpConfig.REQ_STR) reqStr: String
    ): HttpResult<DataSet<HwData>>

    // 获取客户
    // ReqType=AppGetCust
    @GET(HttpConfig.URL_PATH)
    suspend fun fetchCustomer(
        @Query(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_CUSTOMER_SEARCH
    ): HttpResult<DataSet<Customer>>


    // 生成存货档案跟条码
    // ReqType=APPBuildItemCode&ReqStr=ClassId=;ClassNo=;WLQZName=;WLNo=;
    //     WLName=;ProductName=;SpecName=;ColorName=;UnitName=;CodeNo=;CodeQty=;Maker=
    @FormUrlEncoded
    @POST(HttpConfig.URL_PATH)
    suspend fun fetchRkCode(
        @Field(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_RK_CODE_SEARCH,
        @Field(HttpConfig.REQ_STR) reqStr: String
    ): HttpResult<DataSet<CodeData>>

    // 入库 提交
    // ReqType=APPStgPostfromQT&ReqStr=StorageId=;HWNo=;Label=;SeasonName=;CustShoeNo=;CodeNo=;Qty=;Maker=
    @FormUrlEncoded
    @POST(HttpConfig.URL_PATH)
    suspend fun submitRk(
        @Field(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_RK_SUBMIT,
        @Field(HttpConfig.REQ_STR) reqStr: String
    ): HttpResult<Any?>

    // 获取条码信息
    // ReqType=CodeList&ReqStr=CodeNo=M25111132000001
    @FormUrlEncoded
    @POST(HttpConfig.URL_PATH)
    suspend fun fetchCodeInfo(
        @Field(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_CODE_SEARCH,
        @Field(HttpConfig.REQ_STR) reqStr: String
    ): HttpResult<DataSet<CodeData>>


    // 获取仓库档案
    // ReqType=StorageList
    @FormUrlEncoded
    @POST(HttpConfig.URL_PATH)
    suspend fun loadStorage(
        @Field(HttpConfig.REQ_TYPE) reqType: String = HttpConfig.REQ_TYPE_STORAGE_SEARCH
    ): HttpResult<DataSet<Storage>>

    /**-------------- 更新 --------------*/

    /**
     * 获取APP版本号
     * version_name,version_code
     */
    @GET("/ajax/${HttpConfig.VERSION_URL}")
    suspend fun getVersion(): UpdateBean

    /**
     * 下载
     */
    @Streaming
    @GET("/ajax/${HttpConfig.DOWNLOAD_URL}")
    suspend fun downloadApk(@Header("Range") range: String): ResponseBody
}
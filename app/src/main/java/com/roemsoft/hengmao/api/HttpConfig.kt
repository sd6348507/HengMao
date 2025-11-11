package com.roemsoft.hengmao.api

class HttpConfig {

    companion object {

        const val BASE_URL = "http://59.60.18.154:8762/"

        const val URL_PATH = "ajax/ajaxDataServer.aspx"

        const val VERSION_URL = "PDAedition.aspx"

        const val DOWNLOAD_URL = "ZDPD.APK"

        const val DEFAULT_TIME_OUT = 20L  // 单位：秒

        const val REQ_TYPE = "ReqType"
        const val REQ_STR = "ReqStr"

        var hasNetwork: Boolean = false

        /********************** REQ_TYPE *********************/

        const val REQ_HEADER_RANGE = "bytes"

        // 登录
        const val REQ_TYPE_LOGIN = "AppUserLogin"

        // 产品类别 查询
        const val REQ_TYPE_CATEGORY_SEARCH = "AppGetClass"

        // 纹路前缀 查询
        const val REQ_TYPE_WLQZ_SEARCH = "AppGetWLQZ"

        // 纹路名称 查询
        const val REQ_TYPE_WL_SEARCH = "AppGetWL"

        // 产品信息 查询
        const val REQ_TYPE_PRODUCT_SEARCH = "AppGetProduct"

        // 规格 查询
        const val REQ_TYPE_SPEC_SEARCH = "AppGetSpec"

        // 颜色 查询
        const val REQ_TYPE_COLOR_SEARCH = "AppGetColor"

        // 产品建档 提交
        const val REQ_TYPE_CPJD_SUBMIT = "AppBuildItem"

        // 仓库 查询
        const val REQ_TYPE_STORAGE_SEARCH = "StorageList"

        // 货位 查询
        const val REQ_TYPE_HW_SEARCH = "AppGetPostion"

        // 客户 查询
        const val REQ_TYPE_CUSTOMER_SEARCH = "AppGetCust"

        // 存货档案 查询
        const val REQ_TYPE_ITEM_SEARCH = "Item"

        // 生成存货档案跟条码
        const val REQ_TYPE_RK_CODE_SEARCH = "APPBuildItemCode"

        // 入库 提交
        const val REQ_TYPE_RK_SUBMIT = "APPStgPostfromQT"

        // 销售出库单 提交
        const val REQ_TYPE_XSCK_SUBMIT = "SMOut"

        // 条码查询
        const val REQ_TYPE_CODE_SEARCH = "CodeList"



        /********************** REQ_STR *********************/

        // 用户名
        const val REQ_STR_USER_NO = "UserNo"
        // 用户密码
        const val REQ_STR_USER_PW = "UserPwd"

        // 产品类别Id
        const val REQ_STR_CATEGORY_ID = "ClassId"
        // 产品类别编码
        const val REQ_STR_CATEGORY_NO = "ClassNo"
        // 纹路前缀
        const val REQ_STR_WLQZ = "WLQZName"
        // 纹路编码
        const val REQ_STR_WL_NO = "WLNo"
        // 纹路名称
        const val REQ_STR_WL_NAME = "WLName"
        // 产品名称
        const val REQ_STR_PRODUCT_NAME = "ProductName"
        // 存货编码
        const val REQ_STR_ITEM_NO = "ItemNo"
        // 存货名称
        const val REQ_STR_NAME = "ItemName"
        // 规格
        const val REQ_STR_SPEC = "SpecName"
        // 颜色
        const val REQ_STR_COLOR = "ColorName"
        // 单位
        const val REQ_STR_UNIT = "UnitName"

        //条码
        const val REQ_STR_CODE = "CodeNo"

        //仓库ID
        const val REQ_STR_STORAGE_ID = "StorageId"

        const val REQ_STR_MAKER = "Maker"

        // 货位编号
        const val REQ_STR_HW_NO = "HWNo"
        // 客户
        const val REQ_STR_CUSTOMER = "CustomerName"

        // 品牌
        const val REQ_STR_BRAND = "Label"
        // 季度
        const val REQ_STR_SEASON = "SeasonName"
        // 型体
        const val REQ_STR_MODEL = "CustShoeNo"
        // 数量
        const val REQ_STR_QTY = "CodeQty"
        // 胶膜
        const val REQ_STR_JM = "JMRemark"

    }

}
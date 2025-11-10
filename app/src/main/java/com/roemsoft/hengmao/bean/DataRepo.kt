package com.roemsoft.hengmao.bean

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json


/**
 * {
 *      Result:1,
 *      Msg:'获取成功。',
 *      DataSet:{}
 * }
 */
data class HttpResult<T>(
    @Json(name = "Result") val result: Int,
    @Json(name = "Msg") val msg: String,
    @Json(name = "DataSet") val dataSet: T? = null
)

/**
 * 数据集
 * {
 *      totalCount:8,
 *      fields:'CustomerNo,CustomerName',
 *      data:[]
 * }
 */
data class DataSet<T>(
    @Json(name = "totalCount") val totalCount: Int = 0,
    @Json(name = "fields") val fields: String = "",
    @Json(name = "data") val data: List<T>
)

/**
 *  {
 *         "Maker":"彭靖惠"
 *  }
 */
data class User(
    @Json(name = "Maker") val name: String = ""
)

/**
 * 产品类别
 * {
 *      "ClassId":"BBE088C8C8DF63CA0B5147A5B472D0F3",
 *      "ClassNo":"A",
 *      "ClassName":"原材料"
 * }
 */
data class Category(
    @Json(name = "ClassId") val id: String = "",
    @Json(name = "ClassNo") val no: String = "",
    @Json(name = "ClassName") val name: String = ""
) : Parcelable {

    val nameStr: String
        get() = "$name ($no)"

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(no)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "$id:$no:$name"
    }

}

/**
 * 纹路前缀
 * {
 *  "WLQZName":"ZD-"
 * }
 */
data class WLQZData(
    @Json(name = "WLQZName") val name: String
)

/**
 * 纹路名称
 * {
 *  "WLNo":"0004",
 *  "WLName":"UM34纹"
 * }
 */
data class WLData(
    @Json(name = "WLNo") val no: String,
    @Json(name = "WLName") var name: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(no)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WLData> {
        override fun createFromParcel(parcel: Parcel): WLData {
            return WLData(parcel)
        }

        override fun newArray(size: Int): Array<WLData?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "$no:$name"
    }

}

/**
 * 产品名称
 * {
 *  "ProductName":"TPU高低温膜 珠光"
 * }
 */
data class Product(
    @Json(name = "ProductName") val name: String = ""
)

/**
 * 规格
 * {
 *  "SpecName":"0.1MM*137CM"
 * }
 */
data class Spec(
    @Json(name = "SpecName") val name: String = ""
)

/**
 * 颜色
 * {
 *  "ColorName":"深航舰蓝42215"
 * }
 */
data class ColorData(
    @Json(name = "ColorName") val name: String = ""
)

/**
 * 仓库
 * {
 *      "StorageId":"7EC44E205CE236BE0E6D80946B886388",
 *      "StorageName":"次品仓"
 * }
 */
data class Storage(
    @Json(name = "StorageId") val id: String = "",
    @Json(name = "StorageName") val name: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Storage> {
        override fun createFromParcel(parcel: Parcel): Storage {
            return Storage(parcel)
        }

        override fun newArray(size: Int): Array<Storage?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "$id:$name"
    }

}

/**
 * 货位
 * {
 *      "HWNo":"B01",
 *      "HWName":"二楼A区"
 * }
 */
data class HwData(
    @Json(name = "HWNo") val no: String = "",
    @Json(name = "HWName") val name: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(no)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HwData> {
        override fun createFromParcel(parcel: Parcel): HwData {
            return HwData(parcel)
        }

        override fun newArray(size: Int): Array<HwData?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "$no:$name"
    }

}

data class PrinterData(
    val name: String,
    val customer: String,
    val model: String,
    val brand: String,
    val season: String,
    val spec: String,
    val color: String,
    val unit: String,
    val qty: String,
    val code: String,
    val date: String,
    val jm: String
) {
    val dateStr: String
        get() = "日期:$date"

    val specStr: String
        get() {
            return if (jm.isEmpty()) {
                spec
            } else {
                "$spec ($jm)"
            }
        }

    val seasonStr: String
        get() = "$brand$season"

    val qtyStr: String
        get() = "$qty$unit"
}

/**
 * 客户
 * {
 *      "CustomerName":"安徽亳州喜宝鞋服有限公司"
 * }
 */
data class Customer(
    @Json(name = "CustomerName") var name: String = ""
)

data class CkData(
    val name: String = "",
    val spec: String = "",
    val color: String = "",
    val unit: String = "",
    val qty: String = ""
) {
    var code: String = ""

    fun coverToCkListData(): CkListData {
        val data = CkListData(name, spec, color, unit)
        data.list.add(Pair(code, qty))
        return data
    }

    override fun toString(): String {
        return "$name:$spec:$color:$unit"
    }
}

/**
 * 出库列表适配数据
 */
data class CkListData(
    val name: String = "",
    val spec: String = "",
    val color: String = "",
    val unit: String = "",
    val list: ArrayList<Pair<String, String>> = ArrayList() // 添加条码和数量
) {
    override fun toString(): String {
        return "$name:$spec:$color:$unit"
    }
}

/**
 * 存货
 * {
 *      "ItemId":"{005345B376094E71B222717BB060499F}",
 *      "ItemNo":"C110001000108",
 *      "ItemName":"ZD-init UMT纹TPU高低温膜(0.2MM TPE胶膜)黑胶 珠光",
 *      "ColorName":"黑色11302",
 *      "SpecName":"0.7MM*135CM",
 *      "UnitName":"Y",
 *      "ItemType":"0",
 *      "MItemId":"{005345B376094E71B222717BB060499F}"
 * }
 */
data class ItemData(
    @Json(name = "ItemId") var id: String = "",
    @Json(name = "ItemNo") val no: String = "",
    @Json(name = "ItemName") var name: String = "",
    @Json(name = "ColorName") var color: String = "",
    @Json(name = "SpecName") var spec: String = "",
    @Json(name = "UnitName") var unit: String = "",
    @Json(name = "ItemType") var type: String = "",
    @Json(name = "MItemId") var mItemId: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(no)
        parcel.writeString(name)
        parcel.writeString(color)
        parcel.writeString(spec)
        parcel.writeString(unit)
        parcel.writeString(type)
        parcel.writeString(mItemId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemData> {
        override fun createFromParcel(parcel: Parcel): ItemData {
            return ItemData(parcel)
        }

        override fun newArray(size: Int): Array<ItemData?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "$id:$no:$name:$color:$spec:$unit:$type:$mItemId"
    }

}

/**
 * 入库条码
 * {
 *      "CodeNo":"QI202508270004"
 * }
 */
data class CodeData(
    @Json(name = "CodeNo") var code: String = ""
)

/**
 * 销售出库单 明细
 * {
 *      "SrcDetailId":"",
 *      "CustOrderNo":"AT25246-B1",
 *      "CustPurchaseNo":"P25030001",
 *      "ItemNo":"ZP02355",
 *      "ItemName":"AT25Q1ZD1085加厚双色中间插条织带(T)(定型)",
 *      "SpecName":"25mm",
 *      "ColorName":"黑",
 *      "Length":"",
 *      "OQty":"4.0000",
 *      "Qty":""
 * }
 */
data class XsOrderDetailData(
    @Json(name = "SrcDetailId") val id: String = "",
    @Json(name = "CustOrderNo") val cOrderNo: String = "",          // 客户订单号
    @Json(name = "CustPurchaseNo") val cPurchaseNo: String = "",    // 客户采购单号
    @Json(name = "ItemNo") val itemNo: String = "",
    @Json(name = "ItemName") val item: String = "",
    @Json(name = "SpecName") val spec: String = "",
    @Json(name = "ColorName") val color: String = "",
    @Json(name = "Length") val length: String = "",
    @Json(name = "UnitName") val unit: String = "",
    @Json(name = "OQty") val oQty: String = "",                      // 通知数
    @Json(name = "Qty") var rQty: String = "0",                      // 实发数
    var qty: String = "0"
) {
    val cOrderNoStr: String
        get() = "客户订单号: $cOrderNo"

    val cPurchaseNoStr: String
        get() = "客户采购单号: $cPurchaseNo"

    val itemStr: String
        get() = "品名: $item"

    val specStr: String
        get() = "规格: $spec"

    val colorStr: String
        get() = "颜色: $color"

    val lengthStr: String
        get() = "长度: $length"

    val unitStr: String
        get() = "单位: $unit"

    val oQtyStr: String
        get() = "通知数: $oQty"
}


data class  UpdateBean(
    var code: Int = 0,
    var name: String = ""
)
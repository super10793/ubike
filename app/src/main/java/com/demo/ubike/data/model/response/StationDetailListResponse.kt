package com.demo.ubike.data.model.response

import com.google.gson.annotations.SerializedName

class StationDetailListResponse : ArrayList<StationDetailListResponse.Data>() {
    /**
     * @property stationUid 站點唯一識別代碼
     * @property stationId 站點代碼
     * @property serviceStatus 服務狀態，參考[com.demo.ubike.data.model.ServiceStatus]
     * @property serviceType 服務類型，參考[com.demo.ubike.data.model.ServiceType]
     * @property availableRentBikes 可租借車數
     * @property availableReturnBikes 可歸還車數
     * @property availableRentBikesDetail 可租借一般自行車、電動輔助車數量
     * @property srcUpdateTime 來源端平台資料更新時間(ISO8601格式:yyyy-MM-ddTHH:mm:sszzz)
     * @property updateTime 資料更新日期時間(ISO8601格式:yyyy-MM-ddTHH:mm:sszzz)
     * */
    data class Data(
        @SerializedName("StationUID")
        val stationUid: String,
        @SerializedName("StationID")
        val stationId: String,
        @SerializedName("ServiceStatus")
        val serviceStatus: Int,
        @SerializedName("ServiceType")
        val serviceType: Int,
        @SerializedName("AvailableRentBikes")
        val availableRentBikes: Int,
        @SerializedName("AvailableReturnBikes")
        val availableReturnBikes: Int,
        @SerializedName("AvailableRentBikesDetail")
        val availableRentBikesDetail: AvailableRentBikesDetail,
        @SerializedName("SrcUpdateTime")
        val srcUpdateTime: String,
        @SerializedName("UpdateTime")
        val updateTime: String,
    )

    /**
     * @property generalBikes：一般自行車可租借車數
     * @property electricBikes：電動輔助車可租借車數
     */
    data class AvailableRentBikesDetail(
        @SerializedName("GeneralBikes")
        val generalBikes: Int,
        @SerializedName("ElectricBikes")
        val electricBikes: Int
    )
}

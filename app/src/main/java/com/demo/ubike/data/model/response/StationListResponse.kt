package com.demo.ubike.data.model.response

import com.google.gson.annotations.SerializedName

class StationListResponse : ArrayList<StationListResponse.Data>() {
    /**
     * @property stationUid 站點唯一識別代碼
     * @property stationId 站點代碼
     * @property authorityId 業管單位代碼
     * @property stationName 站點名稱
     * @property stationPosition 站點位置
     * @property stationAddress 站點地址
     * @property bikesCapacity 可容納之自行車總數
     * @property serviceType 服務類型，參考[com.demo.ubike.data.model.ServiceType]
     * */
    data class Data(
        @SerializedName("StationUID")
        val stationUid: String,
        @SerializedName("StationID")
        val stationId: String,
        @SerializedName("AuthorityID")
        val authorityId: String,
        @SerializedName("BikesCapacity")
        val bikesCapacity: Int? = 0,
        @SerializedName("ServiceType")
        val serviceType: Int,
        @SerializedName("StationPosition")
        val stationPosition: StationPosition,
        @SerializedName("StationName")
        val stationName: StationName,
        @SerializedName("StationAddress")
        val stationAddress: StationAddress
    )

    /**
     * @property positionLon：位置經度(WGS84)
     * @property positionLat 位置緯度(WGS84)
     * */
    data class StationPosition(
        @SerializedName("PositionLon")
        val positionLon: Double,
        @SerializedName("PositionLat")
        val positionLat: Double
    )

    /**
     * @property zhTw：中文繁體名稱
     * @property en 英文名稱
     * */
    data class StationName(
        @SerializedName("Zh_tw")
        val zhTw: String,
        @SerializedName("En")
        val en: String? = ""
    )

    /**
     * @property zhTw：中文繁體名稱
     * @property en 英文名稱
     * */
    data class StationAddress(
        @SerializedName("Zh_tw")
        val zhTw: String? = "",
        @SerializedName("En")
        val en: String? = ""
    )
}
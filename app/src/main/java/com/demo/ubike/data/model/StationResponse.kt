package com.demo.ubike.data.model

import com.google.gson.annotations.SerializedName

class StationResponse : ArrayList<StationResponse.Data>() {
    /**
     * @property stationUID 站點唯一識別代碼
     * @property stationID 站點代碼
     * @property authorityID 業管單位代碼
     * @property stationName 站點名稱
     * @property stationPosition 站點位置
     * @property stationAddress 站點地址
     * @property bikesCapacity 可容納之自行車總數
     * @property serviceType 服務類型
     * */
    data class Data(
        @SerializedName("StationUID")
        val stationUID: String,
        @SerializedName("StationID")
        val stationID: String,
        @SerializedName("AuthorityID")
        val authorityID: String,
        @SerializedName("BikesCapacity")
        val bikesCapacity: Int,
        @SerializedName("ServiceType")
        private val serviceType: Int,
        @SerializedName("StationPosition")
        val stationPosition: StationPosition,
        @SerializedName("StationName")
        val stationName: StationName,
        @SerializedName("StationAddress")
        val stationAddress: StationAddress
    ) {
        fun getServiceType(): ServiceType {
            return when (serviceType) {
                ServiceType.UBike1_0.key -> ServiceType.UBike1_0
                ServiceType.UBike2_0.key -> ServiceType.UBike2_0
                ServiceType.TBike.key -> ServiceType.TBike
                ServiceType.PBike.key -> ServiceType.PBike
                ServiceType.KBike.key -> ServiceType.KBike
                else -> ServiceType.Unknown
            }
        }
    }

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
     * @property zh_tw：中文繁體名稱
     * @property en 英文名稱
     * */
    data class StationName(
        @SerializedName("Zh_tw")
        val zh_tw: String,
        @SerializedName("En")
        val en: String
    )

    /**
     * @property zh_tw：中文繁體名稱
     * @property en 英文名稱
     * */
    data class StationAddress(
        @SerializedName("Zh_tw")
        val zh_tw: String,
        @SerializedName("En")
        val en: String
    )
}

package com.demo.ubike.data.mapper

import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.ServiceType
import com.demo.ubike.data.model.vo.StationVO
import javax.inject.Inject

class StationEntityMapper @Inject constructor() : Mapper<StationEntity, StationVO>() {

    override fun map(from: StationEntity): StationVO {
        return StationVO(
            stationUid = from.stationUid,
            stationId = from.stationId,
            city = from.city,
            serviceType = ServiceType.fromValue(from.serviceType),
            positionLon = from.positionLon,
            positionLat = from.positionLat,
            stationNameZhTw = from.stationNameZhTw,
            stationNameEn = from.stationNameEn,
            stationAddressZhTw = from.stationAddressZhTw,
            stationAddressEn = from.stationAddressEn
        )
    }
}
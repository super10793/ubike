package com.demo.ubike.data.mapper

import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.response.StationListResponse
import javax.inject.Inject

class StationDataResponseMapper @Inject constructor() :
    Mapper<StationListResponse.Data, StationEntity>() {

    override fun map(from: StationListResponse.Data): StationEntity {
        return StationEntity(
            stationUid = from.stationUid,
            stationId = from.stationId,
            authorityId = from.authorityId,
            city = City.fromValue(from.authorityId),
            bikesCapacity = from.bikesCapacity ?: 0,
            serviceType = from.serviceType,
            positionLon = from.stationPosition.positionLon,
            positionLat = from.stationPosition.positionLat,
            stationNameZhTw = from.stationName.zhTw,
            stationNameEn = from.stationName.en.orEmpty(),
            stationAddressZhTw = from.stationAddress.zhTw.orEmpty(),
            stationAddressEn = from.stationAddress.en.orEmpty()
        )
    }
}
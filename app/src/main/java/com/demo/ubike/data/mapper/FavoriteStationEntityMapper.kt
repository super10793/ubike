package com.demo.ubike.data.mapper

import com.demo.ubike.data.local.favorite.FavoriteStation
import com.demo.ubike.data.model.ServiceType
import com.demo.ubike.data.model.vo.FavoriteStationVO
import javax.inject.Inject

class FavoriteStationEntityMapper @Inject constructor() :
    Mapper<List<FavoriteStation>, List<FavoriteStationVO>>() {

    override fun map(from: List<FavoriteStation>): List<FavoriteStationVO> {

        return from.mapNotNull {
            if (it.station == null) return@mapNotNull null

            FavoriteStationVO(
                stationUid = it.favorite.stationUid,
                stationId = it.station.stationId,
                authorityId = it.station.authorityId,
                city = it.station.city,
                serviceType = ServiceType.fromValue(it.station.serviceType),
                positionLon = it.station.positionLon,
                positionLat = it.station.positionLat,
                stationNameZhTw = it.station.stationNameZhTw,
                stationNameEn = it.station.stationNameEn,
                stationAddressZhTw = it.station.stationAddressZhTw,
                stationAddressEn = it.station.stationAddressEn,
                stationDetailVO = null
            )
        }
    }
}
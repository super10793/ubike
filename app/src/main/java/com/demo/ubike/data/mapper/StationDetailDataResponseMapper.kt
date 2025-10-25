package com.demo.ubike.data.mapper

import com.demo.ubike.data.model.ServiceStatus
import com.demo.ubike.data.model.ServiceType
import com.demo.ubike.data.model.response.StationDetailListResponse
import com.demo.ubike.data.model.vo.StationDetailVO
import javax.inject.Inject

class StationDetailDataResponseMapper @Inject constructor() :
    Mapper<StationDetailListResponse.Data, StationDetailVO>() {

    override fun map(from: StationDetailListResponse.Data): StationDetailVO {
        return StationDetailVO(
            stationUid = from.stationUid,
            stationId = from.stationId,
            serviceStatus = ServiceStatus.fromValue(from.serviceStatus),
            serviceType = ServiceType.fromValue(from.serviceType),
            canRentBikes = from.availableRentBikes,
            canRentGeneralBikes = from.availableRentBikesDetail.generalBikes,
            canRentElectricBikes = from.availableRentBikesDetail.electricBikes,
            canReturnBikes = from.availableReturnBikes,
            srcUpdateTime = from.srcUpdateTime,
            updateTime = from.updateTime,
        )
    }
}
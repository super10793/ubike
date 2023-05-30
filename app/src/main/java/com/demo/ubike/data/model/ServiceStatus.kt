package com.demo.ubike.data.model

/**
 * @property ServiceStatus 服務類型 :
 * @param 0:'停止營運'
 * @param 1:'正常營運'
 * @param 2:'暫停營運'
 * @param 99:'unknown'
 * */
enum class ServiceStatus(val key: Int) {
    Stop(0),
    Normal(1),
    Pause(2),
    Unknown(99);
}

fun getServiceStatusByKey(key: Int): ServiceStatus {
    return when (key) {
        0 -> ServiceStatus.Stop
        1 -> ServiceStatus.Normal
        2 -> ServiceStatus.Pause
        else -> ServiceStatus.Unknown
    }
}

fun getKeyByServiceStatus(status: ServiceStatus): Int {
    return when (status) {
        ServiceStatus.Stop -> ServiceStatus.Stop.key
        ServiceStatus.Normal -> ServiceStatus.Normal.key
        ServiceStatus.Pause -> ServiceStatus.Pause.key
        else -> ServiceStatus.Unknown.key
    }
}
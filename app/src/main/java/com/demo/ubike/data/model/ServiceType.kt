package com.demo.ubike.data.model

/**
 * @property ServiceType 服務類型 :
 * @param 1:'YouBike1.0'
 * @param 2:'YouBike2.0'
 * @param 3:'T-Bike'
 * @param 4:'P-Bike'
 * @param 5:'K-Bike'
 * @param 99:'unknown'
 * */
enum class ServiceType(val value: Int) {
    UBike1_0(1),
    UBike2_0(2),
    TBike(3),
    PBike(4),
    KBike(5),
    Unknown(99);

    companion object {

        fun fromValue(value: Int): ServiceType {
            return entries.find { it.value == value } ?: Unknown
        }
    }
}
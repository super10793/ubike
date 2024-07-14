package com.demo.ubike

object Config {
    const val BASE_URL = "https://tdx.transportdata.tw/"
    const val GRANT_TYPE = "client_credentials"

    // client ids & client secrets
    const val CLIENT_ID_1 = BuildConfig.API_CLIENT_ID_1
    const val CLIENT_SECRET_1 = BuildConfig.API_CLIENT_SECRET_1
    const val CLIENT_ID_2 = BuildConfig.API_CLIENT_ID_2
    const val CLIENT_SECRET_2 = BuildConfig.API_CLIENT_SECRET_2
    const val CLIENT_ID_3 = BuildConfig.API_CLIENT_ID_3
    const val CLIENT_SECRET_3 = BuildConfig.API_CLIENT_SECRET_3

    const val API_FORMAT = "JSON"
    const val API_STATION_URL = "api/basic/v2/Bike/Station/City/%s"
    const val API_STATION_DETAIL_URL = "api/basic/v2/Bike/Availability/City/%s"
    const val API_HEADER_TOKEN = "Bearer %s"
    const val DATABASE_NAME = "app-database"

    const val VISIBLE_DISTANCE_LAT = 0.02
    const val VISIBLE_DISTANCE_LON = 0.017
}
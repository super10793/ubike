package com.demo.ubike

object Config {
    const val BASE_URL = "https://tdx.transportdata.tw/"
    const val GRANT_TYPE = "client_credentials"
    const val CLIENT_ID = BuildConfig.API_CLIENT_ID
    const val CLIENT_SECRET = BuildConfig.API_CLIENT_SECRET
    const val API_FORMAT = "JSON"
    const val API_STATION_URL = "api/basic/v2/Bike/Station/City/%s"
    const val API_STATION_DETAIL_URL = "api/basic/v2/Bike/Availability/City/%s"
    const val API_HEADER_TOKEN = "Bearer %s"
    const val DATABASE_NAME = "app-database"
}
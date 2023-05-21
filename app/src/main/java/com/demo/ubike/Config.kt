package com.demo.ubike

object Config {
    const val BASE_URL = "https://tdx.transportdata.tw/"
    const val GRANT_TYPE = "client_credentials"
    const val CLIENT_ID = "super10793-12949aee-7824-4a0f"
    const val CLIENT_SECRET = "c462fc92-0ace-4d2f-be45-dfc8873ee72c"
    const val API_FORMAT = "JSON"
    const val API_STATION_URL = "api/basic/v2/Bike/Station/City/%s"
    const val API_STATION_DETAIL_URL = "api/basic/v2/Bike/Availability/City/%s"
    const val API_HEADER_TOKEN = "Bearer %s"
    const val DATABASE_NAME = "app-database"
}
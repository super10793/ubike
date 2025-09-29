package com.demo.ubike.data.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val tokensFlow: Flow<List<String>>

    suspend fun saveTokens(tokens: List<String>)
}
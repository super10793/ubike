package com.demo.ubike.usecase

import com.demo.ubike.Config
import com.demo.ubike.data.repository.DataStoreRepository
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.di.IoDispatcher
import com.demo.ubike.result.Result
import com.demo.ubike.result.data
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchTokensUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val homeRepository: HomeRepository,
    private val dataStoreRepository: DataStoreRepository
) : FlowUseCase<Unit, Unit>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Result<Unit>> = flow {

        val certificates = listOf(
            ClientCertificate(Config.CLIENT_ID_1, Config.CLIENT_SECRET_1),
            ClientCertificate(Config.CLIENT_ID_2, Config.CLIENT_SECRET_2),
            ClientCertificate(Config.CLIENT_ID_3, Config.CLIENT_SECRET_3)
        )

        val tokens = coroutineScope {
            certificates.map { (clientId, clientSecret) ->
                async {
                    try {
                        homeRepository.fetchToken(clientId, clientSecret)
                    } catch (e: Exception) {
                        Result.Error(e)
                    }
                }
            }.awaitAll()
        }.mapNotNull { result ->
            result.data?.accessToken?.takeIf { token -> token.isNotBlank() }
        }

        if (tokens.isNotEmpty()) {
            dataStoreRepository.saveTokens(tokens)
            emit(Result.Success(Unit))
        } else {
            emit(Result.Error(Exception("Token list is empty")))
        }
    }

    data class ClientCertificate(val clientId: String, val clientSecret: String)
}
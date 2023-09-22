package com.mr3y.ludi.core.network.datasources.internal

import com.mr3y.ludi.core.network.model.ApiResult
import com.mr3y.ludi.core.network.model.RAWGGenresPage
import com.mr3y.ludi.core.network.model.RAWGPage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import javax.inject.Inject

interface RAWGDataSource {

    suspend fun queryGames(url: String): ApiResult<RAWGPage>

    suspend fun queryGamesGenres(url: String): ApiResult<RAWGGenresPage>
}

class RAWGDataSourceImpl @Inject constructor(
    private val client: HttpClient
) : RAWGDataSource {
    override suspend fun queryGames(url: String): ApiResult<RAWGPage> {
        return try {
            val response = client.get(url)
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(code = response.status.value)
            }
        } catch (e: Exception) {
            ApiResult.Error(code = null, throwable = e)
        }
    }

    override suspend fun queryGamesGenres(url: String): ApiResult<RAWGGenresPage> {
        return try {
            val response = client.get(url)
            if (response.status.isSuccess()) {
                ApiResult.Success(response.body())
            } else {
                ApiResult.Error(code = response.status.value)
            }
        } catch (e: Exception) {
            ApiResult.Error(code = null, throwable = e)
        }
    }
}

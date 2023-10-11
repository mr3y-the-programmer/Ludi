package com.mr3y.ludi.shared.core.network.datasources.internal

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.mr3y.ludi.shared.core.network.fixtures.KtorClientForTesting
import com.mr3y.ludi.shared.core.network.fixtures.doCleanup
import com.mr3y.ludi.shared.core.network.fixtures.enqueueMockResponse
import com.mr3y.ludi.shared.core.network.model.ApiResult
import com.mr3y.ludi.shared.core.network.model.RAWGPage
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

@RunWith(TestParameterInjector::class)
class RAWGDataSourceTest {

    private lateinit var sut: RAWGDataSourceImpl
    private val dispatcher = StandardTestDispatcher()
    private val client = KtorClientForTesting.getInstance(dispatcher)

    @Before
    fun setUp() {
        sut = RAWGDataSourceImpl(client)
    }

    @Test
    fun whenQueryingDataFromAPISuccessfully_dataIsProperlyDeserialized(@TestParameter mockedResponses: PolymorphicMockedResponses) = runTest(dispatcher) {
        // given an enqueued mocked response for querying games
        client.enqueueMockResponse(mockedResponses.serializedResponse, HttpStatusCode.OK)

        // when trying to query games
        val result = sut.queryGames("https://api.rawg.io/api/games?page_size=3")

        // then expect the result is success & it is transformed to our model
        expectThat(result).isA<ApiResult.Success<RAWGPage>>()
        result as ApiResult.Success
        expectThat(result.data).isEqualTo(mockedResponses.deserializedResponse)
    }

    @Test
    fun `whenThingsDon'tGoAsExpected_failureIsWrapped`() = runTest(dispatcher) {
        // given an enqueued 404 error response
        val mockResponse =
            """
                    {
                        "status": 0,
                        "status_message": "Bad query request!"
                    }
            """.trimIndent()
        client.enqueueMockResponse(mockResponse, HttpStatusCode.NotFound)

        // when trying to query the latest news
        val result = sut.queryGames("https://api.rawg.io/api/games?page_size=3")

        // then expect the result is HttpFailure
        expectThat(result).isA<ApiResult.Error>()
        result as ApiResult.Error
        expectThat(result.code).isEqualTo(HttpStatusCode.NotFound.value)
    }

    @After
    fun teardown() {
        client.doCleanup()
    }

    enum class PolymorphicMockedResponses(val serializedResponse: String, val deserializedResponse: RAWGPage) {
        ResponseA(serializedMockResponseA, deserializedMockResponseA),
        ResponseB(serializedMockResponseB, deserializedMockResponseB)
    }
}

package com.mr3y.ludi.core.network.datasources.internal

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.mr3y.ludi.core.network.fixtures.RetrofitClientForTesting
import com.mr3y.ludi.core.network.model.RAWGPage
import com.slack.eithernet.ApiResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.create
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.net.HttpURLConnection

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(TestParameterInjector::class)
class RAWGDataSourceTest {

    private lateinit var sut: RAWGDataSource
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start()
        sut = RetrofitClientForTesting.getInstance(baseUrl = mockWebServer.url("/api.rawg.io/api/")).create()
    }

    @Test
    fun whenSendingRequestToRAWGEndpointResource_APIKeyInterceptorIsAddingAuthentication() = runTest {
        // given a new unauthenticated request to RAWG API endpoint
        sut.queryGames(mockWebServer.url("/api.rawg.io/api/games?page_size=3").toString())
        // then assert that Interceptor is taking effect and appending the authentication key to the end
        val request = mockWebServer.takeRequest()
        val authenticatedRequestPattern = """/api.rawg.io/api/games\?page_size=3&key=[a-zA-Z0-9]{32}$""".toRegex()
        assert(authenticatedRequestPattern.matches(request.path!!))
    }

    @Test
    fun whenSendingRequestToOtherEndpoints_APIKeyInterceptorHasNoEffect() = runTest {
        // given a new unauthenticated request to any other endpoint other than RAWG API endpoint
        sut.queryGames(mockWebServer.url("/www.freetogame.com/api/games?platform=browser").toString())
        // then assert that Interceptor has no effect on the request url
        val request = mockWebServer.takeRequest()
        expectThat(request.path).isEqualTo("/www.freetogame.com/api/games?platform=browser")
    }

    @Test
    fun whenQueryingDataFromAPISuccessfully_dataIsProperlyDeserialized(@TestParameter mockedResponses: PolymorphicMockedResponses) = runTest {
        // given an enqueued mocked response for querying games
        mockWebServer.enqueue(mockedResponses.serializedResponse)

        // when trying to query games
        val result = sut.queryGames(mockWebServer.url("/").toString())

        // then expect the result is success & it is transformed to our model
        expectThat(result).isA<ApiResult.Success<RAWGPage>>()
        result as ApiResult.Success
        expectThat(result.value).isEqualTo(mockedResponses.deserializedResponse)
    }

    @Test
    fun `whenThingsDon'tGoAsExpected_failureIsWrapped`() = runTest {
        // given an enqueued 404 error response
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
            .setBody(
                """
                    {
                        "status": 0,
                        "status_message": "Bad query request!"
                    }
                """.trimIndent()
            )
        mockWebServer.enqueue(mockResponse)

        // when trying to query the latest news
        val result = sut.queryGames(mockWebServer.url("/").toString())

        // then expect the result is HttpFailure
        expectThat(result).isA<ApiResult.Failure<Unit>>()
        result as ApiResult.Failure
        expectThat(result).isA<ApiResult.Failure.HttpFailure<Unit>>()
        result as ApiResult.Failure.HttpFailure
        expectThat(result.code).isEqualTo(HttpURLConnection.HTTP_NOT_FOUND)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    enum class PolymorphicMockedResponses(val serializedResponse: MockResponse, val deserializedResponse: RAWGPage) {
        ResponseA(serializedMockResponseA, deserializedMockResponseA),
        ResponseB(serializedMockResponseB, deserializedMockResponseB)
    }
}

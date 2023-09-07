package com.example.githubapp

import com.example.githubapp.feature_github_user.data.remote.RetrofitApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
@OptIn(ExperimentalCoroutinesApi::class)
class RetrofitApiTest {

    lateinit var mockWebServer : MockWebServer
    lateinit var retrofitApi: RetrofitApi


    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        retrofitApi= Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitApi::class.java)
    }



    @Test
    fun `get empty list of user from server`() = runTest {
        val mockResponse = MockResponse()
        val responseContent = Utils.readFileResource("/GetUserEmptyResponse.json")
        mockResponse.setBody(responseContent)
        mockWebServer.enqueue(mockResponse)

        val response = retrofitApi.getGetUser(1,10,"query")
        mockWebServer.takeRequest()

        Assert.assertEquals(true,response.body()?.toResponseUserList()?.items?.isEmpty())
    }


    @Test
    fun `get non empty User list from server`() = runTest {
        val mockResponse = MockResponse()
        val responseContent = Utils.readFileResource("/GetUserListResponse.json")
        mockResponse.setBody(responseContent)
        mockWebServer.enqueue(mockResponse)

        val response = retrofitApi.getGetUser(1,10,"query")
        mockWebServer.takeRequest()

        Assert.assertEquals(true,response.body()?.toResponseUserList()?.items?.isNotEmpty())
        Assert.assertEquals(6,response.body()?.toResponseUserList()?.items?.size)
    }


    @Test
    fun `get non empty followers from server`() = runTest {
        val mockResponse = MockResponse()
        val responseContent = Utils.readFileResource("/GetFollowersListResponse.json")
        mockResponse.setBody(responseContent)
        mockWebServer.enqueue(mockResponse)

        val response = retrofitApi.getFollowerList("URL")
        mockWebServer.takeRequest()

        Assert.assertEquals(true,response.body()?.isNotEmpty())
        Assert.assertEquals(11,response.body()?.size)
    }


    @Test
    fun `get non empty repo list from server`() = runTest {
        val mockResponse = MockResponse()
        val responseContent = Utils.readFileResource("/GetRepoListResponse.json")
        mockResponse.setBody(responseContent)
        mockWebServer.enqueue(mockResponse)

        val response = retrofitApi.getRepoList("URL")
        mockWebServer.takeRequest()

        Assert.assertEquals(true,response.body()?.isNotEmpty())
        Assert.assertEquals(4,response.body()?.size)
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}
package com.example.githubapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import androidx.paging.map
import com.example.githubapp.core.Resource
import com.example.githubapp.core.UIState
import com.example.githubapp.feature_github_user.domain.model.followers.ResponseFollowersItem
import com.example.githubapp.feature_github_user.domain.model.repo.RepoItem
import com.example.githubapp.feature_github_user.domain.model.user.UserItem
import com.example.githubapp.feature_github_user.domain.repository.GithubRepository
import com.example.githubapp.feature_github_user.domain.usecase.GetFollowersUseCase
import com.example.githubapp.feature_github_user.domain.usecase.GetRepoUseCase
import com.example.githubapp.feature_github_user.domain.usecase.GetUserListUseCase
import com.example.githubapp.feature_github_user.presentation.vm.UserListingViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class UserListingViewModelTest {

//    private val testDispatcher = StandardTestDispatcher()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineScopeRule()


    @Mock
    private lateinit var getUserListUseCase: GetUserListUseCase
    @Mock
    private lateinit var getRepoUseCase: GetRepoUseCase
    @Mock
    private lateinit var getFollowersUseCase: GetFollowersUseCase

    @Mock
    private lateinit var observer: Observer<UIState<PagingData<UserItem>>>

    private lateinit var viewModel: UserListingViewModel

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = UserListingViewModel(getUserListUseCase,getRepoUseCase,getFollowersUseCase)
    }

    @After
    fun tearDown() {
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `get valid list of Users `() = TestScope().runTest {

        val testData = PagingData.from(
            listOf(UserItem(/* ... */), UserItem(/* ... */), UserItem(/* ... */))
        )

        Mockito.`when`(getUserListUseCase.invoke("Query")).thenReturn(flowOf(
            testData))

        viewModel.fetchPagingData("Query")
        // Verify that the LiveData emits the expected number of items
        val result = viewModel.uiStateUserList.getOrAwaitValueTest()
        Assert.assertEquals(true,(result is UIState.Success))
        Assert.assertEquals(testData ,(result as UIState.Success).data)
    }
    @Test
    fun `get empty list of repo `() = TestScope().runTest {
        Mockito.`when`(getRepoUseCase.invoke("Url")).thenReturn(flowOf(Resource.Success(
            emptyList()
        )))

        viewModel.fetchRepoData("Url")
        val result = viewModel.uiStateRepoList.getOrAwaitValueTest()
        Assert.assertEquals(true,(result is UIState.Error))
        Assert.assertEquals("Have no repository yet !",(result as UIState.Error).message)
    }
    @Test
    fun `get valid list of repo `() = TestScope().runTest {
        Mockito.`when`(getRepoUseCase.invoke("Url")).thenReturn(flowOf(Resource.Success(
            listOf(RepoItem(/* ... */),RepoItem(/* ... */),RepoItem(/* ... */))
        )))

        viewModel.fetchRepoData("Url")
        val result = viewModel.uiStateRepoList.getOrAwaitValueTest()
        Assert.assertEquals(true,(result is UIState.Success))
        Assert.assertEquals(3 ,(result as UIState.Success).data.size)
    }

    @Test
    fun `get empty list of followers `() = TestScope().runTest {
        Mockito.`when`(getFollowersUseCase.invoke("Url")).thenReturn(flowOf(Resource.Success(
            emptyList()
        )))

        viewModel.fetchFollowersData("Url")
        val result = viewModel.uiStateFollowerList.getOrAwaitValueTest()
        Assert.assertEquals(true,(result is UIState.Error))
        Assert.assertEquals("No followers yet !",(result as UIState.Error).message)
    }
    @Test
    fun `get valid list of followers `() = TestScope().runTest {
        Mockito.`when`(getFollowersUseCase.invoke("Url")).thenReturn(flowOf(Resource.Success(
            listOf(ResponseFollowersItem(/* ... */),ResponseFollowersItem(/* ... */),ResponseFollowersItem(/* ... */),ResponseFollowersItem(/* ... */)),
        )))

        viewModel.fetchFollowersData("Url")
        val result = viewModel.uiStateFollowerList.getOrAwaitValueTest()
        Assert.assertEquals(true,(result is UIState.Success))
        Assert.assertEquals(4 ,(result as UIState.Success).data.size)
    }
}

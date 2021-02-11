package com.mshaw.squaretest.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mshaw.squaretest.models.EmployeesResponse
import com.mshaw.squaretest.network.EmployeeManager
import com.mshaw.squaretest.util.state.AwaitResult
import com.mshaw.squaretest.util.state.State
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.Response
import okio.Buffer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.lang.IllegalStateException
import java.util.*

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class MainViewModelTest : TestCase() {
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var observer: Observer<in State<EmployeesResponse>>

    @Mock
    private lateinit var response: Response

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var employeesManager: EmployeeManager

    @Mock
    private lateinit var viewModel: MainViewModel

    private var successfulResponse: EmployeesResponse? = null
    private lateinit var exception: Exception
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .build()

    @Before
    public override fun setUp() {
        super.setUp()
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(employeesManager)
        viewModel.employeesResponseLiveData.observeForever(observer)

        successfulResponse = try {
            val stream = javaClass.getResourceAsStream("/employees.json") ?: return
            val reader = JsonReader.of(Buffer().readFrom(stream))
            moshi.adapter(EmployeesResponse::class.java).fromJson(reader)
        } catch (e: Exception) {
            null
        }

        exception = try {
            val stream = javaClass.getResourceAsStream("/employees_malformed.json") ?: return
            val reader = JsonReader.of(Buffer().readFrom(stream))
            moshi.adapter(EmployeesResponse::class.java).fromJson(reader)
            IllegalStateException("this shouldn't happen")
        } catch (e: Exception) {
            e
        }
    }

    @Test
    fun shouldParseJsonAsResponse() {
        val successfulResponse = successfulResponse
        if (successfulResponse == null) {
            fail("successResponse is null")
            return
        }

        assert(successfulResponse.employees.isNotEmpty())
        assert(successfulResponse.employees.size == 11)
    }

    @Test
    fun shouldEmitSuccessState() = runBlocking {
        val successfulResponse = successfulResponse
        if (successfulResponse == null) {
            fail("successResponse is null")
            return@runBlocking
        }

        Mockito.`when`(employeesManager.getEmployees())
            .thenReturn(AwaitResult.Ok(successfulResponse, response))

        viewModel.fetchEmployees()
        Mockito.verify(observer).onChanged(State.Success(successfulResponse))
        viewModel.employeesResponseLiveData.removeObserver(observer)
    }

    @Test
    fun shouldEmitErrorState() = runBlocking {
        Mockito.`when`(employeesManager.getEmployees()).thenReturn(AwaitResult.Error(exception))

        viewModel.fetchEmployees()
        Mockito.verify(observer).onChanged(State.Error(exception))
        viewModel.employeesResponseLiveData.removeObserver(observer)
    }

    @After
    public override fun tearDown() {
        super.tearDown()
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
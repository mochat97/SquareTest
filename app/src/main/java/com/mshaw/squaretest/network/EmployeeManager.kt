package com.mshaw.squaretest.network

import com.mshaw.squaretest.models.EmployeesResponse
import com.mshaw.squaretest.util.extensions.awaitResult
import com.mshaw.squaretest.util.state.AwaitResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeeManager @Inject constructor(private val service: EmployeeService) {
    suspend fun getEmployees(): AwaitResult<EmployeesResponse> {
        return try {
            withContext(Dispatchers.IO) { service.getEmployees().awaitResult() }
        } catch (e: Exception) {
            AwaitResult.Error(e)
        }
    }
}
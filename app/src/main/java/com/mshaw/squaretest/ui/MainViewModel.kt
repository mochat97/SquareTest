package com.mshaw.squaretest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshaw.squaretest.models.EmployeesResponse
import com.mshaw.squaretest.network.EmployeeManager
import com.mshaw.squaretest.util.state.AwaitResult
import com.mshaw.squaretest.util.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val manager: EmployeeManager): ViewModel() {
    private val _employeesResponseLiveData: MutableLiveData<State<EmployeesResponse>> = MutableLiveData()
    val employeesResponseLiveData: LiveData<State<EmployeesResponse>> = _employeesResponseLiveData

    fun fetchEmployees() {
        _employeesResponseLiveData.value = State.Loading
        viewModelScope.launch {
            when (val result = manager.getEmployees()) {
                is AwaitResult.Ok -> {
                    _employeesResponseLiveData.value = State.Success(result.value)
                }
                is AwaitResult.Error -> {
                    _employeesResponseLiveData.value = State.Error(result.exception)
                }
            }
        }
    }
}
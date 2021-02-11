package com.mshaw.squaretest.ui

import android.os.Bundle
import android.util.TypedValue
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshaw.squaretest.R
import com.mshaw.squaretest.adapters.EmployeeAdapter
import com.mshaw.squaretest.databinding.ActivityMainBinding
import com.mshaw.squaretest.models.Employee
import com.mshaw.squaretest.util.EqualSpacingItemDecorator
import com.mshaw.squaretest.util.state.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val adapter: EmployeeAdapter by lazy {
        EmployeeAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setupBinding()

        viewModel.employeesResponseLiveData.observe(this) { response ->
            when (response) {
                State.Loading -> {
                    binding.progressBar.show()
                }
                is State.Success -> {
                    val sortedEmployees = response.value.employees.sortedBy { it.fullName }
                    binding.progressBar.hide()
                    updateRecyclerView(sortedEmployees)
                }
                is State.Error -> {
                    binding.progressBar.hide()
                    showErrorState(getString(R.string.error_getting_results))
                }
            }
        }

        viewModel.fetchEmployees()
    }

    private fun setupBinding() {
        binding.progressBar.hide()
        val dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16f, resources.displayMetrics).toInt()
        val recyclerView = binding.employeesRecyclerView

        recyclerView.addItemDecoration(EqualSpacingItemDecorator(dp))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun updateRecyclerView(employees: List<Employee>) {
        binding.employeesRecyclerView.isVisible = employees.isNotEmpty()
        adapter.employees = employees
    }

    private fun showErrorState(message: String) {
        binding.employeesRecyclerView.isVisible = false
        binding.errorTextView.text = message
    }
}
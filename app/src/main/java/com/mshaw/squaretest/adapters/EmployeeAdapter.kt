package com.mshaw.squaretest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mshaw.squaretest.databinding.ItemEmployeeBinding
import com.mshaw.squaretest.models.Employee

class EmployeeAdapter: RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {
    var employees: List<Employee> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemEmployeeBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(employees[position])
    }

    override fun getItemCount() = employees.size

    inner class ViewHolder(private val binding: ItemEmployeeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: Employee) {
            binding.employee = employee
        }
    }
}
package com.mshaw.squaretest.models
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class EmployeesResponse(
    @Json(name = "employees")
    val employees: List<Employee>
)

@JsonClass(generateAdapter = true)
data class Employee(
    @Json(name = "biography")
    val biography: String?,
    @Json(name = "email_address")
    val emailAddress: String,
    @Json(name = "employee_type")
    val employeeType: EmployeeType,
    @Json(name = "full_name")
    val fullName: String,
    @Json(name = "phone_number")
    val phoneNumber: String?,
    @Json(name = "photo_url_large")
    val photoUrlLarge: String?,
    @Json(name = "photo_url_small")
    val photoUrlSmall: String?,
    @Json(name = "team")
    val team: String,
    @Json(name = "uuid")
    val uuid: String
)

enum class EmployeeType {
    FULL_TIME, PART_TIME, CONTRACTOR
}
package com.kdani.network_helper.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents DTO object of single employee.
 */
@JsonClass(generateAdapter = true)
internal data class Employee(
    @Json(name = "uuid") val uuid: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "phone_number") val phoneNumber: String?,
    @Json(name = "email_address") val email: String,
    @Json(name = "biography") val biography: String?,
    @Json(name = "photo_url_small") val photoImage: String?,
    @Json(name = "photo_url_large") val photoImageLrg: String?,
    @Json(name = "team") val team: String,
    @Json(name = "employee_type") val employeeType: EmployeeType,
    @Transient val salary: String = "",
)

/**
 * Different types of employee
 */
internal enum class EmployeeType {
    FULL_TIME, PART_TIME, CONTRACTOR
}
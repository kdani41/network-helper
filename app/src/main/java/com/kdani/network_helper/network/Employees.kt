package com.kdani.network_helper.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO object representing all employees.
 */
@JsonClass(generateAdapter = true)
internal data class Employees(@Json(name = "employees") val info: List<Employee>)
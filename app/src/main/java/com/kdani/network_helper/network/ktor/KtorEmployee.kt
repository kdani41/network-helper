package com.kdani.network_helper.network.ktor

import com.kdani.network_helper.network.EmployeeType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class KtorEmployee(
    @SerialName("uuid") val uuid: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("phone_number") val phoneNumber: String?,
    @SerialName("email_address") val email: String,
    @SerialName("biography") val biography: String?,
    @SerialName("photo_url_small") val photoImage: String?,
    @SerialName("photo_url_large") val photoImageLrg: String?,
    @SerialName("team") val team: String,
    @SerialName("employee_type") val employeeType: EmployeeType,
)

@Serializable
internal data class KtorEmployees(@SerialName("employees") val info: List<KtorEmployee>)

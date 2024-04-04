package uk.ac.tees.mad.d3857102.screens.auth.domain

data class LoginStatus(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)
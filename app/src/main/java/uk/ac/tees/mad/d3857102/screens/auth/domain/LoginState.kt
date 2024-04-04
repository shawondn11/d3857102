package uk.ac.tees.mad.d3857102.screens.auth.domain

data class LoginState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)

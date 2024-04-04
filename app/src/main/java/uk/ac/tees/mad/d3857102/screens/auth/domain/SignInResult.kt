package uk.ac.tees.mad.d3857102.screens.auth.domain

import uk.ac.tees.mad.d3857102.domain.UserData

data class SignInResult(
    val data: UserData? = null,
    val isSuccessful: Boolean = false,
    val errorMessage: String? = null
)
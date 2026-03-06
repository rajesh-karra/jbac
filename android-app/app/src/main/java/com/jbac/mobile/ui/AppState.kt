package com.jbac.mobile.ui

import com.jbac.mobile.model.User

data class AppState(
    val isLoading: Boolean = false,
    val token: String? = null,
    val user: User? = null,
    val errorMessage: String? = null,
)

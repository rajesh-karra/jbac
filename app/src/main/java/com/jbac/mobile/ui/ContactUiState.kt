package com.jbac.mobile.ui

data class ContactUiState(
    val isSubmitting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
)

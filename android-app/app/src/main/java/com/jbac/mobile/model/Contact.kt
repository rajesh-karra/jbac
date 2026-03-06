package com.jbac.mobile.model

data class ContactRequest(
    val name: String,
    val email: String,
    val message: String,
)

data class ContactResponse(
    val message: String,
)

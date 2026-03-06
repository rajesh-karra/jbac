package com.jbac.mobile.data

import com.jbac.mobile.model.ContactRequest
import com.jbac.mobile.model.LoginRequest
import com.jbac.mobile.network.RetrofitClient

class JbacRepository {
    suspend fun login(username: String, password: String) = RetrofitClient.api.login(
        LoginRequest(username = username, password = password)
    )

    suspend fun profile() = RetrofitClient.api.profile()

    suspend fun getHome() = RetrofitClient.api.home()

    suspend fun getNotices() = RetrofitClient.api.notices().items

    suspend fun getEvents() = RetrofitClient.api.events().items

    suspend fun submitContact(name: String, email: String, message: String) {
        val payload = ContactRequest(name = name, email = email, message = message)
        RetrofitClient.api.submitContact(payload)
    }

    fun setToken(token: String?) {
        RetrofitClient.updateToken(token)
    }
}

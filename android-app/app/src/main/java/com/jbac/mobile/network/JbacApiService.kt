package com.jbac.mobile.network

import com.jbac.mobile.model.ContactRequest
import com.jbac.mobile.model.ContactResponse
import com.jbac.mobile.model.EventResponse
import com.jbac.mobile.model.HomeResponse
import com.jbac.mobile.model.LoginRequest
import com.jbac.mobile.model.LoginResponse
import com.jbac.mobile.model.NoticeResponse
import com.jbac.mobile.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JbacApiService {
    @GET("api/health")
    suspend fun health(): Map<String, String>

    @GET("api/home")
    suspend fun home(): HomeResponse

    @GET("api/notices")
    suspend fun notices(): NoticeResponse

    @GET("api/events")
    suspend fun events(): EventResponse

    @POST("api/contact")
    suspend fun submitContact(@Body payload: ContactRequest): ContactResponse

    @POST("api/login")
    suspend fun login(@Body payload: LoginRequest): LoginResponse

    @GET("api/profile")
    suspend fun profile(): User
}

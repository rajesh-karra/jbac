package com.jbac.mobile.model

data class HomeResponse(
    val title: String,
    val tagline: String,
    val highlights: List<String>,
    val stats: HomeStats,
)

data class HomeStats(
    val students: Int,
    val faculty: Int,
    val programs: Int,
)

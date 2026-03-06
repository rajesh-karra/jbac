package com.jbac.mobile.model

data class Notice(
    val id: Int,
    val title: String,
    val publishedOn: String,
    val category: String,
    val important: Boolean,
)

data class NoticeResponse(
    val items: List<Notice>,
)

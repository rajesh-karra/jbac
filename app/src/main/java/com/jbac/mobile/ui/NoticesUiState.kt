package com.jbac.mobile.ui

import com.jbac.mobile.model.Notice

data class NoticesUiState(
    val isLoading: Boolean = true,
    val query: String = "",
    val showImportantOnly: Boolean = false,
    val notices: List<Notice> = emptyList(),
    val errorMessage: String? = null,
) {
    val filteredNotices: List<Notice>
        get() = notices.filter { notice ->
            val matchesQuery = query.isBlank() ||
                notice.title.contains(query, ignoreCase = true) ||
                notice.category.contains(query, ignoreCase = true)
            val matchesFlag = !showImportantOnly || notice.important
            matchesQuery && matchesFlag
        }
}

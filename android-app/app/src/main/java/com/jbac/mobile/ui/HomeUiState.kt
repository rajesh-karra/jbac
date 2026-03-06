package com.jbac.mobile.ui

import com.jbac.mobile.model.Event
import com.jbac.mobile.model.HomeResponse
import com.jbac.mobile.model.Notice

data class HomeUiState(
    val isLoading: Boolean = true,
    val home: HomeResponse? = null,
    val notices: List<Notice> = emptyList(),
    val events: List<Event> = emptyList(),
    val errorMessage: String? = null,
)

package com.jbac.mobile.ui

import com.jbac.mobile.model.Event

data class EventsUiState(
    val isLoading: Boolean = true,
    val query: String = "",
    val events: List<Event> = emptyList(),
    val errorMessage: String? = null,
) {
    val filteredEvents: List<Event>
        get() = events.filter { event ->
            query.isBlank() ||
                event.name.contains(query, ignoreCase = true) ||
                event.venue.contains(query, ignoreCase = true) ||
                event.description.contains(query, ignoreCase = true)
        }
}

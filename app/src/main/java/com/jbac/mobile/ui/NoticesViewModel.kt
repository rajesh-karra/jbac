package com.jbac.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jbac.mobile.data.JbacRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoticesViewModel(
    private val repository: JbacRepository = JbacRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoticesUiState())
    val uiState: StateFlow<NoticesUiState> = _uiState.asStateFlow()

    init {
        loadNotices()
    }

    fun loadNotices() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val notices = repository.getNotices()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    notices = notices,
                )
            } catch (ex: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = ex.message ?: "Unable to load notices",
                )
            }
        }
    }

    fun updateQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun toggleImportantOnly(value: Boolean) {
        _uiState.value = _uiState.value.copy(showImportantOnly = value)
    }
}

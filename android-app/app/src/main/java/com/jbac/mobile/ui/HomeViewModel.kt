package com.jbac.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jbac.mobile.data.JbacRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: JbacRepository = JbacRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val home = repository.getHome()
                val notices = repository.getNotices()
                val events = repository.getEvents()
                _uiState.value = HomeUiState(
                    isLoading = false,
                    home = home,
                    notices = notices,
                    events = events,
                )
            } catch (ex: Exception) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    errorMessage = ex.message ?: "Failed to load data",
                )
            }
        }
    }
}

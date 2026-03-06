package com.jbac.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jbac.mobile.data.JbacRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: JbacRepository = JbacRepository(),
) : ViewModel() {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            try {
                val auth = repository.login(username = username, password = password)
                repository.setToken(auth.token)
                val profile = repository.profile()
                _state.value = AppState(
                    isLoading = false,
                    token = auth.token,
                    user = profile,
                )
            } catch (ex: Exception) {
                _state.value = AppState(
                    isLoading = false,
                    errorMessage = ex.message ?: "Login failed",
                )
            }
        }
    }

    fun logout() {
        repository.setToken(null)
        _state.value = AppState()
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}

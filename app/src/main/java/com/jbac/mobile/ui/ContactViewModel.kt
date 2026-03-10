package com.jbac.mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jbac.mobile.data.JbacRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactViewModel(
    private val repository: JbacRepository = JbacRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()

    fun submit(name: String, email: String, message: String) {
        viewModelScope.launch {
            _uiState.value = ContactUiState(isSubmitting = true)
            try {
                repository.submitContact(name = name, email = email, message = message)
                _uiState.value = ContactUiState(successMessage = "Message sent successfully")
            } catch (ex: Exception) {
                _uiState.value = ContactUiState(
                    errorMessage = ex.message ?: "Unable to send message",
                )
            }
        }
    }

    fun clearStatus() {
        _uiState.value = ContactUiState()
    }
}

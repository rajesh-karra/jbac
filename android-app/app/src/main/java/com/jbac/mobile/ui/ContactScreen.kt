package com.jbac.mobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jbac.mobile.ui.theme.JbacTheme

@Composable
fun ContactScreen(
    viewModel: ContactViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    ContactScreenContent(
        uiState = uiState,
        onClearStatus = viewModel::clearStatus,
        onSubmit = viewModel::submit,
        modifier = modifier,
    )
}

@Composable
fun ContactScreenContent(
    uiState: ContactUiState,
    onClearStatus: () -> Unit,
    onSubmit: (name: String, email: String, message: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Contact", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                onClearStatus()
            },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                onClearStatus()
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        OutlinedTextField(
            value = message,
            onValueChange = {
                message = it
                onClearStatus()
            },
            label = { Text("Message") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
        )

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
            )
        }

        if (uiState.successMessage != null) {
            Text(
                text = uiState.successMessage,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Button(
            onClick = { onSubmit(name.trim(), email.trim(), message.trim()) },
            enabled = !uiState.isSubmitting,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (uiState.isSubmitting) {
                CircularProgressIndicator(modifier = Modifier.padding(2.dp))
            } else {
                Text("Send")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ContactScreenPreview() {
    JbacTheme {
        ContactScreenContent(
            uiState = ContactUiState(),
            onClearStatus = {},
            onSubmit = { _, _, _ -> },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

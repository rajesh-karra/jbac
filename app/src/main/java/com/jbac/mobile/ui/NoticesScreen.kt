package com.jbac.mobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoticesScreen(
    viewModel: NoticesViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    NoticesScreenContent(
        uiState = uiState,
        onRetry = viewModel::loadNotices,
        onQueryChange = viewModel::updateQuery,
        onImportantOnlyToggle = viewModel::toggleImportantOnly,
        modifier = modifier,
    )
}

@Composable
private fun NoticesScreenContent(
    uiState: NoticesUiState,
    onRetry: () -> Unit,
    onQueryChange: (String) -> Unit,
    onImportantOnlyToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(24.dp))
            }
        }

        uiState.errorMessage != null -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                )
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item {
                    Text("Notices", style = MaterialTheme.typography.headlineSmall)
                }

                item {
                    OutlinedTextField(
                        value = uiState.query,
                        onValueChange = onQueryChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Search by title/category") },
                        singleLine = true,
                    )
                }

                item {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Important only")
                        Switch(
                            checked = uiState.showImportantOnly,
                            onCheckedChange = onImportantOnlyToggle,
                        )
                    }
                }

                items(uiState.filteredNotices, key = { it.id }) { notice ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(notice.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "${notice.category} | ${notice.publishedOn}",
                                style = MaterialTheme.typography.bodySmall,
                            )
                            if (notice.important) {
                                Text(
                                    "Important",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

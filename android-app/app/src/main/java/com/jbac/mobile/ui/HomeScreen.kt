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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onOpenContact: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

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
                    text = "Error: ${uiState.errorMessage}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                )
                Button(onClick = viewModel::loadHomeData) {
                    Text("Retry")
                }
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Text(
                        text = uiState.home?.college ?: "JBAC",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = uiState.home?.tagline.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Button(onClick = onOpenContact, modifier = Modifier.padding(top = 8.dp)) {
                        Text("Contact Us")
                    }
                }

                item {
                    StatsCard(
                        students = uiState.home?.stats?.students ?: 0,
                        faculty = uiState.home?.stats?.faculty ?: 0,
                        programs = uiState.home?.stats?.programs ?: 0,
                    )
                }

                item {
                    Text("Notices", style = MaterialTheme.typography.titleLarge)
                }

                items(uiState.notices, key = { it.id }) { notice ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(notice.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "${notice.category} | ${notice.publishedOn}",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }

                item {
                    Text("Events", style = MaterialTheme.typography.titleLarge)
                }

                items(uiState.events, key = { it.id }) { event ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(event.name, style = MaterialTheme.typography.titleMedium)
                            Text("${event.date} | ${event.venue}")
                            Text(event.description, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsCard(
    students: Int,
    faculty: Int,
    programs: Int,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text("Students: $students")
            Text("Faculty: $faculty")
            Text("Programs: $programs")
        }
    }
}

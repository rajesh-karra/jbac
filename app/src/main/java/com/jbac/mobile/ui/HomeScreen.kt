package com.jbac.mobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jbac.mobile.model.Event
import com.jbac.mobile.model.HomeResponse
import com.jbac.mobile.model.HomeStats
import com.jbac.mobile.model.Notice
import com.jbac.mobile.ui.theme.JbacTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onOpenContact: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreenContent(
        uiState = uiState,
        onRetry = viewModel::loadHomeData,
        onOpenContact = onOpenContact,
        modifier = modifier,
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onRetry: () -> Unit,
    onOpenContact: () -> Unit,
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
                    text = "Error: ${uiState.errorMessage}",
                    style = MaterialTheme.typography.bodyLarge,
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
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Text(
                        text = uiState.home?.title ?: "JBAC",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = uiState.home?.tagline.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    if (uiState.home?.highlights?.isNotEmpty() == true) {
                        uiState.home.highlights.forEach { point ->
                            Text(
                                text = "- $point",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
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

                // Prefixing keys with "notice_" to ensure uniqueness across different item types in the same LazyColumn
                items(uiState.notices, key = { "notice_${it.id}" }) { notice ->
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

                // Prefixing keys with "event_" to avoid collisions with other items like notices
                items(uiState.events, key = { "event_${it.id}" }) { event ->
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
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text("Students", style = MaterialTheme.typography.labelMedium)
                Text("$students", style = MaterialTheme.typography.titleMedium)
            }
            Column {
                Text("Faculty", style = MaterialTheme.typography.labelMedium)
                Text("$faculty", style = MaterialTheme.typography.titleMedium)
            }
            Column {
                Text("Programs", style = MaterialTheme.typography.labelMedium)
                Text("$programs", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun HomeScreenPreview() {
    val previewState = HomeUiState(
        isLoading = false,
        home = HomeResponse(
            title = "JBAC Mobile Portal",
            tagline = "Your campus updates in one place",
            highlights = listOf("Offline ready", "Quick updates"),
            stats = HomeStats(students = 1200, faculty = 85, programs = 14),
        ),
        notices = listOf(
            Notice(
                id = 1,
                title = "Admissions open for 2026",
                publishedOn = "2026-03-01",
                category = "Admission",
                important = true,
            ),
            Notice(
                id = 2,
                title = "Semester exam schedule released",
                publishedOn = "2026-02-27",
                category = "Exam",
                important = false,
            ),
        ),
        events = listOf(
            Event(
                id = 1,
                name = "Annual Cultural Fest",
                date = "2026-03-20",
                venue = "Main Auditorium",
                description = "Music, dance, and student performances.",
            )
        ),
    )

    JbacTheme {
        HomeScreenContent(
            uiState = previewState,
            onRetry = {},
            onOpenContact = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

package com.jbac.mobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("About", style = MaterialTheme.typography.headlineSmall)

        Card {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("JBAC Mobile")
                Text(
                    "A student-friendly app for campus notices, upcoming events, and direct contact support.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Features", style = MaterialTheme.typography.titleMedium)
                Text("- Offline-first data")
                Text("- Searchable notices and events")
                Text("- Contact form with validation")
            }
        }
    }
}

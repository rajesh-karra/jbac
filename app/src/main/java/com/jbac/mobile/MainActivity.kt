package com.jbac.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jbac.mobile.ui.AboutScreen
import com.jbac.mobile.ui.ContactScreen
import com.jbac.mobile.ui.ContactViewModel
import com.jbac.mobile.ui.EventsScreen
import com.jbac.mobile.ui.EventsViewModel
import com.jbac.mobile.ui.HomeScreen
import com.jbac.mobile.ui.HomeViewModel
import com.jbac.mobile.ui.NoticesScreen
import com.jbac.mobile.ui.NoticesViewModel
import com.jbac.mobile.ui.theme.JbacTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JbacTheme {
                JbacApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JbacApp() {
    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = viewModel()
    val noticesViewModel: NoticesViewModel = viewModel()
    val eventsViewModel: EventsViewModel = viewModel()
    val contactViewModel: ContactViewModel = viewModel()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route
    val activeRoute = currentRoute ?: Routes.Home

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(routeTitle(activeRoute)) },
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = activeRoute == Routes.Home,
                    onClick = { navController.navigateSingleTopTo(Routes.Home) },
                    label = { Text("Home") },
                    icon = { Text("H") },
                )
                NavigationBarItem(
                    selected = activeRoute == Routes.Notices,
                    onClick = { navController.navigateSingleTopTo(Routes.Notices) },
                    label = { Text("Notices") },
                    icon = { Text("N") },
                )
                NavigationBarItem(
                    selected = activeRoute == Routes.Events,
                    onClick = { navController.navigateSingleTopTo(Routes.Events) },
                    label = { Text("Events") },
                    icon = { Text("E") },
                )
                NavigationBarItem(
                    selected = activeRoute == Routes.Contact,
                    onClick = { navController.navigateSingleTopTo(Routes.Contact) },
                    label = { Text("Contact") },
                    icon = { Text("C") },
                )
                NavigationBarItem(
                    selected = activeRoute == Routes.About,
                    onClick = { navController.navigateSingleTopTo(Routes.About) },
                    label = { Text("About") },
                    icon = { Text("A") },
                )
            }
        },
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            homeViewModel = homeViewModel,
            noticesViewModel = noticesViewModel,
            eventsViewModel = eventsViewModel,
            contactViewModel = contactViewModel,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

private object Routes {
    const val Home = "home"
    const val Notices = "notices"
    const val Events = "events"
    const val Contact = "contact"
    const val About = "about"
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    noticesViewModel: NoticesViewModel,
    eventsViewModel: EventsViewModel,
    contactViewModel: ContactViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home,
        modifier = modifier,
    ) {
        composable(Routes.Home) {
            HomeScreen(
                viewModel = homeViewModel,
                modifier = Modifier.fillMaxSize(),
                onOpenContact = { navController.navigate(Routes.Contact) },
            )
        }

        composable(Routes.Notices) {
            NoticesScreen(
                viewModel = noticesViewModel,
                modifier = Modifier.fillMaxSize(),
            )
        }

        composable(Routes.Events) {
            EventsScreen(
                viewModel = eventsViewModel,
                modifier = Modifier.fillMaxSize(),
            )
        }

        composable(Routes.Contact) {
            ContactScreen(
                viewModel = contactViewModel,
                modifier = Modifier.fillMaxSize(),
            )
        }

        composable(Routes.About) {
            AboutScreen(modifier = Modifier.fillMaxSize())
        }
    }
}

private fun routeTitle(route: String): String = when (route) {
    Routes.Home -> "Dashboard"
    Routes.Notices -> "Notices"
    Routes.Events -> "Events"
    Routes.Contact -> "Contact"
    Routes.About -> "About"
    else -> "JBAC Mobile"
}

private fun NavHostController.navigateSingleTopTo(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
    }
}

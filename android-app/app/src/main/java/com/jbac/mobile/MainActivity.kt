package com.jbac.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jbac.mobile.ui.AppState
import com.jbac.mobile.ui.AppViewModel
import com.jbac.mobile.ui.ContactScreen
import com.jbac.mobile.ui.ContactViewModel
import com.jbac.mobile.ui.HomeScreen
import com.jbac.mobile.ui.HomeViewModel
import com.jbac.mobile.ui.LoginScreen
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

@Composable
private fun JbacApp() {
    val navController = rememberNavController()
    val appViewModel: AppViewModel = viewModel()
    val appState by appViewModel.state.collectAsState()

    val title = if (appState.token == null) "JBAC Login" else "JBAC"

    val homeViewModel: HomeViewModel = viewModel()
    val contactViewModel: ContactViewModel = viewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(title) },
                actions = {
                    if (appState.token != null) {
                        Button(onClick = {
                            appViewModel.logout()
                            navController.navigate(Routes.Login) {
                                popUpTo(0)
                            }
                        }) {
                            Text("Logout")
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            appState = appState,
            appViewModel = appViewModel,
            homeViewModel = homeViewModel,
            contactViewModel = contactViewModel,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

private object Routes {
    const val Login = "login"
    const val Home = "home"
    const val Contact = "contact"
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    appState: AppState,
    appViewModel: AppViewModel,
    homeViewModel: HomeViewModel,
    contactViewModel: ContactViewModel,
    modifier: Modifier = Modifier,
) {
    val startDestination = if (appState.token == null) Routes.Login else Routes.Home

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Routes.Login) {
            LoginScreen(
                appState = appState,
                onLogin = { username, password ->
                    appViewModel.login(username = username, password = password)
                },
            )

            if (appState.token != null) {
                navController.navigate(Routes.Home) {
                    popUpTo(Routes.Login) { inclusive = true }
                }
            }
        }

        composable(Routes.Home) {
            HomeScreen(
                viewModel = homeViewModel,
                modifier = Modifier.fillMaxSize(),
                onOpenContact = { navController.navigate(Routes.Contact) },
            )
        }

        composable(Routes.Contact) {
            ContactScreen(
                viewModel = contactViewModel,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

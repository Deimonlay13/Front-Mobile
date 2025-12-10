package com.gdl.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gdl.view.LoginScreen
import com.gdl.view.PokeListScreen
import com.gdl.view.RegisterScreen
import com.gdl.screens.CarritoScreen
import com.gdl.viewmodel.LoginViewModel
import com.gdl.viewmodel.RegisterViewModel
import com.gdl.viewmodel.PokeViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Carrito : Screen("carrito")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel()
    val pokeViewModel: PokeViewModel = viewModel() // <-- agregado

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // LOGIN
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // REGISTER
        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = viewModel()
            RegisterScreen(
                viewModel = registerViewModel,
                onRegisterSuccess = {
                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        // HOME
        composable(Screen.Home.route) {
            PokeListScreen(
                navController = navController,
                vm = pokeViewModel
            )
        }

        // CARRITO
        composable(Screen.Carrito.route) {
            CarritoScreen(
                viewModel = pokeViewModel,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}

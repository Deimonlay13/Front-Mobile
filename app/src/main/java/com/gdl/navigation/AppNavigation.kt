package com.gdl.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gdl.view.LoginScreen
import com.gdl.view.PokeListScreen
import com.gdl.view.RegisterScreen
import com.gdl.screens.CarritoScreen
import com.gdl.view.FormularioScreen
import com.gdl.view.PagoSimuladoScreen
import com.gdl.viewmodel.FormularioViewModel
import com.gdl.viewmodel.LoginViewModel
import com.gdl.viewmodel.RegisterViewModel
import com.gdl.viewmodel.PokeViewModel

// RUTAS CON PARÁMETRO
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Carrito : Screen("carrito")
    object Formulario : Screen("formulario/{total}")
    object Pago : Screen("pago/{total}")     // ← Simulado (NO Stripe)
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel()
    val pokeViewModel: PokeViewModel = viewModel()

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
                onVolver = { navController.popBackStack() },
                onComprar = {
                    val total = pokeViewModel.totalCarrito
                    navController.navigate("formulario/$total")
                }
            )
        }

        // FORMULARIO (RECIBE TOTAL + CARRITO)
        composable("formulario/{total}") { backStackEntry ->
            val total = backStackEntry.arguments?.getString("total")?.toInt() ?: 0
            val formularioViewModel: FormularioViewModel = viewModel()

            FormularioScreen(
                idUsuario = loginViewModel.uiState.value.loginResponse?.id ?: 0L,
                totalAmount = total,

                // 🔥 AHORA SÍ SE PASA EL CARRITO COMPLETO
                carrito = pokeViewModel.carrito.collectAsState().value,

                onNavigateToPago = { monto ->
                    navController.navigate("pago/$monto")
                },
                viewModel = formularioViewModel
            )
        }

        // PAGO SIMULADO
        composable("pago/{total}") { backStackEntry ->
            val total = backStackEntry.arguments?.getString("total")?.toInt() ?: 0

            // Pantalla súper simple de confirmación
            PagoSimuladoScreen(
                total = total,
                onFinalizar = {
                    Toast.makeText(context, "Compra realizada con éxito 🎉", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

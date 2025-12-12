package com.gdl.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gdl.components.BottomBar
import com.gdl.data.UserSession
import com.gdl.network.ApiClient
import com.gdl.network.ApiService
import com.gdl.repository.ComprasRepository
import com.gdl.repository.UserRepository
import com.gdl.view.LoginScreen
import com.gdl.view.PokeListScreen
import com.gdl.view.RegisterScreen
import com.gdl.screens.CarritoScreen
import com.gdl.view.ComprasScreen
import com.gdl.view.FormularioScreen
import com.gdl.view.PagoSimuladoScreen
import com.gdl.view.PerfilScreen
import com.gdl.viewmodel.ComprasViewModel
import com.gdl.viewmodel.ComprasViewModelFactory
import com.gdl.viewmodel.FormularioViewModel
import com.gdl.viewmodel.LoginViewModel
import com.gdl.viewmodel.PerfilViewModel
import com.gdl.viewmodel.PerfilViewModelFactory
import com.gdl.viewmodel.RegisterViewModel
import com.gdl.viewmodel.PokeViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Carrito : Screen("carrito")
    object Formulario : Screen("formulario/{total}")
    object Pago : Screen("pago/{total}")     // ← Simulado (NO Stripe)
    object Perfil : Screen("perfil")

    object Compras : Screen("compras")

    object Salir : Screen("salir")

}

@Composable
fun AppNavigation(navController: NavHostController) {

    val context = LocalContext.current

    // ViewModels
    val loginViewModel: LoginViewModel = viewModel()
    val pokeViewModel: PokeViewModel = viewModel()
    val session = remember { UserSession(context) }
    val idUsuario by session.getUserId().collectAsState(initial = 0L)

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val apiService = remember { ApiClient.retrofit.create(ApiService::class.java) }
    val userRepository = remember { UserRepository(apiService) }
    val perfilFactory = remember { PerfilViewModelFactory(userRepository) }
    println("📌 [NavHost] CURRENT ROUTE = $currentRoute")

    Scaffold(
        bottomBar = {
            val hideBar = currentRoute in listOf(Screen.Login.route, Screen.Register.route)
            println("👀 BottomBar visible? ${!hideBar}")
            if (!hideBar) BottomBar(navController)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(Screen.Login.route) {
                println("🟢 Estamos en LOGIN")

                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = {
                        println("➡️ LOGIN OK → HOME")
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        println("➡️ Ir a REGISTER")
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            composable(Screen.Register.route) {
                println("🟣 Estamos en REGISTER")
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

            composable(Screen.Home.route) {
                println("🏠 Estamos en HOME")
                PokeListScreen(navController, pokeViewModel)
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

            composable(Screen.Perfil.route) {
                // Aquí usamos PerfilViewModelFactory para crear el ViewModel
                val perfilViewModel: PerfilViewModel = viewModel(factory = perfilFactory)

                PerfilScreen(
                    viewModel = perfilViewModel,
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Compras.route) {
                val repository = ComprasRepository(apiService)
                val factory = ComprasViewModelFactory(repository)
                val comprasViewModel: ComprasViewModel = viewModel(factory = factory)

                ComprasScreen(
                    viewModel = comprasViewModel,
                    usuarioId = idUsuario
                )
            }

            composable("formulario/{total}") { backStackEntry ->

                val total = backStackEntry.arguments?.getString("total")?.toInt() ?: 0
                val formularioViewModel: FormularioViewModel = viewModel()

                // AHORA SÍ: idUsuario REAL desde la sesión
                val idUsuario by session.getUserId().collectAsState(initial = 0L)

                LaunchedEffect(idUsuario) {
                    formularioViewModel.cargarDatosIniciales(idUsuario)
                    formularioViewModel.setTotal(total)
                }

                FormularioScreen(
                    idUsuario = idUsuario,
                    totalAmount = total,
                    carrito = pokeViewModel.carrito.collectAsState().value,
                    onNavigateToPago = { monto -> navController.navigate("pago/$monto") },
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
                        Toast.makeText(context, "Compra realizada con éxito 🎉", Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }}

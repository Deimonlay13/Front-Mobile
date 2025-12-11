package com.gdl.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gdl.navigation.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import com.gdl.data.UserSession
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@Composable
fun BottomBar(navController: NavHostController) {

    val context = LocalContext.current
    val session = remember { UserSession(context) }
    val scope = rememberCoroutineScope()

    val items = listOf(
        Screen.Home,
        Screen.Compras,
        Screen.Perfil,
        Screen.Salir
    )

    val currentRoute by navController.currentBackStackEntryAsState()

    NavigationBar {
        items.forEach { screen ->

            NavigationBarItem(
                selected = currentRoute?.destination?.route == screen.route,
                onClick = {
                    println("👉 CLICK EN: ${screen.route}")

                    when (screen) {
                        Screen.Salir -> {
                            println("🚪 CERRANDO SESIÓN...")

                            scope.launch {
                                session.clear()
                                println("🧽 SESSION CLEAR ejecutado!")

                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                    launchSingleTop = true
                                }

                                println("➡️ Navegamos a LOGIN limpiando el stack")
                            }
                        }

                        else -> {
                            println("➡️ Navegando hacia ${screen.route}")
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route)
                                launchSingleTop = true
                            }
                        }
                    }
                },
                icon = {
                    when (screen) {
                        Screen.Home -> Icon(Icons.Default.Home, contentDescription = "Home")
                        Screen.Compras -> Icon(Icons.Default.ShoppingCart, contentDescription = "Compras")
                        Screen.Perfil -> Icon(Icons.Default.Person, contentDescription = "Perfil")
                        Screen.Salir -> Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Salir")
                        else -> {}
                    }
                },
                label = {
                    when (screen) {
                        Screen.Home -> Text("Home")
                        Screen.Compras -> Text("Mis Compras")
                        Screen.Perfil -> Text("Perfil")
                        Screen.Salir -> Text("Salir")
                        else -> {}
                    }
                }
            )
        }
    }
}

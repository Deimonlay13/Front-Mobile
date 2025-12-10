package com.gdl.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.gdl.screens.CarritoScreen
import com.gdl.viewmodel.PokeViewModel
import com.gdl.view.PokeListScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    pokeViewModel: PokeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // Pantalla principal
        composable("home") {
            PokeListScreen(
                navController = navController,
                vm = pokeViewModel
            )
        }

        // Pantalla del carrito
        composable("carrito") {
            CarritoScreen(
                viewModel = pokeViewModel ,
                onVolver = {
                    navController.popBackStack()
                }



            )
        }
    }
}

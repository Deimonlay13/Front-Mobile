package com.gdl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gdl.ui.theme.FrontMobileTheme
import com.gdl.view.FormularioScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FrontMobileTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "formulario"
    ) {

        // =========================
        // PANTALLA FORMULARIO
        // =========================
        composable("formulario") {
            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    FormularioScreen(
                        idUsuario = 1L,
                        onNavigateToPago = {
                            navController.navigate("pago")
                        }
                    )
                }
            }
        }

        // =========================
        // PANTALLA PAGO (TEMPORAL)
        // =========================
        composable("pago") {
            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    PantallaPago()
                }
            }
        }
    }
}

@Composable
fun PantallaPago() {
    androidx.compose.material3.Text("Pantalla de pago — aquí irá Stripe o tu pasarela")
}


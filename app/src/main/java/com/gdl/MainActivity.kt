package com.gdl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gdl.ui.theme.FrontMobileTheme
import com.gdl.navigation.AppNavigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita edge-to-edge para que la UI use toda la pantalla
        enableEdgeToEdge()

        setContent {
            // Aplica tu tema con colores y tipografía definidos
            FrontMobileTheme {

                // Surface principal
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Controlador de navegación
                    val navController = rememberNavController()

                    // Aquí va tu navegación entre pantallas
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}

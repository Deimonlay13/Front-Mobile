package com.gdl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.gdl.navigation.AppNavigation
import com.gdl.viewmodel.PokeViewModel


class MainActivity : ComponentActivity() {

    private val pokeViewModel: PokeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            Surface(color = MaterialTheme.colorScheme.background) {

                AppNavigation(
                    navController = navController,
                    pokeViewModel = pokeViewModel
                )
            }
        }
    }
}

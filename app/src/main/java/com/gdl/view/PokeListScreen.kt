package com.gdl.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gdl.viewmodel.PokeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeListScreen(
    navController: NavHostController,
    vm: PokeViewModel
) {

    Column(modifier = Modifier.fillMaxSize()) {

        // ⭐ Top Bar con botón de carrito
        TopAppBar(
            title = { Text("Cartas Pokémon") },
            actions = {
                IconButton(onClick = { navController.navigate("carrito") }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                }
            }
        )

        // ⭐ Estados del ViewModel
        val cartas by vm.cartas.collectAsState()
        val cargando by vm.cargando.collectAsState()

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
        ) {

            Text(
                text = "Descubre nuestra colección Pokémon",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )

            // ⭐ Cargando
            if (cargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // ⭐ Grid de cartas
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartas) { carta ->
                        ProductoCard(carta, vm)
                    }
                }
            }
        }
    }
}

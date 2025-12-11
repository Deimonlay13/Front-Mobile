package com.gdl.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.gdl.viewmodel.PokeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeListScreen(
    navController: NavHostController,
    vm: PokeViewModel
) {
    val yellow = Color(0xFFFFCC01)
    val carrito by vm.carrito.collectAsState()
    val cartas by vm.cartas.collectAsState()
    val cargando by vm.cargando.collectAsState()

    // ⭐ USAMOS BOX PARA PONER EL FONDO
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // ⭐ IMAGEN DE FONDO
        Image(
            painter = rememberAsyncImagePainter(
                "https://tcg.pokemon.com/assets/img/sv-expansions/black-white/header/header/hero-image/zsv-10pt5-medium-up.jpg"
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // ⭐ CAPA NEGRA CON TRANSPARENCIA (OPACIDAD = 60%)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA000000)) // negro con transparencia
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // ⭐ TOP BAR
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xCC000000), // negro con un poco de transparencia
                    titleContentColor = yellow,
                    actionIconContentColor = Color.White
                ),
                title = {
                    Text(
                        text = "Cartas Pokémon",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = yellow,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    BadgedBox(
                        badge = {
                            if (carrito.isNotEmpty()) {
                                Badge(
                                    containerColor = yellow,
                                    contentColor = Color.Black
                                ) {
                                    Text(carrito.sumOf { it.cantidad }.toString())
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = { navController.navigate("carrito") }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {



                if (cargando) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = yellow)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(26.dp)
                    ) {
                        items(cartas) { carta ->
                            ProductoCard(carta, vm)
                        }
                    }
                }
            }
        }
    }
}

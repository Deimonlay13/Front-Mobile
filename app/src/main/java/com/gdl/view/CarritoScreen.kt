package com.gdl.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gdl.view.CarritoItemRow
import com.gdl.viewmodel.PokeViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun CarritoScreen(
    viewModel: PokeViewModel,
    onComprar: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val carrito = viewModel.carrito.collectAsState().value
    val totalGeneral = carrito.sumOf { it.cantidad * it.producto.precio }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // TOP BAR
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.Black
                )
            }

            Text(
                text = "Tu carrito 🛒",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LISTA DE ITEMS
        carrito.forEach { item ->
            CarritoItemRow(
                item = item,
                onSumar = { viewModel.agregarAlCarrito(item.producto, 1) },
                onRestar = { viewModel.restarCantidad(item.producto) },
                onEliminar = { viewModel.eliminarDelCarrito(item.producto) }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // TOTAL GENERAL
        Text(
            text = "Total: $${totalGeneral}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { onComprar(totalGeneral) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E8C4E)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(text = "Comprar", fontSize = 18.sp, color = Color.White)
        }
    }
}

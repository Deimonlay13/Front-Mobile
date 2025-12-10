package com.gdl.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.gdl.models.Producto
import com.gdl.viewmodel.PokeViewModel
import com.gdl.view.PokeListScreen

@Composable
fun ProductoCard(
    producto: Producto,
    vm: PokeViewModel
) {
    var cantidad by remember { mutableStateOf(0) } // <- empieza en 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            // IMAGEN
            Image(
                painter = rememberAsyncImagePainter(producto.img),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // NOMBRE + PRECIO
            Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
            Text("Precio: $${producto.precio}", style = MaterialTheme.typography.bodyMedium)
            Text("Stock: ${producto.stock}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            // CONTADOR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { if (cantidad > 0) cantidad-- },
                ) { Text("-") }

                Text(
                    text = cantidad.toString(),
                    style = MaterialTheme.typography.titleMedium
                )

                Button(
                    onClick = { if (cantidad < producto.stock) cantidad++ }
                ) { Text("+") }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // BOTÓN AGREGAR AL CARRITO
            Button(
                onClick = {
                    if (cantidad > 0) {
                        vm.agregarAlCarrito(producto, cantidad)
                        cantidad = 0 // <- resetear contador a 0
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar al carrito")
            }
        }
    }
}

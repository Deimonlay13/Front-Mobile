package com.gdl.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.gdl.models.Producto
import com.gdl.viewmodel.PokeViewModel

@Composable
fun ProductoCard(
    producto: Producto,
    vm: PokeViewModel
) {
    val yellow = Color(0xFFFFCC01)
    var cantidad by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()          // ← MUY IMPORTANTE
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xB31A1A1A) // 🖤 gris oscuro con transparencia elegante
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, yellow.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ⭐ IMAGEN
            Image(
                painter = rememberAsyncImagePainter(producto.img),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ⭐ NOMBRE + PRECIO
            Text(
                producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                "Precio: $${producto.precio}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                "Stock: ${producto.stock}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ⭐ CONTADOR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedButton(
                    onClick = { if (cantidad > 0) cantidad-- },
                    border = BorderStroke(1.dp, yellow),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = yellow
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("-")
                }

                Text(
                    text = cantidad.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                OutlinedButton(
                    onClick = { if (cantidad < producto.stock) cantidad++ },
                    border = BorderStroke(1.dp, yellow),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = yellow
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("+")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ⭐ BOTÓN AGREGAR AL CARRITO
            Button(
                onClick = {
                    if (cantidad > 0) {
                        vm.agregarAlCarrito(producto, cantidad)
                        cantidad = 0
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = yellow,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    "Agregar al carrito",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

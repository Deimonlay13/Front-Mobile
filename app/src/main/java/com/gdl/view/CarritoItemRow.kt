package com.gdl.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import com.gdl.models.CarritoItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Delete

@Composable
fun CarritoItemRow(
    item: CarritoItem,
    onSumar: () -> Unit,
    onRestar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // IMAGEN
            Image(
                painter = rememberAsyncImagePainter(item.producto.img),
                contentDescription = item.producto.nombre,
                modifier = Modifier.size(70.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // NOMBRE + PRECIO TOTAL
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.producto.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = "Precio: $${item.producto.precio * item.cantidad}",
                    fontSize = 14.sp
                )

                Text(
                    text = "Cantidad: ${item.cantidad}",
                    fontSize = 14.sp
                )
            }

            // BOTONES DE CANTIDAD CON ICONOS
            Row {
                // Botón restar
                IconButton(onClick = onRestar) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Restar"
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Botón sumar
                IconButton(onClick = onSumar) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Sumar"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botón eliminar
                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar"
                    )
                }
            }
        }
    }
}

package com.gdl.view
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.gdl.models.Producto
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CounterComposable(carta: Producto) {
    var count by remember { mutableStateOf(0) }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Button(onClick = { if (count > 0) count-- }) {
            Text("-")
        }

        Text(text = "$count", modifier = Modifier.padding(horizontal = 12.dp))

        Button(onClick = { if (count < carta.stock) count++ }) {
            Text("+")
        }
        Button(onClick = { if (count < carta.stock) count++ }) {
            Text("+")
        }

    }
}
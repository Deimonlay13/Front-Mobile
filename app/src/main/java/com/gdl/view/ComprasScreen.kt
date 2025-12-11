package com.gdl.view
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.gdl.viewmodel.ComprasViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ComprasScreen(viewModel: ComprasViewModel, usuarioId: Long) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(usuarioId) {
        viewModel.loadCompras(usuarioId)
    }

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (uiState.ventas.isEmpty()) {
        Text("No tienes compras registradas.", modifier = Modifier.padding(16.dp))
        return
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        items(uiState.ventas) { venta ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val fecha = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                            .parse(venta.fechaCreacion)
                        Text("Compra #${venta.idVenta} - ${fecha?.let { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it) }}")

                        Button(onClick = { viewModel.verDetalle(venta.idVenta) }) {
                            Text("Ver detalle")
                        }
                    }

                    if (uiState.ventaSeleccionada == venta.idVenta) {
                        Spacer(Modifier.height(8.dp))
                        Column {
                            uiState.detalles.forEach { detalle ->
                                Row(
                                    Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (!detalle.imagenProducto.isNullOrEmpty()) {
                                        Image(
                                            painter = rememberAsyncImagePainter(detalle.imagenProducto),
                                            contentDescription = detalle.nombreProducto,
                                            modifier = Modifier.size(50.dp, 70.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Text(detalle.nombreProducto ?: "Carta desconocida")
                                    Spacer(Modifier.weight(1f))
                                    Text("${detalle.cantidad} x $${detalle.precio}")
                                }
                            }

                            Spacer(Modifier.height(8.dp))
                            Text("Total: $${venta.total}", modifier = Modifier.align(Alignment.End))
                        }
                    }
                }
            }
        }
    }
}

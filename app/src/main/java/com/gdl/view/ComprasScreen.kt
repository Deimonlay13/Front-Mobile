package com.gdl.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.gdl.viewmodel.ComprasViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ComprasScreen(viewModel: ComprasViewModel, usuarioId: Long) {

    val uiState by viewModel.uiState.collectAsState()

    val yellow = Color(0xFFFFCC01)

    // Cargar compras
    LaunchedEffect(usuarioId) {
        viewModel.loadCompras(usuarioId)
    }

    Box(Modifier.fillMaxSize()) {

        // 🟦 Fondo Pokémon
        AsyncImage(
            model = "https://d1i787aglh9bmb.cloudfront.net/assets/img/home/featured-switcher/me02/booster-art-4-large-up.jpg",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🖤 Capa oscura
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.70f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Título estilo Pokémon
            Text(
                text = "Mis Compras",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = yellow,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = yellow)
                }
                return@Column
            }

            if (uiState.ventas.isEmpty()) {
                Text(
                    "No tienes compras registradas.",
                    modifier = Modifier.padding(16.dp),
                    color = Color.LightGray
                )
                return@Column
            }

            // LISTA
            LazyColumn {
                items(uiState.ventas) { venta ->

                    // CARD estilo Pokémon
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.45f)
                        ),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {

                        Column(Modifier.padding(16.dp)) {

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                val fecha = try {
                                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                                        .parse(venta.fechaCreacion)
                                } catch (e: Exception) {
                                    null
                                }

                                Text(
                                    "Compra #${venta.idVenta}\n" +
                                            (fecha?.let {
                                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
                                            } ?: "Fecha inválida"),
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Button(
                                    onClick = { viewModel.verDetalle(venta.idVenta) },
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = yellow,
                                        contentColor = Color.Black
                                    )
                                ) {
                                    Text("Ver detalle", fontWeight = FontWeight.Bold)
                                }
                            }

                            // DETALLE DESPLEGADO
                            if (uiState.ventaSeleccionada == venta.idVenta) {

                                Divider(
                                    Modifier.padding(vertical = 12.dp),
                                    color = Color.White.copy(alpha = 0.4f)
                                )

                                uiState.detalles.forEach { detalle ->

                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        if (!detalle.imagenProducto.isNullOrEmpty()) {
                                            AsyncImage(
                                                model = detalle.imagenProducto,
                                                contentDescription = detalle.nombreProducto,
                                                modifier = Modifier
                                                    .size(60.dp, 80.dp)
                                                    .clip(RoundedCornerShape(8.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                        }

                                        Spacer(Modifier.width(10.dp))

                                        Column {
                                            Text(
                                                detalle.nombreProducto ?: "Carta desconocida",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Text(
                                                "${detalle.cantidad} x $${detalle.precio}",
                                                color = Color.LightGray
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.height(10.dp))

                                Text(
                                    "Total: $${venta.total}",
                                    modifier = Modifier.align(Alignment.End),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = yellow,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

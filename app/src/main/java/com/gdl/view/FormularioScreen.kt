package com.gdl.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.gdl.models.CarritoItem
import com.gdl.viewmodel.FormularioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    idUsuario: Long,
    totalAmount: Int,
    carrito: List<CarritoItem>,
    onNavigateToPago: (Int) -> Unit,
    viewModel: FormularioViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    val scroll = rememberScrollState()
    val yellow = Color(0xFFFFCC01)

    // Cargar datos al entrar
    LaunchedEffect(idUsuario) {
        viewModel.cargarDatosIniciales(idUsuario)
        viewModel.setTotal(totalAmount)
    }

    // Navegar al pago
    LaunchedEffect(state.navigateToPago) {
        if (state.navigateToPago) {
            onNavigateToPago(state.totalAmount)
            viewModel.resetNavigation()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // IMAGEN DE FONDO
        AsyncImage(
            model = "https://tcg.pokemon.com/assets/img/home/featured-switcher/booster-art-4-large-up.jpg",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // CAPA OSCURA
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
        )

        // CONTENIDO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Completa tu información",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = yellow,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(24.dp))

            // -----------------------------
            //        CUADRO GRANDE
            // -----------------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.40f)
                )
            ) {

                Column(Modifier.padding(20.dp)) {

                    // ------- ESTILO DE CAMPOS -------
                    @Composable
                    fun fieldColors() = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = yellow,
                        unfocusedBorderColor = yellow.copy(alpha = 0.6f),
                        focusedLabelColor = yellow,
                        unfocusedLabelColor = yellow,
                        focusedLeadingIconColor = yellow,
                        unfocusedLeadingIconColor = yellow,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = yellow
                    )

                    fun Modifier.f() = this.fillMaxWidth().padding(bottom = 14.dp)

                    //   CAMPOS NO EDITABLES (Usuario)
                    DisabledField("Nombre", state.nombre, Icons.Default.Person)
                    DisabledField("Apellido", state.apellido, Icons.Default.Person)
                    DisabledField("Correo", state.correo, Icons.Default.Email)
                    DisabledField("RUT", state.rut, Icons.Default.Badge)

                    Spacer(Modifier.height(12.dp))

                    //  CAMPOS EDITABLES (Dirección)
                    OutlinedTextField(
                        value = state.region,
                        onValueChange = viewModel::onRegionChange,
                        label = { Text("Región") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                        isError = state.validated && state.region.isBlank(),
                        colors = fieldColors(),
                        singleLine = true,
                        modifier = Modifier.f()
                    )

                    OutlinedTextField(
                        value = state.comuna,
                        onValueChange = viewModel::onComunaChange,
                        label = { Text("Comuna") },
                        leadingIcon = { Icon(Icons.Default.LocationCity, null) },
                        isError = state.validated && state.comuna.isBlank(),
                        colors = fieldColors(),
                        singleLine = true,
                        modifier = Modifier.f()
                    )

                    OutlinedTextField(
                        value = state.calle,
                        onValueChange = viewModel::onCalleChange,
                        label = { Text("Calle") },
                        leadingIcon = { Icon(Icons.Default.Home, null) },
                        isError = state.validated && state.calle.isBlank(),
                        colors = fieldColors(),
                        singleLine = true,
                        modifier = Modifier.f()
                    )

                    OutlinedTextField(
                        value = state.numero,
                        onValueChange = viewModel::onNumeroChange,
                        label = { Text("Número") },
                        leadingIcon = { Icon(Icons.Default.Tag, null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.validated && state.numero.isBlank(),
                        colors = fieldColors(),
                        singleLine = true,
                        modifier = Modifier.f()
                    )

                    Spacer(Modifier.height(8.dp))

                    // GUARDAR DIRECCIÓN
                    Button(
                        onClick = { viewModel.guardarDireccion(idUsuario) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = yellow,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Guardar dirección", fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(12.dp))

                    // CONTINUAR PAGO
                    Button(
                        onClick = {
                            viewModel.realizarCompra(idUsuario, carrito)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = yellow.copy(alpha = 0.85f),
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Continuar con el pago", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // MODAL
    if (state.showModal) {
        AlertDialog(
            onDismissRequest = { viewModel.cerrarModal() },
            title = { Text(state.modalTitle) },
            text = { Text(state.modalMessage) },
            confirmButton = {
                Button(onClick = { viewModel.cerrarModal() }) { Text("Cerrar") }
            }
        )
    }
}

@Composable
fun DisabledField(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    val yellow = Color(0xFFFFCC01)

    Column(Modifier.padding(bottom = 12.dp)) {
        Text(label, color = yellow, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text(value, color = Color.White)
        }
    }
}

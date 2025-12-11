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
import com.gdl.viewmodel.FormularioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    idUsuario: Long,
    onNavigateToPago: () -> Unit,
    viewModel: FormularioViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    val scrollState = rememberScrollState()
    val yellow = Color(0xFFFFCC01)

    // Cargar datos al entrar
    LaunchedEffect(idUsuario) {
        viewModel.cargarDatosIniciales(idUsuario)
    }

    // Navegar a pago
    LaunchedEffect(state.navigateToPago) {
        if (state.navigateToPago) onNavigateToPago()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo Pokémon
        AsyncImage(
            model = "https://tcg.pokemon.com/assets/img/sv-expansions/destined-rivals/header/sv10-hero-0-medium-up.jpg",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Capa de oscurecimiento
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.60f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState)
        ) {

            // Título
            Text(
                "Completa tu información",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = yellow,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(24.dp))

            // CARD con el contenido del formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.40f)
                ),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
            ) {

                Column(modifier = Modifier.padding(24.dp)) {

                    fun Modifier.fieldSpacing() = this.fillMaxWidth().padding(bottom = 16.dp)

                    @Composable
                    fun styleCampos() = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = yellow,
                        unfocusedBorderColor = yellow.copy(alpha = 0.4f),
                        cursorColor = yellow,
                        focusedLabelColor = yellow,
                        unfocusedLabelColor = yellow,
                        focusedLeadingIconColor = yellow,
                        unfocusedLeadingIconColor = yellow,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )

                    // DATOS PERSONALES
                    Text(
                        "Datos personales",
                        style = MaterialTheme.typography.titleMedium.copy(color = yellow)
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.nombre,
                        onValueChange = {},
                        label = { Text("Nombre") },
                        readOnly = true,
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        colors = styleCampos(),
                        modifier = Modifier.fieldSpacing()
                    )

                    OutlinedTextField(
                        value = state.apellido,
                        onValueChange = {},
                        label = { Text("Apellido") },
                        readOnly = true,
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        colors = styleCampos(),
                        modifier = Modifier.fieldSpacing()
                    )

                    OutlinedTextField(
                        value = state.correo,
                        onValueChange = {},
                        label = { Text("Correo") },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        readOnly = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = styleCampos(),
                        modifier = Modifier.fieldSpacing()
                    )

                    OutlinedTextField(
                        value = state.rut,
                        onValueChange = {},
                        label = { Text("RUT") },
                        leadingIcon = { Icon(Icons.Default.Badge, null) },
                        readOnly = true,
                        colors = styleCampos(),
                        modifier = Modifier.fieldSpacing()
                    )

                    Spacer(Modifier.height(16.dp))

                    // DIRECCIÓN
                    Text(
                        "Dirección",
                        style = MaterialTheme.typography.titleMedium.copy(color = yellow)
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.region,
                        onValueChange = { viewModel.onRegionChange(it) },
                        label = { Text("Región *") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                        colors = styleCampos(),
                        isError = state.validated && state.region.isBlank(),
                        modifier = Modifier.fieldSpacing()
                    )

                    OutlinedTextField(
                        value = state.comuna,
                        onValueChange = { viewModel.onComunaChange(it) },
                        label = { Text("Comuna *") },
                        leadingIcon = { Icon(Icons.Default.LocationCity, null) },
                        colors = styleCampos(),
                        isError = state.validated && state.comuna.isBlank(),
                        modifier = Modifier.fieldSpacing()
                    )

                    OutlinedTextField(
                        value = state.calle,
                        onValueChange = { viewModel.onCalleChange(it) },
                        label = { Text("Calle *") },
                        leadingIcon = { Icon(Icons.Default.Map, null) },
                        colors = styleCampos(),
                        isError = state.validated && state.calle.isBlank(),
                        modifier = Modifier.fieldSpacing()
                    )

                    OutlinedTextField(
                        value = state.numero,
                        onValueChange = { viewModel.onNumeroChange(it) },
                        label = { Text("Número *") },
                        leadingIcon = { Icon(Icons.Default.Home, null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = styleCampos(),
                        isError = state.validated && state.numero.isBlank(),
                        modifier = Modifier.fieldSpacing()
                    )

                    Spacer(Modifier.height(12.dp))

                    // Guardar dirección
                    Button(
                        onClick = { viewModel.guardarDireccion(idUsuario) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = yellow,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Guardar dirección", fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(12.dp))

                    // Continuar con pago
                    Button(
                        onClick = { viewModel.continuarConPago() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
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
                Button(onClick = { viewModel.cerrarModal() }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

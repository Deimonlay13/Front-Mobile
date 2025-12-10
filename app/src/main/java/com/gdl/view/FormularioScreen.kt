package com.gdl.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gdl.viewmodel.FormularioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    idUsuario: Long,
    onNavigateToPago: () -> Unit,
    viewModel: FormularioViewModel = viewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    // Cargar datos iniciales
    LaunchedEffect(Unit) {
        viewModel.cargarDatosIniciales(idUsuario)
    }

    // Navegación
    LaunchedEffect(state.navigateToPago) {
        if (state.navigateToPago) onNavigateToPago()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Completa tus datos para procesar tu compra",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(16.dp))

        // ==========================
        // CORREO
        // ==========================
        OutlinedTextField(
            value = state.correo,
            onValueChange = { viewModel.onCorreoChange(it) },
            label = { Text("Correo electrónico *") },
            isError = state.validated && state.correo.isBlank(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        if (state.validated && state.correo.isBlank()) {
            Text("Ingresa un correo válido.", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        // ==========================
        // NOMBRE
        // ==========================
        OutlinedTextField(
            value = state.nombre,
            onValueChange = { viewModel.onNombreChange(it) },
            label = { Text("Nombre *") },
            isError = state.validated && state.nombre.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        if (state.validated && state.nombre.isBlank()) {
            Text("Este campo es obligatorio.", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        // ==========================
        // APELLIDO
        // ==========================
        OutlinedTextField(
            value = state.apellido,
            onValueChange = { viewModel.onApellidoChange(it) },
            label = { Text("Apellido *") },
            isError = state.validated && state.apellido.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        if (state.validated && state.apellido.isBlank()) {
            Text("Este campo es obligatorio.", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        // ==========================
        // RUT
        // ==========================
        OutlinedTextField(
            value = state.rut,
            onValueChange = { viewModel.onRutChange(it) },
            label = { Text("RUT *") },
            isError = state.validated && state.rut.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        if (state.validated && state.rut.isBlank()) {
            Text("Ingresa tu RUT.", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        // ==========================
        // REGIÓN
        // ==========================
        var expandedRegion by remember { mutableStateOf(false) }
        val regiones = listOf("Metropolitana", "Valparaíso", "Biobío")

        ExposedDropdownMenuBox(
            expanded = expandedRegion,
            onExpandedChange = { expandedRegion = !expandedRegion }
        ) {
            OutlinedTextField(
                value = state.region,
                onValueChange = {},
                readOnly = true,
                label = { Text("Región") },
                trailingIcon = {
                    IconButton(onClick = { expandedRegion = !expandedRegion }) {
                        Icon(Icons.Default.ArrowDropDown, null)
                    }
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            DropdownMenu(
                expanded = expandedRegion,
                onDismissRequest = { expandedRegion = false }
            ) {
                regiones.forEach { region ->
                    DropdownMenuItem(
                        text = { Text(region) },
                        onClick = {
                            viewModel.onRegionChange(region)
                            expandedRegion = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ==========================
        // COMUNA
        // ==========================
        if (state.region.isNotBlank()) {

            val comunas = when (state.region) {
                "Metropolitana" -> listOf("Santiago", "Puente Alto", "Maipú")
                "Valparaíso" -> listOf("Valparaíso", "Viña del Mar", "Quilpué")
                "Biobío" -> listOf("Concepción", "Talcahuano", "Hualpén")
                else -> emptyList()
            }

            val opcionesComuna = comunas + "otra"
            var expandedComuna by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expandedComuna,
                onExpandedChange = { expandedComuna = !expandedComuna }
            ) {
                OutlinedTextField(
                    value = state.comuna,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Comuna") },
                    trailingIcon = {
                        IconButton(onClick = { expandedComuna = !expandedComuna }) {
                            Icon(Icons.Default.ArrowDropDown, null)
                        }
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                DropdownMenu(
                    expanded = expandedComuna,
                    onDismissRequest = { expandedComuna = false }
                ) {
                    opcionesComuna.forEach { comuna ->
                        DropdownMenuItem(
                            text = { Text(comuna) },
                            onClick = {
                                viewModel.onComunaChange(comuna)
                                expandedComuna = false
                            }
                        )
                    }
                }
            }
        }

        // ==========================
        // OTRA COMUNA
        // ==========================
        if (state.comuna == "otra") {
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.otraComuna,
                onValueChange = { viewModel.onOtraComunaChange(it) },
                label = { Text("Ingrese su comuna") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(16.dp))

        // ==========================
        // CALLE
        // ==========================
        OutlinedTextField(
            value = state.calle,
            onValueChange = { viewModel.onCalleChange(it) },
            label = { Text("Dirección *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // ==========================
        // NÚMERO
        // ==========================
        OutlinedTextField(
            value = state.numero,
            onValueChange = { viewModel.onNumeroChange(it) },
            label = { Text("Número *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // ==========================
        // BOTÓN GUARDAR
        // ==========================
        Button(
            onClick = { viewModel.guardarDireccion(idUsuario) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Dirección")
        }

        Spacer(Modifier.height(16.dp))

        // ==========================
        // BOTÓN CONTINUAR AL PAGO
        // ==========================
        Button(
            onClick = { viewModel.continuarConPago() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar con el pago")
        }

        // ==========================
        // ALERTA VERDE
        // ==========================
        if (state.showSuccess) {
            Spacer(Modifier.height(16.dp))
            Text("✔ Datos ingresados correctamente.", color = MaterialTheme.colorScheme.primary)
        }
    }

    // ==========================
    // MODAL
    // ==========================
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

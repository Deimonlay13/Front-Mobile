package com.gdl.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
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

    // Cargar datos cuando entramos
    LaunchedEffect(idUsuario) {
        println("⚠ ID RECIBIDO FORMULARIO = $idUsuario")  // Debug
        viewModel.cargarDatosIniciales(idUsuario)
    }

    // Navegación automática
    LaunchedEffect(state.navigateToPago) {
        if (state.navigateToPago) onNavigateToPago()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ============================================================
        // TÍTULO
        // ============================================================
        Text(
            text = "Completa tu información",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(20.dp))

        // ============================================================
        // DATOS PERSONALES (READONLY)
        // ============================================================
        Text("Datos personales", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.nombre,
            onValueChange = {},
            label = { Text("Nombre") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.apellido,
            onValueChange = {},
            label = { Text("Apellido") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.correo,
            onValueChange = {},
            label = { Text("Correo") },
            readOnly = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.rut,
            onValueChange = {},
            label = { Text("RUT") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))


        // ============================================================
        // DIRECCIÓN (EDITABLE)
        // ============================================================
        Text("Dirección", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.region,
            onValueChange = { viewModel.onRegionChange(it) },
            label = { Text("Región *") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.validated && state.region.isBlank()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.comuna,
            onValueChange = { viewModel.onComunaChange(it) },
            label = { Text("Comuna *") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.validated && state.comuna.isBlank()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.calle,
            onValueChange = { viewModel.onCalleChange(it) },
            label = { Text("Calle *") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.validated && state.calle.isBlank()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.numero,
            onValueChange = { viewModel.onNumeroChange(it) },
            label = { Text("Número *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = state.validated && state.numero.isBlank()
        )

        Spacer(Modifier.height(20.dp))


        // ============================================================
        // BOTÓN GUARDAR
        // ============================================================
        Button(
            onClick = { viewModel.guardarDireccion(idUsuario) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar dirección")
        }

        Spacer(Modifier.height(16.dp))

        // ============================================================
        // CONTINUAR
        // ============================================================
        Button(
            onClick = { viewModel.continuarConPago() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar con el pago")
        }
    }

    // ============================================================
    // MODAL
    // ============================================================
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

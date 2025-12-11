package com.gdl.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.gdl.data.UserSession
import com.gdl.viewmodel.PerfilViewModel
import kotlinx.coroutines.launch

@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val session = remember { UserSession(context) }
    val coroutineScope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Cargar usuario al abrir la pantalla
    LaunchedEffect(Unit) {
        session.getUserId().collect { id ->
            if (id != 0L) {
                viewModel.loadUser(id)
            }
        }
    }

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val user = uiState.user
    if (user == null) {
        Text("No hay información del usuario.")
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        uiState.message?.let { msg ->
            Text(msg, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(12.dp))
        }

        // EMAIL
        OutlinedTextField(
            value = user.email,
            onValueChange = {},
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        Spacer(Modifier.height(12.dp))

        // RUT
        OutlinedTextField(
            value = user.rut,
            onValueChange = {},
            label = { Text("RUT") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        Spacer(Modifier.height(12.dp))

        // NOMBRE
        OutlinedTextField(
            value = user.nombre,
            onValueChange = { viewModel.updateField("nombre", it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // APELLIDO
        OutlinedTextField(
            value = user.apellido,
            onValueChange = { viewModel.updateField("apellido", it) },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // CONTRASEÑA
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Contraseña (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(Modifier.height(20.dp))

        // BOTÓN GUARDAR
        Button(
            onClick = { viewModel.saveChanges() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        Spacer(Modifier.height(12.dp))

        // BOTÓN ELIMINAR
        Button(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
        ) {
            Text("Eliminar cuenta")
        }
    }

    // DIALOGO CONFIRMACION
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = {
                Text("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteUser()

                        // Limpiar sesión y salir usando coroutineScope
                        coroutineScope.launch {
                            session.clear() // suspending function
                            onLogout()
                        }
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

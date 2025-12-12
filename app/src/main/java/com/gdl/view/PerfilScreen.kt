package com.gdl.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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
    val scrollState = rememberScrollState()

    val yellow = Color(0xFFFFCC01)

    // Cargar usuario al abrir la pantalla
    LaunchedEffect(Unit) {
        session.getUserId().collect { id ->
            if (id != 0L) viewModel.loadUser(id)
        }
    }

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val user = uiState.user ?: run {
        Text("No hay información del usuario.")
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo con imagen
        AsyncImage(
            model = "https://tcg.pokemon.com/assets/img/home/featured-switcher/booster-art-2-large-up.jpg",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Capa oscura
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.70f))
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Mi Perfil",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = yellow,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(24.dp))

            // Card con estilo
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.40f)
                ),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
            ) {

                Column(Modifier.padding(24.dp)) {

                    fun Modifier.fieldSpacing() = this
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)

                    // ==== colores de campos (ahora incluye colores "disabled" visibles) ====
                    @Composable
                    fun fieldColors() = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = yellow,
                        unfocusedBorderColor = yellow.copy(alpha = 0.5f),
                        cursorColor = yellow,
                        focusedLabelColor = yellow,
                        unfocusedLabelColor = yellow.copy(alpha = 0.9f),
                        focusedLeadingIconColor = yellow,
                        unfocusedLeadingIconColor = yellow.copy(alpha = 0.9f),
                        focusedTrailingIconColor = yellow,
                        unfocusedTrailingIconColor = yellow.copy(alpha = 0.9f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        // <<< importantes: colores para campos disabled >>>
                        disabledTextColor = Color.LightGray,
                        disabledLabelColor = Color.LightGray,
                        disabledLeadingIconColor = Color.LightGray,
                        disabledTrailingIconColor = Color.LightGray,
                        disabledPlaceholderColor = Color.LightGray
                    )

                    // EMAIL (con fallback si viene vacío)
                    OutlinedTextField(
                        value = if (user.email.isBlank()) "Correo no disponible" else user.email,
                        onValueChange = {},
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        enabled = false,
                        colors = fieldColors(),
                        modifier = Modifier.fieldSpacing()
                    )

                    // RUT (con fallback si viene vacío)
                    OutlinedTextField(
                        value = if (user.rut.isBlank()) "RUT no disponible" else user.rut,
                        onValueChange = {},
                        label = { Text("RUT") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                        enabled = false,
                        colors = fieldColors(),
                        modifier = Modifier.fieldSpacing()
                    )

                    // NOMBRE
                    OutlinedTextField(
                        value = user.nombre,
                        onValueChange = { viewModel.updateField("nombre", it) },
                        label = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        colors = fieldColors(),
                        modifier = Modifier.fieldSpacing()
                    )

                    // APELLIDO
                    OutlinedTextField(
                        value = user.apellido,
                        onValueChange = { viewModel.updateField("apellido", it) },
                        label = { Text("Apellido") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        colors = fieldColors(),
                        modifier = Modifier.fieldSpacing()
                    )

                    // CONTRASEÑA
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { viewModel.updatePassword(it) },
                        label = { Text("Contraseña (opcional)") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardOptions.Default.keyboardType),
                        colors = fieldColors(),
                        modifier = Modifier.fieldSpacing()
                    )

                    Spacer(Modifier.height(12.dp))

                    // BOTÓN GUARDAR
                    Button(
                        onClick = { viewModel.saveChanges() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = yellow,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Guardar",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // BOTÓN ELIMINAR
                    Button(
                        onClick = { showDeleteDialog = true },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Eliminar cuenta")
                    }
                }
            }
        }
    }

    // DIALOGO ELIMINAR
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Deseas eliminar tu cuenta? Esta acción es irreversible.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteUser()
                        coroutineScope.launch {
                            session.clear()
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

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
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.gdl.viewmodel.RegisterResult
import com.gdl.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onRegisterSuccess: () -> Unit = {},
    onBackToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val yellow = Color(0xFFFFCC01)

    Box(modifier = Modifier.fillMaxSize()) {

        // 🖼 Fondo con imagen
        AsyncImage(
            model = "https://tcg.pokemon.com/assets/img/home/featured-switcher/booster-art-1-large-up.jpg",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🌑 Capa oscura para contraste
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.70f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = yellow,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            RegisterForm(
                name = uiState.name,
                apellido = uiState.apellido,
                rut = uiState.rut,
                email = uiState.email,
                password = uiState.password,
                confirmPassword = uiState.confirmPassword,
                onNameChange = viewModel::onNameChange,
                onApellidoChange = viewModel::onApellidoChange,
                onRutChange = viewModel::onRutChange,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                onRegisterClick = viewModel::onRegisterClick,
                isLoading = uiState.isLoading,
                nameError = uiState.nameError,
                apellidoError = uiState.apellidoError,
                rutError = uiState.rutError,
                emailError = uiState.emailError,
                passwordError = uiState.passwordError,
                confirmPasswordError = uiState.confirmPasswordError,
                onBackToLogin = onBackToLogin
            )

            LaunchedEffect(uiState.registerResult) {
                when (val result = uiState.registerResult) {
                    is RegisterResult.Success -> {
                        onRegisterSuccess()
                        viewModel.resetRegisterResult()
                    }
                    is RegisterResult.Error -> viewModel.resetRegisterResult()
                    else -> Unit
                }
            }
        }
    }
}

@Composable
private fun RegisterForm(
    name: String,
    apellido: String,
    rut: String,
    email: String,
    password: String,
    confirmPassword: String,
    onNameChange: (String) -> Unit,
    onApellidoChange: (String) -> Unit,
    onRutChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    isLoading: Boolean,
    nameError: String?,
    apellidoError: String?,
    rutError: String?,
    emailError: String?,
    passwordError: String?,
    confirmPasswordError: String?,
    onBackToLogin: () -> Unit
) {
    val yellow = Color(0xFFFFCC01)

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

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
            fun fieldColors() = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = yellow,
                unfocusedBorderColor = yellow.copy(alpha = 0.5f),
                cursorColor = yellow,
                focusedLabelColor = yellow,
                unfocusedLabelColor = yellow,
                focusedLeadingIconColor = yellow,
                unfocusedLeadingIconColor = yellow,
                focusedTrailingIconColor = yellow,
                unfocusedTrailingIconColor = yellow,
                focusedPlaceholderColor = yellow.copy(alpha = 0.6f),
                unfocusedPlaceholderColor = yellow.copy(alpha = 0.4f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )

            // Campos (nombre, apellido, rut, etc.)
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Nombre") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = nameError != null,
                singleLine = true,
                enabled = !isLoading,
                colors = fieldColors(),
                modifier = Modifier.fieldSpacing()
            )
            if (nameError != null) Text(nameError, color = Color.Red)

            OutlinedTextField(
                value = apellido,
                onValueChange = onApellidoChange,
                label = { Text("Apellido") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = apellidoError != null,
                singleLine = true,
                enabled = !isLoading,
                colors = fieldColors(),
                modifier = Modifier.fieldSpacing()
            )
            if (apellidoError != null) Text(apellidoError, color = Color.Red)

            OutlinedTextField(
                value = rut,
                onValueChange = onRutChange,
                label = { Text("RUT") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                isError = rutError != null,
                singleLine = true,
                enabled = !isLoading,
                colors = fieldColors(),
                modifier = Modifier.fieldSpacing()
            )
            if (rutError != null) Text(rutError, color = Color.Red)

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                isError = emailError != null,
                singleLine = true,
                enabled = !isLoading,
                colors = fieldColors(),
                modifier = Modifier.fieldSpacing()
            )
            if (emailError != null) Text(emailError, color = Color.Red)

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = yellow
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordError != null,
                singleLine = true,
                enabled = !isLoading,
                colors = fieldColors(),
                modifier = Modifier.fieldSpacing()
            )
            if (passwordError != null) Text(passwordError, color = Color.Red)

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = { Text("Confirmar contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = yellow
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = confirmPasswordError != null,
                singleLine = true,
                enabled = !isLoading,
                colors = fieldColors(),
                modifier = Modifier.fieldSpacing()
            )
            if (confirmPasswordError != null) Text(confirmPasswordError, color = Color.Red)

            Spacer(modifier = Modifier.height(8.dp))

            // Botón
            Button(
                onClick = onRegisterClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = yellow,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Crear cuenta",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = onBackToLogin) {

                    Row {
                        Text(
                            text = "¿Ya tienes cuenta? ",
                            color = Color.White
                        )
                        Text(
                            text = "Inicia sesión",
                            color = Color(0xFFFFCC01)
                        )
                    }

                }
            }
        }
    }
}

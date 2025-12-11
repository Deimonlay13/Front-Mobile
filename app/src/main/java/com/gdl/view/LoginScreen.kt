package com.gdl.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.TextButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.gdl.viewmodel.LoginViewModel
import com.gdl.data.UserSession


@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val session = remember { UserSession(context) }

    // Detecta login exitoso → guarda sesión y navega
    LaunchedEffect(uiState.loginResponse) {
        uiState.loginResponse?.let { resp ->
            session.saveUserSession(resp.id, resp.token)
            onLoginSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🌌 Fondo desde URL
        AsyncImage(
            model = "https://tcg.pokemon.com/assets/img/home/featured-switcher/booster-art-1-large-up.jpg",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🟤 Capa oscura para contraste
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.70f))
        )

        // ✨ Contenido del login
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Header(title = "Bienvenido de nuevo")

            Spacer(modifier = Modifier.height(40.dp))

            LoginForm(
                email = uiState.email,
                password = uiState.password,
                emailError = uiState.emailError,
                passwordError = uiState.passwordError,
                passwordVisible = uiState.passwordVisible,
                isLoading = uiState.isLoading,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onPasswordVisibilityToggle = viewModel::togglePasswordVisibility,
                onLoginClick = viewModel::onLoginClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Row {
                    Text(
                        text = "¿No tienes cuenta? ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Text(
                        text = "Regístrate",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFFFCC01) // Amarillo Pokémon
                    )
                }
            }

            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ErrorMessage(
                    message = uiState.errorMessage ?: "",
                    onDismiss = viewModel::resetLogin
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// -------------------------------------------------------------

@Composable
private fun LoginForm(
    email: String,
    password: String,
    emailError: String?,
    passwordError: String?,
    passwordVisible: Boolean,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onLoginClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.40f) // Transparencia elegante
        ),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.75f)) // Borde glass
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            val yellow = Color(0xFFFFCC01)

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email", color = yellow) },
                placeholder = { Text("ejemplo@correo.com", color = yellow.copy(alpha = 0.7f)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = yellow) },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = yellow,
                    unfocusedBorderColor = yellow.copy(alpha = 0.6f),
                    focusedLabelColor = yellow,
                    cursorColor = yellow,
                    unfocusedLabelColor = yellow,
                    focusedLeadingIconColor = yellow,
                    unfocusedLeadingIconColor = yellow,
                    focusedPlaceholderColor = yellow.copy(alpha = 0.7f),
                    unfocusedPlaceholderColor = yellow.copy(alpha = 0.5f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                supportingText = {
                    if (emailError != null) {
                        Text(emailError, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña", color = yellow) },
                placeholder = { Text("Mínimo 6 caracteres", color = yellow.copy(alpha = 0.7f)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Contraseña", tint = yellow) },
                trailingIcon = {
                    IconButton(onClick = onPasswordVisibilityToggle) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = yellow
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = yellow,
                    unfocusedBorderColor = yellow.copy(alpha = 0.6f),
                    focusedLabelColor = yellow,
                    cursorColor = yellow,
                    unfocusedLabelColor = yellow,
                    focusedLeadingIconColor = yellow,
                    unfocusedLeadingIconColor = yellow,
                    focusedTrailingIconColor = yellow,
                    unfocusedTrailingIconColor = yellow,
                    focusedPlaceholderColor = yellow.copy(alpha = 0.7f),
                    unfocusedPlaceholderColor = yellow.copy(alpha = 0.5f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                supportingText = {
                    if (passwordError != null) {
                        Text(passwordError, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFCC01),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(message: String, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onDismiss) { Text("OK") }
        }
    }
}

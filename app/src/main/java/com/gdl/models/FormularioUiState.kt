package com.gdl.models

data class FormularioUiState(

    // Campos principales
    val correo: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val rut: String = "",

    // Dirección
    val region: String = "",
    val comuna: String = "",
    val otraComuna: String = "",
    val calle: String = "",
    val numero: String = "",

    // Validación general del formulario
    val validated: Boolean = false,
    val showSuccess: Boolean = false,

    // Modal (equivalente al modal de éxito / error del React)
    val showModal: Boolean = false,
    val modalTitle: String = "",
    val modalMessage: String = "",

    // Estados de carga
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,

    // Resumen del carrito (por ahora vacío, lo agregaremos cuando migremos el cart)
    val totalCarrito: Int = 0,

    // Navegación
    val navigateToPago: Boolean = false
)

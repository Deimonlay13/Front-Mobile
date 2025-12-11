package com.gdl.models

data class FormularioUiState(
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val rut: String = "",

    val region: String = "",
    val comuna: String = "",
    val calle: String = "",
    val numero: String = "",

    val validated: Boolean = false,
    val isSaving: Boolean = false,

    val showModal: Boolean = false,
    val modalTitle: String = "",
    val modalMessage: String = "",

    val navigateToPago: Boolean = false
)


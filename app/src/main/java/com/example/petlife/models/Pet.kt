package com.example.petlife.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    val id: Int = 0,  // Valor padrão
    val name: String = "",  // Valor padrão
    val species: String = "",  // Valor padrão
    val breed: String = "",  // Valor padrão
    val age: Int = 0,  // Valor padrão
    val weight: Double = 0.0,  // Valor padrão
    val lastVetVisit: String = ""  // Valor padrão
)

    : Parcelable

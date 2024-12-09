package com.example.petlife.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    val id: Long = 0,
    val name: String = "",
    val species: String = "",
    val breed: String = "",
    val age: Int = 0,
    val weight: Double = 0.0,
    val lastVetVisit: String = ""
)

    : Parcelable

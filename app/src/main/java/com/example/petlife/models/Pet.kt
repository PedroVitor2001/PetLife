package com.example.petlife.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    val id: Long = 0,
    val name: String = "",
    val type: String = "",
    val color: String = "",
    val birthDate: String = "",
    val size: String = "",
)

    : Parcelable

package com.example.petlife.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    var id: Long = 0,
    val petId: Long = 0,
    val tipo: String = "",
    val descricao: String = "",
    val data: String = ""
) : Parcelable

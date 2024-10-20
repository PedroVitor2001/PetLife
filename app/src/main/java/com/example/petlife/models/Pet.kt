package com.example.petlife.models

data class Pet(
    var nome: String,
    var dataNascimento: String,
    var tipo: String,
    var cor: String,
    var porte: String,
    var ultimaIdaVeterinario: String,
    var ultimaVacina: String,
    var ultimaIdaPetshop: String
)

package com.example.petlife.models

interface PetDao {
    fun createPet(pet: Pet): Long
    fun retrievePet(name: String): Pet
    fun retrievePets(): MutableList<Pet>
    fun updatePet(pet: Pet): Int
    fun deletePet(name: String): Int
}

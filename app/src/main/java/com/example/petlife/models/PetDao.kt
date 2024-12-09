package com.example.petlife.models

interface PetDao {
    fun createPet(pet: Pet): Long
    fun retrievePet(id: Long): Pet
    fun retrievePets(): MutableList<Pet>
    fun updatePet(pet: Pet): Int
    fun deletePet(id: Long): Int
}

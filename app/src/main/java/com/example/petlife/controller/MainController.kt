package com.example.petlife.controller

import com.example.petlife.models.Pet
import com.example.petlife.models.PetDao
import com.example.petlife.models.PetSqliteImpl
import com.example.petlife.ui.MainActivity

class MainController(mainActivity: MainActivity) {
    private val petDao: PetDao = PetSqliteImpl(mainActivity)

    fun insertPet(pet: Pet) = petDao.createPet(pet)
    fun getPet(id: Long) = petDao.retrievePet(id)
    fun getPets() = petDao.retrievePets()
    fun modifyPet(pet: Pet) = petDao.updatePet(pet)
    fun removePet(id: Long) = petDao.deletePet(id)
}

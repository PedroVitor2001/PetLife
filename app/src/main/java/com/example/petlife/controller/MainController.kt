package com.example.petlife.controller

import android.content.Context
import com.example.petlife.models.EventSqliteImpl
import com.example.petlife.models.Event
import com.example.petlife.models.EventDAO
import com.example.petlife.models.Pet
import com.example.petlife.models.PetDAO
import com.example.petlife.models.PetSqliteImpl

class MainController(context: Context) {
    private val petDao: PetDAO = PetSqliteImpl(context)
    private val eventDao: EventDAO = EventSqliteImpl(context)

    fun insertEvent(event: Event) = eventDao.createEvent(event)
    fun getEvent(id: Long) = eventDao.retrieveEvent(id)
    fun getEvents(petId: Long? = null) = if (petId != null) {
        eventDao.retrieveEventsByPetId(petId)
    } else {
        eventDao.retrieveEvents()
    }

    fun modifyEvent(event: Event) = eventDao.updateEvent(event)
    fun removeEvent(id: Long) = eventDao.deleteEvent(id)

    fun insertPet(pet: Pet) = petDao.createPet(pet)
    fun getPet(id: Long) = petDao.retrievePet(id)
    fun getPets() = petDao.retrievePets()
    fun modifyPet(pet: Pet) = petDao.updatePet(pet)
    fun removePet(id: Long) = petDao.deletePet(id)
}

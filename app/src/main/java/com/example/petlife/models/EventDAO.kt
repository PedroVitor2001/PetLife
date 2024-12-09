package com.example.petlife.models

interface EventDAO {
    fun createEvent(event: Event): Long
    fun retrieveEvent(id: Long): Event
    fun retrieveEvents(): MutableList<Event>
    fun retrieveEventsByPetId(petId: Long): MutableList<Event>
    fun updateEvent(event: Event): Int
    fun deleteEvent(id: Long): Int
}

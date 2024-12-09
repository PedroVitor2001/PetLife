package com.example.petlife.models

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.petlife.R

class EventSqliteImpl(context: Context) : EventDAO {

    companion object {
        private const val EVENT_DATABASE_FILE = "petlife"
        private const val EVENT_TABLE = "event"
        private const val ID_COLUMN = "id"
        private const val PET_ID_COLUMN = "petId"
        private const val DESCRICAO_COLUMN = "descricao"
        private const val TIPO_COLUMN = "tipo"
        private const val DATA_COLUMN = "data"

        private const val CREATE_EVENT_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $EVENT_TABLE (" +
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$PET_ID_COLUMN INTEGER, " +
                    "$DESCRICAO_COLUMN TEXT NOT NULL, " +
                    "$TIPO_COLUMN TEXT NOT NULL, " +
                    "$DATA_COLUMN TEXT NOT NULL, " +
                    "FOREIGN KEY($PET_ID_COLUMN) REFERENCES pet($ID_COLUMN));"
    }

    private val eventDatabase: SQLiteDatabase = context.openOrCreateDatabase(
        EVENT_DATABASE_FILE,
        Context.MODE_PRIVATE,
        null
    )

    init {
        try {
            eventDatabase.execSQL(CREATE_EVENT_TABLE_STATEMENT)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    override fun createEvent(event: Event): Long {
        val id = eventDatabase.insert(EVENT_TABLE, null, eventToContentValues(event))

        return id
    }

    override fun retrieveEvent(id: Long): Event {
        val cursor = eventDatabase.query(
            true,
            EVENT_TABLE,
            null,
            "$ID_COLUMN = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            cursorToEvento(cursor)
        } else {
            Event()
        }
    }

    override fun retrieveEvents(): MutableList<Event> {
        val eventList = mutableListOf<Event>()

        val cursor = eventDatabase.rawQuery("SELECT * FROM $EVENT_TABLE", null)
        while (cursor.moveToNext()) {
            eventList.add(cursorToEvento(cursor))
        }

        return eventList
    }

    override fun retrieveEventsByPetId(petId: Long): MutableList<Event> {
        val eventList = mutableListOf<Event>()

        val cursor = eventDatabase.rawQuery(
            "SELECT * FROM $EVENT_TABLE WHERE $PET_ID_COLUMN = ?",
            arrayOf(petId.toString())
        )

        while (cursor.moveToNext()) {
            eventList.add(cursorToEvento(cursor))
        }
        cursor.close()
        return eventList
    }

    override fun updateEvent(event: Event) = eventDatabase.update(
        EVENT_TABLE,
        eventToContentValues(event),
        "$ID_COLUMN = ?",
        arrayOf(event.id.toString())
    )

    override fun deleteEvent(id: Long) = eventDatabase.delete(
        EVENT_TABLE,
        "$ID_COLUMN = ?",
        arrayOf(id.toString())
    )

    private fun eventToContentValues(event: Event) = ContentValues().apply {
        with(event) {
            put(PET_ID_COLUMN, petId)
            put(DESCRICAO_COLUMN, descricao)
            put(TIPO_COLUMN, tipo)
            put(DATA_COLUMN, data)
        }
    }

    private fun cursorToEvento(cursor: Cursor): Event = with(cursor) {
        Event(
            id = getLong(getColumnIndexOrThrow(ID_COLUMN)),
            petId = getLong(getColumnIndexOrThrow(PET_ID_COLUMN)),
            descricao = getString(getColumnIndexOrThrow(DESCRICAO_COLUMN)),
            tipo = getString(getColumnIndexOrThrow(TIPO_COLUMN)),
            data = getString(getColumnIndexOrThrow(DATA_COLUMN))
        )
    }
}

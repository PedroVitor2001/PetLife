package com.example.petlife.models

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.petlife.R

class PetSqliteImpl(context: Context) : PetDao {
    companion object {
        private const val PET_DATABASE_FILE = "pet"
        private const val PET_TABLE = "pet"
        private const val NAME_COLUMN = "name"
        private const val SPECIES_COLUMN = "species"
        private const val BREED_COLUMN = "breed"
        private const val AGE_COLUMN = "age"
        private const val WEIGHT_COLUMN = "weight"
        private const val LAST_VET_VISIT_COLUMN = "last_vet_visit"

        private const val ID_COLUMN = "id"
        private const val CREATE_PET_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $PET_TABLE (" +
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$NAME_COLUMN TEXT NOT NULL, " +
                    "$SPECIES_COLUMN TEXT NOT NULL, " +
                    "$BREED_COLUMN TEXT NOT NULL, " +
                    "$AGE_COLUMN INTEGER NOT NULL, " +
                    "$WEIGHT_COLUMN REAL NOT NULL, " +
                    "$LAST_VET_VISIT_COLUMN TEXT NOT NULL);"

    }

    private val petDatabase: SQLiteDatabase = context.openOrCreateDatabase(
        PET_DATABASE_FILE,
        MODE_PRIVATE,
        null
    )

    init {
        try {
            petDatabase.execSQL(CREATE_PET_TABLE_STATEMENT)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    override fun createPet(pet: Pet) =
        petDatabase.insert(PET_TABLE, null, petToContentValues(pet))

    override fun retrievePet(name: String): Pet {
        val cursor = petDatabase.query(
            true,
            PET_TABLE,
            null,
            "$NAME_COLUMN = ?",
            arrayOf(name),
            null,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            cursorToPet(cursor)
        } else {
            // Retorna um objeto Pet com valores padrão
            Pet(
                id = 0,  // Valor padrão para id
                name = "",  // Nome vazio
                species = "",  // Espécie vazia
                breed = "",  // Raça vazia
                age = 0,  // Idade padrão
                weight = 0.0,  // Peso padrão
                lastVetVisit = ""  // Data de visita padrão
            )
        }
    }

    override fun retrievePets(): MutableList<Pet> {
        val petList = mutableListOf<Pet>()
        val cursor = petDatabase.rawQuery("SELECT * FROM $PET_TABLE", null)
        while (cursor.moveToNext()) {
            petList.add(cursorToPet(cursor))
        }
        return petList
    }

    override fun updatePet(pet: Pet) = petDatabase.update(
        PET_TABLE,
        petToContentValues(pet),
        "$NAME_COLUMN = ?",
        arrayOf(pet.name)
    )

    override fun deletePet(name: String) = petDatabase.delete(
        PET_TABLE,
        "$NAME_COLUMN = ?",
        arrayOf(name)
    )

    private fun petToContentValues(pet: Pet) = ContentValues().apply {
        put(NAME_COLUMN, pet.name)
        put(SPECIES_COLUMN, pet.species)
        put(BREED_COLUMN, pet.breed)
        put(AGE_COLUMN, pet.age)
        put(WEIGHT_COLUMN, pet.weight)
        put(LAST_VET_VISIT_COLUMN, pet.lastVetVisit)
    }

    private fun cursorToPet(cursor: Cursor): Pet {
        return with(cursor) {
            Pet(
                getInt(getColumnIndexOrThrow(ID_COLUMN)),
                getString(getColumnIndexOrThrow(NAME_COLUMN)),
                getString(getColumnIndexOrThrow(SPECIES_COLUMN)),
                getString(getColumnIndexOrThrow(BREED_COLUMN)),
                getInt(getColumnIndexOrThrow(AGE_COLUMN)),
                getDouble(getColumnIndexOrThrow(WEIGHT_COLUMN)),
                getString(getColumnIndexOrThrow(LAST_VET_VISIT_COLUMN))
            )
        }
    }
}

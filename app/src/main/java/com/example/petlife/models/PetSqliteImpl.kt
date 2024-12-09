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
        private const val PET_DATABASE_FILE = "petlife"
        private const val PET_TABLE = "pet"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
        private const val SPECIES_COLUMN = "species"
        private const val BREED_COLUMN = "breed"
        private const val AGE_COLUMN = "age"
        private const val WEIGHT_COLUMN = "weight"
        private const val LAST_VET_VISIT_COLUMN = "last_vet_visit"

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

    // Método para criar um novo pet
    override fun createPet(pet: Pet) =
        petDatabase.insert(PET_TABLE, null, petToContentValues(pet))

    // Método para recuperar um pet pelo ID
    override fun retrievePet(id: Long): Pet {
        val cursor = petDatabase.query(
            true,
            PET_TABLE,
            null,
            "$ID_COLUMN = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            cursorToPet(cursor)
        } else {
            Pet()  // Retorna um Pet com valores padrões caso não seja encontrado
        }
    }

    // Método para recuperar todos os pets
    override fun retrievePets(): MutableList<Pet> {
        val petList = mutableListOf<Pet>()

        val cursor = petDatabase.rawQuery("SELECT * FROM $PET_TABLE", null)
        while (cursor.moveToNext()) {
            petList.add(cursorToPet(cursor))
        }

        return petList
    }

    // Método para atualizar os dados de um pet
    override fun updatePet(pet: Pet) = petDatabase.update(
        PET_TABLE,
        petToContentValues(pet),
        "$ID_COLUMN = ?",
        arrayOf(pet.id.toString())
    )

    // Método para excluir um pet
    override fun deletePet(id: Long) = petDatabase.delete(
        PET_TABLE,
        "$ID_COLUMN = ?",
        arrayOf(id.toString())
    )

    // Função para converter os dados de Pet para ContentValues
    private fun petToContentValues(pet: Pet) = ContentValues().apply {
        with(pet) {
            put(NAME_COLUMN, name)
            put(SPECIES_COLUMN, species)
            put(BREED_COLUMN, breed)
            put(AGE_COLUMN, age)
            put(WEIGHT_COLUMN, weight)
            put(LAST_VET_VISIT_COLUMN, lastVetVisit)
        }
    }

    // Função para converter os dados do cursor para um objeto Pet
    private fun cursorToPet(cursor: Cursor): Pet = with(cursor) {
        Pet(
            id = getLong(getColumnIndexOrThrow(ID_COLUMN)),
            name = getString(getColumnIndexOrThrow(NAME_COLUMN)),
            species = getString(getColumnIndexOrThrow(SPECIES_COLUMN)),
            breed = getString(getColumnIndexOrThrow(BREED_COLUMN)),
            age = getInt(getColumnIndexOrThrow(AGE_COLUMN)),
            weight = getDouble(getColumnIndexOrThrow(WEIGHT_COLUMN)),
            lastVetVisit = getString(getColumnIndexOrThrow(LAST_VET_VISIT_COLUMN))
        )
    }
}


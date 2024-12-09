package com.example.petlife.ui

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityPetBinding
import com.example.petlife.models.Constant.PET
import com.example.petlife.models.Constant.VIEW_MODE
import com.example.petlife.models.Pet
import com.example.petlife.models.PetSqliteImpl

class PetActivity : AppCompatActivity() {
    private val abb: ActivityPetBinding by lazy {
        ActivityPetBinding.inflate(layoutInflater)
    }

    private lateinit var petSqliteImpl: PetSqliteImpl
    private var currentPet: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(abb.root)

        petSqliteImpl = PetSqliteImpl(this)

        // Obtendo o modo de visualização (ver/editar) e o pet recebido
        val viewMode = intent.getBooleanExtra(VIEW_MODE, false)
        currentPet = intent.getParcelableExtra(PET)

        currentPet?.let { pet ->
            with(abb) {
                with(pet) {
                    petNameEt.setText(name)
                    speciesEt.setText(species)
                    breedEt.setText(breed)
                    ageEt.setText(age.toString())
                    weightEt.setText(weight.toString())
                    lastVetVisitEt.setText(lastVetVisit)

                    // Desabilitar edição se estiver no modo visualização
                    petNameEt.isEnabled = !viewMode
                    speciesEt.isEnabled = !viewMode
                    breedEt.isEnabled = !viewMode
                    ageEt.isEnabled = !viewMode
                    weightEt.isEnabled = !viewMode
                    lastVetVisitEt.isEnabled = !viewMode

                    // Ocultar botão de salvar no modo visualização
                    saveBt.visibility = if (viewMode) GONE else VISIBLE
                }
            }
        }

        // Definindo o título da toolbar
        abb.toolbarIn.toolbar.let {
            it.subtitle = if (currentPet == null)
                "Novo pet"
            else
                if (viewMode)
                    "Detalhes do pet"
                else
                    "Editar pet"
            setSupportActionBar(it)
        }

// Lógica para salvar ou editar o pet
        abb.saveBt.setOnClickListener {
            val name = abb.petNameEt.text.toString()
            val species = abb.speciesEt.text.toString()
            val breed = abb.breedEt.text.toString()

            val age = abb.ageEt.text.toString().toIntOrNull() ?: 0
            val weight = abb.weightEt.text.toString().toDoubleOrNull() ?: 0.0
            val lastVetVisit = abb.lastVetVisitEt.text.toString()

            val id = currentPet?.id ?: 0

            // Criar objeto Pet
            val pet = Pet(
                id,
                name,
                species,
                breed,
                age,
                weight,
                lastVetVisit
            )

            if (currentPet != null) {
                petSqliteImpl.updatePet(pet)
                Toast.makeText(this, "Pet atualizado", Toast.LENGTH_SHORT).show()
            } else {
                petSqliteImpl.createPet(pet)
                Toast.makeText(this, "Pet adicionado", Toast.LENGTH_SHORT).show()
            }

            setResult(RESULT_OK)
            finish()
        }


    }
}


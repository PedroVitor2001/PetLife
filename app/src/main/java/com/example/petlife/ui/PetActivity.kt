package com.example.petlife.ui

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityPetBinding
import com.example.petlife.models.Constant
import com.example.petlife.models.Constant.PET
import com.example.petlife.models.Constant.VIEW_MODE
import com.example.petlife.models.Pet

class PetActivity : AppCompatActivity() {
    private val abb: ActivityPetBinding by lazy {
        ActivityPetBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(abb.root)

        val viewMode = intent.getBooleanExtra(VIEW_MODE, false)
        val receivedPet = intent.getParcelableExtra<Pet>(PET)

        receivedPet?.let { pet ->
            with(abb) {
                with(pet) {
                    petNameEt.setText(name)  // name
                    speciesEt.setText(species)  // species
                    breedEt.setText(breed)  // breed
                    ageEt.setText(age.toString())  // age -> String conversion
                    weightEt.setText(weight.toString())  // weight -> String conversion
                    lastVetVisitEt.setText(lastVetVisit)  // lastVetVisit

                    // Disable editing when in view mode
                    petNameEt.isEnabled = !viewMode
                    speciesEt.isEnabled = !viewMode
                    breedEt.isEnabled = !viewMode
                    ageEt.isEnabled = !viewMode
                    weightEt.isEnabled = !viewMode
                    lastVetVisitEt.isEnabled = !viewMode
                    saveBt.visibility = if (viewMode) GONE else VISIBLE
                }
            }
        }

        abb.toolbarIn.toolbar.let {
            it.subtitle = if (receivedPet == null)
                "New pet"
            else
                if (viewMode)
                    "Pet details"
                else
                    "Edit pet"
            setSupportActionBar(it)
        }

        abb.saveBt.setOnClickListener {
            val name = abb.petNameEt.text.toString()
            val species = abb.speciesEt.text.toString()
            val breed = abb.breedEt.text.toString()

            // Safely convert age and weight, defaulting to 0 if conversion fails
            val age = abb.ageEt.text.toString().toIntOrNull() ?: 0  // Int
            val weight = abb.weightEt.text.toString().toDoubleOrNull() ?: 0.0  // Double
            val lastVetVisit = abb.lastVetVisitEt.text.toString()

            // Provide a value for the 'id' parameter (e.g., using 0 for a new pet)
            val id = 0  // You might want to generate or retrieve an actual ID from the database

            // Create the Pet object
            val pet = Pet(
                id,  // id
                name,  // name
                species,  // species
                breed,  // breed
                age,  // age (Int)
                weight,  // weight (Double)
                lastVetVisit  // lastVetVisit (String)
            )

            // Send the pet data back in the result intent
            Intent().apply {
                putExtra(Constant.PET, pet)
                setResult(RESULT_OK, this)
                finish()
            }
        }

    }
}

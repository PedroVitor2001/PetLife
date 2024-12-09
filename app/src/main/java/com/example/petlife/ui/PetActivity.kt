package com.example.petlife.ui

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.R
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

        val typeOptions = resources.getStringArray(R.array.pet_types)
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, typeOptions)
        abb.typeSpinner.adapter = typeAdapter

        val sizeOptions = resources.getStringArray(R.array.size_options)
        val sizeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sizeOptions)
        abb.sizeSpinner.adapter = sizeAdapter

        val viewMode = intent.getBooleanExtra(VIEW_MODE, false)
        currentPet = intent.getParcelableExtra(PET)

        currentPet?.let { pet ->
            with(abb) {
                with(pet) {
                    petNameEt.setText(name)
                    colorEt.setText(color)
                    birthDateEt.setText(birthDate)

                    val typePosition = typeOptions.indexOf(type)
                    if (typePosition >= 0) {
                        typeSpinner.setSelection(typePosition)
                    }

                    val sizePosition = sizeOptions.indexOf(size)
                    if (sizePosition >= 0) {
                        sizeSpinner.setSelection(sizePosition)
                    }

                    petNameEt.isEnabled = !viewMode
                    typeSpinner.isEnabled = !viewMode
                    colorEt.isEnabled = !viewMode
                    birthDateEt.isEnabled = !viewMode
                    sizeSpinner.isEnabled = !viewMode

                    saveBt.visibility = if (viewMode) GONE else VISIBLE
                }
            }
        }

        abb.toolbarIn.toolbar.let {
            it.subtitle = if (currentPet == null)
                "Novo pet"
            else if (viewMode)
                "Detalhes do pet"
            else
                "Editar pet"
            setSupportActionBar(it)

            it.setNavigationOnClickListener {
                val intent = Intent(this, EventRegisterActivity::class.java)
                startActivity(intent)
            }
        }

        abb.saveBt.setOnClickListener {
            val name = abb.petNameEt.text.toString()
            val type = abb.typeSpinner.selectedItem.toString()
            val color = abb.colorEt.text.toString()
            val birthDate = abb.birthDateEt.text.toString()
            val size = abb.sizeSpinner.selectedItem.toString()

            val id = currentPet?.id ?: 0

            val pet = Pet(
                id,
                name,
                type,
                color,
                birthDate,
                size
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

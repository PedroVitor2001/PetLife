package com.example.petlife.ui

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

        // Configurar Spinner de tipo
        val typeOptions = resources.getStringArray(R.array.pet_types)
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, typeOptions)
        abb.typeSpinner.adapter = typeAdapter

        // Configurar Spinner de tamanho
        val sizeOptions = resources.getStringArray(R.array.size_options)
        val sizeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sizeOptions)
        abb.sizeSpinner.adapter = sizeAdapter

        // Configurar modo de visualização
        val viewMode = intent.getBooleanExtra(VIEW_MODE, false)
        currentPet = intent.getParcelableExtra(PET)

        currentPet?.let { pet ->
            with(abb) {
                with(pet) {
                    petNameEt.setText(name)
                    colorEt.setText(color)
                    birthDateEt.setText(birthDate)

                    // Selecionar o tipo no Spinner
                    val typePosition = typeOptions.indexOf(type)
                    if (typePosition >= 0) {
                        typeSpinner.setSelection(typePosition)
                    }

                    // Selecionar o tamanho no Spinner
                    val sizePosition = sizeOptions.indexOf(size)
                    if (sizePosition >= 0) {
                        sizeSpinner.setSelection(sizePosition)
                    }

                    // Desabilitar edição se estiver no modo visualização
                    petNameEt.isEnabled = !viewMode
                    typeSpinner.isEnabled = !viewMode
                    colorEt.isEnabled = !viewMode
                    birthDateEt.isEnabled = !viewMode
                    sizeSpinner.isEnabled = !viewMode

                    // Ocultar botão de salvar no modo visualização
                    saveBt.visibility = if (viewMode) GONE else VISIBLE
                }
            }
        }

        // Configurar título da toolbar
        abb.toolbarIn.toolbar.let {
            it.subtitle = if (currentPet == null)
                "Novo pet"
            else if (viewMode)
                "Detalhes do pet"
            else
                "Editar pet"
            setSupportActionBar(it)
        }

        // Lógica para salvar ou editar o pet
        abb.saveBt.setOnClickListener {
            val name = abb.petNameEt.text.toString()
            val type = abb.typeSpinner.selectedItem.toString() // Recuperar valor do Spinner de tipo
            val color = abb.colorEt.text.toString()
            val birthDate = abb.birthDateEt.text.toString()
            val size = abb.sizeSpinner.selectedItem.toString() // Recuperar valor do Spinner de tamanho

            val id = currentPet?.id ?: 0

            // Criar objeto Pet
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

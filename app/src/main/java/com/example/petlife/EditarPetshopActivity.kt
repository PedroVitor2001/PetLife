package com.example.petlife

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityEditarPetshopBinding

class EditarPetshopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPetshopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPetshopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar os dados enviados pela MainActivity
        val petshop = intent.getStringExtra("ultimaIdaPetshop")

        // Preencher o campo com o dado existente
        binding.inputPetshop.setText(petshop)

        // Botão para salvar os dados
        binding.btnSalvarPetshop.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("ultimaIdaPetshop", binding.inputPetshop.text.toString())
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}


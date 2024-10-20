package com.example.petlife

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityEditarVeterinarioBinding

class EditarVeterinarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarVeterinarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarVeterinarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar os dados enviados pela MainActivity
        val ultimaIda = intent.getStringExtra("ultimaIda")

        // Preencher o campo com o dado existente
        binding.inputVeterinario.setText(ultimaIda)

        // Botão para salvar os dados
        binding.btnSalvarVeterinario.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("ultimaIda", binding.inputVeterinario.text.toString())
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}

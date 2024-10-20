package com.example.petlife

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityEditarVacinaBinding

class EditarVacinaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarVacinaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarVacinaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar os dados enviados pela MainActivity
        val vacina = intent.getStringExtra("vacina")

        // Preencher o campo com o dado existente
        binding.inputVacina.setText(vacina)

        // Botão para salvar os dados
        binding.btnSalvarVacina.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("vacina", binding.inputVacina.text.toString())
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}

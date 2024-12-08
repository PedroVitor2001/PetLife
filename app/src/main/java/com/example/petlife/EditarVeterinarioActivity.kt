package com.example.petlife

import android.app.Activity
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

        // Preencher os campos com valores atuais, se disponíveis
        val ultimaIdaVeterinario = intent.getStringExtra("ultimaIdaVeterinario") ?: ""
        val telefoneConsultorio = intent.getStringExtra("telefoneConsultorio") ?: ""
        val siteConsultas = intent.getStringExtra("siteConsultas") ?: ""

        binding.inputUltimaIdaVeterinario.setText(ultimaIdaVeterinario)
        binding.inputTelefoneConsultorio.setText(telefoneConsultorio)
        binding.inputSiteConsultas.setText(siteConsultas)

        // Botão de salvar
        binding.btnSalvar.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("ultimaIdaVeterinario", binding.inputUltimaIdaVeterinario.text.toString())
                putExtra("telefoneConsultorio", binding.inputTelefoneConsultorio.text.toString())
                putExtra("siteConsultas", binding.inputSiteConsultas.text.toString())
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}

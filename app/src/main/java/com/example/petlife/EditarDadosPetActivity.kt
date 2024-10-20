package com.example.petlife

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityEditarDadosPetBinding

class EditarDadosPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarDadosPetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarDadosPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar os dados enviados pela MainActivity
        val nome = intent.getStringExtra("nome") ?: ""
        val dataNascimento = intent.getStringExtra("dataNascimento") ?: ""
        val tipo = intent.getStringExtra("tipo") ?: ""
        val cor = intent.getStringExtra("cor") ?: ""
        val porte = intent.getStringExtra("porte") ?: ""

        // Preencher os campos com os dados existentes
        binding.inputNome.setText(nome)
        binding.inputDataNascimento.setText(dataNascimento)
        binding.inputTipo.setText(tipo)
        binding.inputCor.setText(cor)
        binding.inputPorte.setText(porte)

        // Botão para salvar os dados
        binding.btnSalvar.setOnClickListener {
            // Cria um Intent para retornar os dados alterados
            val resultIntent = Intent().apply {
                putExtra("nome", binding.inputNome.text.toString())
                putExtra("dataNascimento", binding.inputDataNascimento.text.toString())
                putExtra("tipo", binding.inputTipo.text.toString())
                putExtra("cor", binding.inputCor.text.toString())
                putExtra("porte", binding.inputPorte.text.toString())
            }
            // Define o resultado e finaliza a atividade
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}

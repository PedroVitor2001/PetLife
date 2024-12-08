package com.example.petlife

import Pet
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var pet = Pet(
        nome = "Rex",
        dataNascimento = "01/01/2020",
        tipo = "Cão",
        cor = "Marrom",
        porte = "Médio",
        ultimaIdaVeterinario = "15/06/2024",
        ultimaVacina = "20/07/2024",
        ultimaIdaPetshop = "25/07/2024",
        telefoneConsultorio = "123456789",
        siteConsultas = "https://consultas.vet"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atualiza a interface com os dados do pet
        updateUI()

        // Botões para abrir telas de edição
        binding.btnEditarDados.setOnClickListener {
            val intent = Intent(this, EditarDadosPetActivity::class.java)
            val bundle = Bundle()
            bundle.putString("nome", pet.nome)
            bundle.putString("dataNascimento", pet.dataNascimento)
            bundle.putString("tipo", pet.tipo)
            bundle.putString("cor", pet.cor)
            bundle.putString("porte", pet.porte)
            bundle.putString("ultimaIdaVeterinario", pet.ultimaIdaVeterinario)
            bundle.putString("ultimaVacina", pet.ultimaVacina)
            intent.putExtras(bundle)
            startActivityForResult(intent, 1)
        }

        binding.btnEditarVeterinario.setOnClickListener {
            val intent = Intent(this, EditarVeterinarioActivity::class.java)
            intent.putExtras(createPetBundle())
            startActivityForResult(intent, 2)
        }

        binding.btnEditarVacina.setOnClickListener {
            val intent = Intent(this, EditarVacinaActivity::class.java)
            intent.putExtras(createPetBundle())
            startActivityForResult(intent, 3)
        }

        binding.btnEditarPetshop.setOnClickListener {
            val intent = Intent(this, EditarPetshopActivity::class.java)
            intent.putExtras(createPetBundle())
            startActivityForResult(intent, 4)
        }
    }

    private fun createPetBundle(): Bundle {
        return Bundle().apply {
            putString("nome", pet.nome)
            putString("dataNascimento", pet.dataNascimento)
            putString("tipo", pet.tipo)
            putString("cor", pet.cor)
            putString("porte", pet.porte)
            putString("ultimaIdaVeterinario", pet.ultimaIdaVeterinario)
            putString("ultimaVacina", pet.ultimaVacina)
        }
    }

    private fun updateUI() {
        binding.nomePet.text = "Nome: ${pet.nome}"
        binding.dataNascimentoPet.text = "Data de Nascimento: ${pet.dataNascimento}"
        binding.tipoPet.text = "Tipo: ${pet.tipo}"
        binding.corPet.text = "Cor: ${pet.cor}"
        binding.portePet.text = "Porte: ${pet.porte}"
        binding.ultimaIdaVeterinario.text = "Última Ida ao Veterinário: ${pet.ultimaIdaVeterinario}"
        binding.ultimaVacina.text = "Última Vacina: ${pet.ultimaVacina}"
        binding.ultimaIdaPetshop.text = "Última Ida ao Petshop: ${pet.ultimaIdaPetshop}"

        binding.telefoneConsultorio.text = "Telefone: ${pet.telefoneConsultorio}"
        binding.siteConsultas.text = "Site: ${pet.siteConsultas}"

        binding.btnDiscar.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${pet.telefoneConsultorio}")
            }
            startActivity(intent)
        }

        binding.btnAbrirSite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(pet.siteConsultas)
            }
            startActivity(intent)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                1 -> { // Editar Dados do Pet
                    pet = Pet(
                        nome = data.getStringExtra("nome") ?: pet.nome,
                        dataNascimento = data.getStringExtra("dataNascimento") ?: pet.dataNascimento,
                        tipo = data.getStringExtra("tipo") ?: pet.tipo,
                        cor = data.getStringExtra("cor") ?: pet.cor,
                        porte = data.getStringExtra("porte") ?: pet.porte,
                        ultimaIdaVeterinario = data.getStringExtra("ultimaIdaVeterinario") ?: pet.ultimaIdaVeterinario,
                        ultimaVacina = data.getStringExtra("ultimaVacina") ?: pet.ultimaVacina,
                        ultimaIdaPetshop = pet.ultimaIdaPetshop, // Mantém o valor atual
                        telefoneConsultorio = pet.telefoneConsultorio, // Mantém o valor atual
                        siteConsultas = pet.siteConsultas // Mantém o valor atual
                    )
                }
                2 -> { // Editar Veterinário
                    pet.ultimaIdaVeterinario = data.getStringExtra("ultimaIdaVeterinario") ?: pet.ultimaIdaVeterinario
                    pet.telefoneConsultorio = data.getStringExtra("telefoneConsultorio") ?: pet.telefoneConsultorio
                    pet.siteConsultas = data.getStringExtra("siteConsultas") ?: pet.siteConsultas
                }
                3 -> { // Editar Vacina
                    pet.ultimaVacina = data.getStringExtra("ultimaVacina") ?: pet.ultimaVacina
                }
                4 -> { // Editar Petshop
                    pet.ultimaIdaPetshop = data.getStringExtra("ultimaIdaPetshop") ?: pet.ultimaIdaPetshop
                }
            }
            updateUI() // Atualiza a interface com os novos dados
        }
    }



}
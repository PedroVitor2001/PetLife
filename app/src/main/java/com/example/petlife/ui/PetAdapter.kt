package com.example.petlife.ui

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.petlife.R
import com.example.petlife.databinding.TilePetBinding
import com.example.petlife.models.Pet

class PetAdapter(
    context: Context,
    private val petList: MutableList<Pet>
): ArrayAdapter<Pet>(context, R.layout.tile_pet, petList) {

    private data class PetTileHolder(
        val petNameTv: TextView,
        val breedTv: TextView,
        val ageOrLastVisitTv: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var tpb: TilePetBinding

        // Pegar o pet que vai ser usado para preencher a célula
        val pet = petList[position]

        // Verificando se a célula já foi inflada ou não.
        var petTile = convertView
        if (petTile == null) {
            // Se não foi, infla uma nova célula
            tpb = TilePetBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            petTile = tpb.root

            // Criar um holder para a nova célula
            val newPetTileHolder = PetTileHolder(tpb.petNameTv, tpb.breedTv, tpb.ageOrLastVisitTv)

            // Associar holder à nova célula
            petTile.tag = newPetTileHolder
        }

        // Preenche os valores da célula com o novo pet
        val holder = petTile.tag as PetTileHolder
        holder.let {
            it.petNameTv.text = pet.name
            it.breedTv.text = pet.breed
            it.ageOrLastVisitTv.text = pet.age.toString() // Ou a data da última visita, conforme necessário
        }

        // Retorna a célula preenchida
        return petTile
    }
}



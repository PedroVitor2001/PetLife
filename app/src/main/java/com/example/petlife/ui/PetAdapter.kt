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

        val pet = petList[position]

        var petTile = convertView
        if (petTile == null) {
            tpb = TilePetBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            petTile = tpb.root

            val newPetTileHolder = PetTileHolder(tpb.petNameTv, tpb.breedTv, tpb.ageOrLastVisitTv)

            petTile.tag = newPetTileHolder
        }

        val holder = petTile.tag as PetTileHolder
        holder.let {
            it.petNameTv.text = pet.name
            it.breedTv.text = pet.color
            it.ageOrLastVisitTv.text = pet.birthDate
        }

        return petTile
    }
}



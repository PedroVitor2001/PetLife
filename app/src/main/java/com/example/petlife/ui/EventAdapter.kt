package com.example.petlife.ui

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.petlife.R
import com.example.petlife.databinding.TileEventBinding
import com.example.petlife.models.Event

class EventAdapter(
    context: Context,
    private val eventList: MutableList<Event>
) : ArrayAdapter<Event>(context, R.layout.tile_event, eventList) {

    private data class EventTileHolder(
        val typeTv: TextView,
        val dateTv: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var teb: TileEventBinding
        val event = eventList[position]

        var eventTile = convertView
        if (eventTile == null) {
            teb = TileEventBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )
            eventTile = teb.root

            val newEventTileHolder = EventTileHolder(teb.eventTypeTv, teb.eventDateTv)
            eventTile.tag = newEventTileHolder
        }

        val holder = eventTile.tag as EventTileHolder
        holder.let {
            it.typeTv.text = event.tipo
            it.dateTv.text = event.data
        }

        return eventTile
    }

}


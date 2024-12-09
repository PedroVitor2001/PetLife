package com.example.petlife.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityEventBinding
import com.example.petlife.models.Event
import com.example.petlife.models.EventSqliteImpl

class EventRegisterActivity : AppCompatActivity() {
    private val abb: ActivityEventBinding by lazy {
        ActivityEventBinding.inflate(layoutInflater)
    }

    private lateinit var eventSqliteImpl: EventSqliteImpl
    private var currentEvent: Event? = null
    private var petId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(abb.root)

        eventSqliteImpl = EventSqliteImpl(this)

        val viewMode = intent.getBooleanExtra("VIEW_MODE", false)
        currentEvent = intent.getParcelableExtra("EVENT")

        petId = intent.getLongExtra("PET_ID", -1)

        currentEvent?.let { event ->
            abb.descriptionEt.setText(event.descricao)
            abb.dateEt.setText(event.data)
            abb.typeEt.setText(event.tipo)

            abb.typeEt.isEnabled = !viewMode
            abb.descriptionEt.isEnabled = !viewMode
            abb.dateEt.isEnabled = !viewMode

            abb.saveBt.visibility = if (viewMode) View.GONE else View.VISIBLE
        }

        abb.saveBt.setOnClickListener {
            val tipo = abb.typeEt.text.toString()
            val descricao = abb.descriptionEt.text.toString()
            val data = abb.dateEt.text.toString()

            if (tipo.isBlank() || descricao.isBlank() || data.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = currentEvent?.id ?: 0

            val event = Event(
                id,
                petId,
                tipo,
                descricao,
                data
            )

            if (currentEvent != null) {
                eventSqliteImpl.updateEvent(event)
                Toast.makeText(this, "Evento atualizado", Toast.LENGTH_SHORT).show()
            } else {
                eventSqliteImpl.createEvent(event)  // Criar novo evento
                Toast.makeText(this, "Evento adicionado", Toast.LENGTH_SHORT).show()
            }

            setResult(RESULT_OK)
            finish()
        }
    }
}


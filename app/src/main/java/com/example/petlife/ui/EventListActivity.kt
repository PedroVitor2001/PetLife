package com.example.petlife.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.R
import com.example.petlife.controller.MainController
import com.example.petlife.databinding.ActivityMainBinding
import com.example.petlife.models.Event

class EventListActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val eventList: MutableList<Event> = mutableListOf()

    private val eventAdapter: EventAdapter by lazy {
        EventAdapter(this, eventList)
    }

    private val mainController: MainController by lazy {
        MainController(this)
    }

    private lateinit var barl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        val petId = intent.getLongExtra("PET_ID", -1)
        if (petId != -1L) {
            fillEventList(petId)
            Log.v("id", "Pet ID: $petId")
        }

        barl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val event = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra<Event>("EVENT")
                } else {
                    result.data?.getParcelableExtra("EVENT", Event::class.java)
                }
                event?.let { receivedEvent ->
                    val position = eventList.indexOfFirst { it.id == receivedEvent.id }
                    if (position == -1) {
                        eventList.add(receivedEvent)
                        mainController.insertEvent(receivedEvent)
                    } else {
                        eventList[position] = receivedEvent
                        mainController.modifyEvent(receivedEvent)
                    }
                    eventAdapter.notifyDataSetChanged()
                }
            }
        }

        amb.toolbarIn.toolbar.let {
            it.subtitle = getString(R.string.event_list)
            setSupportActionBar(it)
        }

        amb.petsLv.adapter = eventAdapter

        registerForContextMenu(amb.petsLv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_event_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.addEventMi -> {
            // Obtendo petId (se disponível) para adicionar um novo evento
            val petId = intent.getLongExtra("PET_ID", -1)
            // Passando petId para a EventRegisterActivity
            val intent = Intent(this, EventRegisterActivity::class.java).apply {
                putExtra("PET_ID", petId)
            }
            barl.launch(intent)
            true
        }
        else -> {
            false
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.context_menu_event_list, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position
        return when (item.itemId) {
            R.id.editEventMi -> {
                val petId = intent.getLongExtra("PET_ID", -1)
                Intent(this, EventRegisterActivity::class.java).apply {
                    putExtra("EVENT", eventList[position])
                    putExtra("PET_ID", petId)
                    putExtra("VIEW_MODE", false)
                    barl.launch(this)
                }
                true
            }
            R.id.removeEventMi -> {
                mainController.removeEvent(eventList[position].id)
                eventList.removeAt(position)
                eventAdapter.notifyDataSetChanged()
                true
            }
            else -> {
                false
            }
        }
    }


    override fun onResume() {
        super.onResume()
        val petId = intent.getLongExtra("PET_ID", -1)
        if (petId != -1L) {
            fillEventList(petId)
        }
    }

    private fun fillEventList(petId: Long) {
        Thread {
            val eventsFromDb = mainController.getEvents(petId)
            Log.v("id", "eventosDB: $eventsFromDb")
            runOnUiThread {
                eventList.clear()
                eventList.addAll(eventsFromDb)
                eventAdapter.notifyDataSetChanged()
            }
        }.start()
    }
}

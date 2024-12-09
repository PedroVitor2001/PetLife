package com.example.petlife.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import com.example.petlife.models.Constant
import com.example.petlife.models.Constant.PET
import com.example.petlife.models.Constant.VIEW_MODE
import com.example.petlife.models.Pet

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val petList: MutableList<Pet> = mutableListOf()

    private val petAdapter: PetAdapter by lazy {
        PetAdapter(this, petList)
    }

    private val mainController: MainController by lazy {
        MainController(this)
    }

    private lateinit var barl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        barl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val pet = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra<Pet>(Constant.PET)
                } else {
                    result.data?.getParcelableExtra(Constant.PET, Pet::class.java)
                }
                pet?.let { receivedPet ->
                    val position = petList.indexOfFirst { it.id == receivedPet.id }
                    if (position == -1) {
                        petList.add(receivedPet)
                        mainController.insertPet(receivedPet)
                    } else {
                        petList[position] = receivedPet
                        mainController.modifyPet(receivedPet)
                    }
                    petAdapter.notifyDataSetChanged()
                }
            }
        }

        amb.toolbarIn.toolbar.let {
            it.subtitle = getString(R.string.pet_list)
            setSupportActionBar(it)
        }

        fillPetList()

        amb.petsLv.adapter = petAdapter
        amb.petsLv.setOnItemClickListener { _, _, position, _ ->
            val petId = petList[position].id
            Intent(this, EventListActivity::class.java).apply {
                putExtra("PET_ID", petId)
                startActivity(this)
            }
        }

        registerForContextMenu(amb.petsLv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.addPetMi -> {
            barl.launch(Intent(this, PetActivity::class.java))
            true
        }
        else -> {
            false
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) = menuInflater.inflate(R.menu.context_menu_main, menu)

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position
        return when (item.itemId) {
            R.id.editPetMi -> {
                Intent(this, PetActivity::class.java).apply {
                    putExtra(PET, petList[position])
                    putExtra(VIEW_MODE, false)
                    barl.launch(this)
                }
                true
            }
            R.id.removePetMi -> {
                mainController.removePet(petList[position].id)
                petList.removeAt(position)
                petAdapter.notifyDataSetChanged()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fillPetList()
    }

    private fun fillPetList() {
        Thread {
            val petsFromDb = mainController.getPets()
            runOnUiThread {
                petList.clear()
                petList.addAll(petsFromDb)
                petAdapter.notifyDataSetChanged()
            }
        }.start()
    }
}

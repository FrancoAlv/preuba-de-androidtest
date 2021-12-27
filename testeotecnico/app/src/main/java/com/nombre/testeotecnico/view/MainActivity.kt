package com.nombre.testeotecnico.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nombre.testeotecnico.R
import com.nombre.testeotecnico.databinding.ActivityMainBinding
import com.nombre.testeotecnico.viewmodel.viewmodel
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nombre.testeopruebakotlin.modelo.adacter.adacteractividad
import com.nombre.testeotecnico.model.actividad

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val conect: viewmodel by viewModels()
    private var lisactividad = ArrayList<actividad>()
    private var adat: adacteractividad? = null

    @SuppressLint("NotifyDataSetChanged", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        conect.getContext(this)
        setContentView(binding.root)
        binding.toolbar1.title = "Agenda"
        conect.oncreateini()
        setSupportActionBar(binding.toolbar1)
        adat = adacteractividad(this, conect, vistalauncher)
        conect.oblisactividad.observe(this, Observer {
            lisactividad = it
            adat?.udpatelis(it)
        })
        binding.fab.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.dialogo, null)
            conect.createdialo(view)
        }
        binding.conten.list.layoutManager = LinearLayoutManager(this)
        binding.conten.list.adapter = adat

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val seleccion = item.itemId
        // Toast.makeText(applicationContext,"ghasda",Toast.LENGTH_SHORT).show()
        when (seleccion) {
            R.id.menu2 -> {
                if (bandera) {
                    conect.busquedadeboleanos()
                    item.setIcon(R.drawable.ic_baseline_visibility_off_24)
                    bandera = false
                } else {
                    bandera = true
                    conect.listanormal()
                    item.setIcon(R.drawable.ic_baseline_remove_red_eye_24)
                }
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }


    override fun onPause() {
        conect.guardaractividades()
        super.onPause()
    }

    companion object {
        var bandera = true
    }
    private val vistalauncher = registerForActivityResult(StartActivityForResult()) {
        if (it?.resultCode == RESULT_CANCELED) {
            val num = it.data
            num?.let { it1 -> conect.modificar(it1) }
        } else if (it?.resultCode == RESULT_OK) {
            val num = it.data?.getIntExtra("elminar", -1)
            num?.let { it1 -> conect.eleminarelemento(it1) }
        }
    }
}
package com.nombre.testeotecnico.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nombre.testeotecnico.R
import com.nombre.testeotecnico.databinding.ActivityVista2Binding
import com.nombre.testeotecnico.viewmodel.viewmodel2
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.nombre.testeotecnico.model.adacter.adactercomentario

class vista2 : AppCompatActivity() {

    private val conect: viewmodel2 by viewModels<viewmodel2>()
    private lateinit var binding: ActivityVista2Binding
    private lateinit var adapter: adactercomentario
    private var primar : Int?=null
    private var posicion : Int?=null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVista2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter=adactercomentario(this)
        conect.getContext(this)
        recogiendodatos()
        setSupportActionBar(binding.toolbar2)
        conect.oblisactividad.observe(this, Observer {
            binding.texcomentario.text=" Comentarios (${it.size})"
            adapter.udlistcomen(it)
            binding.listcomentario.adapter=adapter
        })
    }
    @SuppressLint("SetTextI18n")
    fun recogiendodatos(){
        val bun = intent.extras
        binding.comentario.text = bun?.getString("contenido")
        binding.fechacreada.text = "Creada: "+bun?.getString("fechacreada")
        binding.fechafinalizadad.text ="Finaliza: "+ bun?.getString("fechafinalizada")
        binding.toolbar2.title = bun?.getString("titulo")
        binding.texcomentario.text = " Comentarios (0)"
        binding.listcomentario.layoutManager=LinearLayoutManager(this)
        primar=bun?.getInt("primary")!!
        binding.boingresar.setOnClickListener{
            if(!binding.textocomentario.text.isNullOrEmpty()){
            conect.ingresadodecomentario(binding.textocomentario.text.toString(), primar!!)
            binding.textocomentario.setText("")
            }
        }
        conect.inicio(primar!!)
        posicion=bun.getInt("posicion")
        lifecycleScope
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu2, menu)
        return true
    }

    @SuppressLint("InflateParams")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val seleccion = item.itemId
        // Toast.makeText(applicationContext,"ghasda",Toast.LENGTH_SHORT).show()
        when (seleccion) {
            R.id.basuras -> {

                conect.creardialogodeboorado(primar!!,this, RESULT_OK,posicion!!)

                //Toast.makeText(applicationContext,"ghasda",Toast.LENGTH_SHORT).show()
            }
            R.id.lapiz -> {
                val view = layoutInflater.inflate(R.layout.dialogo, null)
               // Toast.makeText(applicationContext,"ghasda",Toast.LENGTH_SHORT).show()
                conect.createdialo(view,primar!!,this, RESULT_CANCELED,posicion!!)
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }





}
package com.nombre.testeotecnico.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nombre.testeotecnico.databinding.DialogoBinding
import com.nombre.testeotecnico.model.actividad
import com.nombre.testeotecnico.model.basesdedatos.Baseactivity
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class viewmodel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    private var con: Context? = null
    var lisctidad = ArrayList<actividad>()
    var oblisactividad = MutableLiveData<ArrayList<actividad>>()
    var base: Baseactivity? = null
    var bandera = true
    fun getContext(con: Context) {
        this.con = con
        this.base = Baseactivity(con, "bd23", null, 3)
    }

    fun oncreateini() {
        if (bandera) {
            viewModelScope.launch {
                lisctidad = base?.busqueda()!!
                oblisactividad.postValue(lisctidad)
            }
            bandera = false
        } else {
            oblisactividad.postValue(lisctidad)
        }
    }

    fun createdialo(vie: View) {
        val dialo = con?.let { AlertDialog.Builder(it) }
        val binding = DialogoBinding.bind(vie)
        binding.fecha.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val ddmmyyyy = "DDMMYYYY"
            private val cal = Calendar.getInstance()
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                seq: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (seq.toString() != current) {
                    var clean = seq.toString().replace("[^\\d.]|\\.".toRegex(), "")
                    val cleanC = current.replace("[^\\d.]|\\.".toRegex(), "")
                    val cl = clean.length
                    var sel = cl
                    var i = 2
                    while (i <= cl && i < 6) {
                        sel++
                        i += 2
                    }
                    if (clean == cleanC) sel--
                    if (clean.length < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length)
                    } else {
                        var day = clean.substring(0, 2).toInt()
                        var mon = clean.substring(2, 4).toInt()
                        var year = clean.substring(4, 8).toInt()
                        mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                        cal[Calendar.MONTH] = mon - 1
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        cal[Calendar.YEAR] = year
                        day =
                            if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(
                                Calendar.DATE
                            ) else day
                        clean = String.format("%02d%02d%02d", day, mon, year)
                    }
                    clean = String.format(
                        "%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8)
                    )
                    sel = if (sel < 0) 0 else sel
                    current = clean
                    binding.fecha.setText(current)
                    binding.fecha.setSelection(if (sel < current.length) sel else current.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        dialo?.setView(vie)
        dialo?.setMessage("NUEVA ACTIVIDAD ")
        dialo?.setPositiveButton("Confirmar") { dialog, which ->
            try {
                val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
                val titulo = "${binding.titulo.text}-${(1..20).random()}"
                val contenidod = binding.contenido.text.toString()
                val fecha = binding.fecha.text.toString()
                base?.insert(
                    titulo = titulo,
                    contenido = contenidod,
                    fechafinalizada = LocalDate.parse(fecha, formatter).toString(),
                    false
                )
                lisctidad = base?.busqueda()!!
                oblisactividad.postValue(lisctidad)
            } catch (e: Exception) {
                Toast.makeText(con, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        dialo?.setNegativeButton("Cancelar") { dialog, which ->

        }
        dialo?.show()
    }

    fun busquedadeboleanos() {
        val listanueva = ArrayList<actividad>()
        for (a in lisctidad.indices) {
            if (lisctidad[a].bole) {
                listanueva.add(lisctidad[a])
            }
        }
        oblisactividad.postValue(listanueva)
    }

    fun listanormal() {
        oblisactividad.postValue(lisctidad)
    }

    fun eleminarelemento(num: Int) {
        if (num != -1) {
            lisctidad.remove(lisctidad[num])
            oblisactividad.postValue(lisctidad)
        }
    }

    fun modificar(inten: Intent) {
        val position = inten.getIntExtra("modifca", -1)
        if (position != -1) {
            val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
            val acti = actividad(
                primary = lisctidad[position].primary,
                titulo = inten.getStringExtra("titulo").orEmpty(),
                contenido = inten.getStringExtra("contenido").orEmpty(),
                fechafinalizada = LocalDate.parse(inten.getStringExtra("fecha"), formatter)
            )
            lisctidad.set(position, acti)
            oblisactividad.postValue(lisctidad)
        }
    }

    fun guardaractividades() {
    lisctidad.forEach {
        base?.udpate(it.primary!!,it.bole)
    }

    }
}
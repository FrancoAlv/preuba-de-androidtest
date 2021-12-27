package com.nombre.testeotecnico.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nombre.testeotecnico.databinding.DialogoBinding
import com.nombre.testeotecnico.model.SingleLiveEvent
import com.nombre.testeotecnico.model.basesdedatos.Baseactivity
import com.nombre.testeotecnico.model.basesdedatos.basecometario
import com.nombre.testeotecnico.model.comentario
import com.nombre.testeotecnico.view.vista2
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class viewmodel2 : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var con: Context? = null
    var lisctidad = ArrayList<comentario>()
    var oblisactividad = SingleLiveEvent<ArrayList<comentario>>()
    var base: basecometario? = null
    var baseactivi: Baseactivity? = null
    var bandera = true
    fun getContext(con: Context) {
        this.con = con
        this.base = basecometario(con, "bd", null, 2)
        this.baseactivi = Baseactivity(con, "bd23", null, 3)
    }

    fun inicio(num: Int) {
        if (bandera) {
            viewModelScope.launch {
                val lisctidad1 = base?.busqueda()!!
                if (!lisctidad1.isNullOrEmpty()) {
                    for (a in lisctidad1.indices) {
                        if (lisctidad1[a].clavefore == num) {
                            lisctidad.add(lisctidad1[a])
                        }
                    }
                }
                oblisactividad.postValue(lisctidad)
            }
            bandera = false
        } else {
            oblisactividad.postValue(lisctidad)
        }
    }

    fun ingresadodecomentario(comen: String, num: Int) {
        val com = comentario(null, comen, num)
        lisctidad.add(com)
        base?.insert(comen, num)
        oblisactividad.postValue(lisctidad)
    }

    fun createdialo(vie: View, num: Int, vista2: vista2, RESULT_CANCELED: Int, posicion: Int) {
        val dialo = con?.let { AlertDialog.Builder(it) }
        val binding = DialogoBinding.bind(vie)
        dialo?.setView(vie)
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
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012
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

            override fun afterTextChanged(s: Editable?) {
            }
        })
        dialo?.setMessage("Modificar Actividad")
        dialo?.setPositiveButton("Confirmar") { dialog, which ->
            try {
                val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
                val titulo = "${binding.titulo.text}-${(1..20).random()}"
                val contenidod = binding.contenido.text.toString()
                val fecha = binding.fecha.text.toString()
                baseactivi?.udpate(
                    num,
                    titulo,
                    contenidod,
                    LocalDate.parse(fecha, formatter).toString()
                )
                val inten = Intent()
                inten.putExtra("modifca", posicion)
                inten.putExtra("titulo", titulo)
                inten.putExtra("contenido", contenidod)
                inten.putExtra("fecha", fecha)
                vista2.setResult(RESULT_CANCELED, inten)
                vista2.finish()
            } catch (e: Exception) {

            }
        }
        dialo?.setNegativeButton("Cancelar") { dialog, which ->

        }
        dialo?.show()
    }

    fun creardialogodeboorado(num: Int, vista2: vista2, RESULT_OK: Int, posicion: Int) {
        val dialo = con?.let { AlertDialog.Builder(it) }
        dialo?.setMessage("Desea eleminar esta actividad")
        dialo?.setPositiveButton("Confirmar") { dialog, which ->
            baseactivi?.delete(num)
            val inten = Intent()
            inten.putExtra("elminar", posicion)
            vista2.setResult(RESULT_OK, inten)
            vista2.finish()
        }
        dialo?.setNegativeButton("Cancelar") { dialog, which ->

        }
        dialo?.show()
    }


}

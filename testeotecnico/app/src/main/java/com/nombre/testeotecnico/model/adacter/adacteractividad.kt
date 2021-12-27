package com.nombre.testeopruebakotlin.modelo.adacter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.nombre.testeotecnico.R
import com.nombre.testeotecnico.databinding.LayoutviewBinding
import com.nombre.testeotecnico.model.actividad
import com.nombre.testeotecnico.view.MainActivity
import com.nombre.testeotecnico.view.vista2
import com.nombre.testeotecnico.viewmodel.viewmodel

class adacteractividad(val context: Context, val conect: viewmodel,val lanzado: ActivityResultLauncher<Intent>) :
    RecyclerView.Adapter<adacteractividad.actividadviewholder>() {

    val listactividad = ArrayList<actividad>()

    @SuppressLint("NotifyDataSetChanged")
    fun udpatelis(listanueva: ArrayList<actividad>) {
        listactividad.clear()
        listactividad.addAll(listanueva)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun udpateactividad(actividad: actividad) {
        listactividad.add(actividad)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): actividadviewholder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.layoutview, parent, false)
        return actividadviewholder(vista)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: actividadviewholder, position: Int) {
        holder.binding1.textitle.text = listactividad[position].titulo
        holder.binding1.texconted.text = listactividad[position].contenido
        holder.binding1.texnumero.text = listactividad[position].primary.toString()
        holder.binding1.texfechafin.text = listactividad[position].fechafinalizada.toString()
        holder.binding1.texfechainicio.text = listactividad[position].fechacreada.toString()
        holder.binding1.check.isChecked = listactividad[position].bole
        holder.binding1.check.setOnClickListener {
            listactividad[position].bole = !listactividad[position].bole
            notifyItemChanged(position)
            conect.lisctidad[position].bole = listactividad[position].bole
            if(!MainActivity.bandera){
                conect.busquedadeboleanos()
            }

        }
    }

    override fun getItemCount(): Int {
        return listactividad.size
    }

    inner class actividadviewholder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val binding1 = LayoutviewBinding.bind(itemView)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val intent = Intent(context, vista2::class.java)
            intent.putExtra("primary", listactividad[layoutPosition].primary)
            intent.putExtra("titulo", listactividad[layoutPosition].titulo)
            intent.putExtra("contenido", listactividad[layoutPosition].contenido)
            intent.putExtra("fechacreada", listactividad[layoutPosition].fechacreada.toString())
            intent.putExtra("fechafinalizada", listactividad[layoutPosition].fechafinalizada.toString())
            intent.putExtra("bole", listactividad[layoutPosition].bole)
            intent.putExtra("posicion",layoutPosition)
            lanzado.launch(intent)
        }
    }

}
package com.nombre.testeotecnico.model.adacter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nombre.testeopruebakotlin.modelo.adacter.adacteractividad
import com.nombre.testeotecnico.model.comentario
import android.content.Context
import android.view.LayoutInflater
import com.nombre.testeotecnico.R
import com.nombre.testeotecnico.databinding.LayoutviewBinding
import com.nombre.testeotecnico.databinding.VistacomentarioBinding

class adactercomentario(val context:Context) :
    RecyclerView.Adapter<adactercomentario.comentarioholder>() {

    val listcometario=ArrayList<comentario>()

    @SuppressLint("NotifyDataSetChanged")
    fun udlistcomen(listcometario:ArrayList<comentario>){
        this.listcometario.clear()
        this.listcometario.addAll(listcometario)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): comentarioholder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.vistacomentario, parent, false)
        return comentarioholder(vista)
    }

    override fun onBindViewHolder(holder: comentarioholder, position: Int) {
        val comen=listcometario[position]
        holder.binding1.imageView.setImageResource(R.drawable.descarga1)
        holder.binding1.textView.text=comen.texnombre
    }

    override fun getItemCount(): Int {
      return listcometario.size
    }

    inner class comentarioholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding1 = VistacomentarioBinding.bind(itemView)
    }
}
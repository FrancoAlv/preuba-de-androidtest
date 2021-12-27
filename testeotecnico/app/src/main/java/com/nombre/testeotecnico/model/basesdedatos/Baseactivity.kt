package com.nombre.testeotecnico.model.basesdedatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nombre.testeotecnico.model.actividad
import android.widget.Toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Baseactivity(
    val context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(dbq: SQLiteDatabase?) {
        dbq?.execSQL("CREATE TABLE actividad (primar INTEGER PRIMARY KEY AUTOINCREMENT  , titulo TEXT NOT NULL ,contenido TEXT NOT NULL , fechacreada  TEXT NOT NULL ,fechafinalizada TEXT NOT NULL ,bole BOOLEAN NOT NULL )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS actividad")
        onCreate(db)
    }


    fun insert(
        titulo: String,
        contenido: String,
        fechafinalizada: String,
        bole: Boolean
    ) {
        val bd = writableDatabase
        val registro = ContentValues()
        // registro.put("primary", inr)
        registro.put("titulo", titulo)
        registro.put("contenido", contenido)
        registro.put("fechacreada", LocalDate.now().toString())
        registro.put("fechafinalizada", fechafinalizada)
        registro.put("bole", bole)
        bd?.insert("actividad", null, registro)
        bd?.close()
    }

    fun delete(pri: Int): Boolean {
        val bd = writableDatabase
        val res = bd.delete("actividad", "primar=$pri", null)
        bd.close()
        if (res == 1) {
            return true
        }
        return false
    }

    fun udpate(
        pri: Int, titulo: String,
        contenido: String,
        fechafinalizada: String,
    ) {
        val bd = writableDatabase
        val registro = ContentValues()
        // registro.put("primary", inr)
        registro.put("titulo", titulo)
        registro.put("contenido", contenido)
        registro.put("fechafinalizada", fechafinalizada)
        bd?.update("actividad", registro, "primar=$pri", null)
    }

    fun udpate(
        pri: Int,
        bole: Boolean
    ) {
        val bd = writableDatabase
        val registro = ContentValues()
        registro.put("bole", bole)
        val num = bd?.update("actividad", registro, "primar=$pri", null)
        if (num == 1) {

        }
        bd.close()
    }

    fun busqueda(): ArrayList<actividad> {
        val bd = writableDatabase
        val lisactivi = ArrayList<actividad>()
        val filadb = bd.rawQuery("SELECT * FROM actividad", null)
        while (filadb.moveToNext()) {
            val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
            val bole = if (filadb.getInt(5) == 1) true else false
            val acti = actividad(
                filadb.getInt(0),
                filadb.getString(1),
                filadb.getString(2),
                LocalDate.parse(filadb.getString(3)),
                LocalDate.parse(filadb.getString(4)),
                bole
            )
            lisactivi.add(acti)
        }
        filadb.close()
        bd.close()
        return lisactivi
    }


    fun busqueda(prima: Int): actividad? {
        val bd = writableDatabase
        val filadb = bd.rawQuery("SELECT * FROM actividad WHERE actividad.primar = $prima", null)
        val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
        if (filadb.moveToFirst()) {
            val acti = actividad(
                filadb.getInt(0),
                filadb.getString(1),
                filadb.getString(2),
                LocalDate.parse(filadb.getString(3)),
                LocalDate.parse(filadb.getString(4), formatter),
                filadb.getString(5).toBoolean()
            )
            bd?.close()
            return acti
        }
        bd?.close()
        return null
    }

}
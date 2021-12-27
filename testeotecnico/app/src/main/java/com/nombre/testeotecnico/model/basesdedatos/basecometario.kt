package com.nombre.testeotecnico.model.basesdedatos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nombre.testeotecnico.model.comentario


class basecometario(
    val context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE comentario( idprimar INTEGER PRIMARY KEY AUTOINCREMENT  , texnombre TEXT NOT NULL,clavefore INTEGER )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS comentario")
        onCreate(db)
    }

    fun insert(
       nombre: String,
        clave:Int
    ) {
        val bd = writableDatabase
        val registro = ContentValues()
        registro.put("texnombre", nombre)
        registro.put("clavefore",  clave)
        bd?.insert("comentario", null, registro)
        bd?.close()
    }

    suspend fun busqueda():ArrayList<comentario> {
        val bd = writableDatabase
        val lisactivi=ArrayList<comentario>()
        val filadb = bd.rawQuery("SELECT * FROM comentario", null)
        while(filadb.moveToNext()){
            val acti = comentario(
                filadb.getInt(0),
                filadb.getString(1),
                filadb.getInt(2)
            )
            lisactivi.add(acti)
        }
        filadb.close()
        bd.close()
        return lisactivi
    }

    @SuppressLint("Recycle")
    fun busqueda(prima: Int): comentario? {
        val bd = writableDatabase
        val filadb = bd.rawQuery("SELECT * FROM actividad WHERE comentario.idprimar = $prima", null)
        if (filadb.moveToFirst()) {
            val acti = comentario(
                filadb.getInt(0),
                filadb.getString(1),
                filadb.getInt(2)
            )
            bd?.close()
            return acti
        }
        bd?.close()
        return null
    }



}
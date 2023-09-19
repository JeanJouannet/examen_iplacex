package com.example.examen_iplacex.db

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.Nullable

@Entity
data class PlaceEntity (

    @PrimaryKey(autoGenerate = true)
    val uid:Int,

    @ColumnInfo
    var place:String,

    @ColumnInfo
    var imgRef: Bitmap?,

    @ColumnInfo
    var longitud:Double?,

    @ColumnInfo
    var latitud:Double?,

    @ColumnInfo
    var order:Int,

    @ColumnInfo
    var price:Double,

    @ColumnInfo
    var movePrice:Double,

    @ColumnInfo
    var comments:String?
)
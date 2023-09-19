package com.example.examen_iplacex.db

import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaceDao {

    @Query("SELECT * FROM PlaceEntity ORDER BY `order` ASC")
    fun findAll(): List<PlaceEntity>

    @Delete
    fun delete(product:PlaceEntity)

    @Insert
    fun insert(product:PlaceEntity):Long

    @Update
    fun update(product: PlaceEntity)

    @Query("DELETE FROM PlaceEntity")
    fun deleteAll()

    @Query("UPDATE PlaceEntity SET imgRef = :imgRef WHERE uid = :uid")
    fun updateImgRef(uid:Int, imgRef:Bitmap)

    @Query("UPDATE PlaceEntity SET place = :place, `order` = :order, price = :price, movePrice = :movePrice, comments = :comments WHERE uid = :uid")
    fun updatePlace(uid:Int, place:String, order:Int, price:Double, movePrice:Double, comments:String)

    @Query("UPDATE PlaceEntity SET longitud = :longitud, latitud = :latitud WHERE uid = :uid")
    fun updateLatLon(uid:Int, longitud:Double, latitud:Double)
}
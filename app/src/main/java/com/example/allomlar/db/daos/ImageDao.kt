package com.mac.allomalar.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mac.allomalar.models.Image

@Dao
interface ImageDao {

    @Query("Select * from images")
    suspend fun readImages(): List<Image?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: Image)

    @Query("Select * from images where id =:imageId")
    suspend fun getImageById(imageId: String): Image?
}
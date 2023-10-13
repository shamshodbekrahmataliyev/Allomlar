package com.mac.allomalar.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mac.allomalar.models.Book
import com.mac.allomalar.models.Science

@Dao
interface ScienceDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAllScience(list: List<Science?>?)

    @Query("Select * from science where author_id =:authId")
    suspend fun getAllScienceOfAlloma(authId: Int):  List<Science>
}
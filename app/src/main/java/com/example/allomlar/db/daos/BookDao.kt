package com.mac.allomalar.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mac.allomalar.models.Book

@Dao
interface BookDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAllBooks(list: List<Book?>?)

    @Query("Select * from books where author_id =:authId")
    suspend fun getAllBookOfAlloma(authId: Int):  List<Book>
}
package com.mac.allomalar.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    var author_id: Int,
    @PrimaryKey  var id: Int,
    var name: String
)
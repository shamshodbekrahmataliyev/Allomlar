package com.mac.allomalar.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "madrasas")
data class Madrasa(
    val century_id: Int,
    @PrimaryKey val id: Int,
    val name: String,
    val regionId: Int?
)
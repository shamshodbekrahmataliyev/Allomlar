package com.mac.allomalar.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "centuries")
data class Century(
    @ColumnInfo(name = "century_name") var century: String,
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "sum_madrasa") var sum_madrasa: String
)
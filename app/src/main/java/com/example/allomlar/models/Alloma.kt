package com.mac.allomalar.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Alloma(
    @ColumnInfo(name="alloma_about")val about: String,
    @ColumnInfo(name="alloma_birth_area")val birth_area: String,
    @ColumnInfo(name="alloma_birth_year")val birth_year: String,
    @PrimaryKey val id: Int,
    @ColumnInfo(name="alloma_image")val image_url: String,
    @ColumnInfo(name="alloma_madrasa")val madrasa_alloma: String,
    @ColumnInfo(name="alloma_name")val name: String
)
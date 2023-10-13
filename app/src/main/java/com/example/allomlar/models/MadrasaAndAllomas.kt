package com.mac.allomalar.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "madrasa_and_allomas")
data class MadrasaAndAllomas(
    @PrimaryKey var id: Int, // 1
    var image_url: String, // /images/%D0%98%D0%B1%D0%BD_%D0%A1%D0%B8%D0%BD%D0%BE.png
    var madrasa_alloma: Int, // Dor ul-marza X asr
    var name: String // Abu Ali ibn Sino
)
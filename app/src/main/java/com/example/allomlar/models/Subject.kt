package com.mac.allomalar.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class Subject(
    var allomaId: Int? = null,
    @PrimaryKey var id: Int, // 1
    var image_url: String, // /images/abc.png
    var menu: String, // Abu Ali ibn Sino
    var name: String // Ta'lim
)
package com.mac.allomalar.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="alloma_and_subjects")
data class AllomaAndSubjects(
    @PrimaryKey var id: Int, // 1
    var image_url: String, // https://ic-v.herokuapp.com/images/abc.png
    var menu: String, // Abu Ali ibn Sino
    var name: String // Ta'lim
)
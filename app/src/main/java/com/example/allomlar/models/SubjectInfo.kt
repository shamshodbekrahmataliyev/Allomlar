package com.mac.allomalar.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject_info")
data class SubjectInfo(
    @PrimaryKey var id: Int, // 1
    var image_url: String, // https://ic-v.herokuapp.com/images/left1.jpg
    var subject: Int, // 2
    var text: String // Ibn Sinoning tabobat sohasidagi eng katta xizmati o'z davrining tibbiy bilimlarini tizimlashtirdi va tibbiyot nazariyasini shakllantirdi
)
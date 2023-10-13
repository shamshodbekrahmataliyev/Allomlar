package com.mac.allomalar.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "science")
data class Science(
    var author_id: Int, // 1
    @PrimaryKey var id: Int, // 3
    var image_url: String, // /images/left3.jpg
    var info: String // Odam nafas olganda havo o'pka orqali borishini aniqlagan. Ungacha bo'lgan olimlar havo o'pka orqali emas to'g'ri yurakka boradi deb tasavvur qilishgan.
)
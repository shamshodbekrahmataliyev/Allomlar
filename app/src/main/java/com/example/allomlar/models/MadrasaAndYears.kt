package com.mac.allomalar.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "madrasa_and_years")
class MadrasaAndYears {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var name: String? = null
    var years: ArrayList<Int>? = null

    constructor() {
        years = ArrayList()
    }

    constructor(name: String?, years: ArrayList<Int>?) {
        this.name = name
        this.years = years
    }
}
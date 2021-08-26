package com.example.g_maintain.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "region_table")
data class Region(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    val regionName:String
) : Parcelable

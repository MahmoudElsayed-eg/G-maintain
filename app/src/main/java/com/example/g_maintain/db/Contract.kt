package com.example.g_maintain.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "contract_table")
data class Contract(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name: String,
    var period: Int,
    val firstDate: String = "",
    val clientId: Int = 0,
    val region: String = ""
) : Parcelable

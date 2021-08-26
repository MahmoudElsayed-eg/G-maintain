package com.example.g_maintain.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "visit_table")
data class Visit(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val contractId: Int,
    val name: String,
    val description: String = "",
    val price: Int = 0,
    val notes: String = "",
    val done: Boolean = false,
    val date: String = ""
) : Parcelable

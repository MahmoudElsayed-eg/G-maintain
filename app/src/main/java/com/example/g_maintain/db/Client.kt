package com.example.g_maintain.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "client_table" , indices = [Index(value = ["name"], unique = true)])
data class Client(
    val name: String,
    val phone: String,
    val superVisor: String = "",
    val phone2: String = "",
    val address: String = "",
    val notes: String = "",
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable

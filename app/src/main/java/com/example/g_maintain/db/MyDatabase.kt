package com.example.g_maintain.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [Client::class,Contract::class,Region::class,Visit::class],version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getClientDao() : ClientDao
    abstract fun getVisitsDao() : VisitDao
    abstract fun getRegionDao() : RegionDao
    abstract fun getContractDao() : ContractsDao
    companion object {
        @Volatile private var instance : MyDatabase? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDataBase(context).also {
                instance = it
            }
        }
        private fun buildDataBase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MyDatabase::class.java,
            "MyDataBase"
        ).fallbackToDestructiveMigration().build()
    }
}
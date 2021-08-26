package com.example.g_maintain.db

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVisit(visit: Visit)

    @Update
    suspend fun updateVisit(visit: Visit)

    @Delete
    suspend fun deleteVisit(visit: Visit)

    @Query("SELECT * FROM visit_table")
    fun getAllVisits(): PagingSource<Int,Visit>

    @Query("SELECT * FROM visit_table WHERE date = :date AND done = 0")
    fun searchByDate(date: String) : PagingSource<Int,Visit>

    @Query("SELECT * FROM visit_table WHERE contractId = (SELECT id FROM contract_table WHERE region Like '%' || :region || '%') AND done = 0")
    fun searchByRegion(region: String) : PagingSource<Int,Visit>

    @Query("SELECT * FROM visit_table WHERE contractId = :id")
    fun searchById(id : Int): PagingSource<Int,Visit>

    @Query("DELETE FROM visit_table WHERE contractId = :id")
    fun deleteById(id: Int)

    @Query("SELECT * FROM visit_table WHERE contractId = :id")
    fun searchByIdList(id : Int): List<Visit>

    @Query("SELECT * FROM visit_table WHERE contractId = (SELECT id FROM contract_table WHERE region Like '%' || :region || '%') AND done = 0 AND date = :date")
    fun searchByRegionAndDate(region: String , date: String) : PagingSource<Int,Visit>
}
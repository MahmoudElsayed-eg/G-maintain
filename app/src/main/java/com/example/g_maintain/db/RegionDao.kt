package com.example.g_maintain.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RegionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRegion(region: Region)

    @Update
    suspend fun updateRegion(region: Region)

    @Delete
    suspend fun deleteRegion(region: Region)

    @Query("SELECT * FROM REGION_TABLE")
    fun getAllRegions(): PagingSource<Int,Region>

    @Query("select * from region_table where regionName like '%' || :string || '%'")
    fun getRegionsByName(string: String) : LiveData<Region>

    @Query("select regionName from region_table where regionName like '%' || :string || '%' limit 10")
    fun getRegionsByNameList(string: String) : List<String>
}
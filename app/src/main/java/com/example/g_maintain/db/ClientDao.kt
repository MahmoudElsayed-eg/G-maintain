package com.example.g_maintain.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Insert
    suspend fun addClient(client: Client)

    @Query("SELECT * FROM client_table")
    fun getClients(): PagingSource<Int, Client>

    @Update
    suspend fun updateClient(client: Client)

    @Delete
    suspend fun deleteClient(client: Client)

    @Query("select * from client_table where id = :id")
    suspend fun getClientById(id: Int): Client?

    @Query("select * from client_table where name = :name")
    suspend fun getClientByName(name: String): Client?

    @Query("SELECT * FROM client_table where name like '%' || :string || '%'")
    fun getAllClientsByName(string: String): LiveData<Client>

    @Query("SELECT * FROM client_table where name like '%' || :string || '%'")
    fun getAllClientsByNamePaging(string: String): PagingSource<Int,Client>

    @Query("SELECT name FROM client_table where name like '%' || :string || '%' limit 10")
    fun getAllClientsByNameList(string: String): List<String>
}
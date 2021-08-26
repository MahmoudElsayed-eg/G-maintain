package com.example.g_maintain.db

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addContract(contract: Contract): Long

    @Update
    suspend fun updateContract(contract: Contract)

    @Delete
    suspend fun deleteContract(contract: Contract)

    @Query("Select * from contract_table")
    fun getAllContracts(): PagingSource<Int, Contract>

    @Query("select * from contract_table where clientId in (select client_table.id from client_table where client_table.name like '%' || :string || '%')")
    fun searchContractByName(string: String): PagingSource<Int, Contract>

    @Query("select * from contract_table where clientId in (select client_table.id from client_table where client_table.phone like '%' || :phone || '%' or client_table.phone2 like '%' || :phone || '%' )")
    fun searchContractByPhone(phone: String): PagingSource<Int, Contract>

    @Query("select * from contract_table where id = :id")
    fun getContractById(id: Int): Contract?

    @Query("select * from contract_table where clientId = :id")
    fun getContractByClientId(id: Int): Contract?

    @Query("select * from contract_table where clientId in (select client_table.id from client_table where client_table.name like '%' || :string || '%' and (client_table.phone like '%' || :phone || '%' or client_table.phone2 like '%' || :phone || '%'))")
    fun searchContractByPhoneAndName(phone: String, string: String): PagingSource<Int, Contract>
}
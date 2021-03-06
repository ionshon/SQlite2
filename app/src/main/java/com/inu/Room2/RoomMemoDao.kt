package com.inu.Room2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

// 입출력 접근
@Dao
interface RoomMemoDao {
    @Query("select * from room_memo")
    fun getAll(): List<RoomMemo>

    @Insert(onConflict = REPLACE)
    fun  insert(memo: RoomMemo)

    @Delete
    fun delete(memo: RoomMemo)
}
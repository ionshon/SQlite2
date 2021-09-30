package com.inu.Room2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 메모 형식
@Entity(tableName = "room_memo")
class RoomMemo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var no:Long? = null

    @ColumnInfo
    var content: String = ""

    @ColumnInfo(name = "date")
    var datetime: Long = 0

    constructor(content: String, datetime: Long) {
        this.content = content
        this.datetime =  datetime
    }
}
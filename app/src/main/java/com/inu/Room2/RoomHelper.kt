package com.inu.Room2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//데이터 베이스인데 입출력 함수 있음
@Database(entities = arrayOf(RoomMemo::class), version = 1, exportSchema = false)
abstract class RoomHelper: RoomDatabase() {
    abstract fun roomMemoDao(): RoomMemoDao

    // 소스 : https://blog.yena.io/studynote/2018/09/08/Android-Kotlin-Room.html
    companion object {
        private var DB: RoomHelper? = null

        fun getInstance(context: Context): RoomHelper? {
            if (INSTANCE == null) {
                synchronized(RoomHelper::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RoomHelper::class.java, "room_db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
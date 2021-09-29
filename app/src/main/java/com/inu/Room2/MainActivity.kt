package com.inu.Room2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.inu.Room2.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    var helper: RoomHelper? = null
    val memoAdapter = RecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        helper = Room.databaseBuilder(this, RoomHelper::class.java, "room_memo")
            .addMigrations(MigrateDatabase.MIGRATE_1_2)
            .allowMainThreadQueries() // 공부할 때만 쓴다
            .build()



        memoAdapter.helper = helper

        memoAdapter.listData.addAll(helper?.roomMemoDao()?.getAll() ?: listOf())

        with(binding) {
            recyclerMemo.adapter = memoAdapter
            recyclerMemo.layoutManager = LinearLayoutManager(this@MainActivity)

            buttonSave.setOnClickListener {
                if (editMemo.text.toString().isNotEmpty()) {
                    val memo = RoomMemo(binding.editMemo.text.toString(), System.currentTimeMillis())
                    helper?.roomMemoDao()?.insert(memo)
                    memoAdapter.listData.clear()

                    memoAdapter.listData.addAll(helper?.roomMemoDao()?.getAll() ?: listOf())


                    memoAdapter.notifyDataSetChanged()
                    editMemo.setText("")
                }
            }
        }
    }

    fun refreshAdapter() {
        CoroutineScope(Dispatchers.IO)
    }
}

//룸 변경사항 적용하기
object MigrateDatabase {
    val MIGRATE_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val alter = "ALTER table room_memo add column new_title text"
            database.execSQL(alter)
        }
    }
}

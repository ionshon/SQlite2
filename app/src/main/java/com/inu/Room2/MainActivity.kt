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
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    var helper: RoomHelper? = null // Room 데이터베이스 초기화
    val memoAdapter = RecyclerAdapter() // 어댑터 객체 생성 연결
    lateinit var memoDAO: RoomMemoDao // DAO 객체 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // DB객체에 커스텀DB 연결
        helper = Room.databaseBuilder(this, RoomHelper::class.java, "room_memo")
            .addMigrations(MigrateDatabase.MIGRATE_1_2)
            .allowMainThreadQueries() // 공부할 때만 쓴다
            .build()
        memoDAO = helper!!.roomMemoDao() // DB에 있는 DB접근인터페이스(roomMemoDao) 연결

        memoAdapter.helper = helper // 어댑터 DB 변수에 커스텀DB 연결

        refreshAdapter()
        //memoAdapter.listData.addAll(helper?.roomMemoDao()?.getAll() ?: listOf())

        with(binding) {
            recyclerMemo.adapter = memoAdapter // 메모아답타 형식으로
            recyclerMemo.layoutManager = LinearLayoutManager(this@MainActivity) // 리니어 형식으로

            buttonSave.setOnClickListener {
                if (editMemo.text.toString().isNotEmpty()) {
                    val memo = RoomMemo(binding.editMemo.text.toString(), System.currentTimeMillis())
                    insertMemo(memo)

                    editMemo.setText("")
                }
            }
        }
    }

    fun insertMemo(memo:RoomMemo) {
        memoDAO.insert(memo)
        //helper?.roomMemoDao()?.insert(memo)

        refreshAdapter()
    }

// 아탑터 클리어후 DAO를 통해 아답터에 현재 모든 DB내 데이터 다시 가져온다
    // 간단한 것만 이방식
    fun refreshAdapter() {
     //   CoroutineScope(Dispatchers.IO).launch {
            memoAdapter.listData.clear()
            memoAdapter.listData.addAll(memoDAO.getAll() ?: listOf())
    //        withContext(Dispatchers.Main) { // 화면을 갱신할 때만 메인 쓰레드를 실행해
                memoAdapter.notifyDataSetChanged()
      //      }

     //   }
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

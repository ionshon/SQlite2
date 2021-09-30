package com.inu.Room2

import java.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inu.Room2.databinding.ItemRecyclerBinding

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    var helper: RoomHelper? = null
    var listData = mutableListOf<RoomMemo>()

    // 아이템뷰 바인딩하고 홀더 리턴
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    // 아이템 갯수
    override fun getItemCount(): Int {
        return listData.size
    }

    // 홀더(아이템) 위치에 홀더 통해 메모 세팅
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = listData.get(position)
        holder.setRoomMemo(memo)
    }

    // 아답터에서 제일 먼저 만든다
    // 아이템뷰 구멍
    inner class Holder(val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        var mRoomMemo:RoomMemo? = null
        init {
            binding.buttonDelete.setOnClickListener {
                helper?.roomMemoDao()?.delete(mRoomMemo!!)
                listData.remove(mRoomMemo)
                notifyDataSetChanged()
            }
        }
        fun setRoomMemo(memo:RoomMemo) {
            with(binding) {
                textNo.text = "${memo.no}"
                textContent.text = memo.content
                val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
                // 날짜 포맷은 SimpleDateFormat으로 설정합니다.
                textDatetime.text = "${sdf.format(memo.datetime)}"
            }
            this.mRoomMemo = memo
        }
    }
}

package com.ark.noahs.historymanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.ark.noahs.historymanager.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity(), MainRecyclerAdapter.MyInterface {
    private lateinit var binding: ActivityMainBinding                                               //데이터바인딩
    var introduced : Boolean = false                                                                //최초에 인트로 화면 표시 후 별도 조작이 있었는지 나타내는 불린

    interface ClickListener {                                                                       //리사이클러 뷰 아이템 터치용
        fun onClick(view: View?, position: Int)
    }

    class RecyclerTouchListener(                                                                    //리사이클러뷰 터치 리스너
        context: Context?,
        recyclerView: RecyclerView,
        clickListener: ClickListener?
    ) : OnItemTouchListener {
        private val gestureDetector: GestureDetector = GestureDetector(                                 //솔직히 복붙이라 잘 모름
            context,
            object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }
            })
        private val clickListener: ClickListener? = clickListener

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)                  //데이터바인딩용 컨텐츠뷰 연결

        //Recycler View Setup
        var dataList : ArrayList<MainRecyclerData> = arrayListOf()                                      //리사이클러 뷰 용 데이터셋
        val mAdapter = MainRecyclerAdapter(this, this, dataList)                    //리사이클러 뷰 커스텀 어댑터
        binding.recView.adapter = mAdapter                                                              //어댑터 연결
        val lm = LinearLayoutManager(this)                                                      //레이아웃 매니저
        binding.recView.layoutManager = lm                                                              //레이아웃 매니저 연결
        binding.recView.setHasFixedSize(true)                                                           //고정 사이즈 토글

        binding.recView.addOnItemTouchListener(                                                         //리사이클러 뷰 터치 리스너
            RecyclerTouchListener(
                applicationContext,
                binding.recView,
                object : ClickListener {
                    override fun onClick(
                        view: View?,
                        position: Int
                    ) {                                              //여기 안에 기능구현 하면 됨
                        //여기여기
                        var data = dataList[position]
                        val intent = Intent(baseContext, CategoryActivity::class.java)

                        intent.putExtra("image", data.getDataImage())
                        intent.putExtra("title", data.getDataTitle())
                        intent.putExtra("desc", data.getDataDesc())

                        startActivity(intent)
                    }
                })
        )

        //FAB onClick
        binding.BigFAB.setOnClickListener {                                                             //FAB 클릭 리스너
            MaterialDialog(this@MainActivity, BottomSheet(LayoutMode.WRAP_CONTENT)).show {                            //afollestad의 Material Dialog 호출
                customView(R.layout.dialog_category)                                                            //커스텀 뷰 할당
                cornerRadius(res = R.dimen.dial_corner_radius)                                                    //다이얼로그 모서리 둥글게
                this.findViewById<MaterialButton>(R.id.confirm_button).setOnClickListener {                       //다이얼로그 안 확인 버튼 클릭 리스너
                    val tempText1 = this.findViewById<TextInputEditText>(R.id.dial_cat_input1).text.toString()        //다이얼로그 안 텍스트 필드 1 값
                    val tempText2 = this.findViewById<TextInputEditText>(R.id.dial_cat_input2).text.toString()        //다이얼로그 안 텍스트 필드 2 값
                    if(tempText1.isNotEmpty()) {                                                                        //다이얼로그 안 텍스트 필드 1이 비지 않았고
                        if(tempText2.isNotEmpty())                                                                          //다이얼로그 안 텍스트 필드 2가 비지 않았다면
                            dataList.add(MainRecyclerData("", tempText1, tempText2))                                    //둘 모두 입력해 새 항목 생성
                        else                                                                                                //2가 비었으면
                            dataList.add(MainRecyclerData("", tempText1, " "))                                      //제목만 입력해 사용
                        mAdapter.notifyItemInserted(mAdapter.itemCount - 1)                                         //어댑터에 데이터셋 변경 고지
                        if(0 < binding.recView.adapter?.itemCount ?: 0) {                                                   //변경된 후 데이터셋의 크기가 0보다 크면
                            binding.root.transitionToState(R.id.nonzero_root)                                                   //레이아웃을 '0이 아님'으로 변경
                            binding.Intro.transitionToState(R.id.nonzero_intro)                                                 //...
                        }
                        this.dismiss()                                                                                    //다이얼로그 닫기
                    }                                                                                                   //다이얼로그 안 텍스트 필드 1이 비었으면 아무것도 하지 않음
                }
            }
        }
    }

    fun onClickIntro(view: View?) {                                                                 //최초 클릭 리스너
        if (!introduced){                                                                               //이제 막 켜졌으면
            if(0 < binding.recView.adapter?.itemCount ?: 0) {                                               //데이터셋에 기존 값이 있으면
                binding.root.transitionToState(R.id.nonzero_root)                                               //'0이 아님' 레이아웃 적용
                binding.Intro.transitionToState(R.id.nonzero_intro)                                             //...
            } else {                                                                                        //기존 값이 없으면
                binding.root.transitionToState(R.id.zero_root)                                                  //'0' 레이아웃 적용
                binding.Intro.transitionToState(R.id.zero_intro)                                                //...
            }
            binding.txtIntro.text = getString(R.string.introguide2)                                         //텍스트는 조건 무관히 2단계로 변경
        }
    }

    override fun notifyChange(position: Int) {                                                      //어댑터에서 값 변경 알려오는 용 인터페이스 구현 메소드
        val adapter = binding.recView.adapter                                                           //어댑터 불러오는 일이 많아서 이름만 로컬화
        (adapter as MainRecyclerAdapter).removeAt(position)                                             //요청된 항목 삭제
        adapter.notifyItemRemoved(position)                                                             //어댑터에 삭제 사실 고지
        adapter.notifyDataSetChanged()                                                                  //...
        if(0 == binding.recView.adapter?.itemCount ?: 0) {                                              //변경된 후 데이터셋의 크기가 0보다 크면
            binding.root.transitionToState(R.id.zero_root)                                                  //레이아웃을 '0이 아님'으로 변경
            binding.Intro.transitionToState(R.id.zero_intro)                                                //...
        }
    }
}
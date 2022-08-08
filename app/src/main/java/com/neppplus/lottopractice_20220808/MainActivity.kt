package com.neppplus.lottopractice_20220808

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

//    내 번호 6개 저장
    val mMyNumList = arrayOf(5,10,15,16,21,34)

//    컴퓨터가 뽑은 랜덤 당첨번호 6개를 저장할 ArrayList를 만들어주자
    var mWinNumList = ArrayList<Int>()
    var mBonusNum = 0

//    랜덤 번호 6개를 집어넣을 텍스트뷰 자료형의 ArrayList를 만들자
    val mWinNumViewList = ArrayList<TextView>()

//    사용금액 / 당첨금액
    var mUsedMoney = 0
    var mWinMoney = 0L  // 30억 이상의 당첨 대비, Long 타입으로 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mWinNumViewList.add(winNum1Txt)
        mWinNumViewList.add(winNum2Txt)
        mWinNumViewList.add(winNum3Txt)
        mWinNumViewList.add(winNum4Txt)
        mWinNumViewList.add(winNum5Txt)
        mWinNumViewList.add(winNum6Txt)

        buyLottoBtn.setOnClickListener {
//            로또 한장 구매 버튼이 클릭 이벤트 처리
            buyLotto()
        }

        autoBuyBtn.setOnClickListener {

        }
    }

    fun buyLotto() {
        mUsedMoney += 1000

//        1. 로또 당첨 번호 6개 선정
        for (i in 0 until 6) {
//            등록되도 괜찮은(중복되지 않은) 숫자가 나올때까지 무한 반복
            while (true) {
                val randomNum = (Math.random() * 45 + 1).toInt()

//                var isRepeat = false
//                for (num in mWinNumList) {
//                    if (num == randomNum) {
//                        isRepeat = true
//                        break
//                   }
//                }
//                if (!isRepeat) {
//                    mWinNumList.add(randomNum)
//                    break
//                }

                if(!mWinNumList.contains(randomNum)) {
                    mWinNumList.add(randomNum)
                    break
                }
            }
        }

//        2. 당첨 번호 정렬 (Bubble sort)  > 텍스트뷰에 표현
        mWinNumList.sort()  // 작은수 ~ 큰수 정리 완료

        for ((index, num) in mWinNumList.withIndex()) {
//            텍스트뷰 변수를 가져와서 text속성으로 넣기
//            텍스트 뷰들을 ArrayList의 재료
//            for-each문 역시 index값을 같이 활용 for문 작성 > .withIndex()
            mWinNumViewList[index].text = num.toString()
        }
    }

    fun checkLotto() {

    }


}
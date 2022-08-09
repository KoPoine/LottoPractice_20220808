package com.neppplus.lottopractice_20220808

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
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

//    사용금액 / 당첨금액 / 당첨 횟수
    var mUsedMoney = 0
    var mWinMoney = 0L  // 30억 이상의 당첨 대비, Long 타입으로 설정

    var mFirstCount = 0
    var mSecondCount = 0
    var mThirdCount = 0
    var mFourthCount = 0
    var mFifthCount = 0
    var mLoseCount = 0

    var isAuto = false

    lateinit var mHandler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHandler = Handler(Looper.getMainLooper())

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

            if (!isAuto) {
//            처음 눌리면 > 반복 구매 시작  => 이 버튼이 처음눌렸는지? 아닌지에 대한 정보

//                단순 반복 => UI 갱신속도가 못따라온다 (사용자의 입장에서 앱이 죽은 것 처럼 보인다.)
//                while (true) {
//                    buyLotto()
//
//                    if (mUsedMoney >= 50000000) {
//                        break
//                    }
//                }
                isAuto = true
                mHandler.post(buyLottoRunnable)
            }
            else {
//            다시 눌리면 > 반복 구매 정지 > 핸들러로 동작
//                핸들러에게 등록된 다음 할 일(buyLottoRunnable) 제거
                mHandler.removeCallbacks(buyLottoRunnable)

                isAuto = false
            }



        }
    }

    val buyLottoRunnable  = object : Runnable{
        override fun run() {
//            1000만원을 아직 안썻다면 > 로또 한장을 추가 구매
            if (mUsedMoney <= 10000000) {
                buyLotto()

//            핸들러에게 다시 로또 한장을 사는 행위(buyLottonRunnable)를 등록 (재귀함수)
                mHandler.post(this)
            }
//            1000만원이 넘었다면 할 일 정지
            else {
                Toast.makeText(this@MainActivity, "자동 구매가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun buyLotto() {
        mUsedMoney += 1000

        mWinNumList.clear()

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

//        3. 보너스 번호 하나 선정 > 텍스트 뷰에 배치
        while (true) {
            val randomNum = (Math.random() * 45 + 1).toInt()

            if (!mWinNumList.contains(randomNum)) {
                mBonusNum = randomNum
                bonusNumTxt.text = mBonusNum.toString()
                break
            }
        }

        checkLotto()
    }

    fun checkLotto() {
//        4. 비교
        var correctCount = 0

//        내 번호를 하나씩 조회 (for each)
        for (myNum in mMyNumList) {
//            당첨 번호를 맞췄는가? => 당첨번호 목록에 내 번호가 들어있나?
            if (mWinNumList.contains(myNum)) {
                correctCount++
            }
        }

//        5. 순위 선정 (맞춘 갯수에 따라) > 등수 판단 // 텍스뷰에 출력
        when (correctCount) {
            6 -> {
//                1등 당첨
                mWinMoney += 3000000000
                mFirstCount++
            }
            5 -> {
//                보너스 번호를 맞췄는지 => 보너스 번호가 내 번호 목록에 들어있나?
                if (mMyNumList.contains(mBonusNum)) {
//                    가지고 있다 > 2등
                    mWinMoney += 50000000
                    mSecondCount ++
                }
                else {
                    mWinMoney += 2000000
                    mThirdCount ++
                }
            }
            4 -> {
                mWinMoney += 50000
                mFourthCount ++
            }
            3 -> {
                mWinMoney += 5000
                mFifthCount ++
            }
            else -> {
                mLoseCount ++
            }
        }

//        사용금액 / 당첨 금액 및 당첨 횟수를 텍스트뷰에 각각 반영
        usedMoneyTxt.text = "사용금액 : ${mUsedMoney}원"
        winMoneyTxt.text = "당첨금액 : ${mWinMoney}원"

        rank1stTxt.text = "1등 당첨 횟수 : ${mFirstCount}회"
        rank2ndTxt.text = "2등 당첨 횟수 : ${mSecondCount}회"
        rank3rdTxt.text = "3등 당첨 횟수 : ${mThirdCount}회"
        rank4thTxt.text = "4등 당첨 횟수 : ${mFourthCount}회"
        rank5thTxt.text = "5등 당첨 횟수 : ${mFifthCount}회"
        loseTxt.text = "낙첨 횟수 : ${mLoseCount}회"
    }


}
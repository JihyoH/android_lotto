package com.joeh.lottojoeh;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LinearLayout lottoLayout;
    Button bt;
    DisplayMetrics dm;
    int ballWidth;
    TextView nextLottoDate;
    TextView nextLottoDday;
    String lottoPageUrl = "https://dhlottery.co.kr/gameResult.do?method=byWin";
    String lottoResult;
    TextView lastLotto;
    String selectClassId=".win_result";
    ImageButton refreshBtn;
    ImageButton menuBtn;
    LinearLayout mainContent;
    TextView tabMenu1;
    TextView tabMenu2;
    TextView lottoTitle;
    final int id_int = 20000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dm = getApplicationContext().getResources().getDisplayMetrics();
        mainContent = findViewById(R.id.mainContent);
        tabMenu1 = findViewById(R.id.tabMenuBottom1);
        tabMenu2 = findViewById(R.id.tabMenuBottom2);
        lottoTitle = findViewById(R.id.lottoTitle);
        lastLotto = findViewById(R.id.lastNums);

        bt = findViewById(R.id.getNumsBtn);
        ballWidth = (int) (dm.widthPixels) / 8;
        lottoLayout = findViewById(R.id.LottoNums);
        nextLottoDate = findViewById(R.id.nextLottoDate);
        nextLottoDday = findViewById(R.id.nextLottoDday);
        refreshBtn = findViewById(R.id.refreshBtn);
        menuBtn = findViewById(R.id.menuBtn);


        lottoLayout.removeAllViews();
        getLastLotto();

        getDate(7);

        for(int i=0; i<6; i++){
            textview(i,lottoLayout,8);
        }

        for(int i=0; i< 6; i++) {
            TextView tview = (TextView) lottoLayout.getChildAt(i);

            tview.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            tview.setWidth(ballWidth);
            tview.setHeight(ballWidth);

        }

        tabMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastLotto.setText("불러오는 중...");
                tabMenu1.setBackgroundColor(getResources().getColor(R.color.colorDullGreen));
                tabMenu2.setBackgroundColor(getResources().getColor(R.color.colorDullDark));
                lottoTitle.setText("다음 로또 발표예정일");
                getDate(7);
                lottoPageUrl = "https://dhlottery.co.kr/gameResult.do?method=byWin";
                selectClassId=".win_result";
                getLastLotto();

                lottoLayout.removeAllViews();
                for(int i=0; i<6; i++){
                    textview(i,lottoLayout,8);
                }

            }
        });

        tabMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastLotto.setText("불러오는 중...");
                tabMenu1.setBackgroundColor(getResources().getColor(R.color.colorDullDark));
                tabMenu2.setBackgroundColor(getResources().getColor(R.color.colorDullGreen));
                lottoTitle.setText("다음 연금복권 발표예정일");
                getDate(5);

                lottoPageUrl = "https://dhlottery.co.kr/gameResult.do?method=win720";
                selectClassId = "#after720";
                getLastLotto();

                lottoLayout.removeAllViews();
                for(int i=0; i<7; i++){
                    textview(i,lottoLayout,10);
                }

            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {


                int[] lottoResult = lottoNum(lottoLayout.getChildCount());
                for(int i=0; i< lottoResult.length; i++) {
                    TextView tview = (TextView) lottoLayout.getChildAt(i);

                    tview.setText(lottoResult[i]+"");

                    if(lottoResult.length == 6) {
                        switch ((int) lottoResult[i] / 10) {
                            case 0:
                                tview.setBackgroundColor(getResources().getColor(R.color.lottoYellow));
                                break;
                            case 1:
                                tview.setBackgroundColor(getResources().getColor(R.color.lottoBlue));
                                break;
                            case 2:
                                tview.setBackgroundColor(getResources().getColor(R.color.lottoRed));
                                break;
                            case 3:
                                tview.setBackgroundColor(getResources().getColor(R.color.lottoGrey));
                                break;
                            case 4:
                                tview.setBackgroundColor(getResources().getColor(R.color.lottoGreen));
                                break;
                            default:
                                tview.setBackgroundColor(getResources().getColor(R.color.lottoYellow));
                                break;

                        }
                    }else {
                        switch (i) {
                            case 0:
                                tview.setBackgroundColor(getResources().getColor(R.color.pensionOne));
                                break;
                            case 1:
                                tview.setBackgroundColor(getResources().getColor(R.color.pensionTwo));
                                break;
                            case 2:
                                tview.setBackgroundColor(getResources().getColor(R.color.pensionThree));
                                break;
                            case 3:
                                tview.setBackgroundColor(getResources().getColor(R.color.pensionFour));
                                break;
                            case 4:
                                tview.setBackgroundColor(getResources().getColor(R.color.pensionFive));
                                break;
                            case 5:
                                tview.setBackgroundColor(getResources().getColor(R.color.pensionSix));
                                break;
                            case 6:
                                tview.setBackgroundColor(getResources().getColor(R.color.pensionSeven));
                                break;
                            default:
                                tview.setBackgroundColor(getResources().getColor(R.color.pensionOne));
                                break;

                        }
                    }
                }

            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastLotto.setText("불러오는 중...");
                getLastLotto();
            }
        });
    }

    // 로또번호 출력을 위한 textview 하나씩 만들어주는 function
    public void textview(int cnt, LinearLayout parentView, int widthPiece){

        //TextView 생성
        TextView view1 = new TextView(this);
        view1.setId(cnt + id_int);
        view1.setWidth(dm.widthPixels/widthPiece);
        view1.setHeight(dm.widthPixels/8);
        view1.setText("?");
        view1.setTextColor(getResources().getColor(R.color.colorWhite));
        view1.setTextSize(20);
        view1.setBackgroundResource(R.color.lottoYellow);
        view1.setPadding(10,10,10,10);

        view1.setGravity(Gravity.CENTER);

        //layout_width, layout_height, gravity 설정
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        view1.setLayoutParams(lp);

        //부모 뷰에 추가
        parentView.addView(view1);
    }

    public int[] lottoNum(int childCnt){
        int[] mylotto = new int[childCnt];
        switch (childCnt){
            case 6:
                int singleDigitCnt = 0;
                int doubleDigitCnt = 0;

                for(int i=0; i< mylotto.length; i++){
                    mylotto[i] = (int)(Math.random()*45 +1);
                    if(mylotto[i] <10){
                        singleDigitCnt++;
                        if(singleDigitCnt > 1){
                            i--;
                        }
                    }else if(mylotto[i] <20){
                        doubleDigitCnt++;
                        if(doubleDigitCnt > 2){
                            i--;
                        }
                    }
                    for(int j=0; j<i; j++){
                        if(mylotto[j] == mylotto[i]){
                            i--;
                            break;
                        }
                    }
                }

                Arrays.sort(mylotto);
                break;
            case 7:
                for(int i=0; i< mylotto.length; i++){
                    mylotto[i] = (int)(Math.random()*10);
                }
                break;
            default:
                break;
        }

        return mylotto;
    }

    // 다음 로또 발표일정. 무조건 가장 가까운 미래의 토요일만 구하기
    public void getDate(int dayNum){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        int cur_week = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH); // 이번주차

        String year = yearFormat.format(currentTime);
        String month = monthFormat.format(currentTime);
        String day = dayFormat.format(currentTime);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,Integer.parseInt(year));
        cal.set(Calendar.MONTH,Integer.parseInt(month)-1);
        cal.set(Calendar.WEEK_OF_MONTH,cur_week);
        cal.set(Calendar.DAY_OF_WEEK,dayNum);
        Date curSat = cal.getTime();

        if(Integer.parseInt(dayFormat.format(currentTime))>=Integer.parseInt(dayFormat.format(curSat))){
            cal.set(Calendar.YEAR,Integer.parseInt(year));
            cal.set(Calendar.MONTH,Integer.parseInt(month)-1);
            cal.set(Calendar.WEEK_OF_MONTH,cur_week+1);
            cal.set(Calendar.DAY_OF_WEEK,dayNum);
            curSat = cal.getTime();
        }

        String month2 = monthFormat.format(curSat);
        String day2 = dayFormat.format(curSat);

        nextLottoDate.setText(month2 + "/" + day2 );//year2 + "-" +
        int dDayCalc = (Integer.parseInt(day2)-Integer.parseInt(day));
        String dDay = dDayCalc<0? "+"+dDayCalc*(-1) : "-"+dDayCalc;
        nextLottoDday.setText("D"+dDay);
    }

    private void getLastLotto(){
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TextView lastLotto = findViewById(R.id.lastNums);
                lastLotto.setText(lottoResult);
            }
        } ;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(lottoPageUrl).get();
                    Elements contents;
                    contents = doc.select(selectClassId);
                    lottoResult = contents.text().replace(") ",")\n").replace(" 보너스", "\n보너스");


                } catch (IOException e) {
                    //e.printStackTrace();
                    Log.d("Joeh LottoNum의 getNumber()함수 에러 : ",e.getMessage());
                    System.out.println("Joeh LottoNum의 getNumber()함수 에러 : "+e.getMessage());
                    lottoResult = "인터넷 연결 상태를 확인해주세요.";

                }

                runOnUiThread(runnable);


            }
        });

        t.start();

    }

}

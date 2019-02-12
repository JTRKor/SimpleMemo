package com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.adapter.MainListAdapter;
import com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.bus.BusProvider;
import com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.data.Memo;
import com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.db.DBManager;
import com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.event.Delcheck;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    ArrayList<Memo> items = new ArrayList<>(); // 메모리스트 정보
    Boolean delitem[]; //체크박스 용 구분
    MainListAdapter mainlistadapter; //아답터 등록
    Bus bus = BusProvider.getInstance().getBus(); //오픈 소스 이벤트 버스 load
    DBManager dbManager; //DB 등록

    @BindView(R.id.main_lv) ListView main_lv; //리스트뷰
    @BindView(R.id.main_tv_totalcount) TextView main_tv_totalcount; //총개수
    @BindView(R.id.main_tv_finish) TextView main_tv_finish; //종료(끝내기)
    @BindView(R.id.main_iv_write) ImageView main_iv_write; //메모추가버튼
    @BindView(R.id.main_sp_sort) Spinner main_sp_sort; //정렬 스피너
    @BindView(R.id.main_et_search) EditText main_et_search; //검색창
    @BindView(R.id.main_layout_search) LinearLayout main_layout_search; //검색창 레이아웃
    @BindView(R.id.main_tv_del) TextView main_tv_del; //삭제 버튼
    @BindView(R.id.main_scrollview) ScrollView main_scrollview; //스크롤뷰


    Integer sort = 0; // 정렬 구분 변수 등록
    String searchword = ""; // 검색어 변수 등록


    @Override
    protected void onCreate(Bundle savedInstanceState) { // 기본 생성 시 행동하는 함수
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this); // 오픈 소스 버터나이프 사용하기 위한 등록
        bus.register(this); // 오픈 소스 이벤트 버스 레지스터 등록

        /*
        //검색창의 하단라인의 위치 값구하기
        int y = main_layout_search.getBottom();
        //해당 위치값으로 포커스 이동하기
        main_scrollview.smoothScrollTo(0, y);
        */

        ArrayAdapter sortAdapter = ArrayAdapter.createFromResource(this, R.array.sort, R.layout.custom_spinner_list);
        // 정렬 스피너 항목 등록
        sortAdapter.setDropDownViewResource(R.layout.customer_spinner); //스피너 아답터에 스피너항목 등록
        main_sp_sort.setAdapter(sortAdapter); //스피너에 아답터 등록

        main_sp_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //스피너의 항목 선택시 정렬 변경을 위한 함수
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort = position;
                refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        refresh(); // 시작시 화면 및 등록사항 화면 표시
        //포커스얻을대상.requestFocus();
        //리스트뷰의 최상단으로 포커스 맞추기
        main_lv.requestFocus();

        main_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // 메모 리스트뷰에서 메모를 선택할 때 메모를 열기위한 함수
                Intent intent = new Intent(MainActivity.this, WriteActivity.class); // 메모를 열 수 있는 화면 지정
                intent.putExtra("id", items.get(position).getId()); // 지정한 화면으로 이동 시에 정보를 넘길 내용(id 값이 없으면 새로 등록하는 화면이고 값이 있으면 기존 정보를 불러오기)
                startActivityForResult(intent, 1); // 화면으로 이동하면서 받아야할 값이 있으므로 requestcode 등록)
            }
        });

        TextWatcher textWatcher = new TextWatcher() { // 검색 시 이용할 텍스트와쳐 함수
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { //키잉 입력 전에 행동할 내용을 입력하는 구간

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { //키잉 입력과 동시에 행동할 내용을 입력하는 구간

            }

            @Override
            public void afterTextChanged(Editable s) { //키잉이 완료된 후에 행동할 내용을 입력하는 구간
                searchword = main_et_search.getText().toString(); //검색어를 확인 후에 해당 변수에 바로 입력
                refresh(); //검색어가 바뀔 때 마다 바로 화면에 해당 검색으로 변경하기 위한 함수
            }
        };
        main_et_search.addTextChangedListener(textWatcher); //main_et_search에 위에서 설정한 택스트와쳐 등록

        //리스트뷰에 포커스를 이동할 시
        //키보드를 내리는 함수를 로드하여 키보드 안보이게 하기
        main_lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                downKeyboard(MainActivity.this, main_et_search);
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() { //해당 액티비티가 죽을 때 사용하는 함수
        super.onDestroy();
        bus.unregister(this); //이벤트 버스 등록해지(메모리를 위한 필수)
    }

    @OnClick(R.id.main_iv_write) // 해당 화면 클릭시 내용에 대한 함수
    void main_lv_write(View view) { //함수 이름 정의
        Intent intent = new Intent(MainActivity.this, WriteActivity.class); //이동할 화면 등록
        //startActivity(intent);
        startActivityForResult(intent, 0); //해당화면으로 이동하면서 리퀘스트코드 등록
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 화면 이동시에 등록했던 리퀘스트 코드에 대해서 행동할 내용 입력 함수
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) { //리퀘스트코드가 0 일때 행동하는 구간
                refresh();
                //포커스얻을대상.requestFocus();
                //리스트뷰의 최상단으로 포커스 맞추기
                main_lv.requestFocus();
            }
            else if (requestCode == 1) { //리퀘스트코드가 1 일때 행동하는 구간
                refresh();
                //포커스얻을대상.requestFocus();
                //리스트뷰의 최상단으로 포커스 맞추기
                main_lv.requestFocus();
            }
        }
    }

    public void refresh() { //화면 재 정의를 위한 함수
        items.clear(); //모든 메모항목을 클리어 시킨다
        dbManager = new DBManager(MainActivity.this, "memo.db", null, 1); //db 로드

        //아이템 받기
        if (sort == 0) {
            items = dbManager.getMemoList0(searchword);
        }
        else if (sort == 1) {
            items = dbManager.getMemoList1(searchword);
        }
        else if (sort == 2) {
            items = dbManager.getMemoList2(searchword);
        }
        //아이템에 따른 체크박스 초기화
        delitem = null;
        delitem = new Boolean[items.size()]; // 해당 사이즈만큼 체크길이 생성
        for (int i = 0; i < items.size(); i++) { //전부 false값으로 등록
            delitem[i] = false;
        }
        mainlistadapter = new MainListAdapter(items); //메모 아답터의 메모리스트 새로 등록
        main_lv.setAdapter(mainlistadapter); //리스트뷰 안에 아답터 등록
        setListViewHeightBasedOnChildren(main_lv); // 검색창과 리스트뷰의 화면이 동시에 스크롤 될 수 있도록 등록

        main_tv_totalcount.setText(items.size() + "개의 메모"); // 총 메모의 갯수 재 정의
    }

    //리스트뷰의 최상단 포커스를 맞추기 위한 높이 계산함수
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    //키보드를 내리는 함수
    public void downKeyboard(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    //삭제 체크 이벤트
    @Subscribe
    public void delCheck(Delcheck event) {
        delitem[event.getPosition()] = event.isChecked();
    }

    //삭제 버튼 눌렀을 시
    @OnClick(R.id.main_tv_del)
    public void main_tv_del() {
        Integer count = 0;
        for (int i = 0; i < items.size(); i++) {
            if (delitem[i] == true) {
                count++;
            }
        }
        if (count == 0) {
            AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
            ad.setTitle("확인 요청").setMessage("삭제 체크된 메모가 없습니다.").setNeutralButton("확인",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create().show();
        }
        else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("경고");
            alertDialog.setMessage(count + "개의 메모를 정말 삭제하시겠습니까?");
            alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < items.size(); i++) {
                        if (delitem[i] == true) {
                            dbManager.delMemo(items.get(i).getId());
                        }
                    }
                    refresh();
                }
            });
            alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
        }
    }

    @OnClick(R.id.main_tv_finish)
    public void main_tv_finish() {
        finish();
    }
}

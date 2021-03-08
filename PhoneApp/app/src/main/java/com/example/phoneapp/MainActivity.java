package com.example.phoneapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity2";

    private ConstraintLayout mainLayout;
    private RecyclerView rvPhone;
    private PhoneAdapter phoneAdapter;
    private FloatingActionButton fabSave;
    private PhoneService phoneService = PhoneService.refRetrofit.create(PhoneService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        download();
        fabClickEvent();
    }
    public void init(){
        mainLayout = findViewById(R.id.main_layout);
        fabSave = findViewById(R.id.fab_save);
        rvPhone = findViewById(R.id.rv_phone);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvPhone.setLayoutManager(manager);

        phoneAdapter = new PhoneAdapter(this);
        rvPhone.setAdapter(phoneAdapter);
    }
    public void download(){
        Call<CMRespDto<List<Phone>>> call = phoneService.findAll();

        call.enqueue(new Callback<CMRespDto<List<Phone>>>() {
            @Override
            public void onResponse(Call<CMRespDto<List<Phone>>> call, Response<CMRespDto<List<Phone>>> response) {
                CMRespDto<List<Phone>> cmRespDto = response.body();
                List<Phone> phones = cmRespDto.getData();
                // 어댑터 넘기기
                phoneAdapter.setPhones(phones);
            }

            @Override
            public void onFailure(Call<CMRespDto<List<Phone>>> call, Throwable t) {
                Log.d(TAG, "onFailure: findAll Fail");
            }
        });
    }
    public void fabClickEvent(){
        fabSave.setOnClickListener(v -> {
            View dialogView = v.inflate(v.getContext(),R.layout.layout_add_phone,null);
            final TextInputEditText etName = dialogView.findViewById(R.id.et_name);
            final TextInputEditText etTel = dialogView.findViewById(R.id.et_tel);
            AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
            dlg.setTitle("연락처 등록");
            dlg.setView(dialogView);
            dlg.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    savePhone(new Phone(null, etName.getText().toString(), etTel.getText().toString()));
                }
            });
            dlg.setNegativeButton("닫기", null);
            dlg.show();
        });
    }

    public void savePhone(Phone phone){
        Call<CMRespDto<Phone>> call = phoneService.save(phone);
        call.enqueue(new Callback<CMRespDto<Phone>>() {
            @Override
            public void onResponse(Call<CMRespDto<Phone>> call, Response<CMRespDto<Phone>> response) {
                CMRespDto<Phone> cmRespDto = response.body();
                if(cmRespDto.getCode() == 1){
                    phoneAdapter.addPhone(cmRespDto.getData());
                }else {
                    Snackbar.make(mainLayout, "저장을 실패하였습니다.",2000).show();
                }
            }

            @Override
            public void onFailure(Call<CMRespDto<Phone>> call, Throwable t) {
                Snackbar.make(mainLayout, "저장을 실패하였습니다.",2000).show();
            }
        });
    }
    public void updatePhone(Long id, Phone phone, int positoion){
        Call<CMRespDto<Phone>> call = phoneService.update(id, phone);
        call.enqueue(new Callback<CMRespDto<Phone>>() {
            @Override
            public void onResponse(Call<CMRespDto<Phone>> call, Response<CMRespDto<Phone>> response) {
                CMRespDto<Phone> cmRespDto = response.body();
                if(cmRespDto.getCode() == 1){
                    phoneAdapter.updateItem(positoion, cmRespDto.getData());
                }else{
                    Snackbar.make(mainLayout, "저장을 실패하였습니다.",2000).show();
                }
            }

            @Override
            public void onFailure(Call<CMRespDto<Phone>> call, Throwable t) {
                Snackbar.make(mainLayout, "수정을 실패하였습니다.",2000).show();
            }
        });
    }
    public void deletePhone(Long id, int position){
        Call<CMRespDto<Phone>> call = phoneService.delete(id);
        call.enqueue(new Callback<CMRespDto<Phone>>() {
            @Override
            public void onResponse(Call<CMRespDto<Phone>> call, Response<CMRespDto<Phone>> response) {
                CMRespDto<Phone> cmRespDto = response.body();
                if(cmRespDto.getCode() == 1){
                    phoneAdapter.deleteItem(position);
                }else{
                    Snackbar.make(mainLayout, "저장을 실패하였습니다.",2000).show();
                }
            }

            @Override
            public void onFailure(Call<CMRespDto<Phone>> call, Throwable t) {
                Snackbar.make(mainLayout, "삭제를 실패하였습니다.",2000).show();
            }
        });
    }
}
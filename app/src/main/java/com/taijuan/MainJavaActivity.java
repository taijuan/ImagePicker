package com.taijuan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.taijuan.data.ImageItem;
import com.taijuan.imagepicker.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubert
 * <p>
 * Created on 2017/10/31.
 * <p>
 * 这个类与MainActivity的java实现，内容完全相同
 */

public class MainJavaActivity extends AppCompatActivity implements OnPickImageResultListener {

    private ImageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckBox cb_crop = findViewById(R.id.cb_crop);
        cb_crop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ImagePicker.isCrop(isChecked);
            }
        });
        CheckBox cb_multi = findViewById(R.id.cb_multi);
        cb_multi.setChecked(true);//默认是多选
        cb_multi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ImagePicker.multiMode(isChecked);
            }
        });
        Button btn_pick = findViewById(R.id.btn_pick);
        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择图片，第二次进入会自动带入之前选择的图片（未重置图片参数）
                ImagePicker.pick(MainJavaActivity.this, MainJavaActivity.this);
            }
        });
        Button btn_camera = findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接打开相机
                ImagePicker.camera(MainJavaActivity.this, MainJavaActivity.this);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ImageAdapter(new ArrayList<ImageItem>());

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onImageResult(@NotNull List<ImageItem> imageItems) {
        adapter.updateData(imageItems);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImagePicker.clear();//清除缓存已选择的图片
    }
}

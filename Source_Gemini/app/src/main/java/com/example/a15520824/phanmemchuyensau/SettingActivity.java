package com.example.a15520824.phanmemchuyensau;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        this.listView = findViewById(R.id.listView);

        ArrayList<ActionModel> listData = DataManager.Instance().ListData;

        ActionAdapter adapter = new ActionAdapter(this, R.layout.layout_listview_item, listData);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent editIntent = new Intent(view.getContext(), EditActionActivity.class);
                String index = i+ "";
                editIntent.putExtra("INDEX", index);
                startActivity(editIntent);
            }
        });

    }
}

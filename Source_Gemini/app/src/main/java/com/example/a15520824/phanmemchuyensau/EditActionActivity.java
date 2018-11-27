package com.example.a15520824.phanmemchuyensau;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActionActivity extends AppCompatActivity {

    private TextView txtName;
    private EditText txtKey;
    private Button btnSave;
    private ActionModel action;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_action);

        index = Integer.parseInt(getIntent().getStringExtra("INDEX"));

        action = DataManager.Instance().ListData.get(index);

        this.txtName = findViewById(R.id.txtName);
        this.txtKey = findViewById(R.id.txtKey);

        this.txtName.setText(action.getName());
        this.txtKey.setText(action.getKey());

        this.txtKey.setSelection(txtKey.getText().length());
        this.txtKey.setSelected(false);

        this.btnSave = findViewById(R.id.btnSave);
        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAction();
                onBackPressed();
            }
        });
    }

    private void saveAction(){
        String newKey = txtKey.getText().toString();
        if (!DataManager.Instance().checkKey(newKey)) {
            Toast.makeText(this, "Key bạn vừa nhập đã tồn tại, vui lòng nhập key khác", Toast.LENGTH_LONG).show();
            return;
        }

        this.action.setKey(txtKey.getText().toString().toLowerCase());
        DataManager.Instance().ListData.set(index, action);
        DataManager.Instance().saveDatabase();
    }
}

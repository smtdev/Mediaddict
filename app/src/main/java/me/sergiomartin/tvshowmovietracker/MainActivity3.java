package me.sergiomartin.tvshowmovietracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity3 extends AppCompatActivity {
    @BindView(R.id.lbl_title)
    TextView lblTitle;
    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.btn_enter)
    Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_enter)
    public void onClick() {
    }
}
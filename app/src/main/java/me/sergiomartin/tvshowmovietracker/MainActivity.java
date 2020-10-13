package me.sergiomartin.tvshowmovietracker;

import androidx.appcompat.app.AppCompatActivity;
import me.sergiomartin.tvshowmovietracker.databinding.ActivityMainBinding;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(view -> Toast.makeText(this, "Test", Toast.LENGTH_LONG).show());
    }

}
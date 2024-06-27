package com.cos.mediumclone.util.global_error;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cos.mediumclone.R;
import com.cos.mediumclone.databinding.ActivityErrorBinding;

public class ErrorActivity extends AppCompatActivity {
    private static final String TAG = "MyExceptionHandler";

    private ActivityErrorBinding binding;

    public static final String EXTRA_ERROR_TEXT = "EXTRA_ERROR_TEXT";
    public static final String EXTRA_INTENT = "EXTRA_INTENT";

    private Intent lastActivityIntent;
    private String errorText;

    @Override
    public Intent getIntent() {
        return super.getIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityErrorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initListener();
        initSetting();
    }
    private void initListener() {
        binding.reloadBtn.setOnClickListener(v->{
            startActivity(lastActivityIntent);
            finish();
        });
    }

    protected void initSetting() {
        lastActivityIntent = getIntent().getParcelableExtra(EXTRA_INTENT);
        errorText = getIntent().getStringExtra(EXTRA_ERROR_TEXT);

        binding.errorTv.setText(errorText);
        Log.d(TAG, "initSetting: " + lastActivityIntent);
        Log.d(TAG, "errorText: " + errorText);
    }
}
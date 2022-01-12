package com.cos.mediumclone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.airbnb.lottie.L;
import com.cos.mediumclone.databinding.ActivityTestBinding;
import com.cos.mediumclone.util.InitSettings;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TestActivity extends AppCompatActivity implements InitSettings {

    private static final String TAG = "TestActivity";

    private ActivityTestBinding binding;
    private Long selectedStartDate;
    private Long selectedEndDate;

    LocalDateTime time = LocalDateTime.now();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();
        initLr();


    }

    @Override
    public void init() {

    }

    @Override
    public void initLr() {
        // datePicker dialog
        binding.btnStartDate.setOnClickListener(v->{
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            CalendarConstraints.Builder caBuilder = new CalendarConstraints.Builder();
            // 오늘 이후 선택 불가
            caBuilder.setStart(selectedStartDate);
            caBuilder.setValidator(DateValidatorPointBackward.now());
            caBuilder.setOpenAt(selectedStartDate);

            Log.d(TAG, "달력 selectedStartDate : " + selectedStartDate);
            builder.setCalendarConstraints(caBuilder.build());
            builder.setTitleText("날짜를 선택해주세요");
            MaterialDatePicker<Long> picker = builder.build();


            picker.show(getSupportFragmentManager(), picker.toString());
            picker.addOnPositiveButtonClickListener(selection -> {
                selectedStartDate = selection;
                binding.btnStartDate.setText(setFormatDate(selection));

                if (selectedStartDate > selectedEndDate){
                    binding.btnEndDate.setText(setFormatDate(selectedStartDate));
                }
            });


        });

        binding.btnEndDate.setOnClickListener(v->{
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("날짜를 선택해주세요");

            CalendarConstraints.Builder caBuilder = new CalendarConstraints.Builder();
            // 오늘 이후 선택 불가
//            caBuilder.setEnd(Calendar.getInstance().getTimeInMillis());
//            caBuilder.setStart(selectedStartDate);
            caBuilder.setValidator(DateValidatorPointBackward.now());
            builder.setCalendarConstraints(caBuilder.build());
            builder.setTheme(R.style.CustomDatePicker);
            MaterialDatePicker<Long> picker = builder.build();

            picker.addOnPositiveButtonClickListener(selection -> {
                selectedEndDate = selection;
                binding.btnEndDate.setText(setFormatDate(selection));

                if (selectedStartDate > selectedEndDate){
                    binding.btnStartDate.setText(setFormatDate(selectedEndDate));
                }

            });
            picker.show(getSupportFragmentManager(), picker.toString());
        });

        binding.btnAddOneMonth.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked){

                time = time.minusMonths(1);
                ZonedDateTime zonedDateTime = ZonedDateTime.of(time, ZoneId.systemDefault());
                selectedStartDate = zonedDateTime.toInstant().toEpochMilli();
                Log.d(TAG, "변경된 selectedStartDate : " + selectedStartDate);
                binding.btnStartDate.setText(setFormatDate(selectedStartDate));
            } else {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void initData() {
        selectedStartDate = Calendar.getInstance().getTimeInMillis();
        Log.d(TAG, "처음 selectedStartDate : " + selectedStartDate);
        selectedEndDate = Calendar.getInstance().getTimeInMillis();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA);
        String date = formatter.format(time);
        binding.btnStartDate.setText(date);
        binding.btnEndDate.setText(date);

    }

    private String setFormatDate(Long selectedDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date(selectedDate));
    }
}
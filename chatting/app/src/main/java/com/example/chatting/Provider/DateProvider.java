package com.example.chatting.Provider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateProvider {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    public static DateFormat fullTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //Convert yyyy-MM-dd to dd/MM/yyyy
    public static String convertDateSqliteToPerson(String date){
        String[] units = date.split("-");
        return units[2] + "/" + units[1] + "/" + units[0];
    }
    //Convert dd/MM/yyyy to yyyy-MM-dd
    public static String convertDatePersonToSqlite(String date){
        String[] units = date.split("/");
        return units[2] + "-" + units[1] + "-" + units[0];
    }

    public static String standardization(int value, int length){
        String str = String.valueOf(value);
        int lenStr = str.length();
        for (int i = 0; i < length - lenStr; i++){
            str = "0" + str;
        }
        return str;
    }

    public static String getDateTimeNow(){
        Calendar cal = Calendar.getInstance();
        return DateProvider.datetimeFormat.format(cal.getTime());
    }

    public static long getTime(){
        Calendar cal = Calendar.getInstance();
        return cal.getTime().getTime();
    }

    //Convert yyyy-MM-dd HH:mm to HH:mm dd/MM/yyyy
    public static String convertDateTimeSqliteToPerson(String fullTime){
        String[] times = fullTime.split(" ");
        return times[1] + " " + convertDateSqliteToPerson(times[0]);
    }

    //Convert HH:mm dd/MM/yyyy to yyyy-MM-dd HH:mm
    public static String convertDateTimePersonToSqlite(String date){
        String[] times = date.split(" ");
        return convertDatePersonToSqlite(times[1]) + " " + times[0];
    }

    public static void selectDate(EditText txtDate){
        String[] units = txtDate.getText().toString().split("/");
        int mYear = Integer.parseInt(units[2]);
        int mMonth = Integer.parseInt(units[1]);
        int mDay = Integer.parseInt(units[0]);
        DatePickerDialog datePickerDialog = new DatePickerDialog(txtDate.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        txtDate.setText(DateProvider.standardization(dayOfMonth, 2) + "/" + DateProvider.standardization(monthOfYear + 1, 2) + "/" + year);
                    }
                }, mYear, mMonth - 1, mDay);
        datePickerDialog.show();
    }

    public static void selectTime(EditText txtTime){
        String[] times = txtTime.getText().toString().split(":");
        int mHour = Integer.parseInt(times[0]);
        int mMinute = Integer.parseInt(times[1]);
        TimePickerDialog timePickerDialog = new TimePickerDialog(txtTime.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        txtTime.setText(DateProvider.standardization(hourOfDay, 2) + ":" + DateProvider.standardization(minute, 2));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public static void selectDateTime(EditText txtDatetime){
        String[] units = txtDatetime.getText().toString().split("/");
        int mYear = Integer.parseInt(units[2]);
        int mMonth = Integer.parseInt(units[1]);
        int mDay = Integer.parseInt(units[0]);
        DatePickerDialog datePickerDialog = new DatePickerDialog(txtDatetime.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String date = DateProvider.standardization(dayOfMonth, 2) + "/" + DateProvider.standardization(monthOfYear + 1, 2) + "/" + year;
                        selectTime(txtDatetime, date);
                    }
                }, mYear, mMonth - 1, mDay);
        datePickerDialog.show();
    }

    private static void selectTime(EditText txtDatetime, String date){
        String[] times = txtDatetime.getText().toString().split(" ")[0].split(":");
        int mHour = Integer.parseInt(times[0]);
        int mMinute = Integer.parseInt(times[1]);
        TimePickerDialog timePickerDialog = new TimePickerDialog(txtDatetime.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String time = DateProvider.standardization(hourOfDay, 2) + ":" + DateProvider.standardization(minute, 2);
                        txtDatetime.setText(time + " " + date);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}

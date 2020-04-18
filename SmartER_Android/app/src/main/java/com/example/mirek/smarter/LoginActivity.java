package com.example.mirek.smarter;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Calendar;


/**
 * Created by Mirek on 26/04/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText inputUsername;
    private EditText inputPassword;
    private CheckBox checkBox;
    private Button login;
    private Button signUp;
    private TextView message;
    private String checkUsername;
    private String username;
    private String password;
    private String passwordHash;
    protected DBManager dbManager;
    protected TextView textView;
    String today;
    String temp;
    int time;
    int usageid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbManager = new DBManager(this);
        message = (TextView) findViewById(R.id.tv_wronginfo);
        checkUsername = "";
        inputUsername = (EditText) findViewById(R.id.et_username);
        inputPassword = (EditText) findViewById(R.id.et_password);
        checkBox = (CheckBox) findViewById(R.id.ch_show);
        login = (Button) findViewById(R.id.b_login);
        signUp = (Button) findViewById(R.id.b_rego);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = inputUsername.getText().toString();
                password = inputPassword.getText().toString();
                passwordHash = MD5(password);
                if (username.length() == 0)
                    message.setText("Username cannot be empty!");
                else {
                    if (password.length() == 0)
                        message.setText("Password cannot be empty!");
                    else {
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... params) {
                                return RestClient.findByUsername(username);
                            }
                            @Override
                            protected void onPostExecute(String response) {
                                checkUsername = response;
                                if (!checkUsername.contains(username))
                                    message.setText("Username doesn't exist, please try again!");
                                else {
                                    if (checkUsername.contains(passwordHash)) {
                                        message.setText("Logged in successful!");
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("username", username);
                                        startActivity(intent);
                                    } else
                                        message.setText("Username and password don't match, please try again!");
                                }
                            }
                        }.execute();
                    }
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent to_rego = new Intent(LoginActivity.this, RegoActivity.class);
                startActivity(to_rego);
            }
        });

    }

    private String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    public void insertData(){
        try {
            dbManager.open();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        today = getDate();
        time = getTime();
        Weather.placeIdTask asyncTask = new Weather.placeIdTask(new Weather.AsyncResponse() {
            public void processFinish(String weather_city, String weather_temperature) {
                temp = weather_temperature.substring(0,weather_temperature.length()-1);
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        return RestClient.findAllUsage();
                    }
                    @Override
                    protected void onPostExecute(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int number = jsonArray.length();
                            usageid = number + 1;
                            new AsyncTask<Void, Void, String>() {
                                @Override
                                protected String doInBackground(Void... params) {
                                    return RestClient.findAllResident();
                                }
                                @Override
                                protected void onPostExecute(String response) {
                                    try {
                                        JSONArray jsonArray1 = new JSONArray(response);
                                        for (int i = 0; i < jsonArray1.length(); i++)
                                        {
                                            JSONObject jsonObject = jsonArray1.getJSONObject(i);
                                            String resid = jsonObject.getString("resid");
                                            Generator generator = new Generator();
                                            Double[] allUsage = generator.allUsageGenerator(time,Double.parseDouble(temp));
                                            dbManager.insertData(String.valueOf(usageid), resid, today, String.valueOf(time), String.valueOf(allUsage[0]), String.valueOf(allUsage[1]), String.valueOf(allUsage[2]), temp);
                                            usageid ++;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.execute();
            }
        });
        asyncTask.execute("-37.8136276", "144.9630576");


        dbManager.close();
    }

    public String readData(){
        Cursor c = dbManager.getAllData();
        String s="";
        if (c.moveToFirst()) {
            do {
                s+="usageid: " + c.getString(0) + "\t" + "resid: " + c.getString(1) + "\t" + "Date: " + c.getString(2) + "\t" + "Time: " + c.getString(3) + "\t" + "fridge: " + c.getString(4) + "\t"
                        + "AC: " + c.getString(5) + "\t" + "WM: " + c.getString(6) + "\t" + "Temperature: " + c.getString(7) + "\n";
            } while (c.moveToNext());
        }
        return s;
    }
    public int getTime(){
        long time = System.currentTimeMillis();
        final Calendar mCalender = Calendar.getInstance();
        mCalender.setTimeInMillis(time);
        return mCalender.get(Calendar.HOUR_OF_DAY);
    }

    public String getDate(){
        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd", sysTime);
        return sysTimeStr.toString();
    }
}

package com.example.mirek.smarter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.collections4.BagUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.mirek.smarter.RestClient.createCredential;
import static com.example.mirek.smarter.RestClient.createResident;
import static com.example.mirek.smarter.RestClient.findByEmail;
import static com.example.mirek.smarter.RestClient.findByUsername;

/**
 * Created by Mirek on 26/04/2018.
 */

public class RegoActivity extends AppCompatActivity {
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputDoB;
    private EditText inputAddress;
    private EditText inputPostCode;
    private EditText inputMobile;
    private EditText inputEmail;
    private Spinner inputNumber;
    private Spinner inputProvider;
    private EditText inputUsername;
    private EditText inputPassword1;
    private EditText inputPassword2;
    private String firstName;
    private String lastName;
    private String DoBString;
    private Date DoB;
    private String address;
    private String postCode;
    private String mobile;
    private String email;
    private String number;
    private String provider;
    private String username;
    private String password1;
    private String password2;

    private Calendar cal;
    private int year,month,day;
    private String checkEmail;
    private String checkUsername;
    private int resid;

    TextView me1;
    TextView me2;
    TextView me3;
    TextView me4;
    TextView me5;
    TextView me6;
    TextView me7;
    TextView me10;
    TextView me11;
    TextView me12;

    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rego);
        inputPassword1 = (EditText) findViewById(R.id.re_pass);
        inputPassword2 = (EditText) findViewById(R.id.re_pass2);
        inputDoB = (EditText) findViewById(R.id.re_dob);
        me1 = (TextView) findViewById(R.id.me_1);
        me2 = (TextView) findViewById(R.id.me_2);
        me3 = (TextView) findViewById(R.id.me_3);
        me4 = (TextView) findViewById(R.id.me_4);
        me5 = (TextView) findViewById(R.id.me_5);
        me6 = (TextView) findViewById(R.id.me_6);
        me7 = (TextView) findViewById(R.id.me_7);
        me10 = (TextView) findViewById(R.id.me_10);
        me11 = (TextView) findViewById(R.id.me_11);
        me12 = (TextView) findViewById(R.id.me_12);

        sdf = new SimpleDateFormat("yyyy-MM-dd");

        checkEmail = "";
        checkUsername = "";
        inputDoB.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        inputDoB.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });

        inputFirstName = (EditText) findViewById(R.id.re_fname);
        inputLastName = (EditText) findViewById(R.id.re_lname);
        inputAddress = (EditText) findViewById(R.id.re_add);
        inputPostCode = (EditText) findViewById(R.id.re_post);
        inputMobile = (EditText) findViewById(R.id.re_mobile);
        inputEmail = (EditText) findViewById(R.id.re_email);
        inputNumber = (Spinner) findViewById(R.id.re_no);
        inputProvider = (Spinner) findViewById(R.id.re_prov);
        inputUsername = (EditText) findViewById(R.id.re_user);
        //========================================================
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    Resident resident = new Resident(Integer.valueOf(params[0]).intValue(), params[1], params[2], sdf.parse(params[3]), params[4], params[5], params[6], params[7], new Short(params[8]), params[9]);
                    RestClient.createResident(resident);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return "Account created!";
            }
            @Override
            protected void onPostExecute(String response) {
                me12.setText(response);
            }
        }.execute("11", "Frank", "Libery", "1996-02-12T00:00:00+10:00", "6 Lindsay St", "3168", "w@a.cc", "1234567890", "3", "AGI");
        //==============================================================

        Button submit = (Button) findViewById(R.id.b_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = inputFirstName.getText().toString();
                lastName = inputLastName.getText().toString();
                DoBString = inputDoB.getText().toString();
                try {
                    DoB = new java.sql.Date(sdf.parse(DoBString).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                address = inputAddress.getText().toString();
                postCode = inputPostCode.getText().toString();
                mobile = inputMobile.getText().toString();
                email = inputEmail.getText().toString();
                number = inputNumber.getSelectedItem().toString();
                provider = inputProvider.getSelectedItem().toString();
                username = inputUsername.getText().toString();
                password1 = inputPassword1.getText().toString();
                password2 = inputPassword2.getText().toString();
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        return RestClient.findAll();
                    }
                    @Override
                    protected void onPostExecute(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            resid = jsonArray.length() + 1;
                            if (firstName.length() == 0 || firstName.length() > 30) {
                                me1.setText("The length of first name should be between 1-30.");
                                me12.setText("The length of first name should be between 1-30.");
                            }
                            else {
                                me1.setText("");
                                me12.setText("");
                                if (lastName.length() == 0 || lastName.length() > 30) {
                                    me2.setText("The length of last name should be between 1-30.");
                                    me12.setText("The length of last name should be between 1-30.");
                                }
                                else {
                                    me2.setText("");
                                    me12.setText("");
                                    String beginTime = new String("1900-01-01");
                                    String endTime = new String("2008-01-01");
                                    Date bt = null;
                                    try {
                                        bt = new java.sql.Date(sdf.parse(beginTime).getTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date et = null;;
                                    try {
                                        et = new java.sql.Date(sdf.parse(endTime).getTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (DoB == null || DoB.before(bt) || DoB.after(et)) {
                                        me3.setText("Please enter the correct DoB.");
                                        me12.setText("Please enter the correct DoB.");
                                    }
                                    else {
                                        me3.setText("");
                                        me12.setText("");
                                        if (address.length() < 5 || address.length() > 60) {
                                            me4.setText("The format of address is 1 XX Rd, Suburb, and the length should be between 5-60.");
                                            me12.setText("Address is wrong");
                                        }
                                        else {
                                            me4.setText("");
                                            me12.setText("");
                                            if (postCode.length() != 4 || !isStringNum(postCode)) {
                                                me5.setText("Postcode should be 4-digit.");
                                                me12.setText("Postcode should be 4-digit.");
                                            }
                                            else {
                                                me5.setText("");
                                                me12.setText("");
                                                if (mobile.length() != 10 || !isStringNum(mobile)) {
                                                    me6.setText("Mobile number should be 10-digit.");
                                                    me12.setText("Mobile number should be 10-digit.");
                                                }
                                                else {
                                                    me6.setText("");
                                                    me12.setText("");
                                                    if (email.length() < 5 || email.length() >50) {
                                                        me7.setText("The length of email should be between 5-50.");
                                                        me12.setText("The length of email should be between 5-50.");
                                                    }
                                                    else {
                                                        me7.setText("");
                                                        me12.setText("");
                                                        new AsyncTask<Void, Void, String>() {
                                                            @Override
                                                            protected String doInBackground(Void... params) {
                                                                return findByEmail(email);
                                                            }
                                                            @Override
                                                            protected void onPostExecute(String response) {
                                                                checkEmail = response;
                                                                if (checkEmail.contains(email)) {
                                                                    me7.setText("The Email address already exists. If you have registered, please log in.");
                                                                    me12.setText("The Email address already exists. If you have registered, please log in.");
                                                                }
                                                                else {
                                                                    me7.setText("");
                                                                    me12.setText("");
                                                                    if (username.length() == 0 ||username.length() > 20)
                                                                        me10.setText("The length of username should be between 1-20.");
                                                                    else {
                                                                        me10.setText("");
                                                                        new AsyncTask<Void, Void, String>() {
                                                                            @Override
                                                                            protected String doInBackground(Void... params) {
                                                                                return findByUsername(username);
                                                                            }
                                                                            @Override
                                                                            protected void onPostExecute(String response) {
                                                                                checkUsername = response;
                                                                                if (checkUsername.contains(username))
                                                                                    me10.setText("The username already exists. If you have registered, please log in.");
                                                                                else {
                                                                                    me10.setText("");
                                                                                    if (password1.length() == 0)
                                                                                        me11.setText("Password cannot be empty.");
                                                                                    else {
                                                                                        me11.setText("");
                                                                                        if (password2.length() == 0)
                                                                                            me12.setText("Password cannot be empty.");
                                                                                        else {
                                                                                            if (!password1.equals(password2))
                                                                                                me12.setText("Passwords should be same.");
                                                                                            else {
                                                                                                me12.setText("");
                                                                                                new AsyncTask<String, Void, String>() {
                                                                                                    @Override
                                                                                                    protected String doInBackground(String... params) {
                                                                                                        try {
                                                                                                            Resident resident = new Resident(Integer.valueOf(params[0]).intValue(), params[1], params[2], new java.sql.Date(sdf.parse(params[3]).getTime()), params[4], params[5], params[6], params[7], new Short(params[8]), params[9]);
                                                                                                            RestClient.createResident(resident);
                                                                                                        } catch (ParseException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }
                                                                                                        return "Account created!";
                                                                                                    }
                                                                                                    @Override
                                                                                                    protected void onPostExecute(String response) {
                                                                                                        me12.setText(response);
                                                                                                    }
                                                                                                }.execute(String.valueOf(resid), firstName, lastName, DoBString, address, postCode, email, mobile, number, provider);
                                                                                                String passwordHash = MD5(password1);
                                                                                                Date nowDate = new Date(System.currentTimeMillis());
                                                                                                String nowDateString = sdf.format(nowDate);
                                                                                                new AsyncTask<String, Void, String>() {
                                                                                                    @Override
                                                                                                    protected String doInBackground(String... params) {
                                                                                                        Credential credential = null;
                                                                                                        try {
                                                                                                            credential = new Credential(params[0], Integer.valueOf(params[1]).intValue(), params[2], new java.sql.Date(sdf.parse(params[3]).getTime()));
                                                                                                        } catch (ParseException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }
                                                                                                        RestClient.createCredential(credential);
                                                                                                        return "Account created!";
                                                                                                    }

                                                                                                    @Override
                                                                                                    protected void onPostExecute(String response) {
                                                                                                    }
                                                                                                }.execute(username, String.valueOf(resid), passwordHash, nowDateString);
                                                                                                Intent to_rego = new Intent(RegoActivity.this, MainActivity.class);
                                                                                                to_rego.putExtra("username", username);
                                                                                                startActivity(to_rego);

                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }.execute();
                                                                    }
                                                                }

                                                            }
                                                        }.execute();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.execute();

            }
        });

    }
    private boolean isStringNum(String input)
    {
        String number = "^[0-9]+$";
        boolean check = input.matches(number);
        return check;
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(RegoActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear += 1;
                if (monthOfYear >= 10) {
                    if (dayOfMonth >= 10)
                        RegoActivity.this.inputDoB.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    else
                        RegoActivity.this.inputDoB.setText(year + "-" + monthOfYear + "-0" + dayOfMonth);
                }
                else {
                    if (dayOfMonth >= 10)
                        RegoActivity.this.inputDoB.setText(year + "-0" + monthOfYear + "-" + dayOfMonth);
                    else
                        RegoActivity.this.inputDoB.setText(year + "-0" + monthOfYear + "-0" + dayOfMonth);
                }

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH ), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
}
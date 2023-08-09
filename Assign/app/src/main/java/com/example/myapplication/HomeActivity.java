package com.example.myapplication;

import static android.text.TextUtils.isEmpty;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.widget.SeekBar;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdaper;
    float total;
    int ppl;
    SeekBar peopleSB;
    TextView peopleTV;
    EditText peopleET;
    EditText amountET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mySQLiteAdaper = new SQLiteAdapter(this);
        final String equalCat = "Equal Break Down";
        final String customCat = "Custom Break Down";
        final String combinationCat = "Combination Break Down";

        /*mySQLiteAdaper.openToWrite();
        mySQLiteAdaper.deleteAll();
        mySQLiteAdaper.close();*/

        amountET = (EditText) findViewById(R.id.amountET);
        peopleSB = findViewById(R.id.peopleSB);
        peopleTV = (TextView)findViewById(R.id.peopleTV);
        peopleET = (EditText)findViewById(R.id.peopleET);


        Button equalBtn = (Button)findViewById(R.id.equalBtn);
        Button customBtn = (Button)findViewById(R.id.btn);
        Button historyBtn = (Button)findViewById(R.id.historyBtn);

        // Set an initial value
        peopleET.setText("1");

        peopleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isEmpty(s)){return;}
                int num = Integer.parseInt(s.toString());
                peopleSB.setProgress(num);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set a listener for SeekBar changes
        peopleSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the displayed number of people
                peopleET.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }
        });

        equalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder str = new StringBuilder();

                if(isEmpty(amountET.getText()) || isEmpty(peopleET.getText())){
                    Toast.makeText(HomeActivity.this,"Error: Please fill in all information",Toast.LENGTH_SHORT).show();
                    return;
                }

                total = Float.parseFloat(amountET.getText().toString());
                ppl = Integer.parseInt(peopleET.getText().toString());
                float eachBill = total/ppl;

                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String time = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());

                mySQLiteAdaper.openToWrite();
                mySQLiteAdaper.insert(date,time,"Equal",ppl+" people",String.format("%.2f", eachBill));
                mySQLiteAdaper.close();

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Result");
                str.append("The amount need to be paid by each person: RM"+String.format("%.2f", eachBill));
                builder.setMessage(str);
                builder.setCancelable(false);

                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                        Toast.makeText(HomeActivity.this,"Your data is stored successfully!",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Share to WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("text/plain");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, str.toString());
                        try {
                            startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(HomeActivity.this,"Error: Whatsapp have not been installed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        customBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isEmpty(amountET.getText()) || isEmpty(peopleET.getText())){
                    Toast.makeText(HomeActivity.this,"Error: Please fill in all information",Toast.LENGTH_SHORT).show();
                    return;
                }

                total = Float.parseFloat(amountET.getText().toString());
                ppl = Integer.parseInt(peopleET.getText().toString());

                SharedPreferences pref = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = pref.edit();
                prefEditor.putFloat("total",total);
                prefEditor.putInt("ppl",ppl);
                prefEditor.commit();

                Intent i = new Intent(HomeActivity.this, CustomActivity.class);
                startActivity(i);
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySQLiteAdaper.openToRead();
                String equalRead = mySQLiteAdaper.queueSome(equalCat);
                String customRead = mySQLiteAdaper.queueSome(customCat);
                mySQLiteAdaper.close();

                Intent i = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(i);
            }
        });

    }

}


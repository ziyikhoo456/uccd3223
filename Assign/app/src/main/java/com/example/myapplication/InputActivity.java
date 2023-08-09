package com.example.myapplication;

import static android.text.TextUtils.isEmpty;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class InputActivity extends Activity {

    private SQLiteAdapter mySQLiteAdapter;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        mySQLiteAdapter = new SQLiteAdapter(this);

        SharedPreferences pref = getSharedPreferences("MySharedPreferences", 0);
        float total = pref.getFloat("total", 0);
        int ppl = pref.getInt("ppl", 1);
        count = 0;
        String[] name = new String[ppl];
        float[] percent = new float[ppl];

        TextView RMSymbol = (TextView) findViewById(R.id.RMSymbol);
        TextView percentSymbol = (TextView) findViewById(R.id.percentSymbol);
        TextView amountTV = (TextView) findViewById(R.id.amountTV);
        TextView pplTV = (TextView) findViewById(R.id.pplTV);
        TextView numberTV = (TextView) findViewById(R.id.numberTV);
        EditText nameET = (EditText) findViewById(R.id.NameET);
        EditText percentET = (EditText) findViewById(R.id.percentET);
        Button minusBtn = (Button) findViewById(R.id.minusBtn);
        Button plusBtn = (Button) findViewById(R.id.plusBtn);
        Button submitBtn = (Button) findViewById(R.id.submitBtn);

        String type = this.getIntent().getStringExtra("TYPE");
        if (type.contentEquals("percentage")) {
            RMSymbol.setText("");
        } else if (type.contentEquals("custom")) {
            percentSymbol.setText("");
        } else {
            percentSymbol.setText("");
            RMSymbol.setText("");
        }


        amountTV.setText("RM " + String.format("%.2f", total));
        pplTV.setText(ppl + " People");

        minusBtn.setEnabled(false);
        numberTV.setText("Person " + (count + 1));

        if ((count + 1) == ppl) {
            plusBtn.setEnabled(false);
        }

        //Initialize all name to blank and all percentage to 0
        for (int i = 0; i < ppl; i++) {
            name[i] = "";
            percent[i] = 0;
        }


        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(nameET.getText())) {
                    nameET.setText("Person " + (count + 1));
                }
                if (isEmpty(percentET.getText())) {
                    percentET.setText("0");
                }
                name[count] = nameET.getText().toString();
                percent[count] = Float.parseFloat(percentET.getText().toString());

                count++;
                nameET.setText(name[count]);
                percentET.setText(Float.toString(percent[count]));

                numberTV.setText("Person " + (count + 1));
                minusBtn.setEnabled(true);

                if ((count + 1) == ppl) {
                    plusBtn.setEnabled(false);
                } else {
                    plusBtn.setEnabled(true);
                }
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(nameET.getText())) {
                    nameET.setText("Person " + (count + 1));
                }
                if (isEmpty(percentET.getText())) {
                    percentET.setText("0");
                }
                name[count] = nameET.getText().toString();
                percent[count] = Float.parseFloat(percentET.getText().toString());

                count--;
                nameET.setText(name[count]);
                percentET.setText(Float.toString(percent[count]));
                numberTV.setText("Person " + (count + 1));
                plusBtn.setEnabled(true);

                if ((count + 1) == 1) {
                    minusBtn.setEnabled(false);
                } else {
                    minusBtn.setEnabled(true);
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float sum = 0;
                float eachBill;

                AlertDialog.Builder builder = new AlertDialog.Builder(InputActivity.this);
                builder.setTitle("Result");
                StringBuilder str = new StringBuilder();

                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String time = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());

                if (isEmpty(nameET.getText())) {
                    nameET.setText("Person " + (count + 1));
                }
                name[count] = nameET.getText().toString();
                percent[count] = Float.parseFloat(percentET.getText().toString());

                if (type.contentEquals("percentage")) {
                    sum = 0;
                    for (int i = 0; i < ppl; i++) {
                        sum += percent[i];
                    }

                    if (sum != 100) {
                        Toast.makeText(InputActivity.this, "Error: The sum must be 100%", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (int i = 0; i < ppl; i++) {
                        eachBill = total * percent[i] / 100;
                        str.append(name[i] + " : RM" + String.format("%.2f", eachBill) + "\n");

                        //write into database
                        mySQLiteAdapter.openToWrite();
                        mySQLiteAdapter.insert(date, time, "Custom", name[i], String.format("%.2f", eachBill));
                        mySQLiteAdapter.close();
                    }

                    builder.setMessage(str.toString());
                }

                if (type.contentEquals("ratio")) {
                    sum = 0;
                    for (int i = 0; i < ppl; i++) {
                        sum += percent[i];
                    }

                    if (sum == 0) {
                        Toast.makeText(InputActivity.this, "Error: The sum must not be 0", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (int i = 0; i < ppl; i++) {
                        eachBill = total / sum * percent[i];
                        str.append(name[i] + " : RM" + String.format("%.2f", eachBill) + "\n");

                        //write into database
                        mySQLiteAdapter.openToWrite();
                        mySQLiteAdapter.insert(date, time, "Custom", name[i], String.format("%.2f", eachBill));
                        mySQLiteAdapter.close();
                    }

                    builder.setMessage(str.toString());
                }

                if (type.contentEquals("custom")) {
                    sum = 0;
                    for (int i = 0; i < ppl; i++) {
                        sum += percent[i];
                    }

                    if (sum != total) {
                        Toast.makeText(InputActivity.this, "Error: The sum must be RM" + String.format("%.2f", total), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (int i = 0; i < ppl; i++) {
                        eachBill = percent[i];
                        str.append(name[i] + " : RM" + String.format("%.2f", eachBill) + "\n");

                        //write into database
                        mySQLiteAdapter.openToWrite();
                        mySQLiteAdapter.insert(date, time, "Custom", name[i], String.format("%.2f", eachBill));
                        mySQLiteAdapter.close();
                    }

                    builder.setMessage(str.toString());
                }

                builder.setCancelable(false);

                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                        Toast.makeText(InputActivity.this, "Your data is stored successfully!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(InputActivity.this, "Error: Whatsapp have not been installed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }

        });
    }
}


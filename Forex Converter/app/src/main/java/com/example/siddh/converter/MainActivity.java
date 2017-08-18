package com.example.siddh.converter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView textView;
    EditText convertFrom;
    Spinner spinnerConvertFrom;
    Spinner spinnerConvertTo;

    DownloadTask task;
    DownloadTask defaultTask;

    String from = "INR";
    String to = "USD";

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {

                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }

                return result;

            } catch (Exception e) {

                e.printStackTrace();
                return "Failed";

            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject rates = jsonObject.getJSONObject("rates");
                String exchangeRate = rates.getString(to);

                Log.i("Rates", String.valueOf(rates));

                Log.i("Exchange Rate", exchangeRate);

                conversion(exchangeRate);

            } catch (Exception e) {
                e.printStackTrace();
                Log.i(" Error ", "Could not fetch data");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        convertFrom = (EditText) findViewById(R.id.convertFrom);
        spinnerConvertFrom  = (Spinner) findViewById(R.id.spinner1);
        spinnerConvertTo = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.array));
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConvertFrom.setAdapter(spinnerAdapter1);

        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.array));
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConvertTo.setAdapter(spinnerAdapter2);

        int spinnerPosition = spinnerAdapter1.getPosition(from);
        spinnerConvertFrom.setSelection(spinnerPosition);

        spinnerConvertFrom.setOnItemSelectedListener(MainActivity.this);
        spinnerConvertTo.setOnItemSelectedListener(MainActivity.this);

        task = new DownloadTask();
        defaultTask = new DownloadTask();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinnerConvertFrom = (Spinner) adapterView;
        Spinner spinnerConvertTo = (Spinner) adapterView;

        if (spinnerConvertFrom.getId() == R.id.spinner1) {

            from = spinnerConvertFrom.getSelectedItem().toString();
            try {

                DownloadTask task = new DownloadTask();
                task.execute("http://api.fixer.io/latest?base=" + from);

            } catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(this, "Could not connect", Toast.LENGTH_LONG);

            }

        }
        if (spinnerConvertTo.getId() == R.id.spinner2) {

            to = spinnerConvertTo.getSelectedItem().toString();
            try {

                DownloadTask task = new DownloadTask();
                task.execute("http://api.fixer.io/latest?base=" + from);

            } catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(this, "Could not connect", Toast.LENGTH_LONG);
            }
            }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }

    public void conversion(final String exchangeRate) {

        convertFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                double init = Double.parseDouble(convertFrom.getText().toString());
                double rate = Double.parseDouble(exchangeRate);
                double ans = init * rate;
                textView.setText(String.valueOf(ans));

            }
        });


    }

}

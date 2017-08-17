package com.example.siddh.converter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                String query = jsonObject.getString("query");
                JSONObject queryObject = new JSONObject(query);
                String results = queryObject.getString("results");
                JSONObject rateObject = new JSONObject(results);
                String rate = rateObject.getString("rate");

                JSONObject exchangeObject = new JSONObject(rate);
                String exchangeRate = exchangeObject.getString("Rate");

                double currencyRate = Double.parseDouble(exchangeRate);
                double value = Double.parseDouble(convertFrom.getText().toString());
                double ans = value * currencyRate;
                textView.setText(String.format("%2f", ans));


            } catch (JSONException e) {
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

        DownloadTask task = new DownloadTask();

        try {

            task.execute("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20%28%22" + from + to + "%22%29&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(this, "Could not connect", Toast.LENGTH_LONG);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinnerConvertFrom = (Spinner) adapterView;
        Spinner spinnerConvertTo = (Spinner) adapterView;

        if (spinnerConvertFrom.getId() == R.id.spinner1) {

            from = spinnerConvertFrom.getSelectedItem().toString();

        }
        if (spinnerConvertTo.getId() == R.id.spinner2) {

            to = spinnerConvertTo.getSelectedItem().toString();

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        from = "INR";
        to = "USD";

    }

}

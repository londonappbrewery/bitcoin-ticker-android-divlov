package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    private final String CMC_PRO_API_KEY = "195a9443-1d08-494a-baa1-17c4a9254cd7";
    private final String TAG = "bitcoindebug";
    // TODO: Create the base URL
    private final String BASE_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest";

    // Member Variables:
    TextView mPriceTextView;
    String currency;
    String[] currencyArray;
    Double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = findViewById(R.id.priceLabel);
        Spinner spinner = findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currency=(String)adapterView.getItemAtPosition(i);
                RequestParams params = new RequestParams();
                params.put("convert", currency);
                params.put("CMC_PRO_API_KEY", CMC_PRO_API_KEY);
                params.put("symbol", "BTC");
                letsDoSomeNetworking(params);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(TAG,"Nothing selected");
            }
        });
    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(RequestParams params) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(BASE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "Success! JSONObject: " + response.toString());
                try {
                    price=response.getJSONObject("data").getJSONObject("BTC").getJSONObject("quote").getJSONObject(currency).getDouble("price");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String final_price=String.format("%.2f",price);
                mPriceTextView.setText(final_price);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "Failed!" + throwable.getMessage());
            }
        });


    }

}

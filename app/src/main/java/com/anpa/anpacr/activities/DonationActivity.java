package com.anpa.anpacr.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.common.PaypalConfig;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.math.BigDecimal;

public class DonationActivity extends AnpaAppFraqmentActivity {

    private static final String TAG = "paymentExample";

    public static final int PAYPAL_REQUEST_CODE = 123;
    private EditText editTextAmount;
    private TextView etExchangeRate;
    //Payment Amount
    private String paymentAmount;
    private float bccrExchange;
    private Button btnConvert;

    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID)
            .acceptCreditCards(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        editTextAmount = (EditText) findViewById(R.id.et_donation);
        etExchangeRate = (TextView)findViewById(R.id.tv_exchange_rate);
        btnConvert = (Button)findViewById(R.id.btn_send_calculate);

        // Btn de back (anterior)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_DONATION);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        new AsyncExchangeRate().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {
            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    public void getPayment(View view) {
        //Getting the amount from editText
        paymentAmount = editTextAmount.getText().toString();
        if(paymentAmount.equals("")){
            Toast.makeText(this, "Debes ingresar un monto primero", Toast.LENGTH_SHORT).show();
            return;
        }

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", Constants.PAYPAL_PURCHASE_ITEM,
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    private class AsyncExchangeRate extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpClient Client = new DefaultHttpClient();
            String URL = PaypalConfig.BCCR_WS_EXCHANGE_RATE;

            try {
                String setServerString = "";
                HttpGet httpget = new HttpGet(URL);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                setServerString = Client.execute(httpget, responseHandler);

                String amount = setServerString.substring(setServerString.indexOf("NUM_VALOR&gt;"), setServerString.indexOf("&lt;/NUM_VALOR&gt"));
                amount = amount.replace("NUM_VALOR&gt;", "");
                bccrExchange = Float.parseFloat(amount);
                return "Venta: ¢" + bccrExchange;
            } catch (Exception ex) {
                Log.e("BCCR", ex.getMessage());
                return Constants.WS_BCCR_ERROR;
            }
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            etExchangeRate.setText(result);
            if(!result.equals(Constants.WS_BCCR_ERROR))
                btnConvert.setVisibility(View.VISIBLE);
        }
    }


    public void showConvertDialog(View view){
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(DonationActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        final View layout = View.inflate(this, R.layout.dialog_calculate_currency, null);
        builder.setView(layout)
                // Add action buttons
                .setPositiveButton(R.string.bt_send_calculate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText etColones = (EditText)layout.findViewById(R.id.et_donation_colones);
                        if(!etColones.getText().toString().equals("")) {
                            float colonesValue = Float.parseFloat(etColones.getText().toString());
                            float dollarValue = colonesValue / bccrExchange;
                            dollarValue = roundValue(dollarValue, 2);
                            editTextAmount.setText(Float.toString(dollarValue));
                            Toast.makeText(getApplicationContext(), "¢" + colonesValue + " = $" + dollarValue, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.txt_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    //Redondear el valor
    public static float roundValue(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return ( (float) ( (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) ) ) / pow;
    }
}

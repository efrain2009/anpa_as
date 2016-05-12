package com.anpa.anpacr.activities;

import java.math.BigDecimal;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.common.Constants;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class DonationActivity extends AnpaAppFraqmentActivity {

	private static final String TAG = "paymentExample";
	/**
	 * - Set to PaymentActivity.ENVIRONMENT_PRODUCTION to move real money.
	 * 
	 * - Set to PaymentActivity.ENVIRONMENT_SANDBOX to use your test credentials
	 * from https://developer.paypal.com
	 * 
	 * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
	 * without communicating to PayPal's servers.
	 */
	// private static final String CONFIG_ENVIRONMENT =
	// PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

	// note that these credentials will differ between live & sandbox
	// environments.
	private static final String CONFIG_CLIENT_ID = "AfOe9hBfyRLSUMhj4QGoczcJrLl51frXndQkl5vPgxGvW7ARR6-UoaXVzdsV";

	private static final int REQUEST_CODE_PAYMENT = 1;
	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

	private static PayPalConfiguration config = new PayPalConfiguration()
			.environment(CONFIG_ENVIRONMENT)
			.clientId(CONFIG_CLIENT_ID)
			// The following are only used in PayPalFuturePaymentActivity.
			.merchantName("ANPA")
			.merchantPrivacyPolicyUri(
					Uri.parse("https://www.example.com/privacy"))
			.merchantUserAgreementUri(
					Uri.parse("https://www.example.com/legal"));

	PayPalPayment donation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donation);

		// Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_DONATION);

		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		startService(intent);
	}

	public void onBuyPressed(View pressed) {
		// PAYMENT_INTENT_SALE will cause the payment to complete immediately.
		// Change PAYMENT_INTENT_SALE to PAYMENT_INTENT_AUTHORIZE to only
		// authorize payment and
		// capture funds later.
		if (pressed.getId() == R.id.btn_send_donation) {
			EditText etDonationAmount = (EditText) findViewById(R.id.et_donation);
			String amount = etDonationAmount.getText().toString();
			if (!amount.equals(""))
				donation = new PayPalPayment(new BigDecimal(amount), "USD",
						"Donación vía ANPA App Android",
						PayPalPayment.PAYMENT_INTENT_SALE);
			else
				Toast.makeText(getApplicationContext(),
						"Debés ingresar una cantidad...", Toast.LENGTH_SHORT)
						.show();
		}

		Intent intent = new Intent(DonationActivity.this, PaymentActivity.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, donation);
		startActivityForResult(intent, REQUEST_CODE_PAYMENT);
	}

	public void onFuturePaymentPressed(View pressed) {
		Intent intent = new Intent(DonationActivity.this,
				PayPalFuturePaymentActivity.class);
		startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data
						.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {
						Log.i(TAG, confirm.toJSONObject().toString(4));
						Log.i(TAG, confirm.getPayment().toJSONObject()
								.toString(4));

						Toast.makeText(
								getApplicationContext(),
								"Transacci�n confirmada por Paypal. �Muchas Gracias!",
								Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e(TAG, "Ha ocurrido un error: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i(TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i(TAG,
						"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}
		} else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				PayPalAuthorization auth = data
						.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
				if (auth != null) {
					try {
						Log.i("FuturePaymentExample", auth.toJSONObject()
								.toString(4));

						String authorization_code = auth.getAuthorizationCode();
						Log.i("FuturePaymentExample", authorization_code);

						sendAuthorizationToServer(auth);
						Toast.makeText(getApplicationContext(),
								"Future Payment code received from PayPal",
								Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e("FuturePaymentExample",
								"an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i("FuturePaymentExample", "The user canceled.");
			} else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i("FuturePaymentExample",
						"Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
			}
		}
	}

	private void sendAuthorizationToServer(PayPalAuthorization authorization) {

	}

	public void onFuturePaymentPurchasePressed(View pressed) {
		// Get the Application Correlation ID from the SDK
		String correlationId = PayPalConfiguration
				.getApplicationCorrelationId(this);

		Log.i("FuturePaymentExample", "Application Correlation ID: "
				+ correlationId);

		// TODO: Send correlationId and transaction details to your server for
		// processing with
		// PayPal...
		Toast.makeText(getApplicationContext(),
				"App Correlation ID received from SDK", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onDestroy() {
		// Stop service when done
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}
}

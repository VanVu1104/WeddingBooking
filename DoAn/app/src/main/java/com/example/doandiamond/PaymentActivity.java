package com.example.doandiamond;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaymentActivity extends AppCompatActivity {

    public static final String EXTRA_PAYMENT =  "" ;
    public static final String EXTRA_RESULT_CONFIRMATION = "";
    private static final int PAYPAL_REQUEST_CODE = 7171;
    public static final int RESULT_EXTRAS_INVALID = 245;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Use sandbox for testing
            .clientId("AUZrpGyeUIUTdb5Cqh7q2hHqxw1U6Uzgxk1yof42GnG3lZSY_FpzC2-3Q6BiAmBSNFAh1I6dX_7lfhEr");

    Button btnPayNow;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
            Intent intent = new Intent(this, PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            startService(intent);

            btnPayNow = findViewById(R.id.btnPayNow);
            btnPayNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    processPayment();
                }
            });
        }

        private void processPayment() {
            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal("10.00"), "USD", "Test payment",
                    PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PAYPAL_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if (confirmation != null) {
                        try {
                            String paymentDetails = confirmation.toJSONObject().toString(4);
                            Log.d("PayPalActivity", paymentDetails);
                            Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show();
                } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                    Toast.makeText(this, "Invalid payment or PayPal configuration", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onDestroy() {
            stopService(new Intent(this, PayPalService.class));
            super.onDestroy();
        }
    }

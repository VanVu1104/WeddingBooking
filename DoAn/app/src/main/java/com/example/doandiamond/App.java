package com.example.doandiamond;

import com.paypal.android.sdk.payments.PayPalConfiguration;

public class App {
    // Sandbox: để kiểm tra, Production: để sử dụng thật
    public static final String PAYPAL_CLIENT_ID = "AUZrpGyeUIUTdb5Cqh7q2hHqxw1U6Uzgxk1yof42GnG3lZSY_FpzC2-3Q6BiAmBSNFAh1I6dX_7lfhEr";

    public static final int PAYPAL_REQUEST_CODE = 7171;

    public static PayPalConfiguration getConfig() {
        return new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Chế độ Sandbox để kiểm tra
                .clientId(PAYPAL_CLIENT_ID);

    }
}

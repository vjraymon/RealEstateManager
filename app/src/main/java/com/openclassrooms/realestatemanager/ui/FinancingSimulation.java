package com.openclassrooms.realestatemanager.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.R;

public class FinancingSimulation extends Dialog {

    interface FullNameListener {
        void fullNameEntered(String fullName);
    }

    public final Context context;

    private EditText priceEditText;
    private EditText contributionEditText;
    private EditText rateEditText;
    private EditText durationEditText;

    public FinancingSimulation(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.financing_simulation);

        TextView priceTextView = findViewById(R.id.financing_price_textView);
        this.priceEditText = findViewById(R.id.financing_price_editText);
        TextView contributionTextView = findViewById(R.id.financing_contribution_textView);
        this.contributionEditText = findViewById(R.id.financing_contribution_editText);
        TextView rateTextView = findViewById(R.id.financing_rate_textView);
        this.rateEditText = findViewById(R.id.financing_rate_editText);
        TextView durationTextView = findViewById(R.id.financing_duration_textView);
        this.durationEditText = findViewById(R.id.financing_duration_editText);
        Button buttonOK = findViewById(R.id.financing_button_ok);
        Button buttonCancel = findViewById(R.id.financing_button_cancel);

        buttonOK.setOnClickListener(v -> buttonOKClick());
        buttonCancel.setOnClickListener(v -> buttonCancelClick());
    }

    // User click "OK" button.
    private void buttonOKClick() {
        try {
            int price = Integer.parseInt(this.priceEditText.getText().toString());
            int contribution = Integer.parseInt(this.contributionEditText.getText().toString());
            int duration = Integer.parseInt(this.durationEditText.getText().toString());
            double rate = Double.parseDouble(this.rateEditText.getText().toString());

            int M = price - contribution;
            double t = rate / 100;
            int n = 12 * duration;
            double m = ((M * t) / 12) / (1 - Math.pow(1 + (t / 12), -n));

            TextView resultTextView = findViewById(R.id.financing_result_textView);
            resultTextView.setText(String.format(context.getString(R.string.monthly_payment), (int)Math.round(m)));
        } catch (Exception e) {
            Toast toast = Toast.makeText(context, R.string.bad_input, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // User click "Cancel" button.
    private void buttonCancelClick() {
        this.dismiss();
    }
}


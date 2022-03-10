package com.openclassrooms.realestatemanager.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;

public class CustomConfirm extends Dialog {

    interface ConfirmListener {
        void confirmOk();
    }

    public Context context;
    public String name;


    private TextView textView;
    private Button buttonOK;
    private Button buttonCancel;

    private ConfirmListener listener;

    public CustomConfirm(Context context, ConfirmListener listener, String name) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_custom_confirm);

        this.textView = (TextView) findViewById(R.id.confirmtextView);
        this.buttonOK = (Button) findViewById(R.id.confirmbutton_ok);
        this.buttonCancel = (Button) findViewById(R.id.confirmbutton_cancel);

        this.textView.setText(this.name);

        this.buttonOK.setOnClickListener(v -> buttonOKClick());
        this.buttonCancel.setOnClickListener(v -> buttonCancelClick());
    }

    // User click "OK" button.
    private void buttonOKClick() {
        this.dismiss(); // Close Dialog

        if (this.listener != null) {
            this.listener.confirmOk();
        }
    }

    // User click "Cancel" button.
    private void buttonCancelClick() {
        this.dismiss();
    }
}


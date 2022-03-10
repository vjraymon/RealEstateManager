package com.openclassrooms.realestatemanager.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;

public class CustomConfirm extends Dialog {

    interface ConfirmListener {
        void confirmOk();
    }

    public final Context context;
    public final String name;

    private final ConfirmListener listener;

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

        TextView textView = findViewById(R.id.confirmtextView);
        Button buttonOK = findViewById(R.id.confirmbutton_ok);
        Button buttonCancel = findViewById(R.id.confirmbutton_cancel);

        textView.setText(this.name);

        buttonOK.setOnClickListener(v -> buttonOKClick());
        buttonCancel.setOnClickListener(v -> buttonCancelClick());
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


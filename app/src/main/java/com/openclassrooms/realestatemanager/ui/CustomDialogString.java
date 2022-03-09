package com.openclassrooms.realestatemanager.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;

public class CustomDialogString extends Dialog {

        interface FullNameListener {
            void fullNameEntered(String fullName);
        }

        public Context context;
        private String name;
        private boolean presence;
        private String initialValue;


        private TextView textView;
        private EditText editText;
        private Button buttonOK;
        private Button buttonCancel;
        private Button buttonClear;

        private FullNameListener listener;

        public CustomDialogString(Context context, FullNameListener listener, String name, boolean presence, String value) {
            super(context);
            this.context = context;
            this.listener = listener;
            this.name = name;
            this.presence = presence;
            this.initialValue = value;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.layout_custom_dialog);

            this.textView = (TextView) findViewById(R.id.dialogtextView);
            this.editText = (EditText) findViewById(R.id.dialogeditText);
            this.buttonOK = (Button) findViewById(R.id.dialogbutton_ok);
            this.buttonCancel = (Button) findViewById(R.id.dialogbutton_cancel);
            this.buttonClear = (Button) findViewById(R.id.dialogbutton_clear);

            this.textView.setText(this.name);
            this.editText.setText(presence ? String.valueOf(initialValue) : "") ;
            this.editText.setInputType(EditorInfo.TYPE_CLASS_TEXT);

            this.buttonOK.setOnClickListener(v -> buttonOKClick());
            this.buttonCancel.setOnClickListener(v -> buttonCancelClick());
            this.buttonClear.setOnClickListener(v -> buttonClearClick());
        }

        // User click "OK" button.
        private void buttonOKClick() {
            String fullName = this.editText.getText().toString();

            if(fullName.isEmpty()) fullName = null; // no filtering

            this.dismiss(); // Close Dialog

            if (this.listener != null) {
                this.listener.fullNameEntered(fullName);
            }
        }
        // User click "Clear" button.
        private void buttonClearClick() {
            this.editText.setText("");

        }

        // User click "Cancel" button.
        private void buttonCancelClick() {
            this.dismiss();
        }
    }



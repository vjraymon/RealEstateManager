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

public class CustomDialogString extends Dialog {

        interface FullNameListener {
            void fullNameEntered(String fullName);
        }

        public final Context context;
        private final String name;
        private final boolean presence;
        private final String initialValue;


        private EditText editText;

        private final FullNameListener listener;

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

            TextView textView = findViewById(R.id.dialogtextView);
            this.editText = findViewById(R.id.dialogeditText);
            Button buttonOK = findViewById(R.id.dialogbutton_ok);
            Button buttonCancel = findViewById(R.id.dialogbutton_cancel);
            Button buttonClear = findViewById(R.id.dialogbutton_clear);

            textView.setText(this.name);
            this.editText.setText(presence ? String.valueOf(initialValue) : "") ;
            this.editText.setInputType(EditorInfo.TYPE_CLASS_TEXT);

            buttonOK.setOnClickListener(v -> buttonOKClick());
            buttonCancel.setOnClickListener(v -> buttonCancelClick());
            buttonClear.setOnClickListener(v -> buttonClearClick());
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



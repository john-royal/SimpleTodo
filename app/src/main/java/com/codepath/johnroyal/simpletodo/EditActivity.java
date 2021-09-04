package com.codepath.johnroyal.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etEditItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etEditItem = findViewById(R.id.etEditItem);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit Item");

        etEditItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // User clicks save button when done editing
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent that contains results of editing
                Intent intent = new Intent();

                // Pass data (results of editing)
                int position = getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION);
                String itemText = etEditItem.getText().toString();

                intent.putExtra(MainActivity.KEY_ITEM_POSITION, position);
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, itemText);

                // Set result of intent
                setResult(RESULT_OK, intent);

                // Finish activity
                finish();
            }
        });
    }
}
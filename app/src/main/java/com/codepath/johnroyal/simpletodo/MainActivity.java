package com.codepath.johnroyal.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final Integer EDIT_TEXT_CODE = 10;

    List<String> items;

    ItemsAdapter itemsAdapter;

    EditText etItem;
    Button btnAdd;
    RecyclerView rvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load items
        items = loadItems();

        // Set up ItemsAdapter
        itemsAdapter = new ItemsAdapter(
                items,
                new ItemsAdapter.OnClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        // Create new edit activity
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);

                        // Pass data being edited
                        intent.putExtra(KEY_ITEM_POSITION, position);

                        String itemText = items.get(position);
                        intent.putExtra(KEY_ITEM_TEXT, itemText);

                        // Display edit activity
                        startActivityForResult(intent, EDIT_TEXT_CODE);
                    }
                },
                new ItemsAdapter.OnLongClickListener() {
                    @Override
                    public void onItemLongClicked(int position) {
                        deleteItem(position);
                        showToast("Item deleted");
                    }
                }
        );

        // Set up views
        etItem = findViewById(R.id.etItem);
        btnAdd = findViewById(R.id.btnAdd);
        rvItems = findViewById(R.id.rvItems);

        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create item
                String todoItem = etItem.getText().toString();
                createItem(todoItem);

                // Reset input field
                etItem.setText("");

                // Notify user that item was created
                showToast("Item created");
            }
        });
    }

    // Handle result of edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve item position and text
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            // Update item
            updateItem(position, itemText);

            // Notify user
            showToast("Item updated");
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    // Creates a new todo item
    private void createItem(String itemText) {
        items.add(itemText);

        int position = items.size() - 1;
        itemsAdapter.notifyItemChanged(position);

        saveItems();
    }

    // Updates an existing todo item
    private void updateItem(int position, String itemText) {
        items.set(position, itemText);
        itemsAdapter.notifyItemChanged(position);
        saveItems();
    }

    // Deletes a todo item
    private void deleteItem(int position) {
        items.remove(position);
        itemsAdapter.notifyItemRemoved(position);
        saveItems();
    }

    // Creates a toast with the given message and shows it to the user
    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    // Loads items by reading each line of the data file
    private ArrayList<String> loadItems() {
        try {
            return new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            return new ArrayList<>();
        }
    }

    // Saves items by writing to data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

    // Load data file
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }
}
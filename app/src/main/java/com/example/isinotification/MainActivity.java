package com.example.isinotification;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.isinotification.R.menu;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    EditText txtTitle;
    EditText txtBody;
    EditText categorie;
    EditText priorityLevel;

    String[] item = {"1ING", "2ING", "2IDL", "2IDISC", "2ISEOC"};
    String[] priority = {"High","Medium", "Low"};
    IsiNotification notification;

    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView autoCompleteTextViewPriority;
    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItems1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    //    FirebaseUtil.openFbReference("isinotification", this);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtBody = (EditText) findViewById(R.id.txtBody);
        categorie = (EditText) findViewById(R.id.category);
        priorityLevel = (EditText) findViewById(R.id.priority);


        Intent intent = getIntent();
        IsiNotification notification = (IsiNotification) intent.getSerializableExtra("Notification");
        if(notification == null){
            notification = new IsiNotification();

        }
        this.notification = notification;

        categorie.setText(notification.getCategory());
        txtTitle.setText(notification.getTitle());
        txtBody.setText(notification.getBody());
        priorityLevel.setText(notification.getPriority());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        autoCompleteTextView = findViewById(R.id.category);
        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();

            }
        });
        autoCompleteTextViewPriority = findViewById(R.id.priority);
        adapterItems1 = new ArrayAdapter<>(this, R.layout.priority, priority);
        autoCompleteTextViewPriority.setAdapter(adapterItems1);

        autoCompleteTextViewPriority.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String priority = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, "Priority: " + priority, Toast.LENGTH_SHORT).show();

            }
        });



    }
    private void saveNotification() {
        notification.setCategory(categorie.getText().toString());
        notification.setTitle(txtTitle.getText().toString());
        notification.setBody(txtBody.getText().toString());
        notification.setPriority(priorityLevel.getText().toString());
        if(notification.getId() == null){
            mDatabaseReference.push().setValue(notification);
        }
        else {
            mDatabaseReference.child(notification.getId()).setValue(notification);
        }

    }
    private void deleteNotification() {
        if(notification == null){
            Toast.makeText(this, "Please save the notification before deleting", Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabaseReference.child(notification.getId()).removeValue();
    }
    private void backToList() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        if(FirebaseUtil.isAdmin == true){
            menu.findItem(R.id.delete_menu).setVisible(true);
            menu.findItem(R.id.save_menu).setVisible(true);
            menu.findItem(R.id.insert_menu).setVisible(false);
            enableEditText(true);
        }
        else {
            menu.findItem(R.id.delete_menu).setVisible(false);
            menu.findItem(R.id.save_menu).setVisible(false);
            menu.findItem(R.id.insert_menu).setVisible(false);
            enableEditText(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.save_menu :
                saveNotification();
                Toast.makeText(this, "Notification saved", Toast.LENGTH_LONG).show();
                clean();
                backToList();
                return true;
            case R.id.delete_menu:
                deleteNotification();
                Toast.makeText(this , "Notification deleted", Toast.LENGTH_SHORT);
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void clean() {
        categorie.setText("");
        txtTitle.setText("");
        txtBody.setText("");
        priorityLevel.setText("");
        txtTitle.requestFocus();
    }
    private void enableEditText(boolean isEnabled) {
        txtTitle.setEnabled(isEnabled);
        txtBody.setEnabled(isEnabled);
        categorie.setEnabled(isEnabled);
        priorityLevel.setEnabled(isEnabled);
    }
}
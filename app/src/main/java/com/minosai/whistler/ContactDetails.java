package com.minosai.whistler;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class ContactDetails extends AppCompatActivity {

    static final int PICK_CONTACT = 1;

    List<String> names = Arrays.asList("Mom","Dad","Brother");
    List<String> numbers = Arrays.asList("9841079300","9841062300","9841062377");
    RecyclerView recyclerView;
//    EmergencyContactAdapter adapter;
    ConstraintLayout constraintLayout;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        constraintLayout = (ConstraintLayout)findViewById(R.id.contactDetailsConstraint);

//        recyclerView = (RecyclerView)findViewById(R.id.emergencyContactList);
//        adapter = new EmergencyContactAdapter(names,numbers);
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        recyclerView.setAdapter(adapter);

        fab = (FloatingActionButton)findViewById(R.id.newContactFab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ContactDetails.this, "asdfasf", Toast.LENGTH_SHORT).show();
                pickContact();
            }
        });
    }

    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);  //should filter only contacts with phone numbers
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PICK_CONTACT != requestCode || RESULT_OK != resultCode) return;
        Uri contactUri = data.getData();
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        if (null == contactUri) return;
        //no tampering with Uri makes this to work without READ_CONTACTS permission
        Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
        if (null == cursor) return;
        try {
            while (cursor.moveToNext()) {
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID));
                // ... use "number" as you wish
                Toast.makeText(this, "number: "+number, Toast.LENGTH_SHORT).show();
            }
        } finally {
            cursor.close();
        }
        // "cursor" is closed here already
    }
}

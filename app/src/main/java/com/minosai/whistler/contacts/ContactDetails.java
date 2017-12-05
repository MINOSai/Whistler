package com.minosai.whistler.contacts;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.minosai.whistler.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ContactDetails extends AppCompatActivity {

    static final int PICK_CONTACT = 1;

    RecyclerView recyclerView;
    ContactsAdapter mAdapter;
    ConstraintLayout constraintLayout;
    FloatingActionButton fab;

    private static final String CONTACT_KEY = "contact_key";
    private static final String TAG = ContactDetails.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private Uri uriContact;
    private String contactID;

    private ArrayList<Contact> mContacts = new ArrayList<>();

    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        constraintLayout = (ConstraintLayout)findViewById(R.id.contactDetailsConstraint);

        mPrefs = getPreferences(Context.MODE_PRIVATE);
        mContacts = loadContacts();

        recyclerView = (RecyclerView)findViewById(R.id.emergencyContactList);
        mAdapter = new ContactsAdapter(mContacts);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        (ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)) {
                    @Override
                    public boolean onMove(RecyclerView rV,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        mContacts.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        saveContacts();
                    }
                });

        helper.attachToRecyclerView(recyclerView);

        fab = (FloatingActionButton)findViewById(R.id.newContactFab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ContactDetails.this,
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ContactDetails.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_REQUEST_READ_CONTACTS);
                }else {
                    onClickSelectContact();
                }
            }
        });

        String userMailId = getIntent().getStringExtra("userEmailId");
        Toast.makeText(this, "inside contact details: "+userMailId, Toast.LENGTH_SHORT).show();
    }

    public void onClickSelectContact(){
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK){
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

//            retrieveContactName();
//            retrieveContactNumber();
//            retrieveContactPhoto();
            mContacts.add(new Contact(retrieveContactName(),retrieveContactNumber(),retrieveContactPhoto()));
            mAdapter.notifyDataSetChanged();
            saveContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    onClickSelectContact();
                }else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private String retrieveContactNumber() {

        String contactNumber = null;

        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);
        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursorPhone.close();
        Log.d(TAG, "Contact Phone Number: " + contactNumber);
        return contactNumber;
    }

    private String retrieveContactName() {

        String contactName = null;
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        Log.d(TAG, "Contact Name: " + contactName);
        return contactName;

    }

    private Bitmap retrieveContactPhoto() {

        Bitmap photo = null;
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
//                return photo;
            }

            assert inputStream != null;
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return photo;
        }
    }

    private ArrayList<Contact> loadContacts() {
        Set<String> contactSet = mPrefs.getStringSet
                (CONTACT_KEY, new HashSet<String>());
        ArrayList<Contact> contacts = new ArrayList<>();
        for (String contactString : contactSet) {
            contacts.add(new Gson().fromJson(contactString, Contact.class));
        }
        return contacts;
    }

    private void saveContacts() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();
        HashSet<String> contactSet = new HashSet<>();
        for (Contact contact : mContacts) {
            contactSet.add(new Gson().toJson(contact));
        }
        editor.putStringSet(CONTACT_KEY, contactSet);
        editor.apply();
    }
}

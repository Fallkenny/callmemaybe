package com.example.callmemaybe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class ContactCreate extends AppCompatActivity {

    private final int GALLERY = 1;
    private File pictureFile = null;
    private Bitmap bitmap = null;
    private Contact editingContact;
    private DBSQLiteHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_create);
        editingContact = new Contact();
        dbHelper = new DBSQLiteHelper(this);

        Intent intent = getIntent();
        final int id = intent.getIntExtra("ID", 0);
        final boolean isReadOnly = intent.getBooleanExtra("readOnly",false);
        EditText nameField = findViewById(R.id.editTextName);
        EditText phoneNumberField = findViewById(R.id.editTextPhone);
        EditText observationField = findViewById(R.id.editTextObservation);
        ImageButton imageButton = findViewById(R.id.imageButton);
        FloatingActionButton saveButton = findViewById(R.id.saveButton);
        FloatingActionButton callButton = findViewById(R.id.callButton);

        if(isReadOnly)
        {
            nameField.setEnabled(false);
            phoneNumberField.setEnabled(false);
            observationField.setEnabled(false);
            imageButton.setEnabled(false);
            saveButton.setVisibility(View.INVISIBLE);
            callButton.setVisibility(View.VISIBLE);
        }

        if(id!=0)
        {
            editingContact = dbHelper.getContact(id);
            nameField.setText(editingContact.getName());
            phoneNumberField.setText(editingContact.getPhoneNumber());
            observationField.setText(editingContact.getObservation());

            bitmap = editingContact.getProfilePicture();
            imageButton.setImageBitmap(bitmap);
        }
    }

    private void loadPicture(String filepath) {
        bitmap = BitmapFactory.decodeFile(filepath);
        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
        imageButton.setImageBitmap(bitmap);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null,
                    null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            pictureFile = new File(picturePath);
            loadPicture(pictureFile.getAbsolutePath());
        }
    }

    public void pictureSelect(View view){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY);

    }

    public void btnCallClick(View view){
        TextView number = findViewById(R.id.editTextPhone);
        String numberTxt = number.getText().toString();
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:"+numberTxt));
        startActivity(phoneIntent);
    }


    public void btnSaveClick(View view){
        EditText nameField = findViewById(R.id.editTextName);
        EditText phoneNumberField = findViewById(R.id.editTextPhone);
        EditText observationField = findViewById(R.id.editTextObservation);

        String nameFieldContent = nameField.getText().toString();
        String phoneFieldContent = phoneNumberField.getText().toString();
        String observationFieldContent = observationField.getText().toString();

        if(nameFieldContent.isEmpty())
        {
            nameField.setError(getString(R.string.fieldValidation));
            return;
        }

        if(phoneFieldContent.isEmpty())
        {
            phoneNumberField.setError(getString(R.string.fieldValidation));
            return;
        }

        editingContact.setName(nameFieldContent);
        editingContact.setPhoneNumber(phoneFieldContent);
        editingContact.setObservation(observationFieldContent);
        if(bitmap != null){
            editingContact.setProfilePicture(bitmap);
        }

        if (editingContact.getId() != 0)
            dbHelper.updateContact(editingContact);
        else
            dbHelper.addContact(editingContact);

        this.finish();
    }
}
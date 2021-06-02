package com.example.callmemaybe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ContactsClickListener{

    private DBSQLiteHelper dbHelper;
    private RecyclerView recyclerView;
    private final int PERMISSION_REQUEST = 2;
    private final int REQUEST_PHONE_CALL = 4;
    ArrayList<Contact> contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_PHONE_CALL);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST);
            }
        }

        dbHelper = new DBSQLiteHelper(this);
        contacts = dbHelper.getAllContacts();

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ContactsAdapter contactsAdapter = new ContactsAdapter(contacts, this);
        recyclerView.setAdapter(contactsAdapter);

        ItemTouchHelper.Callback myCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                c.clipRect(0f, viewHolder.itemView.getTop(),
                        dX, viewHolder.itemView.getBottom());
                Drawable icon = ContextCompat.getDrawable(getBaseContext(),R.drawable.delete_icon);
                icon.setBounds(0, viewHolder.itemView.getTop()+50, 300, viewHolder.itemView.getBottom()-50);

                c.drawColor(Color.RED);
                icon.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);



            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            // More code here

        };
        ItemTouchHelper.Callback myCallback2 = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                c.clipRect(viewHolder.itemView.getRight()+dX, viewHolder.itemView.getTop(),
                        viewHolder.itemView.getRight(), viewHolder.itemView.getBottom());

                Drawable icon = ContextCompat.getDrawable(getBaseContext(),R.drawable.edit_icon);
                icon.setBounds((int)(viewHolder.itemView.getRight()-300), viewHolder.itemView.getTop()+50,viewHolder.itemView.getRight() , viewHolder.itemView.getBottom()-50);
                c.drawColor(Color.BLUE);
                icon.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);



            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            // More code here

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(myCallback);
        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(myCallback2);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        itemTouchHelper2.attachToRecyclerView(recyclerView);



        contactsAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onStart() {
        super.onStart();

        contacts = dbHelper.getAllContacts();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        ContactsAdapter adapter = new ContactsAdapter(contacts, this);
        recyclerView.setAdapter(adapter);

    }

    public void add_BtnClick(View view){
        Intent intent = new Intent(getBaseContext(), ContactCreate.class);
        intent.putExtra("readOnly", R.string.MODE_CREATE);
        startActivity(intent);

    }

    @Override
    public void onContactClick(int position) {

        Intent intent = new Intent(MainActivity.this, ContactCreate.class);
        intent.putExtra("ID", contacts.get(position).getId());
        intent.putExtra("readOnly", true);
        startActivity(intent);

    }
}
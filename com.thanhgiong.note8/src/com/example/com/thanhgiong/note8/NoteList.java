package com.example.com.thanhgiong.note8;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NoteList extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_book_list, menu);
        return true;
    }
}

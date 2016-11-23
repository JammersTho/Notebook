package com.app.jammers.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class NoteDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        createAndAddFragment();
    }

    private void createAndAddFragment()
    {
        //grab intent and fragment to launch from main activity list fragment
        Intent intent = getIntent();
        MainActivity.FragmentToLaunch fragmentToLaunch =
                (MainActivity.FragmentToLaunch) intent.getSerializableExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA);

        //grabbing fragment manager and fragment transaction so can add edit or view fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch(fragmentToLaunch)
        {
            case EDIT:
                //create and add note edit fragment to note detail activity if that's whats we want to launch
                NoteEditFragment noteEditFragment = new NoteEditFragment();
                setTitle(R.string.edit_fragment_title);
                fragmentTransaction.add(R.id.noteContainer, noteEditFragment, "NOTE_EDIT_FRAGMENT");
                break;
            case VIEW:
                //create and add note view fragment to note detail activity if that's whats we want to launch
                NoteViewFragment noteViewFragment = new NoteViewFragment();
                setTitle(R.string.view_fragment_title);
                fragmentTransaction.add(R.id.noteContainer, noteViewFragment, "NOTE_VIEW_FRAGMENT");
                break;
        }
        NoteViewFragment noteViewFragment = new NoteViewFragment();
        setTitle(R.string.view_fragment_title);
        fragmentTransaction.add(R.id.noteContainer, noteViewFragment, "NOTE_VIEW_FRAGMENT");

        //commit changes
        fragmentTransaction.commit();
    }
}

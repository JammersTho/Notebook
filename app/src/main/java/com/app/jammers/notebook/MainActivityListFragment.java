package com.app.jammers.notebook;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityListFragment extends ListFragment {
    private ArrayList<Note> notes;
    private NoteAdapter noteAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            /*

            super.onActivityCreated(savedInstanceState);

            String[] values = new String[]{ "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Mac OS X"};

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);

            setListAdapter(adapter);

            */
//        notes = new ArrayList<Note>();
//        notes.add(new Note("This Is A New Note Title!", "This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato.",
//                Note.Category.STAR));
//        notes.add(new Note("These notes are pretty sick", "This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato.",
//                Note.Category.NONE));
//        notes.add(new Note("Shopping list", "This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato.",
//                Note.Category.DONE));
//        notes.add(new Note("Christmas Presents", "This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato.This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato. This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato.",
//                Note.Category.HEART));
//        notes.add(new Note("Potato Potato Potato Potato Potato Potato Potato Potato PotatoPotatoPotato PotatoPotato Potato Potato Potato PotatoPotato Potato Potato Potato PotatoPotatoPotato Potato Potato", "This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato.",
//                Note.Category.NONE));
//        notes.add(new Note("This Is Another Note Title!", "This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato.",
//                Note.Category.DONE));
//        notes.add(new Note("This Is Another Note Title!", "This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato.",
//                Note.Category.DONE));
//        notes.add(new Note("This Is Another Note Title!", "This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato.",
//                Note.Category.DONE));
//        notes.add(new Note("This Is Another Note Title!", "This is the body of the note. There's some really important stuff in here. Keep on typing. La la la, potato.",
//                Note.Category.DONE));
        NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
        dbAdapter.open();
        notes = dbAdapter.getAllNotes();
        dbAdapter.close();

        noteAdapter = new NoteAdapter(getActivity(), notes);

        setListAdapter(noteAdapter);

            /*
            getListView().setDivider(ContextCompat.getDrawable(getActivity(), android.R.color.black));
            getListView().setDividerHeight(3);
            */

        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        launchNoteDetailActivity(MainActivity.FragmentToLaunch.VIEW, position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.long_press_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int rowPosition = info.position;
        Note note = (Note) getListAdapter().getItem(rowPosition);
        //returns to use id of whatever menu item was selected
        switch (item.getItemId()) {
            case R.id.edit:
                launchNoteDetailActivity(MainActivity.FragmentToLaunch.EDIT, rowPosition);
                Log.d("Menu Clicks", "We pressed edit!");
                return true;
            case R.id.delete:
                NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
                dbAdapter.open();
                dbAdapter.deleteNote(note.getId());

                notes.clear();
                notes.addAll(dbAdapter.getAllNotes());
                noteAdapter.notifyDataSetChanged();

                dbAdapter.close();
        }
        return super.onContextItemSelected(item);
    }


    private void launchNoteDetailActivity(MainActivity.FragmentToLaunch ftl, int position) {
        //grab the note information associated with whichever note was clicked
        Note note = (Note) getListAdapter().getItem(position);

        //create a new intent which launches out noteDetailActivity
        Intent intent = new Intent(getActivity(), NoteDetailActivity.class);

        //pass along the information of the note we clicked on to our noteDetailActivity
        intent.putExtra(MainActivity.NOTE_TITLE_EXTRA, note.getTitle());
        intent.putExtra(MainActivity.NOTE_MESSAGE_EXTRA, note.getMessage());
        intent.putExtra(MainActivity.NOTE_CATEGORY_EXTRA, note.getCategory());
        intent.putExtra(MainActivity.NOTE_ID_EXTRA, note.getId());

        switch (ftl) {
            case VIEW:
                intent.putExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA, MainActivity.FragmentToLaunch.VIEW);
                break;
            case EDIT:
                intent.putExtra(MainActivity.NOTE_FRAGMENT_TO_LOAD_EXTRA, MainActivity.FragmentToLaunch.EDIT);
        }
        startActivity(intent);
    }
}
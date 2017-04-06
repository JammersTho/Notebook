package com.app.jammers.notebook;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    private EditText title, message;
    private ImageButton noteCatButton;
    private AlertDialog categoryDialogObject, confirmDialogObject;
    private Note.Category savedNoteCategory;
    private static final String MODIFIED_CATEGORY = "Modified Category";

    private boolean newNote = false;

    private long noteId = 0;

    public NoteEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            newNote = bundle.getBoolean(NoteDetailActivity.NEW_NOTE_EXTRA, false);
        }

        if(savedInstanceState != null)
        {
            savedNoteCategory = (Note.Category) savedInstanceState.get(MODIFIED_CATEGORY);
        }

        //inflate the fragment layout
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit, container, false);

        //grab the widget references from the layout
        title = (EditText) fragmentLayout.findViewById(R.id.editNoteTitle);
        message = (EditText) fragmentLayout.findViewById(R.id.editNoteMessage);
        noteCatButton = (ImageButton) fragmentLayout.findViewById(R.id.editNoteButton);


        Button savedButton = (Button) fragmentLayout.findViewById(R.id.saveNote);

        //populate widgets with note data
        Intent intent = getActivity().getIntent();
        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA, ""));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA, ""));
        noteId = intent.getExtras().getLong(MainActivity.NOTE_ID_EXTRA, 0);

        //if we grabbed from our bundle then we know the we changed orientation and saved information
        //so set the category
        if(savedNoteCategory != null)
        {
            noteCatButton.setImageResource(Note.categoryToDrawable(savedNoteCategory));
        }
        else if (!newNote)
        {
            //otherwise we came from the list fragment so go ahead as normal
            Note.Category noteCat = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
            savedNoteCategory = noteCat;
            noteCatButton.setImageResource(Note.categoryToDrawable(noteCat));
        }

        buildCategoryDialog();
        buildConfirmDialog();

        getActivity();

        noteCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialogObject.show();
            }

        });

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogObject.show();
            }

        });

        return fragmentLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(MODIFIED_CATEGORY, savedNoteCategory);
    }
    private void buildCategoryDialog(){
        final String[] categories = new String[] {"None", "Heart", "Star", "Done"};

        AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(getActivity());
        categoryBuilder.setTitle("Set Note Type");

        categoryBuilder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //dismisses the dialog window
                categoryDialogObject.cancel();

                switch (item) {
                    case 0:
                        savedNoteCategory = Note.Category.NONE;
                        noteCatButton.setImageResource(R.drawable.notepad1);
                        break;
                    case 1:
                        savedNoteCategory = Note.Category.HEART;
                        noteCatButton.setImageResource(R.drawable.notepad2);
                        break;
                    case 2:
                        savedNoteCategory = Note.Category.STAR;
                        noteCatButton.setImageResource(R.drawable.notepad);
                        break;
                    case 3:
                        savedNoteCategory = Note.Category.DONE;
                        noteCatButton.setImageResource(R.drawable.notepad4);
                        break;
                }
            }
        });

        categoryDialogObject = categoryBuilder.create();
    }
    private void buildConfirmDialog(){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle("Are You Sure?");
        confirmBuilder.setMessage("Are you sure you want to save the note?");

        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Save Note", "Note title: " + title.getText() + " Note message: " + message.getText() + " Note category: " + savedNoteCategory);
                NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
                dbAdapter.open();

                if(newNote)
                {
                    //if its a new note, create it in db
                    dbAdapter.createNote(title.getText() + "", message.getText() + "", (savedNoteCategory == null) ? Note.Category.NONE : savedNoteCategory);

                } else {
                    //otherwise its an old note so update it in db
                    dbAdapter.updateNote(noteId, title.getText() + "", message.getText() + "", savedNoteCategory);
                }
                //TODO
                // Next: implementing delete functionality
                //https://www.youtube.com/watch?v=HyAnzf4mrPY&list=PLFhfnOjjZFcSKaZ72gyy7KyWpP5idY1Tq&index=80
                //TODO
                //figure out why i can only add titles, where did my note body go?!

                dbAdapter.close();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing here
            }
        });

        confirmDialogObject = confirmBuilder.create();
    }
}

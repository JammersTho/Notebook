package com.app.jammers.notebook;

/**
 * Created by JammersBlah on 19/11/2016.
 */
public class Note {

    private String title, message;
    private long noteId, dateCreatedMilli;
    private Category category;

    public enum Category{ PERSONAL, TECHNICAL, QUOTE, FINANCE, NONE, HEART, STAR, DONE }

    public Note(String title, String message, Category category)
    {
        this.title = title;
        this.message = message;
        this.category = category;
        this.noteId = 0;
        this.dateCreatedMilli = 0;
    }

    public Note(String title, String message, Category category, long noteId, long dateCreatedMilli)
    {
        this.title = title;
        this.message = message;
        this.category = category;
        this.noteId = noteId;
        this.dateCreatedMilli = dateCreatedMilli;
    }

    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public Category getCategory() { return category; }
    public long getId() { return noteId; }
    public long getDateCreatedMilli() { return dateCreatedMilli; }

    public String toString()
    {
        return "ID: " + noteId + " Title: " + title + " Message: " + message + " IconID: " + category.name() + " Date: " + dateCreatedMilli;
    }

    public int getAssociatedDrawable()
    {
        return categoryToDrawable(category);
    }

    public static int categoryToDrawable(Category noteCategory)
    {
        switch (noteCategory)
        {
            case NONE:
                return R.drawable.notepad1;
            case HEART:
                return R.drawable.notepad2;
            case STAR:
                return R.drawable.notepad;
            case DONE:
                return R.drawable.notepad4;
        }

        return R.drawable.notepad1;
    }
}

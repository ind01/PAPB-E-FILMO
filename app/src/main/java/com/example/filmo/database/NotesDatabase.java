package com.example.filmo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.filmo.model.Notes;
import com.example.filmo.viewmodel.NotesDao;

@Database(entities = {Notes.class}, version = 2)
public abstract class NotesDatabase extends RoomDatabase {

    public abstract NotesDao getNotesDao();

    private volatile static NotesDatabase notesDatabase;

    public static NotesDatabase getDatabase(Context context){
        if (notesDatabase == null) {
            notesDatabase = Room
                    .databaseBuilder(context.getApplicationContext(), NotesDatabase.class, "notes_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return notesDatabase;
    }
}

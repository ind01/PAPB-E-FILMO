package com.example.filmo.viewmodel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.filmo.model.Notes;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM note")
    List<Notes> getList();

    @Query("SELECT * FROM note WHERE id=:id")
    Notes get(Long id);

    @Insert
    void insertNotes(Notes note);

    @Query("DELETE FROM note WHERE id=:id")
    void deleteNotes(Long id);

    @Update
    void updateNotes(Notes notes);
}

package com.example.filmo.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.filmo.model.Notes;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotesViewModel extends ViewModel {

    private NotesDao dao;

    private Executor executor;

    public NotesViewModel(NotesDao dao){
        this.dao = dao;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void createNote(Notes note){
        this.executor.execute(() -> {
            this.dao.insertNotes(note);
        });
    }

    public Notes getById(Long id){
        return this.dao.get(id);
    }

    public List<Notes> getList(){
        return this.dao.getList();
    }

    public void deleteNotes(Long id){
        this.dao.deleteNotes(id);
    }

    public void updateNotes(Notes notes){this.dao.updateNotes(notes);}
}

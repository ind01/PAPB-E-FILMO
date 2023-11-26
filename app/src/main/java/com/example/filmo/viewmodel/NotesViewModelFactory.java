package com.example.filmo.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NotesViewModelFactory implements ViewModelProvider.Factory {

    private NotesDao dao;

    public NotesViewModelFactory(NotesDao dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(NotesViewModel.class)){
            return (T) new NotesViewModel(dao);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel model class");
        }
    }
}

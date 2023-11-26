package com.example.filmo.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmo.CreateNoteActivity;
import com.example.filmo.NoteActivity;
import com.example.filmo.databinding.CardViewBinding;
import com.example.filmo.model.Notes;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<Notes> data;

    public NotesAdapter(
        List<Notes> data
    ){
        this.data = data;
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{

        private CardViewBinding binding;

        public NotesViewHolder(CardViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Notes note){
            this.binding.title.setText(note.getTitle());
            float rating = note.getRating();
            if(rating % 1 == 0){
                this.binding.rating.setText(note.getRating().intValue() + "/5");
            } else {
                this.binding.rating.setText(note.getRating() + "/5");
            }
            this.binding.description.setText(note.getDescription());
            this.binding.getRoot()
                    .setOnClickListener((v) -> {
                        Intent intent = new Intent(v.getContext(), NoteActivity.class);
                        intent.putExtra("note", note);
                        v.getContext().startActivity(intent);
                    });
        }
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardViewBinding binding = CardViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NotesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Notes note = data.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

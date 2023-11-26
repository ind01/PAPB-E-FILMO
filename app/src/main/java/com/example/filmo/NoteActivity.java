package com.example.filmo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.filmo.database.NotesDatabase;
import com.example.filmo.databinding.ActivityNoteBinding;
import com.example.filmo.databinding.CardDialogBinding;
import com.example.filmo.databinding.CardViewBinding;
import com.example.filmo.model.Notes;
import com.example.filmo.viewmodel.NotesViewModel;
import com.example.filmo.viewmodel.NotesViewModelFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoteActivity extends AppCompatActivity {

    private ActivityNoteBinding binding;

    private ImageView back;

    private ImageView trash;

    private Executor executor;

    private TextView title;

    private TextView description;

    private RatingBar rating;

    private TextView date;

    private Button buttonUpdate;

    private NotesViewModel viewModel;

    private CardDialogBinding cardBinding;

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new NotesViewModelFactory(NotesDatabase.getDatabase(this).getNotesDao()).create(NotesViewModel.class);
        binding = ActivityNoteBinding.inflate(this.getLayoutInflater());

        setContentView(binding.getRoot());

        back = binding.back;
        trash = binding.trashNote;
        title = binding.movieTitle;
        description = binding.description;
        rating = binding.ratingBar;
        date = binding.dateCreated;
        buttonUpdate = binding.materialButton;
        cardBinding = CardDialogBinding.inflate(this.getLayoutInflater());
        this.executor = Executors.newSingleThreadExecutor();

        Intent intent = getIntent();
        Notes note = (Notes) intent.getSerializableExtra("note");

        title.setText(note.getTitle());
        description.setText(note.getDescription());
        rating.setRating(note.getRating());
        date.setText(note.getDate());

        back.setOnClickListener((v) -> {
            executor.execute(() -> {
                note.setTitle(title.getText().toString());
                note.setDescription(description.getText().toString());
                note.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm")));
                note.setRating(rating.getRating());
                this.viewModel.updateNotes(note);
            });
            finish();
        });

        trash.setOnClickListener((v) -> {
            executor.execute(() -> {
                this.viewModel.deleteNotes(note.getId());
            });
            runOnUiThread(() -> {
                Toast.makeText(this, "sukses menghapus notes", Toast.LENGTH_SHORT).show();
            });
            finish();
        });

        buttonUpdate.setOnClickListener((v) -> {

            View dialogView = cardBinding.getRoot();

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NoteActivity.this);

            dialogBuilder.setMessage("Apakah Anda ingin mengupdate Note?");

            dialogBuilder.setPositiveButton("YA", (dialog, which) -> {
                executor.execute(() -> {
                    note.setTitle(title.getText().toString());
                    note.setDescription(description.getText().toString());
                    note.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm")));
                    note.setRating(rating.getRating());
                    this.viewModel.updateNotes(note);
                });
                runOnUiThread(() -> {
                    Toast.makeText(this, "sukses mengupdate notes", Toast.LENGTH_SHORT).show();
                });
                dialog.dismiss();
                finish();
            });

            dialogBuilder.setNegativeButton("TIDAK", (dialog, which) -> {
                dialog.dismiss();
            });

            dialog = dialogBuilder.create();

            dialog.setOnShowListener((l) -> {
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setBackground(getResources().getDrawable(R.drawable.background_no_button_drawable));

                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setBackground(getResources().getDrawable(R.drawable.background_no_button_drawable));
            });

            dialog.show();

            if (dialogView.getParent() != null) {
                ((ViewGroup) dialogView.getParent()).removeView(dialogView);
            }
        });
    }
}

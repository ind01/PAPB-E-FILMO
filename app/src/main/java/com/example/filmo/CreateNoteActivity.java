package com.example.filmo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.filmo.database.NotesDatabase;
import com.example.filmo.databinding.ActivityCreateNoteBinding;
import com.example.filmo.databinding.CardDialogBinding;
import com.example.filmo.model.Notes;
import com.example.filmo.viewmodel.NotesViewModel;
import com.example.filmo.viewmodel.NotesViewModelFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CreateNoteActivity extends AppCompatActivity {

    private ActivityCreateNoteBinding binding;

    private CardDialogBinding cardBinding;

    private TextView textView;

    private Button buttonCreate;

    private ImageView back;

    private Executor executor;

    private NotesViewModel viewModel;

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executor = Executors.newSingleThreadExecutor();

        binding = ActivityCreateNoteBinding.inflate(this.getLayoutInflater());

        cardBinding = CardDialogBinding.inflate(this.getLayoutInflater());

        viewModel = new NotesViewModelFactory(NotesDatabase.getDatabase(this).getNotesDao()).create(NotesViewModel.class);

        setContentView(binding.getRoot());

        textView = binding.dateCreated;

        back = binding.back;

        buttonCreate = binding.materialButton;

        textView.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm")));

        back.setOnClickListener((v) -> {

            View dialogView = cardBinding.getRoot();

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateNoteActivity.this);

            dialogBuilder.setMessage("Apakah Anda yakin ingin kembali ke Home?");

            dialogBuilder.setPositiveButton("YA", (dialog, which) -> {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
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

        buttonCreate.setOnClickListener((v) -> {
            View dialogView = cardBinding.getRoot();

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateNoteActivity.this);

            dialogBuilder.setMessage("Buat note?");

            dialogBuilder.setPositiveButton("YA", (dialog, which) -> {
                Notes notes = new Notes();
                notes.setTitle(binding.movieTitle.getText().toString());
                notes.setDescription(binding.description.getText().toString());
                notes.setRating(binding.ratingBar.getRating());
                notes.setDate(textView.getText().toString());
                executor.execute(() -> {
                    viewModel.createNote(notes);
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "sukses membuat notes", Toast.LENGTH_SHORT).show();
                    });
                    finish();
                });
                dialog.dismiss();
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

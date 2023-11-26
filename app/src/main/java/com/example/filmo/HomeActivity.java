package com.example.filmo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmo.adapter.NotesAdapter;
import com.example.filmo.database.NotesDatabase;
import com.example.filmo.databinding.ActivityHomeBinding;
import com.example.filmo.model.Notes;
import com.example.filmo.viewmodel.NotesViewModel;
import com.example.filmo.viewmodel.NotesViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private RecyclerView recyclerView;

    private ImageView imageView;

    private NotesViewModel viewModel;

    private List<Notes> data;

    private Executor executor;

    private NotesAdapter adapter;

    private TextInputEditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = new ArrayList<>();

        this.adapter = new NotesAdapter(data);

        executor = Executors.newSingleThreadExecutor();

        binding = ActivityHomeBinding.inflate(this.getLayoutInflater());

        this.setContentView(binding.getRoot());

        viewModel = new NotesViewModelFactory(NotesDatabase.getDatabase(this)
                .getNotesDao())
                .create(NotesViewModel.class);

        recyclerView = binding.recyclerView;
        imageView = binding.addNotes;
        executor = Executors.newSingleThreadExecutor();
        this.editText = binding.editText;

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageView.setOnClickListener((v) -> {
            Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
            startActivity(intent);
            this.adapter.notifyDataSetChanged();
        });

        this.editText.setOnClickListener((v) -> {
            String title = editText.getText().toString();
            if(title.isEmpty()){
                this.executor.execute(() -> {
                    List<Notes> list = viewModel.getList();
                    data.clear();
                    data.addAll(list);
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                });
            } else {
                this.executor.execute(() -> {
                    List<Notes> datas = this.viewModel.getList();
                    List<Notes> notes = datas
                            .stream()
                            .filter((n) -> n.getTitle().contains(title))
                            .collect(Collectors.toList());
                    this.data.clear();
                    this.data.addAll(notes);
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        executor.execute(() -> {
            List<Notes> datas = viewModel.getList();
            if (data == null) {
                data = new ArrayList<>();
            }
            data.clear();
            data.addAll(datas);

            // Memindahkan kode notifyDataSetChanged ke dalam thread utama
            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
        });
    }
}

package com.example.listaestudosapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TarefaAdapter adapter;
    private List<Estudo> lista = new ArrayList<>();
    private Button btnAdicionar, btnDashboard;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerTarefas);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnDashboard = findViewById(R.id.btnDashboard); // Novo botão
        db = FirebaseFirestore.getInstance();

        adapter = new TarefaAdapter(lista, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db.collection("tarefas")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;

                    lista.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Estudo e = doc.toObject(Estudo.class);
                        lista.add(e);
                    }
                    adapter.notifyDataSetChanged();
                });

        btnAdicionar.setOnClickListener(view -> {
            startActivity(new Intent(this, AddTaskActivity.class));
        });

        btnDashboard.setOnClickListener(view -> {
            startActivity(new Intent(this, DashboardActivity.class));
        });
    }
}

package com.example.listaestudosapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class DetalheTarefaActivity extends AppCompatActivity {

    private EditText editTitulo, editPrazo;
    private Button btnSalvar, btnExcluir;
    private FirebaseFirestore db;
    private String tarefaId;
    private Estudo estudoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_tarefa);

        // Verifica se a ActionBar existe antes de usar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editTitulo = findViewById(R.id.editTitulo);
        editPrazo = findViewById(R.id.editPrazo);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnExcluir = findViewById(R.id.btnExcluir);

        db = FirebaseFirestore.getInstance();
        tarefaId = getIntent().getStringExtra("id");

        db.collection("tarefas").document(tarefaId).get().addOnSuccessListener(doc -> {
            estudoAtual = doc.toObject(Estudo.class);
            if (estudoAtual != null) {
                editTitulo.setText(estudoAtual.getTitulo());
                editPrazo.setText(estudoAtual.getPrazo());
            }
        });

        btnSalvar.setOnClickListener(v -> {
            String novoTitulo = editTitulo.getText().toString().trim();
            String novoPrazo = editPrazo.getText().toString().trim();

            if (!novoTitulo.isEmpty() && !novoPrazo.isEmpty()) {
                estudoAtual.setTitulo(novoTitulo);
                estudoAtual.setPrazo(novoPrazo);

                db.collection("tarefas").document(tarefaId).set(estudoAtual)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Tarefa atualizada!", Toast.LENGTH_SHORT).show();
                            finish();
                        });
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }
        });

        btnExcluir.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Excluir Tarefa")
                    .setMessage("Tem certeza que deseja excluir esta tarefa?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        db.collection("tarefas").document(tarefaId).delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Tarefa excluída", Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

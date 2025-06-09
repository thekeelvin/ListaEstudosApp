package com.example.listaestudosapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTitulo, editPrazo;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Configura toolbar como ActionBar
        Toolbar toolbar = findViewById(R.id.toolbarAdd);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editTitulo = findViewById(R.id.editTitulo);
        editPrazo = findViewById(R.id.editPrazo);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Abre seletor de data
        editPrazo.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (DatePicker view, int year, int month, int dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                editPrazo.setText(sdf.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnSalvar.setOnClickListener(view -> {
            String titulo = editTitulo.getText().toString().trim();
            String prazo = editPrazo.getText().toString().trim();

            if (!titulo.isEmpty() && !prazo.isEmpty()) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String id = db.collection("tarefas").document().getId();

                Estudo novaTarefa = new Estudo(id, titulo, prazo, false);
                db.collection("tarefas").document(id).set(novaTarefa)
                        .addOnSuccessListener(aVoid -> finish())
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Erro ao salvar tarefa", Toast.LENGTH_SHORT).show()
                        );
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

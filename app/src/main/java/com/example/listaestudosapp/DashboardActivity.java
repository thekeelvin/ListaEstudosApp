package com.example.listaestudosapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private TextView txtTotal, txtFeitas, txtPendentes, txtAtrasadas;
    private FirebaseFirestore db;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Configura o Toolbar como ActionBar e ativa botão de voltar
        Toolbar toolbar = findViewById(R.id.toolbarDashboard);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtTotal = findViewById(R.id.txtTotal);
        txtFeitas = findViewById(R.id.txtFeitas);
        txtPendentes = findViewById(R.id.txtPendentes);
        txtAtrasadas = findViewById(R.id.txtAtrasadas);

        db = FirebaseFirestore.getInstance();

        db.collection("tarefas").get().addOnSuccessListener(snapshot -> {
            int total = 0, feitas = 0, pendentes = 0, atrasadas = 0;
            Date hoje = new Date();

            for (QueryDocumentSnapshot doc : snapshot) {
                Estudo e = doc.toObject(Estudo.class);
                total++;
                if (e.isFeito()) {
                    feitas++;
                } else {
                    pendentes++;
                    try {
                        Date prazo = sdf.parse(e.getPrazo());
                        if (prazo != null && prazo.before(hoje)) {
                            atrasadas++;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            txtTotal.setText(String.valueOf(total));
            txtFeitas.setText(String.valueOf(feitas));
            txtPendentes.setText(String.valueOf(pendentes));
            txtAtrasadas.setText(String.valueOf(atrasadas));
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

package com.example.listaestudosapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.MyViewHolder> {

    private List<Estudo> lista;
    private Context context;

    public TarefaAdapter(List<Estudo> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tarefa, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Estudo estudo = lista.get(position);
        holder.checkFeito.setText(estudo.getTitulo());
        holder.checkFeito.setChecked(estudo.isFeito());
        holder.textPrazo.setText("Prazo: " + estudo.getPrazo());

        holder.checkFeito.setOnCheckedChangeListener((buttonView, isChecked) -> {
            estudo.setFeito(isChecked);
            FirebaseFirestore.getInstance()
                    .collection("tarefas")
                    .document(estudo.getId())
                    .set(estudo);
        });

        // TOQUE LONGO no CheckBox
        holder.checkFeito.setOnLongClickListener(v -> {
            Intent intent = new Intent(context, DetalheTarefaActivity.class);
            intent.putExtra("id", estudo.getId());
            context.startActivity(intent);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialCheckBox checkFeito;
        TextView textPrazo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkFeito = itemView.findViewById(R.id.checkFeito);
            textPrazo = itemView.findViewById(R.id.textPrazo);
        }
    }
}

package com.example.projetws.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projetws.R;
import com.example.projetws.beans.Etudiant;

import java.io.File;
import java.util.List;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> {

    private List<Etudiant> etudiants;
    private Context context;

    public EtudiantAdapter(List<Etudiant> etudiants, Context context) {
        this.etudiants = etudiants;
        this.context = context;
    }

    @Override
    public EtudiantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.etudiant_item, parent, false);
        return new EtudiantViewHolder(view);
    }

    public static class EtudiantViewHolder extends RecyclerView.ViewHolder {
        public TextView id, nom, prenom, date, ville, sexe;
        public ImageView image;

        public EtudiantViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.etudiant_id);
            nom = itemView.findViewById(R.id.etudiant_nom);
            prenom = itemView.findViewById(R.id.etudiant_prenom);
            date = itemView.findViewById(R.id.etudiant_date);
            ville = itemView.findViewById(R.id.etudiant_ville);
            sexe = itemView.findViewById(R.id.etudiant_gender);
            image = itemView.findViewById(R.id.etudiant_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder holder, int position) {
        Etudiant e = etudiants.get(position);
        holder.id.setText(String.valueOf(e.getId()));
        holder.nom.setText(e.getNom());
        holder.prenom.setText(e.getPrenom());
        holder.date.setText(e.getDateNaissance());
        holder.ville.setText(e.getVille());
        holder.sexe.setText(e.getSexe());
        String imagePath = e.getImage();

        holder.itemView.setOnClickListener(v -> showOptionsDialog(position));

        if (imagePath != null && !e.getImage().isEmpty()) {
            String imageUrl = "http://192.168.1.8/PhpProject1/ws/Images/" + imagePath;
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.profil)  // Image par défaut en cas de problème
                    .error(R.drawable.profil)  // Image en cas d'erreur de chargement
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.profil); // une image par défaut
        }
    }

    private void showOptionsDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Options");
        builder.setItems(new CharSequence[]{"Modifier", "Supprimer"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Appeler la méthode de modification
                    // modifyItem(position);
                    break;
                case 1:
                    deleteItem(position);  // Appeler la méthode de suppression
                    break;
            }
        });
        builder.show();
    }


    private void deleteItem(int position) {
        Etudiant etudiant = etudiants.get(position); // Récupérer l'étudiant à supprimer

        // URL de votre API PHP de suppression
        String url = "http://192.168.1.8/PhpProject1/controller/deleteEtudiant.php?id=" + etudiant.getId();

        // Effectuer une requête GET pour supprimer l'étudiant du serveur
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Si la suppression réussie, retirer l'étudiant de la liste et notifier l'adaptateur
                    etudiants.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Étudiant supprimé avec succès", Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show());

        // Ajouter la requête à la file d'attente de Volley
        Volley.newRequestQueue(context).add(stringRequest);
    }


    @Override
    public int getItemCount() {
        return etudiants.size();
    }


}


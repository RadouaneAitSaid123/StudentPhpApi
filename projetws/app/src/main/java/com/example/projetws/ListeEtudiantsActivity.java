package com.example.projetws;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetws.adapter.EtudiantAdapter;
import com.example.projetws.beans.Etudiant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListeEtudiantsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EtudiantAdapter adapter;
    private List<Etudiant> etudiants = new ArrayList<>();
    private String url = "http://192.168.1.8/PhpProject1/ws/loadEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_etudiants);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        etudiants = new ArrayList<>();
        adapter = new EtudiantAdapter(etudiants, this);
        recyclerView.setAdapter(adapter);
        loadEtudiants();
    }

    private void loadEtudiants() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("VOLLEY", "Réponse json: " + response.toString());
                        etudiants.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                Etudiant e = new Etudiant(
                                        obj.getInt("id"),
                                        obj.getString("nom"),
                                        obj.getString("prenom"),
                                        obj.getString("ville"),
                                        obj.getString("sexe"),
                                        obj.getString("date_naissance"),
                                        obj.optString("image", "") // Peut être null
                                );

                                etudiants.add(e);
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("VOLLEY", "Erreur JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", "Erreur réseau: " + error.toString());
                    }
                });

        queue.add(request);
    }
}


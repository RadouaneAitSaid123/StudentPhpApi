package com.example.projetws;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.Manifest;

public class AddEtudiant extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE = 1; // Pour la galerie
    private static final int TAKE_PHOTO = 2; // Pour la caméra
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Uri imageUri = null;
    private ImageView studentImage;
    private EditText nom, prenom, dateNaissance;
    private Spinner ville;
    private RadioButton m, f;
    private Button add;
    private String selectedDate = "";
    int MY_REQUEST_CODE = 1;
    Bitmap bitmap;
    String encodeImage;
    Button btnShowList;

    private String insertUrl = "http://192.168.1.8/PhpProject1/ws/createEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_etudiant);

        // Vérification de la permission pour la caméra
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }

        // Initialisation des vues
        btnShowList = findViewById(R.id.btn_liste);
        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lancer l'activité qui affiche la liste
                Intent intent = new Intent(AddEtudiant.this, ListeEtudiantsActivity.class);
                startActivity(intent);
            }
        });
        nom = findViewById(R.id.editTextStudentLastName);
        prenom = findViewById(R.id.editTextStudentFirstName);
        ville = findViewById(R.id.ville);
        dateNaissance = findViewById(R.id.editTextStudentBirth);
        studentImage = findViewById(R.id.studentImge);
        add = findViewById(R.id.add);
        m = findViewById(R.id.m);
        f = findViewById(R.id.f);

        // Événements au clic
        add.setOnClickListener(this);
        studentImage.setOnClickListener(v -> showImageOptions());
        dateNaissance.setOnClickListener(v -> showDatePicker());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permission requise pour accéder à la caméra", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Créez un nom unique pour le fichier
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Déterminez le répertoire où les images seront stockées (ici un répertoire externe)
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Créez un fichier temporaire dans ce répertoire
        File image = File.createTempFile(
                imageFileName,  // Préfixe du nom du fichier
                ".jpg",         // Extension du fichier
                storageDir      // Dossier où le fichier sera stocké
        );

        // Sauvegardez le chemin du fichier si nécessaire (par exemple, dans une variable membre)
        imageUri = Uri.fromFile(image);

        return image;
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Créez un fichier pour stocker la photo
            File photoFile = null;
            try {
                photoFile = createImageFile();  // Créez le fichier photo
            } catch (IOException ex) {
                // Erreur de création du fichier
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.projetws.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, 1); // 1 est le code de requête
            }
        }
    }

    private void showImageOptions() {
        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setItems(options, (dialog, which) -> {
            if (options[which].equals("Take Photo")) {
                openCamera();
            } else if (options[which].equals("Choose from Gallery")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            } else {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
            dateNaissance.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Vérifie si la réponse est OK
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE && data != null) {
                // Image sélectionnée depuis la galerie
                Uri filePath = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(filePath);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    studentImage.setImageBitmap(bitmap);
                    ImageStore(bitmap); // Encode l'image en base64
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                // Image capturée par la caméra
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = (Bitmap) extras.get("data");
                    studentImage.setImageBitmap(photo);
                    ImageStore(photo);
                }
            }
        } else {
            Log.d(TAG, "Image selection or capture failed");
        }
    }

    private void ImageStore(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageByte = stream.toByteArray();
            encodeImage = android.util.Base64.encodeToString(imageByte, Base64.DEFAULT);
        } else {
            Log.d(TAG, "Bitmap is null");
        }
    }

    public void afficher(View view) {
        Intent intent = new Intent(this, ListeEtudiantsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == add) {
            StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(AddEtudiant.this, "Student added", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddEtudiant.this, "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    String sexe = m.isChecked() ? "homme" : "femme";
                    params.put("nom", nom.getText().toString());
                    params.put("prenom", prenom.getText().toString());
                    params.put("ville", ville.getSelectedItem().toString());
                    params.put("sexe", sexe);
                    params.put("date_naissance", selectedDate);
                    params.put("image", encodeImage);

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(AddEtudiant.this);
            requestQueue.add(request);
        }
    }
}

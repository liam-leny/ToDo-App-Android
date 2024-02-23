package com.example.todoapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class taskCreateActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etStartDate, etEndDate, etLien;
    private Spinner spinnerContext;



    private Task task;
    private boolean isEditMode = false;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private Button btnSaveTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);

        // Récupérer la référence des vues
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        spinnerContext = findViewById(R.id.spinner_context);
        btnSaveTask = findViewById(R.id.btn_save_task);
        etLien = findViewById(R.id.et_lien);

        // Récupérer la tâche transmise depuis taskDescActivity
        task = getIntent().getParcelableExtra("task");

        // Vérifier si une tâche a été transmise
        if (task != null) {
            isEditMode = true;
            // Charger les valeurs de la tâche dans les champs de saisie
            etTitle.setText(task.getTitle());
            etDescription.setText(task.getDescription());
            etStartDate.setText(task.getStartDate());
            etEndDate.setText(task.getEndDate());
            etLien.setText(task.getLien());
        }

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Créer une instance de DatePickerDialog
        datePickerDialog = new DatePickerDialog(taskCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String selectedDate = dateFormatter.format(newDate.getTime());

                // Vérifier si le champ de date de début a le focus
                if (etStartDate.hasFocus()) {
                    etStartDate.setText(selectedDate);
                }
                // Vérifier si le champ de date de fin a le focus
                else if (etEndDate.hasFocus()) {
                    etEndDate.setText(selectedDate);
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        /**
         * Permet d'afficher DatePickerDialog lorsque l'utilisateur va vouloir renseigner
         * le champ de date de début
         */
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Afficher le DatePickerDialog
                datePickerDialog.show();
            }
        });

        /**
         * Permet d'afficher DatePickerDialog lorsque l'utilisateur va vouloir renseigner
         * le champ de date de fin
         */
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Afficher le DatePickerDialog
                datePickerDialog.show();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                taskCreateActivity.this,
                R.array.context_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerContext.setAdapter(adapter);

        // Sélectionner une option du Spinner si la tâche existe
        if (isEditMode && task != null) {
            int contextPosition = adapter.getPosition(task.getContext());
            spinnerContext.setSelection(contextPosition);
        }

        // Gérer le clic sur le bouton de sauvegarde
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task newTask = null;

                // Récupérer les valeurs saisies par l'utilisateur
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String startDate = etStartDate.getText().toString();
                String endDate = etEndDate.getText().toString();
                String context = spinnerContext.getSelectedItem().toString();
                String lien = etLien.getText().toString();


                if (!lien.isEmpty()) {
                    // Créer un intent pour démarrer l'activité WebViewActivity
                    Intent webViewIntent = new Intent(taskCreateActivity.this, WebViewActivity.class);
                    webViewIntent.putExtra("lien", lien);
                    startActivity(webViewIntent);
                }



                // Vérifier si tous les champs sont remplis
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(startDate)
                        || TextUtils.isEmpty(endDate) || TextUtils.isEmpty(context)) {
                    Toast.makeText(taskCreateActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Vérifier que les champs de date sont remplis en utilisant le bon format
                if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
                    // Afficher un message d'erreur indiquant le format de date attendu
                    Toast.makeText(taskCreateActivity.this, "Veuillez saisir la date au format dd/mm/yyyy", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Vérifier si la date de début est inférieure ou égale à la date de fin
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                try {
                    Date start = dateFormat.parse(startDate);
                    Date end = dateFormat.parse(endDate);
                    if (start.after(end)) {
                        Toast.makeText(taskCreateActivity.this, "La date de début doit être inférieure ou égale à la date de fin", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Vérifier si une tâche a été transmise et si nous sommes en mode d'édition
                if (isEditMode && task != null) {
                    // Mettre à jour les valeurs de la tâche avec les nouvelles valeurs saisies
                    task.setTitle(title);
                    task.setDescription(description);
                    task.setStartDate(startDate);
                    task.setEndDate(endDate);
                    task.setContext(context);
                    task.setLien(lien);
                } else {
                    // Créer une nouvelle tâche avec les valeurs saisies
                    newTask = new Task(title, description, startDate, endDate, context, lien);
                }

                // Réinitialiser les champs de saisie
                etTitle.setText("");
                etDescription.setText("");
                etStartDate.setText("");
                etEndDate.setText("");
                etLien.setText("");

                // Passer la nouvelle tâche à l'activité précédente
                Intent intent = new Intent();
                if (isEditMode && task != null) {
                    intent.putExtra("editedTask", task);
                } else {
                    intent.putExtra("newTask", newTask);
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * Permet de valider que l'utilisateur renseigne bien une date au format dd/mm/yyyy
     * @param date date renseigné par l'utilisateur
     * @return vrai si l'utilisateur a renseigné une date au bon format, faux sinon
     */
    private boolean isValidDateFormat(String date) {
        String pattern = "\\d{2}/\\d{2}/\\d{4}"; // Format: dd/mm/yyyy
        return date.matches(pattern);
    }
}

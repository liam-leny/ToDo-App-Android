package com.example.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_TASK = 1;
    private static final int REQUEST_CODE_EDIT_TASK = 2;

    private LinearLayout taskListLayout;
    private Button btnAddTask;
    private ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskListLayout = findViewById(R.id.task_list);
        btnAddTask = findViewById(R.id.btn_add_task);

        taskList = new ArrayList<>();
        // Ajouter les tâches d'exemple
        taskList.add(new Task("Faire API StockCounter", "Terminer le développement de mon API ainsi que les tests", "26/06/2023", "07/07/2023", "Travail", ""));
        taskList.add(new Task("Réunion sécurité", "Réunion de 10h à 12h. Apporter ordinateur", "05/07/2023", "05/07/2023", "Travail", ""));
        taskList.add(new Task("Sortie acrobranche", "Sortie d'accrobranche toute la journée en famille", "08/07/2023", "08/07/2023", "Personnel", "https://celtaventures.com/activites/accrobranche/"));
        taskList.add(new Task("Rdv ophtalmologie", "Rdv à la Clinique de la Baie de Morlaix à 10h30", "27/01/2024", "27/01/2024", "Personnel", "https://cliniquedelabaie.vivalto-sante.com/"));


        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Démarrer l'activité taskCreateActivity pour saisir une nouvelle tâche
                Intent intent = new Intent(MainActivity.this, taskCreateActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_TASK);
            }
        });

        displayTaskList("");

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Afficher la liste des tâches en filtrant par le texte de recherche
                displayTaskList(newText);
                return true;
            }
        });
    }

    private void displayTaskList(String filterText) {
        taskListLayout.removeAllViews();

        for (final Task task : taskList) {
            if (filterText.isEmpty() || task.getTitle().toLowerCase().contains(filterText.toLowerCase())) {
                // Créer une vue pour chaque tâche
                View taskView = getLayoutInflater().inflate(R.layout.task_item, taskListLayout, false);

                TextView taskTitle = taskView.findViewById(R.id.task_title);

                ImageButton btnEditTask = taskView.findViewById(R.id.btn_edit_task);
                ImageButton btnDeleteTask = taskView.findViewById(R.id.btn_delete_task);

                taskTitle.setText(task.getTitle());

                // Définir la couleur d'arrière-plan en fonction du contexte de la tâche
                if ("Travail".equals(task.getContext())) {
                    taskView.setBackgroundResource(R.drawable.task_background_travail);
                } else if ("Personnel".equals(task.getContext())) {
                    taskView.setBackgroundResource(R.drawable.task_background_personnel);
                } else {
                    taskView.setBackgroundResource(R.drawable.task_background_voyage);
                }

                btnEditTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Ouvrir l'activité taskCreateActivity pour modifier la tâche
                        Intent intent = new Intent(MainActivity.this, taskCreateActivity.class);
                        intent.putExtra("task", task);
                        startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
                    }
                });

                btnDeleteTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Supprimer la tâche de la liste
                        taskList.remove(task);
                        // Mettre à jour l'affichage du tableau de tâches
                        displayTaskList("");
                    }
                });


                taskView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Ouvrir l'activité taskDescActivity pour afficher les détails de la tâche
                        Intent intent = new Intent(MainActivity.this, taskDescActivity.class);
                        intent.putExtra("title", task.getTitle());
                        intent.putExtra("description", task.getDescription());
                        intent.putExtra("startDate", task.getStartDate());
                        intent.putExtra("endDate", task.getEndDate());
                        intent.putExtra("context", task.getContext());
                        intent.putExtra("lien", task.getLien());
                        startActivity(intent);
                    }
                });
                taskListLayout.addView(taskView);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("n'apparaît pas", "rien");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD_TASK) {
                if (data != null && data.hasExtra("newTask")) {
                    Task newTask = data.getParcelableExtra("newTask");
                    taskList.add(newTask);
                    displayTaskList("");
                }
            } else if (requestCode == REQUEST_CODE_EDIT_TASK) {
                if (data != null && data.hasExtra("editedTask")) {
                    Task editedTask = data.getParcelableExtra("editedTask");
                    this.updateTask(editedTask);
                    displayTaskList("");
                }

            }
        }

    }

    private void updateTask(Task newTask) {
        String taskTitle = newTask.getTitle();
        // Rechercher l'ancienne tâche avec le même titre dans la taskList
        for (int i = 0; i < taskList.size(); i++) {
            Task existingTask = taskList.get(i);
            if (existingTask.getTitle().equals(taskTitle)) {
                // Remplacer l'ancienne tâche par la nouvelle tâche dans la taskList
                taskList.set(i, newTask);
            }
        }
    }


}

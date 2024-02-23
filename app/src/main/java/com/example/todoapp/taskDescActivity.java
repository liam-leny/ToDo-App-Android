package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class taskDescActivity extends AppCompatActivity {

    private Task currentTask;
    private TextView taskLien;

    private static final int REQUEST_CODE_EDIT_TASK = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_desc);

        TextView taskTitle = findViewById(R.id.task_title);
        TextView taskDescription = findViewById(R.id.task_description);
        TextView taskStartDate = findViewById(R.id.task_start_date);
        TextView taskEndDate = findViewById(R.id.task_end_date);
        TextView taskContext = findViewById(R.id.task_context);
        taskLien = findViewById(R.id.task_lien);

        // Récupérer les informations de la tâche sélectionnée
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        String context = getIntent().getStringExtra("context");
        String lien = getIntent().getStringExtra("lien");


        // Afficher les informations de la tâche dans les TextView correspondants
        taskTitle.setText(title);
        taskDescription.setText(description);
        taskStartDate.setText(startDate);
        taskEndDate.setText(endDate);
        taskContext.setText(context);
        taskLien.setText(lien);

        taskLien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(taskDescActivity.this, WebViewActivity.class);
                intent.putExtra("lien", lien);
                startActivity(intent);
            }
        });
    }

}

package com.example.user.talleristamod.PackageGamePreguntas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.talleristamod.GlobalVariables.GlobalVariables;
import com.example.user.talleristamod.PackageProfiles.DatabaseProfiles;
import com.example.user.talleristamod.PackageProfiles.ProfileTallerista.ActivityProfileTallerista;
import com.example.user.talleristamod.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityShowImaginarie extends AppCompatActivity implements View.OnClickListener{

    Button buttonSendSignal, buttonFinishImg, buttonSignalPlay, buttonGivePointsImg;
    TextView textSelectedStudent, textCodeA;
    DatabaseImaginaries dataBaseSets;
    DatabaseProfiles databaseProfiles;
    RecyclerView recyclerViewStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_imaginarie);

        databaseProfiles = new DatabaseProfiles(this, "ActivityImaginaries");

        textSelectedStudent = (TextView) findViewById(R.id.textViewStudentName);
        textCodeA = (TextView) findViewById(R.id.textViewACodeImg);
        textCodeA.setText(GlobalVariables.JOIN_CODE);

        buttonGivePointsImg = findViewById(R.id.buttonGivePointsImg);
        buttonSendSignal = (Button) findViewById(R.id.buttonSendSignal);
        buttonFinishImg = (Button) findViewById(R.id.buttonFinishImg);
        buttonSignalPlay = (Button) findViewById(R.id.buttonTing);

        buttonGivePointsImg.setOnClickListener(this);
        buttonSendSignal.setOnClickListener(this);
        buttonFinishImg.setOnClickListener(this);
        buttonSignalPlay.setOnClickListener(this);
        dataBaseSets = new DatabaseImaginaries(this);
        showStudents();

        }

    public void showStudents(){
        recyclerViewStudents = (RecyclerView) findViewById(R.id.recyclerViewImaginarieStudents);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        dataBaseSets.obtainListStudents(recyclerViewStudents);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonSendSignal:
                dataBaseSets.sendSignal(textSelectedStudent);
                break;
            case R.id.buttonFinishImg:
                finishChallenge();
                break;
            case R.id.buttonTing:
                dataBaseSets.sendSignalPlay(textSelectedStudent);
                break;
            case R.id.buttonGivePointsImg:
                databaseProfiles.givePointsList();
                break;

        }
    }

    public void finishChallenge(){
        new AlertDialog.Builder(this)
                .setTitle("Finalizar Actividad ")
                .setMessage("¿Estas seguro que deseas finalizar la actividad?")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getApplicationContext(), "Actividad Finalizada ", Toast.LENGTH_LONG).show();

                        DatabaseReference databaseStateAImg = FirebaseDatabase.getInstance().getReference("Activity/ActivityImaginaries/"+GlobalVariables.ID_ACTIVITY);

                        if (GlobalVariables.IS_COPY.equals("true")) {
                            databaseStateAImg.removeValue();
                        } else {
                            databaseStateAImg.child("stateA").setValue("Disable");
                            databaseStateAImg.child("elegido").setValue("");
                            databaseStateAImg.child("joinCode").setValue("D4544as56");
                        }

                        Intent intent = new Intent(getApplicationContext(), ActivityProfileTallerista.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    public void onBackPressed (){
        finishChallenge();
    }
}

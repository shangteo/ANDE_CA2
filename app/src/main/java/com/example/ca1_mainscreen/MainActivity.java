package com.example.ca1_mainscreen;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ca1_mainscreen.Adapter.ToDoAdapter;
import com.example.ca1_mainscreen.Model.ToDoModel;
import com.example.ca1_mainscreen.Utils.DatabaseHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

FirebaseAuth auth;

TextView textView;
FirebaseUser user;
    private int progr = 0;
    private int progress = 0;
    private int numProgr = 0;
    private int progrBox = 0;
    private int getProgrBoxTt = 3;
    private int DIW = 7;
    private CheckBox checkBox;
    private boolean CB = false;
    private View onlineIndicator;
    private boolean buttonIncrClicked = false;
    private DatabaseReference databaseReference;

    private boolean buttonIncrClicked1 = false;
    private boolean buttonIncrClicked2 = false;
    private boolean buttonIncrClicked3 = false;
    private boolean buttonIncrClicked4 = false;
    private boolean buttonIncrClicked5 = false;
    private boolean buttonIncrClicked6 = false;
    private RadioButton buttonI1;
    private RadioButton buttonI2;
    private RadioButton buttonI3;
    private RadioButton buttonI4;
    private RadioButton buttonI5;
    private RadioButton buttonI6;
    private RadioButton buttonI7;
    private RecyclerView mRecyclerView;
    private ToDoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHandler db;
    private BottomSheetDialog dialog;

private Button but;
    // private DatabaseReference databaseReference;
    @Override
    protected void onStart() {
        super.onStart();
        updateOnlineStatus(true); // User goes online
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            updateOnlineStatus(false); // User is leaving the app
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button userChallenge = findViewById(R.id.btn_challenge);


        userChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserChallenge.class);
                startActivity(intent);
            }
        });

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();
        onlineIndicator = findViewById(R.id.onlineIndicator);

        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        buttonI1 = findViewById(R.id.r1);
        buttonI2 = findViewById(R.id.r2);
        buttonI3 = findViewById(R.id.r3);
        buttonI4 = findViewById(R.id.r4);
        buttonI5 = findViewById(R.id.r5);
        buttonI6 = findViewById(R.id.r6);
        buttonI7 = findViewById(R.id.r7);
        loadRadioButtonState();
        loadProgress();
        loadProgr();

        // Redirect to login if no user
        if (user == null) {
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            finish();
            return; // Exit the onCreate method if there's no user
        }
       checkBox = findViewById(R.id.checkBox);


        updateProgressBar();


            mRecyclerView = findViewById(R.id.tasksRecyclerView);
            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            db = new DatabaseHandler(this);
            db.openDatabase();
            List<ToDoModel> todoList = db.getAllTasks();
            TodaysPlan todaysPlan = new TodaysPlan();
            mAdapter = new ToDoAdapter(db, todaysPlan);
            mAdapter.setTasks(todoList);
            mRecyclerView.setAdapter(mAdapter);



//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            if(item.getItemId()==R.id.home) {
//                return true;
//            }else if(item.getItemId()==R.id.add) {
//                showDialog();
//
//            }else if(item.getItemId()==R.id.settings) {
//                startActivity(new Intent(MainActivity.this, Settings.class));
//                finish();
//                return true;
//            }else{
//                return false;
//            }
//        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                return true;
            } else if (item.getItemId() == R.id.add) {
                showDialog();
                return true;
            } else if (item.getItemId() == R.id.settings) {
                startActivity(new Intent(MainActivity.this, Settings.class));
                finish();
                return true;
            } else {
                return false;
            }
        });


        ImageButton plusbtn = findViewById(R.id.plus_button2);
        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DailyTask dailyTaskFragment = DailyTask.newInstance();
                dailyTaskFragment.show(getSupportFragmentManager(), DailyTask.TAG);
            }
        });

        TextView habit = findViewById(R.id.textView4);
        habit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when TextView is clicked
                Intent intent = new Intent(MainActivity.this, WeeklyHabit.class);
                startActivity(intent);
            }
        });


        bottomNavigationView.setSelectedItemId(R.id.home);

        buttonI7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int maxProgress = 100;
                final int progressIncrement = 10;
                if (!buttonIncrClicked6 && progr + progressIncrement <= maxProgress) {
                    progr += progressIncrement;
                    progress +=1;
                    buttonIncrClicked6= true;
                }else if (buttonIncrClicked6 && progr >= progressIncrement) {
                    // Decrement progress if it's greater than or equal to 15
                    progr -= progressIncrement;
                    progress -= 1;
                    buttonIncrClicked6 = false;
                }
                buttonI7.setChecked(buttonIncrClicked6);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();
                //updateDatabase();
                saveUserData();
                saveButtonIncrClickedState();
            }
        });

        buttonI6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int maxProgress = 100;
                final int progressIncrement = 15;
                if (!buttonIncrClicked5 && progr + progressIncrement <= maxProgress) {
                    progr += progressIncrement;
                    progress +=1;
                    buttonIncrClicked5= true;
                }else if (buttonIncrClicked5 && progr >= progressIncrement) {
                    // Decrement progress if it's greater than or equal to 15
                    progr -= progressIncrement;
                    progress -= 1;
                    buttonIncrClicked5 = false;
                }
                buttonI6.setChecked(buttonIncrClicked5);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();
                //updateDatabase();
                saveUserData();
                saveButtonIncrClickedState();
            }
        });

        buttonI5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int maxProgress = 100;
                final int progressIncrement = 15;
                if (!buttonIncrClicked4 && progr + progressIncrement <= maxProgress) {
                    progr += progressIncrement;
                    progress +=1;
                    buttonIncrClicked4= true;
                }else if (buttonIncrClicked4 && progr >= progressIncrement) {
                    // Decrement progress if it's greater than or equal to 15
                    progr -= progressIncrement;
                    progress -= 1;
                    buttonIncrClicked4 = false;
                }
                buttonI5.setChecked(buttonIncrClicked4);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();
                //updateDatabase();
                saveUserData();
                saveButtonIncrClickedState();
            }
        });

        buttonI4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int maxProgress = 100;
                final int progressIncrement = 15;
                if (!buttonIncrClicked3 && progr + progressIncrement <= maxProgress) {
                    progr += progressIncrement;
                    progress +=1;
                    buttonIncrClicked3 = true;
                }else if (buttonIncrClicked3 && progr >= progressIncrement) {
                    // Decrement progress if it's greater than or equal to 15
                    progr -= progressIncrement;
                    progress -= 1;
                    buttonIncrClicked3 = false;
                }
                buttonI4.setChecked(buttonIncrClicked3);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();
                //updateDatabase();
                saveUserData();
                saveButtonIncrClickedState();
            }
        });
        buttonI3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int maxProgress = 100;
                final int progressIncrement = 15;
                if (!buttonIncrClicked2 && progr + progressIncrement <= maxProgress) {
                    progr += progressIncrement;
                    progress +=1;
                    buttonIncrClicked2 = true;
                }else if (buttonIncrClicked2 && progr >= progressIncrement) {
                    // Decrement progress if it's greater than or equal to 15
                    progr -= progressIncrement;
                    progress -= 1;
                    buttonIncrClicked2 = false;
                }
                buttonI3.setChecked(buttonIncrClicked2);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();
                //updateDatabase();
                saveUserData();
                saveButtonIncrClickedState();
            }
        });
        buttonI2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int maxProgress = 100;
                final int progressIncrement = 15;
                if (!buttonIncrClicked1 && progr + progressIncrement <= maxProgress) {
                    progr += progressIncrement;
                    progress += 1;
                    buttonIncrClicked1 = true;
                }else if (buttonIncrClicked1 && progr >= progressIncrement) {
                    progr -= progressIncrement;
                    progress -= 1;
                    buttonIncrClicked1 = false;
                }
                buttonI2.setChecked(buttonIncrClicked1);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();

                saveUserData();
                saveButtonIncrClickedState();
               // updateDatabase();
            }
        });



        buttonI1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int maxProgress = 100;
                final int progressIncrement = 15;

                if (!buttonIncrClicked && progr + progressIncrement <= maxProgress) {
                    progr += progressIncrement;
                    progress += 1;
                    buttonIncrClicked = true;
                } else if (buttonIncrClicked && progr >= progressIncrement) {
                    progr -= progressIncrement;
                    progress -= 1;
                    buttonIncrClicked = false;
                }

                buttonI1.setChecked(buttonIncrClicked);
                checkBox.setChecked(CB);

                // Update the progress bar and save user data
                updateProgressBar();
                saveUserData();
                saveButtonIncrClickedState();
            }
        });






    }

    private void showDialog() {
        dialog = new BottomSheetDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout HabitLayout = dialog.findViewById(R.id.LayoutHabit);
        LinearLayout JournalLayout = dialog.findViewById(R.id.LayoutJournal);

        HabitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WeeklyHabit.class);
                startActivity(i);
                dialog.dismiss();
            }
        });

        JournalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Journal.class);
                startActivity(i);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateOnlineStatus(true); // User comes back to the app
        // Reset the state of the button when the activity is resumed
        loadButtonIncrClickedState();
    }
    private void saveButtonIncrClickedState() {
        databaseReference.child("user_data").child(user.getUid()).child("Sun").setValue(buttonIncrClicked);
        databaseReference.child("user_data").child(user.getUid()).child("Sat").setValue(buttonIncrClicked1);
        databaseReference.child("user_data").child(user.getUid()).child("Fri").setValue(buttonIncrClicked2);
        databaseReference.child("user_data").child(user.getUid()).child("Thu").setValue(buttonIncrClicked3);
        databaseReference.child("user_data").child(user.getUid()).child("Wed").setValue(buttonIncrClicked4);
        databaseReference.child("user_data").child(user.getUid()).child("Tue").setValue(buttonIncrClicked5);
        databaseReference.child("user_data").child(user.getUid()).child("Mon").setValue(buttonIncrClicked6)
 .addOnSuccessListener(aVoid -> Log.d("Firebase", "Successfully saved buttonIncrClicked state."))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to save buttonIncrClicked state.", e));
    }

    private void saveUserData() {
        // Save the state of the radio button and progress
        databaseReference.child("user_data").child(user.getUid()).child("buttonI1_state").setValue(buttonI1.isChecked());
        databaseReference.child("user_data").child(user.getUid()).child("buttonI2_state").setValue(buttonI2.isChecked());
        databaseReference.child("user_data").child(user.getUid()).child("buttonI3_state").setValue(buttonI3.isChecked());
        databaseReference.child("user_data").child(user.getUid()).child("buttonI4_state").setValue(buttonI4.isChecked());
        databaseReference.child("user_data").child(user.getUid()).child("buttonI5_state").setValue(buttonI5.isChecked());
        databaseReference.child("user_data").child(user.getUid()).child("buttonI6_state").setValue(buttonI6.isChecked());
        databaseReference.child("user_data").child(user.getUid()).child("buttonI7_state").setValue(buttonI7.isChecked());
        databaseReference.child("user_data").child(user.getUid()).child("progress").setValue(progress);
        databaseReference.child("user_data").child(user.getUid()).child("progr").setValue(progr)

                .addOnSuccessListener(aVoid -> Log.d("Database", "Successfully updated user data."))
                .addOnFailureListener(e -> Log.e("Database", "Failed to write user data.", e));
    }
    private void loadProgr() {
        // Load the progress value
        databaseReference.child("user_data").child(user.getUid()).child("progr")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Integer storedProgr = dataSnapshot.getValue(Integer.class);
                            if (storedProgr != null) {
                                progr = storedProgr;
                                updateProgressBar(); // Update progress bar with loaded progress
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Database", "Failed to read progress.", databaseError.toException());
                    }
                });
    }
    private void loadProgress() {
        // Load the progress value
        databaseReference.child("user_data").child(user.getUid()).child("progress")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Integer storedProgress = dataSnapshot.getValue(Integer.class);
                            if (storedProgress != null) {
                                progress = storedProgress;
                                updateProgressBar(); // Update progress bar with loaded progress
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Database", "Failed to read progress.", databaseError.toException());
                    }
                });
    }
    private void loadButtonIncrClickedState() {
        databaseReference.child("user_data").child(user.getUid()).child("Sun")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            buttonIncrClicked = dataSnapshot.getValue(Boolean.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Failed to read Sun state.", databaseError.toException());
                    }
                });
        databaseReference.child("user_data").child(user.getUid()).child("Sat")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            buttonIncrClicked1 = dataSnapshot.getValue(Boolean.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Failed to read Sat state.", databaseError.toException());
                    }
                });
        databaseReference.child("user_data").child(user.getUid()).child("Fri")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            buttonIncrClicked2 = dataSnapshot.getValue(Boolean.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Failed to read Fri state.", databaseError.toException());
                    }
                });
        databaseReference.child("user_data").child(user.getUid()).child("Thu")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            buttonIncrClicked3 = dataSnapshot.getValue(Boolean.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Failed to read Thu state.", databaseError.toException());
                    }
                });
        databaseReference.child("user_data").child(user.getUid()).child("Wed")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            buttonIncrClicked4 = dataSnapshot.getValue(Boolean.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Failed to read Wed state.", databaseError.toException());
                    }
                });
        databaseReference.child("user_data").child(user.getUid()).child("Tue")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            buttonIncrClicked5 = dataSnapshot.getValue(Boolean.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Failed to read Tue state.", databaseError.toException());
                    }
                });
        databaseReference.child("user_data").child(user.getUid()).child("Mon")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            buttonIncrClicked6 = dataSnapshot.getValue(Boolean.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Failed to read Tue state.", databaseError.toException());
                    }
                });
    }

    private void loadRadioButtonState() {
        databaseReference.child("user_data").child(user.getUid()).child("buttonI1_state")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Boolean state = dataSnapshot.getValue(Boolean.class);
                            buttonI1.setChecked(state != null && state);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Database", "Failed to read radio button state.", databaseError.toException());
                    }
                });
        databaseReference.child("user_data").child(user.getUid()).child("buttonI2_state")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Boolean state = dataSnapshot.getValue(Boolean.class);
                            buttonI2.setChecked(state != null && state);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Database", "Failed to read buttonI2 state.", databaseError.toException());
                    }
                });
        databaseReference.child("user_data").child(user.getUid()).child("buttonI3_state")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Boolean state = dataSnapshot.getValue(Boolean.class);
                            buttonI3.setChecked(state != null && state);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Database", "Failed to read buttonI3 state.", databaseError.toException());
                    }
                });
        databaseReference.child("user_data").child(user.getUid()).child("buttonI4_state")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Boolean state = dataSnapshot.getValue(Boolean.class);
                            buttonI4.setChecked(state != null && state);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Database", "Failed to read radio button state.", databaseError.toException());
                    }
                });
        databaseReference.child("user_data").child(user.getUid()).child("buttonI5_state")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Boolean state = dataSnapshot.getValue(Boolean.class);
                            buttonI5.setChecked(state != null && state);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Database", "Failed to read radio button state.", databaseError.toException());
                    }
                });
        databaseReference.child("user_data").child(user.getUid()).child("buttonI6_state")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Boolean state = dataSnapshot.getValue(Boolean.class);
                            buttonI6.setChecked(state != null && state);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Database", "Failed to read radio button state.", databaseError.toException());
                    }
                });
        databaseReference.child("user_data").child(user.getUid()).child("buttonI7_state")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Boolean state = dataSnapshot.getValue(Boolean.class);
                            buttonI7.setChecked(state != null && state);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Database", "Failed to read radio button state.", databaseError.toException());
                    }
                });
    }

    private void updateProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        TextView textViewProgress = findViewById(R.id.text_view_progress);

        ProgressBar progressBar2 = findViewById(R.id.progress_bar2);
        progressBar2.setProgress(progrBox);
        TextView textViewProgress2 = findViewById(R.id.text_view_progress2);
        textViewProgress2.setText( numProgr+"/"+getProgrBoxTt);
        progressBar.setProgress(progr);
        textViewProgress.setText(progress +"/"+DIW);

        if (progr == 100) {
            checkBox.setChecked(!CB);
            checkBox.setEnabled(CB);

        }
        else {
            checkBox.setChecked(CB);

        }
    }

        private void updateOnlineStatus(boolean isOnline) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                DatabaseReference userStatusRef = FirebaseDatabase.getInstance()
                        .getReference("UserStatus")
                        .child(currentUser.getUid())
                        .child("isOnline");
                userStatusRef.setValue(isOnline);
            }
        }


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });



}
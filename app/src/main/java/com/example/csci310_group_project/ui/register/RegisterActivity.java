package com.example.csci310_group_project.ui.register;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csci310_group_project.ContentActivity;
import com.example.csci310_group_project.Event;
import com.example.csci310_group_project.MainActivity;
import com.example.csci310_group_project.R;
import com.example.csci310_group_project.data.model.ImageProcessor;
import com.example.csci310_group_project.databinding.ActivityRegisterBinding;
import com.example.csci310_group_project.ui.login.LoginActivity;
import com.example.csci310_group_project.ui.register.LoggedInUserView;
import com.example.csci310_group_project.ui.register.RegisterActivity;
import com.example.csci310_group_project.ui.register.LoginFormState;
import com.example.csci310_group_project.ui.register.LoginResult;
import com.example.csci310_group_project.ui.register.LoginViewModel;
import com.example.csci310_group_project.ui.register.LoginViewModelFactory;
import com.example.csci310_group_project.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityRegisterBinding binding;
    private ImageView imageView;
    private StorageReference mSotrage;
    private static final int IMAGEID = 1;
    private Uri selected = null;
    private ImageProcessor imageProcessor;


    private Button birthdayPicker;
    private DatePickerDialog birthdayPickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        imageView = (ImageView) findViewById(R.id.imgView);

        //this version will work
        mSotrage = FirebaseStorage.getInstance().getReference("dummyImage").child("dummyProfile.png");
        try {
            final File local = File.createTempFile("profile","png");
            mSotrage.getFile(local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(local.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        initBirthdayPicker();

        //this is how to use Picasso
        /*Picasso picasso = new Picasso.Builder(RegisterActivity.this)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        // check here the exception
                        Log.d("imageError", exception.getMessage());
                    }
                })
                .build();
        picasso.load(selected).into(imageView);*/

        //try to get uri of stored image
        /*mSotrage = FirebaseStorage.getInstance().getReference("dummyImage").child("dummyProfile.png");
        mSotrage.getDownloadUrl().addOnCompleteListener(
                new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        selected = task.getResult();
                        imageView.setImageURI(selected);
                    }
                });*/

        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, IMAGEID);
            }
        });

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final EditText repasswordEditText = binding.repassword;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, new Observer<com.example.csci310_group_project.ui.register.LoginFormState>() {
            @Override
            public void onChanged(@Nullable com.example.csci310_group_project.ui.register.LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
                if (loginFormState.getRepasswordError() != null) {
                    repasswordEditText.setError(getString(loginFormState.getRepasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<com.example.csci310_group_project.ui.register.LoginResult>() {
            @Override
            public void onChanged(@Nullable com.example.csci310_group_project.ui.register.LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getExcept());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), repasswordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        repasswordEditText.addTextChangedListener(afterTextChangedListener);
        repasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), repasswordEditText.getText().toString());
                }
                return false;
            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String birthday = birthdayPicker.getText().toString();
                Log.i("bday", birthday);
                //String repassword = repasswordEditText.getText().toString();
                if (selected != null){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("users").document(username);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Toast.makeText(getApplicationContext(), "Username already used!", Toast.LENGTH_LONG).show();
                                }else {
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("username", username);
                                    user.put("password", password);
                                    user.put("birthday", birthday);
                                    user.put("registeredEvents", "");
                                    user.put("favorites", "");
                                    user.put("time", "");
                                    //Log.d("Register", "Hi");
                                    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                    //Log.d("Register1", "Here");
                                    //Toast.makeText(getApplicationContext(), "Please wait", Toast.LENGTH_LONG).show();
                                    db2.collection("users").document(username)
                                            .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "Registration Success! Please login", Toast.LENGTH_LONG).show();
                                                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Oops something is off...", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Oops something is off...", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    mSotrage = FirebaseStorage.getInstance().getReference("userImage").child(username+".png");
                    mSotrage.putFile(selected);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please upload an profile image", Toast.LENGTH_LONG).show();
                }

                /*FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("users").document(username);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Toast.makeText(getApplicationContext(), "Username already used!", Toast.LENGTH_LONG).show();
                            }else {
                                Map<String, Object> user = new HashMap<>();
                                user.put("username", username);
                                user.put("password", password);
                                user.put("registeredEvents", "");
                                user.put("favorites", "");
                                user.put("time", "");
                                //Log.d("Register", "Hi");
                                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                //Log.d("Register1", "Here");
                                //Toast.makeText(getApplicationContext(), "Please wait", Toast.LENGTH_LONG).show();
                                db2.collection("users").document(username)
                                        .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Registration Success! Please login", Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Oops something is off...", Toast.LENGTH_LONG).show();
                                            }
                                        });

                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Oops something is off...", Toast.LENGTH_LONG).show();
                        }

                    }
                });*/
                /*mSotrage = FirebaseStorage.getInstance().getReference("userImage").child(username+".png");
                if (selected == null){
                    Toast.makeText(getApplicationContext(), "Please upload an profile image", Toast.LENGTH_LONG).show();
                }else {
                    mSotrage.putFile(selected);
                }*/
            }
        });


    }

    private void updateUiWithUser(com.example.csci310_group_project.ui.register.LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(Exception errorString) {
        Toast.makeText(getApplicationContext(), errorString.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGEID && resultCode == RESULT_OK && data != null){
            selected = data.getData();
            imageView.setImageURI(selected);
        }
    }


    private String makeDateString(int day, int month, int year) {
        return month + "/" + day + "/" + year;
    }

    private void initBirthdayPicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                birthdayPicker.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        birthdayPickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        birthdayPicker = findViewById(R.id.birthdayPickerButton);
        birthdayPicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                birthdayPickerDialog.show();
            }
        });

    }
}
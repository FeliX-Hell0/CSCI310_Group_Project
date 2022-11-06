package com.example.csci310_group_project.ui.register;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csci310_group_project.ContentActivity;
import com.example.csci310_group_project.Event;
import com.example.csci310_group_project.MainActivity;
import com.example.csci310_group_project.R;
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

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

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
                //String repassword = repasswordEditText.getText().toString();

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
}
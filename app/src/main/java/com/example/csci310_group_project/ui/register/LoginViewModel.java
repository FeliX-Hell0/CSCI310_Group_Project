package com.example.csci310_group_project.ui.register;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.csci310_group_project.R;
import com.example.csci310_group_project.data.LoginRepository;
import com.example.csci310_group_project.data.Result;
import com.example.csci310_group_project.data.model.LoggedInUser;
import com.example.csci310_group_project.ui.register.LoggedInUserView;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, String s) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            Exception error = ((Result.Error)result).getError();
            loginResult.setValue(new LoginResult(R.string.login_failed, error));
        }
    }

    public void loginDataChanged(String username, String password, String repassword, String nickname) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null,null, null));
        }
        if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password,null, null));
        }
        if (!isRepasswordValid(password, repassword)){
            loginFormState.setValue(new LoginFormState(null, null ,R.string.invalid_repassword, null));
        }
        if (isNicknameValid(nickname)) {
            loginFormState.setValue(new LoginFormState(null, null ,null, R.string.invalid_nickname));
        }
        if(isUserNameValid(username) && isPasswordValid(password) && isRepasswordValid(password, repassword) && isNicknameValid(nickname)) {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isRepasswordValid(String password, String repassword) {
        if (password == null){
            return false;
        }
        if(repassword == null){
            return false;
        }
        if(password.equals(repassword)){
            return true;
        }
        return false;
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return false;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isNicknameValid(String nn) {
        return nn != null && nn.length() > 0;
    }
}
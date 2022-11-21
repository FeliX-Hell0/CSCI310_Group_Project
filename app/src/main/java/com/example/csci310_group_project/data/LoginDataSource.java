package com.example.csci310_group_project.data;

import android.util.Log;

import com.example.csci310_group_project.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private static final String TAG = "DocSnippets";

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            //System.out.println(username);


            /*
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
             */

            LoggedInUser User =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            return new Result.Success<>(User);
        } catch (Exception e) {
            Log.d("LoginExcept", "login fail");
            return new Result.Error(e);
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
package com.example.csci310_group_project.ui.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer repasswordError;
    private Integer nicknameError;
    private boolean isDataValid;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer repasswordError, @Nullable Integer nicknameError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.repasswordError = repasswordError;
        this.nicknameError = nicknameError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.repasswordError = null;
        this.nicknameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getRepasswordError() {
        return repasswordError;
    }

    @Nullable
    Integer getNicknameError() {
        return nicknameError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.javabasesetupmvvm.ui.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.javabasesetupmvvm.BR;
import com.javabasesetupmvvm.R;
import com.javabasesetupmvvm.databinding.ActivityLoginBinding;
import com.javabasesetupmvvm.di.component.ActivityComponent;
import com.javabasesetupmvvm.ui.base.BaseActivity;
import com.javabasesetupmvvm.ui.main.MainActivity;

import java.util.Objects;
import java.util.concurrent.Callable;

import timber.log.Timber;

import static com.javabasesetupmvvm.utils.AppConstants.RC_GOOGLE_SIGN_IN;


/**
 * Created by Samir on 04/05/2021.
 */

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel>
        implements LoginNavigator {

    private ActivityLoginBinding mActivityLoginBinding;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private LoginManager loginManager;

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void login() {
        String email = Objects.requireNonNull(mActivityLoginBinding.etEmail.getText()).toString();
        String password = Objects.requireNonNull(mActivityLoginBinding.etPassword.getText()).toString();
        if (mViewModel.isEmailAndPasswordValid(email, password)) {
            hideKeyboard();
            mViewModel.login(email, password);
        } else {
            Toast.makeText(this, getString(R.string.invalid_email_password), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.newIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void googleLogin() {
        hideKeyboard();
        mViewModel.googleLogin(this, googleSignInClient);
    }

    @Override
    public void onInstaLogin() {
        hideKeyboard();
        mViewModel.instaLogin(this);
    }

    @Override
    public void fbLogin() {
        hideKeyboard();
        mViewModel.fbLogin(this, mAuth, mCallbackManager, loginManager);
    }

    private void initGoogleSignInClient() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void initFacebookClient() {
        mCallbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding = getViewDataBinding();
        mViewModel.setNavigator(this);
        initGoogleSignInClient();
        initFacebookClient();
//        checkForInstagramData();

    }


    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }


    private void checkForInstagramData() {
        final Uri data = this.getIntent().getData();
        if(data != null && data.getScheme().equals("sociallogin") && data.getFragment() != null) {
            final String accessToken = data.getFragment().replaceFirst("access_token=", "");
            if (accessToken != null) {
                handleSignInResult(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        // Do nothing, just throw the access token away.
                        return null;
                    }
                });
            } else {
                handleSignInResult(null);
            }
        }
    }


    private void handleSignInResult(Callable<Void> logout) {
        if(logout == null) {
            /* Login error */
            Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
        } else {
            /* Login success */
         //   Application.getInstance().setLogoutCallable(logout);
//            startActivity(new Intent(this, LoggedInActivity.class));

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    mViewModel.firebaseWithGoogle(this, mAuth, googleSignInAccount);
                }
            } catch (ApiException e) {
                Timber.e(e);
            }
        }
    }
}

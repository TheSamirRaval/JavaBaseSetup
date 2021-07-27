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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.javabasesetupmvvm.R;
import com.javabasesetupmvvm.data.DataManager;
import com.javabasesetupmvvm.data.model.api.LoginRequest;
import com.javabasesetupmvvm.data.remote.ApiEndPoint;
import com.javabasesetupmvvm.ui.base.BaseViewModel;
import com.javabasesetupmvvm.utils.CommonUtils;
import com.javabasesetupmvvm.utils.rx.SchedulerProvider;
import com.javabasesetupmvvm.widget.AuthenticationDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.javabasesetupmvvm.utils.AppConstants.RC_GOOGLE_SIGN_IN;

/**
 * Created by Samir on 04/05/2021.
 */

public class LoginViewModel extends BaseViewModel<LoginNavigator> {

    public LoginViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

    public boolean isEmailAndPasswordValid(String email, String password) {
        // validate email and password
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (!CommonUtils.isEmailValid(email)) {
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }

    public void login(String email, String password) {
        setIsLoading(true);
        getCompositeDisposable().add(getDataManager()
                .doServerLoginApiCall(new LoginRequest.ServerLoginRequest(email, password))
                .doOnSuccess(response -> getDataManager()
                        .updateUserInfo(
                                response.getAccessToken(),
                                response.getUserId(),
                                DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER,
                                response.getUserName(),
                                response.getUserEmail(),
                                response.getGoogleProfilePicUrl()))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    getNavigator().openMainActivity();
                }, throwable -> {
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
    }

    public void onFbLoginClick() {
        getNavigator().fbLogin();
      /*  setIsLoading(true);
        getCompositeDisposable().add(getDataManager()
                .doFacebookLoginApiCall(new LoginRequest.FacebookLoginRequest("test3", "test4"))
                .doOnSuccess(response -> getDataManager()
                        .updateUserInfo(
                                response.getAccessToken(),
                                response.getUserId(),
                                DataManager.LoggedInMode.LOGGED_IN_MODE_FB,
                                response.getUserName(),
                                response.getUserEmail(),
                                response.getGoogleProfilePicUrl()))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(response -> {
                    setIsLoading(false);
                    getNavigator().openMainActivity();
                }, throwable -> {
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));*/
    }

    public void onGoogleLoginClick() {
        getNavigator().googleLogin();
//        setIsLoading(true);
//        getCompositeDisposable().add(getDataManager()
//                .doGoogleLoginApiCall(new LoginRequest.GoogleLoginRequest("test1", "test1"))
//                .doOnSuccess(response -> getDataManager()
//                        .updateUserInfo(
//                                response.getAccessToken(),
//                                response.getUserId(),
//                                DataManager.LoggedInMode.LOGGED_IN_MODE_GOOGLE,
//                                response.getUserName(),
//                                response.getUserEmail(),
//                                response.getGoogleProfilePicUrl()))
//                .subscribeOn(getSchedulerProvider().io())
//                .observeOn(getSchedulerProvider().ui())
//                .subscribe(response -> {
//                    setIsLoading(false);
//                    getNavigator().openMainActivity();
//                }, throwable -> {
//                    setIsLoading(false);
//                    getNavigator().handleError(throwable);
//                }));
    }


    public void onInstaLoginClick() {
        getNavigator().onInstaLogin();

    }

    public void instaLogin(Activity activity) {
        AuthenticationDialog authenticationDialog = new AuthenticationDialog(activity, code -> getUserInfoByAccessToken(activity, code));
        authenticationDialog.setCancelable(true);
        authenticationDialog.show();
    }

    private void getUserInfoByAccessToken(Activity activity, String code) {
        setIsLoading(true);
        Map<String, String> params = new HashMap<>();
        params.put("client_id", activity.getString(R.string.client_id));
        params.put("client_secret", activity.getString(R.string.client_secret));
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", activity.getString(R.string.redirect_url));
        params.put("code", code);

        getCompositeDisposable().add(getDataManager()
                .getInstagramAccessTokenApiCall(params)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(s -> {
                    String access_token = s.get("access_token").getAsString();
                    String user_id = s.get("user_id").getAsString();
                    ApiEndPoint.ENDPOINT_INSTAGRAM_LOGIN = activity.getResources().getString(R.string.get_user_info_url) + access_token;
                    setIsLoading(true);
                    getCompositeDisposable().add(getDataManager()
                            .doInstagramLoginApiCall()
                            .subscribeOn(getSchedulerProvider().io())
                            .observeOn(getSchedulerProvider().ui())
                            .subscribe(_s -> {
                                setIsLoading(false);
                                String _id = _s.get("id").getAsString();
                                String username = _s.get("username").getAsString();
                                Timber.d("getUserInfoByAccessToken: " + username + "\t\t:::\t\t" + _id);
                            }, throwable -> {
                                setIsLoading(false);
                                getNavigator().handleError(throwable);
                            }));
                    Timber.d("getUserInfoByAccessToken: %s", s);
                }, throwable -> {
                    setIsLoading(false);
                    getNavigator().handleError(throwable);
                }));
        /**/


        //      new RequestInstagramAPI().execute(activity.getResources().getString(R.string.get_user_info_url) + auth_token);
    }

    public void onServerLoginClick() {
        getNavigator().login();
    }

    public void googleLogin(Activity activity, GoogleSignInClient googleSignInClient) {
        setIsLoading(true);
        FirebaseAuth.getInstance().signOut();
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    public void firebaseWithGoogle(Activity activity, FirebaseAuth mAuth, GoogleSignInAccount googleSignInAccount) {
        getGoogleAuthCredential(googleSignInAccount);
        firebaseAuthWithGoogle(activity, mAuth, googleSignInAccount.getIdToken());
    }

    private void firebaseAuthWithGoogle(Activity activity, FirebaseAuth mAuth, String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    setIsLoading(false);

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        assert user != null;
                        Timber.d(user.getEmail() + "\t" + user.getUid() + "\t" + user.getDisplayName()
                                + user.getPhoneNumber() + "\t" + user.getProviderId());
                        getNavigator().openMainActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.w(task.getException(), "signInWithCredential:failure");
//                            updateUI(null);
                        getNavigator().handleError(task.getException());
                    }
                });
    }

    private void getGoogleAuthCredential(GoogleSignInAccount googleSignInAccount) {
        String googleTokenId = googleSignInAccount.getIdToken();
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
        signInWithGoogleAuthCredential(googleAuthCredential);
    }

    private void signInWithGoogleAuthCredential(AuthCredential googleAuthCredential) {
        // TODO: 06/05/21 register user in server or firebase
        Timber.d(googleAuthCredential.toString());
//
    }

    public void fbLogin(Activity activity, FirebaseAuth mAuth, CallbackManager mCallbackManager, LoginManager loginManager) {
        setIsLoading(true);
        FirebaseAuth.getInstance().signOut();
        loginManager.logInWithReadPermissions(activity, Arrays.asList(
                "email",
                "public_profile",
                "user_birthday"));
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Timber.d("facebook:onSuccess:%s", loginResult);
                handleFacebookAccessToken(activity, mAuth, loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                setIsLoading(false);
                Timber.d("facebook:onCancel");
                getNavigator().handleError(null);
            }

            @Override
            public void onError(FacebookException error) {
                setIsLoading(false);
                Timber.d(error, "facebook:onError");
                getNavigator().handleError(error);

            }
        });
    }

    private void handleFacebookAccessToken(Activity activity, FirebaseAuth mAuth, AccessToken token) {
        Timber.d("handleFacebookAccessToken:%s", token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    setIsLoading(false);
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.d("signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        assert user != null;
                        Timber.d(user.toString());
//                      updateUI(user);
                        getNavigator().openMainActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.w(task.getException(), "signInWithCredential:failure");
                        Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        getNavigator().handleError(task.getException());
                    }
                });
    }

    private class RequestInstagramAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String token = params[0];

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(token);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    if (jsonData.has("id")) {
                        //сохранение данных пользователя
//                        appPreferences.putString(AppPreferences.USER_ID, jsonData.getString("id"));
//                        appPreferences.putString(AppPreferences.USER_NAME, jsonData.getString("username"));
//                        appPreferences.putString(AppPreferences.PROFILE_PIC, jsonData.getString("profile_picture"));

                        //TODO: сохранить еще данные
//                        login();
                        Timber.d("onPostExecute: %s", jsonObject.toString());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {

                Toast toast = Toast.makeText(getApplicationContext(), "Ошибка входа!", Toast.LENGTH_LONG);
                toast.show();
            }
        }


    }
}

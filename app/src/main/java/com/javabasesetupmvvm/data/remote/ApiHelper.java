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

package com.javabasesetupmvvm.data.remote;


import com.google.gson.JsonObject;
import com.javabasesetupmvvm.data.model.api.BlogResponse;
import com.javabasesetupmvvm.data.model.api.LoginRequest;
import com.javabasesetupmvvm.data.model.api.LoginResponse;
import com.javabasesetupmvvm.data.model.api.LogoutResponse;
import com.javabasesetupmvvm.data.model.api.OpenSourceResponse;

import java.util.Map;

import io.reactivex.Single;

/**
 * Created by Samir on 04/05/2021.
 */

public interface ApiHelper {

    Single<LoginResponse> doFacebookLoginApiCall(LoginRequest.FacebookLoginRequest request);

    Single<LoginResponse> doGoogleLoginApiCall(LoginRequest.GoogleLoginRequest request);

    Single<JsonObject> getInstagramAccessTokenApiCall(Map<String, String> requestParams);

    Single<JsonObject> doInstagramLoginApiCall();

    Single<LogoutResponse> doLogoutApiCall();

    Single<LoginResponse> doServerLoginApiCall(LoginRequest.ServerLoginRequest request);

    ApiHeader getApiHeader();

    Single<BlogResponse> getBlogApiCall();

    Single<OpenSourceResponse> getOpenSourceApiCall();
}

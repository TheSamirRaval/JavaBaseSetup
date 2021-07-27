package com.javabasesetupmvvm.callbacks;

/**
 * Created by SAM on 20/05/21.
 */
public interface AuthenticationListener {
    void onTokenReceived(String auth_token);
}

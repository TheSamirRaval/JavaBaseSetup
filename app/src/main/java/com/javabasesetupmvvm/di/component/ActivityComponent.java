package com.javabasesetupmvvm.di.component;


import com.javabasesetupmvvm.di.module.ActivityModule;
import com.javabasesetupmvvm.di.scope.ActivityScope;
import com.javabasesetupmvvm.ui.feed.FeedActivity;
import com.javabasesetupmvvm.ui.login.LoginActivity;
import com.javabasesetupmvvm.ui.main.MainActivity;
import com.javabasesetupmvvm.ui.splash.SplashActivity;

import dagger.Component;

/*
 * Author: rotbolt
 */

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {

    void inject(FeedActivity activity);

    void inject(LoginActivity activity);

    void inject(MainActivity activity);

    void inject(SplashActivity splashActivity);

}

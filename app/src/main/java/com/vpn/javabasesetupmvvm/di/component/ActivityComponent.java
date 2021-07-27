package com.vpn.javabasesetupmvvm.di.component;


import com.vpn.javabasesetupmvvm.di.module.ActivityModule;
import com.vpn.javabasesetupmvvm.di.scope.ActivityScope;
import com.vpn.javabasesetupmvvm.ui.feed.FeedActivity;
import com.vpn.javabasesetupmvvm.ui.login.LoginActivity;
import com.vpn.javabasesetupmvvm.ui.main.MainActivity;
import com.vpn.javabasesetupmvvm.ui.splash.SplashActivity;

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

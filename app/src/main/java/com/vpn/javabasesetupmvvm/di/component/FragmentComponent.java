package com.vpn.javabasesetupmvvm.di.component;


import com.vpn.javabasesetupmvvm.di.module.FragmentModule;
import com.vpn.javabasesetupmvvm.di.scope.FragmentScope;
import com.vpn.javabasesetupmvvm.ui.about.AboutFragment;
import com.vpn.javabasesetupmvvm.ui.feed.blogs.BlogFragment;
import com.vpn.javabasesetupmvvm.ui.feed.opensource.OpenSourceFragment;

import dagger.Component;

/*
 * Author: rotbolt
 */

@FragmentScope
@Component(modules = FragmentModule.class, dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(BlogFragment fragment);

    void inject(OpenSourceFragment fragment);

    void inject(AboutFragment fragment);
}

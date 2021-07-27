package com.javabasesetupmvvm.di.component;


import com.javabasesetupmvvm.di.module.FragmentModule;
import com.javabasesetupmvvm.di.scope.FragmentScope;
import com.javabasesetupmvvm.ui.about.AboutFragment;
import com.javabasesetupmvvm.ui.feed.blogs.BlogFragment;
import com.javabasesetupmvvm.ui.feed.opensource.OpenSourceFragment;

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

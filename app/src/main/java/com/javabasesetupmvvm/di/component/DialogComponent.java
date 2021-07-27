package com.javabasesetupmvvm.di.component;


import com.javabasesetupmvvm.di.module.DialogModule;
import com.javabasesetupmvvm.di.scope.DialogScope;
import com.javabasesetupmvvm.ui.main.rating.RateUsDialog;

import dagger.Component;

/*
 * Author: rotbolt
 */

@DialogScope
@Component(modules = DialogModule.class, dependencies = AppComponent.class)
public interface DialogComponent {

    void inject(RateUsDialog dialog);

}

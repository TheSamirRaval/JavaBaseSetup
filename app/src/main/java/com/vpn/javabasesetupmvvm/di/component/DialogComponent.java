package com.vpn.javabasesetupmvvm.di.component;


import com.vpn.javabasesetupmvvm.di.module.DialogModule;
import com.vpn.javabasesetupmvvm.di.scope.DialogScope;
import com.vpn.javabasesetupmvvm.ui.main.rating.RateUsDialog;

import dagger.Component;

/*
 * Author: rotbolt
 */

@DialogScope
@Component(modules = DialogModule.class, dependencies = AppComponent.class)
public interface DialogComponent {

    void inject(RateUsDialog dialog);

}

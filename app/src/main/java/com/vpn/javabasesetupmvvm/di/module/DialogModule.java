package com.vpn.javabasesetupmvvm.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import com.vpn.javabasesetupmvvm.ViewModelProviderFactory;
import com.vpn.javabasesetupmvvm.data.DataManager;
import com.vpn.javabasesetupmvvm.ui.base.BaseDialog;
import com.vpn.javabasesetupmvvm.ui.main.rating.RateUsViewModel;
import com.vpn.javabasesetupmvvm.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/*
 * Author: rotbolt
 */

@Module
public class DialogModule {

    private BaseDialog dialog;

    public DialogModule(BaseDialog dialog){
        this.dialog = dialog;
    }

    @Provides
    RateUsViewModel provideRateUsViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<RateUsViewModel> supplier = () -> new RateUsViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<RateUsViewModel> factory = new ViewModelProviderFactory<>(RateUsViewModel.class, supplier);
        return new ViewModelProvider(dialog.requireActivity(), factory).get(RateUsViewModel.class);
    }

}

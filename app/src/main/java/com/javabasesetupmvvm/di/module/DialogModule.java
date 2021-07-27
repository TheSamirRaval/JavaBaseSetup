package com.javabasesetupmvvm.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import com.javabasesetupmvvm.ViewModelProviderFactory;
import com.javabasesetupmvvm.data.DataManager;
import com.javabasesetupmvvm.ui.base.BaseDialog;
import com.javabasesetupmvvm.ui.main.rating.RateUsViewModel;
import com.javabasesetupmvvm.utils.rx.SchedulerProvider;

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

package com.vpn.javabasesetupmvvm.di.module;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vpn.javabasesetupmvvm.ViewModelProviderFactory;
import com.vpn.javabasesetupmvvm.data.DataManager;
import com.vpn.javabasesetupmvvm.ui.about.AboutViewModel;
import com.vpn.javabasesetupmvvm.ui.base.BaseFragment;
import com.vpn.javabasesetupmvvm.ui.feed.blogs.BlogAdapter;
import com.vpn.javabasesetupmvvm.ui.feed.blogs.BlogViewModel;
import com.vpn.javabasesetupmvvm.ui.feed.opensource.OpenSourceAdapter;
import com.vpn.javabasesetupmvvm.ui.feed.opensource.OpenSourceViewModel;
import com.vpn.javabasesetupmvvm.utils.rx.SchedulerProvider;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/*
 * Author: rotbolt
 */

@Module
public class FragmentModule {

    private BaseFragment<?, ?> fragment;

    public FragmentModule(BaseFragment<?, ?> fragment) {
        this.fragment = fragment;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(fragment.getActivity());
    }

    @Provides
    AboutViewModel provideAboutViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<AboutViewModel> supplier = () -> new AboutViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<AboutViewModel> factory = new ViewModelProviderFactory<>(AboutViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(AboutViewModel.class);
    }

    @Provides
    BlogAdapter provideBlogAdapter() {
        return new BlogAdapter(new ArrayList<>());
    }


    @Provides
    BlogViewModel provideBlogViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<BlogViewModel> supplier = () -> new BlogViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<BlogViewModel> factory = new ViewModelProviderFactory<>(BlogViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(BlogViewModel.class);
    }

    @Provides
    OpenSourceAdapter provideOpenSourceAdapter() {
        return new OpenSourceAdapter();
    }

    @Provides
    OpenSourceViewModel provideOpenSourceViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        Supplier<OpenSourceViewModel> supplier = () -> new OpenSourceViewModel(dataManager, schedulerProvider);
        ViewModelProviderFactory<OpenSourceViewModel> factory = new ViewModelProviderFactory<>(OpenSourceViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(OpenSourceViewModel.class);
    }
}

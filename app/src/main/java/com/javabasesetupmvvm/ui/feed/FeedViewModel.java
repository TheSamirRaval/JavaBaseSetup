package com.javabasesetupmvvm.ui.feed;


import com.javabasesetupmvvm.data.DataManager;
import com.javabasesetupmvvm.ui.base.BaseViewModel;
import com.javabasesetupmvvm.utils.rx.SchedulerProvider;

/**
 * Created by Samir on 04/05/20.
 */

public class FeedViewModel extends BaseViewModel {

    public FeedViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

}

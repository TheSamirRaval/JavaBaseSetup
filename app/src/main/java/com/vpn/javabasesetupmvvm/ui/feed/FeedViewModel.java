package com.vpn.javabasesetupmvvm.ui.feed;


import com.vpn.javabasesetupmvvm.data.DataManager;
import com.vpn.javabasesetupmvvm.ui.base.BaseViewModel;
import com.vpn.javabasesetupmvvm.utils.rx.SchedulerProvider;

/**
 * Created by Samir on 04/05/20.
 */

public class FeedViewModel extends BaseViewModel {

    public FeedViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
    }

}

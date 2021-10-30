package com.example.ft_hangouts;

public class ContactManager extends android.app.Application  {
    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationLifecycleHandler handler = new ApplicationLifecycleHandler();
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);
    }
}
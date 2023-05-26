package com.example.homelooker.ui.ajustes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AjustesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AjustesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ajustes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.example.baigiamasisdarbas.dbControllers;

public interface OnGetDataListener<T> {
    void onSuccess(T data);

    void onFailure(Exception e);

}

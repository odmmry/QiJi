package com.example.myapplication.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MediaPath {

    public MediaPath(@NonNull String path, @Nullable String mimeType) {
        this.path = path;
        this.mimeType = mimeType;
    }

    final String path;
    final String mimeType;

    public @NonNull String getPath() {
        return path;
    }

    public @Nullable String getMimeType() {
        return mimeType;
    }

    @Override
    public String toString() {
        return "{" +
                "path='" + path + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }
}

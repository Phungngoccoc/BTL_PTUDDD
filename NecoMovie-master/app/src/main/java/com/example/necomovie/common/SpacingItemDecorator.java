package com.example.necomovie.common;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingItemDecorator extends RecyclerView.ItemDecoration {
    final private int verticalSpacing;
    final private int horizontalSpacing;

    public SpacingItemDecorator(int verticalSpacing, int horizontalSpacing) {
        this.verticalSpacing = verticalSpacing;
        this.horizontalSpacing = horizontalSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = verticalSpacing;
        outRect.left = horizontalSpacing;
        outRect.top = 10;
    }
}

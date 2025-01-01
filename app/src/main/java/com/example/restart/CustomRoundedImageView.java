package com.example.restart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class CustomRoundedImageView extends AppCompatImageView {
    private float cornerRadius = 100f; // 모서리 둥글기 반경(dp)

    public CustomRoundedImageView(Context context) {
        super(context);
    }

    public CustomRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 모서리 둥글게 자르기
        Path path = new Path();
        float[] radii = {cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                cornerRadius, cornerRadius, cornerRadius, cornerRadius};
        path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), radii, Path.Direction.CW);
        canvas.clipPath(path);

        super.onDraw(canvas);
    }

    public void setCornerRadius(float radius) {
        this.cornerRadius = radius;
        invalidate();
    }
}

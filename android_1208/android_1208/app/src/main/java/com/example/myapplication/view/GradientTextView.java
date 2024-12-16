package com.example.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.myapplication.R;

public class GradientTextView extends AppCompatTextView {

    private int startColor;
    private int endColor;
    private float gradientAngle;

    public GradientTextView(Context context) {
        super(context);
        init(null);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs!= null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.GradientTextView);
            startColor = typedArray.getColor(R.styleable.GradientTextView_startColor, getResources().getColor(android.R.color.holo_blue_dark));
            endColor = typedArray.getColor(R.styleable.GradientTextView_endColor, getResources().getColor(android.R.color.holo_red_dark));
            gradientAngle = typedArray.getFloat(R.styleable.GradientTextView_gradientAngle, 0f);
            typedArray.recycle();
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        TextPaint paint = getPaint();
        LinearGradient linearGradient;
        if (gradientAngle == 0f) {
            linearGradient = new LinearGradient(0, 0, width, height, startColor, endColor, Shader.TileMode.CLAMP);
        } else {
            float x1 = (float) (width * Math.cos(gradientAngle));
            float y1 = (float) (height * Math.sin(gradientAngle));
            float x2 = (float) (width * (1 - Math.cos(gradientAngle)));
            float y2 = (float) (height * (1 - Math.sin(gradientAngle)));
            linearGradient = new LinearGradient(x1, y1, x2, y2, startColor, endColor, Shader.TileMode.CLAMP);
        }
        paint.setShader(linearGradient);
        super.onDraw(canvas);
    }
}

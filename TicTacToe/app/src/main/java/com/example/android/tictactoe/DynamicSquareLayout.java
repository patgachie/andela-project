package com.example.android.tictactoe;

import android.content.Context;
import android.util.AttributeSet;

/**class to create a custom textview that is always square
 *
 */
public class DynamicSquareLayout  extends android.support.v7.widget.AppCompatTextView {
 
    public DynamicSquareLayout(Context context) {
        super(context);
    }
 
 
    public DynamicSquareLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    public DynamicSquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
 
// here we are returning the width in place of height, so width = height
    @Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size, size);
    }
 
 
}
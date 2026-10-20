package com.example.casino;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class FullScreenVideoView extends VideoView {
    private int videoWidth = 0;
    private int videoHeight = 0;

    public FullScreenVideoView(Context context) {

        super(context);
    }
    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setVideoSize(int width, int height) {
        videoWidth = width;
        videoHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewW = MeasureSpec.getSize(widthMeasureSpec);
        int viewH = MeasureSpec.getSize(heightMeasureSpec);

        if (videoWidth > 0 && videoHeight > 0) {
            float scale = Math.max((float) viewW / videoWidth, (float) viewH / videoHeight);
            setMeasuredDimension((int) (videoWidth * scale), (int) (videoHeight * scale));
        } else {
            setMeasuredDimension(viewW, viewH);
        }
    }


}

package com.cgp.saral.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import com.cgp.saral.R;
import com.cgp.saral.myutils.Constants;
import com.cgp.saral.myutils.Utils;

public class CompassView extends View {

    private Paint markerPaint;
    private Paint circlePaint;
    private Paint textPaint;
    private String north, south, east, west;
    private int textHeight;
    private String dirString;
    private float bearing;


    String fab;
    String unFab;

    String mr = "";
    String cr = "";
    String hl = "";
    String w = "";
    String e = "";

    boolean flag = false;
    boolean flag1 = false;
    boolean mr_flag = false;
    boolean health_flag = false;
    boolean wealth_flag = false;
    boolean edu_flag = false;
    boolean career_flag = false;

    private Matrix matrix; // to manage rotation of the compass view
    // private Bitmap bitmap;

    public Handler uiThread = new Handler();

    public void setBearing(float _bearing) {
        try{
        bearing = _bearing;
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    public float getBearing() {
        return bearing;
    }

    public CompassView(Context context) {
        super(context);
        try{
        // this.flag = flg;
        initCompassView();
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        try{
        initCompassView();
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try{
        initCompassView();
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    private void initCompassView() {
try{
        String strF = Constants.GLOBAL_U_LUCK_CHART;
        Log.e("initCompass", strF);
        if (strF!= null && !strF.isEmpty() && !strF.equals("null")) {
            String[] str = Utils.chartAnalysis(strF);
            if(str!= null && str.length>1) {
                fab = str[0].trim();
                unFab = str[1].trim();
            }

            char crW = fab.charAt(0);
            char crC = fab.charAt(0);
            char crM = fab.charAt(2);
            char crE = fab.charAt(3);
            char crH = fab.charAt(1);

            mr = String.valueOf(crM);
            w = String.valueOf(crW);
            hl = String.valueOf(crH);
            e = String.valueOf(crE);
            cr = String.valueOf(crC);

        }

       /* Marriage – 3rd Position
        Career -  1st Direction
        Education – 4th Direction
        Wealth – 1st Direction
        Health- 2nd Direction*/

        setFocusable(true);

        Resources resources = this.getResources();
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(resources.getColor(R.color.transparent));
        circlePaint.setStrokeWidth(3);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);


        north = resources.getString(R.string.north);
        south = resources.getString(R.string.south);
        east = resources.getString(R.string.east);
        west = resources.getString(R.string.west);


        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // textPaint.setColor(resources.getColor(R.color.blue));
        textPaint.setTextSize((float) 50);// Setting here to display the font size.

        textPaint.setStyle(Paint.Style.STROKE);
        // mStrokePaint.setColor(mStrokeColor);

        textHeight = (int) textPaint.measureText("yY");
        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(resources.getColor(R.color.blue));


        matrix = new Matrix();
        // create bitmap for compass icon
      /*  bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.compass_bg);*/
        setWillNotDraw(false);
}catch (Throwable t){
    Log.e("CompassView",t.getMessage(),t);
}
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try{
        // Compass is a filled as much space circle, to set the measurement by setting the minimum boundary, height or width.
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);

        int d = Math.min(measuredWidth, measuredHeight);
        setMeasuredDimension(d, d);
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    private int measure(int measureSpec) {
        int result = 0;
        try{

        // The decoding of the description of the measurement
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) {
            // If you do not specify a default boundaries, the return value is 200
            result = 200;
        } else {
            // Because you want to fill the space available, so always returns the entire available boundary
            result = specSize;
        }
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try{
        // Find the control center, and the minimum edge length as a stored radius compass.
        int mMeasuredWidth = getMeasuredWidth();
        int mMeasuredHeight = getMeasuredHeight();
        int px = mMeasuredWidth / 2;
        int py = mMeasuredHeight / 2;
        int radius = Math.min(px, py);
        // Use the drawCircle method to draw the compass character boundaries, and for its Beijing coloring.

        canvas.drawCircle(px, py, radius - 60, circlePaint);


        Log.e("Radius ", " ---" + radius);

        Log.e("Radius ", " -px -->" + px + "-- py--> " + py);


        canvas.save();
        canvas.rotate(-bearing, px, py);
        canvas.restore();

        for (int i = 1; i < 9; i++) {
            //int is = i + 1;
            String temp = (i + "");
            String angle = String.valueOf(i + 1);


            float angleTextWidth = textPaint.measureText(angle);

            int angleTextX = (int) (px - angleTextWidth / 2);
            int angleTextY = py - radius + (int) angleTextWidth+8;//+10

            /*canvas.rotate(-bearing, px, py);
            if (i == 0) {
                canvas.rotate(45, px, py);
            } else {
                canvas.rotate(47, px, py);
            }*/

            if (fab.contains(temp) && flag) {
                Log.e("Loop Count fav", "--" + temp + " Fav ----> " + fab);
                textPaint.setColor(getContext().getResources().getColor(R.color.green));

                int k = fab.indexOf(temp);
                Log.e("Favorite index", "--" + temp + " ----> " +k+1);
                {
                    canvas.drawText(String.valueOf(k+1), angleTextX, angleTextY, textPaint);
                }

               // canvas.restore();

            }
            if (fab.contains(temp) && mr_flag && mr.contains(temp)) {
                Log.e("Loop Count fav", "--" + temp + " ----> " + angleTextX + " --->" + angleTextY);
                textPaint.setColor(getContext().getResources().getColor(R.color.green));
               // canvas.drawText(temp, angleTextX, angleTextY, textPaint);
                int k = fab.indexOf(temp);
                {
                    canvas.drawText(String.valueOf(k+1), angleTextX, angleTextY, textPaint);
                }

            }
            if (fab.contains(temp) && health_flag && hl.contains(temp)) {
                Log.e("Loop Count fav", "--" + temp + " ----> " + angleTextX + " --->" + angleTextY);
                textPaint.setColor(getContext().getResources().getColor(R.color.green));
                ///canvas.drawText(temp, angleTextX, angleTextY, textPaint);
                int k = fab.indexOf(temp);
                {
                    canvas.drawText(String.valueOf(k+1), angleTextX, angleTextY, textPaint);
                }

            }
            if (fab.contains(temp) && wealth_flag && w.contains(temp)) {
                Log.e("Loop Count fav", "--" + temp + " ----> " + angleTextX + " --->" + angleTextY);
                textPaint.setColor(getContext().getResources().getColor(R.color.green));
               // canvas.drawText(temp, angleTextX, angleTextY, textPaint);
                int k = fab.indexOf(temp);
                {
                    canvas.drawText(String.valueOf(k+1), angleTextX, angleTextY, textPaint);
                }

            }
            if (fab.contains(temp) && career_flag && cr.contains(temp)) {
                Log.e("Loop Count fav", "--" + temp + " ----> " + angleTextX + " --->" + angleTextY);
                textPaint.setColor(getContext().getResources().getColor(R.color.green));
                //canvas.drawText(temp, angleTextX, angleTextY, textPaint);
                int k = fab.indexOf(temp);
                {
                    canvas.drawText(String.valueOf(k+1), angleTextX, angleTextY, textPaint);
                }

            }
            if (fab.contains(temp) && edu_flag && e.contains(temp)) {
                Log.e("Loop Count fav", "--" + temp + " ----> " + angleTextX + " --->" + angleTextY);
                textPaint.setColor(getContext().getResources().getColor(R.color.green));
               // canvas.drawText(temp, angleTextX, angleTextY, textPaint);
                int k = fab.indexOf(temp);
                {
                    canvas.drawText(String.valueOf(k + 1), angleTextX, angleTextY, textPaint);
                }

            }
            if (unFab.contains(temp) && flag1) {
                Log.e("Loop Count unfav", "--" + temp + " ----> " +unFab);
                textPaint.setColor(getContext().getResources().getColor(R.color.red));

                int k = unFab.indexOf(temp);
                Log.e("Unfavorable index", "--" + temp + " ----> " +k);
                {
                    canvas.drawText(String.valueOf(k+1), angleTextX, angleTextY, textPaint);
                }

                // }
                //  canvas.drawText(temp, angleTextX, angleTextY, textPaint);
              //  canvas.restore();
            }
            canvas.rotate(45, px, py);
            canvas.save();


        }

        canvas.restore();
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }


    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.dispatchPopulateAccessibilityEvent(event);
        try{
        if (isShown()) {
            String bearingStr = String.valueOf(bearing);
            if (bearingStr.length() > AccessibilityEvent.MAX_TEXT_LENGTH)
                bearingStr = bearingStr.substring(0,
                        AccessibilityEvent.MAX_TEXT_LENGTH);

            event.getText().add(bearingStr);
            return true;
        }
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
        return false;
    }

    public void update(int id) {
try{
        uiThread.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
}catch (Throwable t){
    Log.e("CompassView",t.getMessage(),t);
}
    }

    public void doToggle(int flag) {
        try{
        if (flag == Constants.FAV) {
            this.flag = true;


        }
        if (flag == Constants.UNFAV) {
            this.flag1 = true;
        }
        if (flag == Constants.MR) {
            this.mr_flag = true;

        }
        if (flag == Constants.WEALTH) {
            this.wealth_flag = true;
        }
        if (flag == Constants.CAREER) {
            this.career_flag = true;
        }
        if (flag == Constants.EDUCATION) {
            this.edu_flag = true;
        }
        if (flag == Constants.HEALTH) {
            this.health_flag = true;
        }


        this.invalidate();
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }

    public void doUnToggle(int flag) {
        try{
        if (flag == Constants.FAV) {
            this.flag = false;
        }
        if (flag == Constants.UNFAV) {
            this.flag1 = false;
        }
        if (flag == Constants.RESET) {
            this.mr_flag = false;
            this.health_flag = false;
            this.edu_flag = false;
            this.wealth_flag = false;
            this.career_flag = false;
            this.flag = false;
            this.flag1 = false;
        }

        this.invalidate();
        }catch (Throwable t){
            Log.e("CompassView",t.getMessage(),t);
        }
    }
}
package com.example.paint4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class PaintCanvas extends View implements View.OnTouchListener{

    private Paint paint = new Paint();
    ArrayList<Integer> allPaints = new ArrayList<Integer>();
    private int currentPathColor = 0;

    private Path currentPath = new Path();
    ArrayList<Path> allPaths = new ArrayList<Path>();
    private int backGroundColor = Color.WHITE;
    private GestureDetector mGestureDetector;


    public PaintCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        setBackgroundColor(backGroundColor);
        initPaint();

    }

    public PaintCanvas(Context context, AttributeSet attrs, GestureDetector mGestureDetector) {
        super(context, attrs);
        this.mGestureDetector = mGestureDetector;
        setOnTouchListener(this);
        setBackgroundColor(backGroundColor);
        initPaint();
    }

    // Draws all the different strokes
    @Override
    protected void onDraw(Canvas canvas) {
        int i = 0;
        for(Path currentPath: allPaths){
            Paint currentPaint = new Paint();
            currentPaint.setAntiAlias(true);
            currentPaint.setStrokeWidth(20f);
            if(i != 0){
                currentPaint.setColor(allPaints.get(i)); // cor atual
            }else{
                currentPaint.setColor(Color.BLACK);
            }
            currentPaint.setStyle(Paint.Style.STROKE);
            currentPaint.setStrokeJoin(Paint.Join.ROUND);
            i++;
            canvas.drawPath(currentPath, currentPaint);// draws the path with the paint





        }
    }

    @Override
    public boolean performClick(){
        return super.performClick();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return false; // let the event go to the rest of the listeners
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //When you press down with the finger
                currentPath.moveTo(eventX, eventY);// updates the path initial point
                return true;
            case MotionEvent.ACTION_MOVE: //When you make the line
                currentPath.lineTo(eventX, eventY);// makes a line to the point each time this event is fired
                break;
            case MotionEvent.ACTION_UP:// when you lift your finger IT ADDS TO THE LIST OF STROKES
                allPaths.add(currentPath);
                currentPath = new Path();
                allPaints.add(paint.getColor()); // Se for a cor igual queremos adicionar na mesma para o for no onDraw() nao morrer
                performClick();
                Log.d("LogDaTuga", "allPaints = " + allPaints.toString());
                Log.d("LogDaTuga", "allPaths = " + allPaths.toString());

                break;
            default:
                return false;
        }

        // Schedules a repaint.
        invalidate();
        return true;
    }

    public void changeBackground(){
        Random r = new Random();
        backGroundColor = Color.rgb(r.nextInt(256), r.nextInt(256), r.nextInt(256));
        setBackgroundColor(backGroundColor);
    }

    public void changeBackgroundBlue(){
        Random r = new Random();
        backGroundColor = Color.BLUE;
        setBackgroundColor(backGroundColor);
    }

    public void erase(){
        paint.setColor(backGroundColor);
    }

    public void deleteDrawing(){
        allPaints.clear();
        allPaths.clear();
        Log.d("LogDaTuga", "allPaints = " + allPaints.toString());
        Log.d("LogDaTuga", "allPaths = " + allPaths.toString());
    }

    private void initPaint(){
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public void setColor(Integer paletteColor){
        paint.setColor(paletteColor);
    }
}

package com.pomodoro.widgets;

import com.pomodoro.activities.R;
import com.pomodoro.activities.R.color;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RingView extends View {
	private float Phase = 0;
	
	public RingView(Context context) {
		super(context);
	}
	public RingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override 
	public void onWindowFocusChanged(boolean hasFocus) {
		// Init all drawing facilities here.
		// Only init here, we can get the height and width of the view.
		initDrawingFacilities();
		super.onWindowFocusChanged(hasFocus); 
		}
	public float getPhase() {
        return Phase;
    }

    public void setPhase(float phase) {
        Phase = phase;
        invalidate();
    }
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 * Things to draw:
	 * 
	 * A background Ring r1 with the same color with the timer text.
	 * A foreground moving arc r2 which shows the progress.
	 * 
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// Draw a background ring.	
		if(circle == null || ringPaint == null)
			return;
		canvas.drawArc(circle, 0, 360, false, ringPaint);
		
		// Draw foreground moving ring.
		canvas.rotate(-90, center.x, center.y);
		canvas.drawArc(circle, 0, -360*Phase, false, ringPaintShowProgress);
	}
	
	private float getRadius() {
		float width = (float)getWidth();
		float height = (float)getHeight();
		if (width > height){
			return height/3;
		}else{
			return width/3;
		}
	}
	
	private Point getCenter() {
		int width = getWidth();
		int height = getHeight();
		return new Point(width/2, height/2);
	}
	
	private RectF createCircle(Point center, float radius) {
		RectF oval = new RectF();
		oval.set(center.x - radius, center.y - radius, center.x + radius, center.y + radius);
		return oval;
	}
	
	private float radius;
	private Point center;
	private RectF circle = new RectF();
	private Paint ringPaint, ringPaintShowProgress;
	private final int RING_WIDTH = 30;
	
	private void initDrawingFacilities() {
		radius = getRadius();
		center = getCenter();
		circle = createCircle(center, radius);
		ringPaint  = new Paint();
		
		ringPaint.setAntiAlias(true);
		ringPaint.setColor(getResources().getColor(R.color.white));
		ringPaint.setStyle(Paint.Style.STROKE);	
		ringPaint.setStrokeWidth(RING_WIDTH);
		
		ringPaintShowProgress = new Paint(ringPaint);
		ringPaintShowProgress.setColor(getResources().getColor(R.color.coral));
	}
}

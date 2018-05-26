package com.exercise.AndroidCompas;

 
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class  MyCompassView extends View {

    public float direction = 0;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean firstDraw;
	private Matrix matriz = new Matrix();
	private Bitmap line;
	private Bitmap person;
	static float radiusCompass;
   GPSTracker gps;
    float width;
    float height;

    float cordX;
    float cordY;
     
   float x_line, y_line;
    
   public MyCompassView(Context context) {
	super(context);
	// TODO Auto-generated constructor stub
	init();
    }

    public MyCompassView(Context context, AttributeSet attrs) {
	super(context, attrs);
	// TODO Auto-generated constructor stub
	init();
    }

    public MyCompassView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	// TODO Auto-generated constructor stub
	init();
    }

    private void init() {

	getCordenadas(25, 100);
	line = BitmapFactory.decodeResource(getResources(),R.drawable.line);
	person = BitmapFactory.decodeResource(getResources(),R.drawable.body);
	x_line=0;
	y_line=0;
	
	paint.setStyle(Paint.Style.STROKE);
	paint.setStrokeWidth(3);
	paint.setColor(Color.WHITE);
	paint.setTextSize(30);
	getCordenadas(25, 100);
	firstDraw = true;

	
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// TODO Auto-generated method stub
	setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
		MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
	// TODO Auto-generated method stub
	//int maxRadius = 100;
	int cxCompass = getMeasuredWidth() / 2;
	int cyCompass = getMeasuredHeight() / 2;
	
	canvas.translate(cxCompass,cyCompass);
	
	
	

	
	paint.setColor(Color.GREEN);
	    radiusCompass = (float) (cxCompass * 1);
	
	    canvas.drawCircle(0, 0, radiusCompass, paint);
	// linea x
	//canvas.drawLine(-(width / 2), 0, width, 0, paint);
	// linea y
	//canvas.drawLine(0, -(height / 2), 0, height, paint);
	// Ponit B
	
	    canvas.drawLine(-cxCompass, 0, getMeasuredWidth(), 0, paint);
		// linea y
	    /*
	    canvas.drawLine(0, -cyCompass, 0, getMeasuredHeight(), paint);
	canvas.drawCircle((float) 0, (float) 0, (float) (maxRadius / 4), paint);
	canvas.drawCircle((float) 0, (float) 0, (float) (( maxRadius) / 4),paint);
	canvas.drawCircle((float) 0, (float) 0, (float) (maxRadius / 2), paint);
	canvas.drawCircle((float) 0, (float) 0, (float) (maxRadius), paint);
	*/
	    canvas.drawCircle((float) 0, (float) 0, (float) (radiusCompass/4), paint);
	    paint.setTextSize(20);
	    canvas.drawText("1km",0,-(radiusCompass/4),paint);
	    canvas.drawText("5km",0,-(radiusCompass/2),paint);
	    canvas.drawText("10km",0,-(radiusCompass*3/4),paint);
	    canvas.drawCircle((float) 0, (float) 0, (float) (radiusCompass*3/4), paint);
	    canvas.drawCircle((float) 0, (float) 0, (float) (radiusCompass/2), paint);
	    
	    canvas.drawCircle((float) 0, (float) 0, (float) (5), paint);
	    canvas.drawCircle((float) 50, (float) 0, (float) (5), paint);
	    canvas.drawCircle((float) 0, (float) 50, (float) (5), paint);
	    
	    
	if(!firstDraw){
	    paint.setColor(Color.YELLOW);
		
	    canvas.drawLine(
			    0,
			    0,(float) ( 120 * Math.sin((double) (-direction)*Math.PI / 180)),
			              (float) ( -120 * Math.cos((double) (-direction) *Math.PI / 180)), paint);
	   
	    float convercion= AndroidCompass.Result*120/100;
	    float dy=(float) (AndroidCompass.latsave2 - AndroidCompass.latsave1);
	    float dx= (float) (Math.cos(AndroidCompass.latsave1)*(	AndroidCompass.lonsave2-AndroidCompass.lonsave1));
	    float ang = (float) Math.atan2(dy, dx);
	    float x= (float) (convercion * (Math.sin((double) (-direction)*Math.PI / 180))-ang);
	    float y= (float) (convercion * (Math.cos((double) (-direction)*Math.PI / 180)-ang));
	    
	    paint.setColor(Color.WHITE);
	    canvas.drawPoint(x, y, paint);
	    paint.setColor(Color.YELLOW);
	    
	    paint.setTextSize(30);
	   //canvas.drawText("N", (float) ( radiusCompass *Math.sin((double) (-direction) *3.14 / 180)),(float)  (-radiusCompass *Math.cos((double) (-direction) *3.14 / 180)), paint);
	    canvas.drawText("N", (float) (radiusCompass *Math.sin((double) (direction) *Math.PI / 180)),0, paint);
	    canvas.drawText("S", 0,(float)  ( radiusCompass *Math.cos((double) (direction) *Math.PI / 180)), paint);
	    
	    
	    matriz.setTranslate(0,0);
		matriz.setRotate(direction, 0, (float)  ( radiusCompass *Math.cos((double) (direction) *Math.PI / 180)));
		canvas.drawBitmap(person, matriz, paint);
/*		paint.setColor(Color.WHITE);
	    canvas.drawPoint(0, 0, paint);
	*/    
	/*
	   canvas.drawLine(
		    cxCompass,
		    cyCompass,(float) (cxCompass + radiusCompass * Math.sin((double) (-direction) * 3.14 / 180)),
		              (float) (cyCompass - radiusCompass * Math.cos((double) (-direction) * 3.14 / 180)), paint);
	*/	  

	}
 
	
/*
	int cxCompass = getMeasuredWidth() / 2;
	int cyCompass = getMeasuredHeight() / 2;
	float radiusCompass;

	if (cxCompass > cyCompass) {
	    radiusCompass = (float) (cyCompass * 0.9);
	} else {
	    radiusCompass = (float) (cxCompass * 0.9);
	}
	canvas.drawCircle(cxCompass, cyCompass, radiusCompass, paint);
	canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

	if (!firstDraw) {

	    canvas.drawLine(
		    cxCompass,
		    cyCompass,
		    (float) (cxCompass + radiusCompass
			    * Math.sin((double) (-direction) * 3.14 / 180)),
		    (float) (cyCompass - radiusCompass
			    * Math.cos((double) (-direction) * 3.14 / 180)),
		    paint);

	    canvas.drawText(String.valueOf(direction), cxCompass, cyCompass,
		    paint);
	}
*/
	
    }
	
    public void updateDirection(float dir) {
	firstDraw = false;
	//pointsgps =false;
	direction = dir;
	invalidate();
	
    }
    public void getCordenadas(float xUTM, float yUTM) {

	cordX = xUTM * AndroidCompass.SCALE;
	cordY = yUTM * AndroidCompass.SCALE;
	// cordX = xUTM;
	// cordY = yUTM;
    }
    

}

package git.sasure.sub;

import git.sasure.Kit.GameKit;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.util.Property;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

public class myFrameLayout extends FrameLayout 
{
//	private Point selectedPoint  = null;
	private int pointX;
	private int pointY;
	private int radius;
	private Paint paint;
	private int height;
	private int width;
//	private boolean successlink = false;
	public myFrameLayout(Context context) 
	{
		super(context);
		

        width = GameKit.screenWidth;
        height = GameKit.screenHeight;
        
		paint = new Paint();
		paint.setAntiAlias(true); 
	}

	@Override
	public void onDraw(Canvas canvas)
	{
	//	super.onDraw(canvas);
		
		canvas.drawCircle(pointX, pointY, radius, paint);
	}
	
	public void startBackAnimator(Point selectedPoint,final int color)
	{
	//	successlink = true;
		paint.setColor(color);
		
		pointX = selectedPoint.x;
		pointY = selectedPoint.y;
		
		int biggerwidth = selectedPoint.x > width - selectedPoint.x ? selectedPoint.x : width - selectedPoint.x;
		int biggerheight = selectedPoint.y > height - selectedPoint.y ? selectedPoint.y : height - selectedPoint.y;
		int biggest = (int) Math.sqrt(biggerheight * biggerheight + biggerwidth * biggerwidth);
		
		ObjectAnimator oa = ObjectAnimator.ofInt(this, radiuProperty,0, biggest);
		oa.setDuration(500);
		oa.setInterpolator(new AccelerateInterpolator());
		
		oa.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
			//	successlink = false;
				myFrameLayout.this.setBackgroundColor(color);
				
				if(!GameKit.hasPieces())
				{
					startBackAnimator(new Point(GameKit.screenWidth / 2, 0), GameKit.defoultbackcolor);
					GameKit.start(-1);
				}
			}
		});
		
		oa.start();
	}
	
	private Property<myFrameLayout, Integer> radiuProperty = new Property<myFrameLayout, Integer>(Integer.class,"radius")
	{
		@Override
		public Integer get(myFrameLayout object)
		{
			return object.radius;
		}
		
		@Override  
        public void set(myFrameLayout object, Integer value) 
		{  
            object.radius = value;
            
            invalidate();
		}
	};
}

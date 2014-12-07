package git.sasure.sub;

import git.sasure.Kit.GameKit;
import git.sasure.linkgame.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class myRelativeLayout extends RelativeLayout 
{
	private int height;
	private int width;
	private Paint paint;
	private int radius;
	private int endradius;
	//boolean beginplay = false;
	//FrameLayout fl = null;
	
	public myRelativeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		 width = GameKit.screenWidth / 2;
	     height = GameKit.screenHeight / 2;
        
        radius = 0;
        
        endradius = (int) Math.sqrt(width * width + height * height);

        paint = new Paint();
        paint.setAntiAlias(true); 
        paint.setColor(context.getResources().getColor(R.color.centerbutton));
	}
	
	
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		canvas.drawCircle(width, height, radius, paint);
	}
	
//	public void startAnimator(final FrameLayout fl)
//	{
//		this.fl = fl;
//		
//		ObjectAnimator oa = ObjectAnimator.ofInt(this, radiuProperty, radius,endradius);
//		oa.setDuration(500);
//		oa.setInterpolator(new DecelerateInterpolator());
//		
//		ObjectAnimator oa2 = ObjectAnimator.ofFloat(this,"alpha", 1,0);
//		oa2.setDuration(500);
//		oa2.setInterpolator(new DecelerateInterpolator());
//
//		AnimatorSet set  = new AnimatorSet();
//		
//		set.play(oa).before(oa2);
//		
//		set.start();
//		
//		oa.addListener(new AnimatorListenerAdapter() 
//		{
//			@Override
//			public void onAnimationEnd(Animator animation) 
//			{
//				fl.removeViewAt(1);
//				
//				if(beginplay == false)
//				{
//					GameKit.start(-1);
//					beginplay = true;
//				}
//			}
//		});
//		
//		oa2.addListener(new AnimatorListenerAdapter()  
//		{
//			@Override
//			public void onAnimationEnd(Animator animation) 
//			{
//				fl.removeViewAt(1);
//				radius = 0;
//			}
//		});
//	}
	
	public int getendradius()
	{
		return endradius;
	}

	public int getRadius()
	{
		return this.radius;
	}
		
    public void setRadius(int value) 
	{  
          this.radius = value;
            
           invalidate();
	}
    
    public void resetRadius()
    {
    	this.radius = 0;
    }
}

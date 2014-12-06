package git.sasure.linkgame;

import git.sasure.Kit.GameKit;
import git.sasure.linkgame.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Property;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class myRelativeLayout extends RelativeLayout 
{
	int height;
	int width;
	Paint paint;
	int radius;
	int endradius;
	boolean beginplay = false;
	FrameLayout fl = null;
	
	public myRelativeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
		DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels / 2;  // ÆÁÄ»¿í¶È£¨ÏñËØ£©Ò»°ë
        height = metric.heightPixels / 2;  // ÆÁÄ»¸ß¶È£¨ÏñËØ£©Ò»°ë
        
        radius = 0;
        
        endradius = (int) Math.sqrt(width * width + height * height);

        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.centerbutton));
	}
	
	
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		canvas.drawCircle(width, height, radius, paint);
	}
	
	
	public void startAnimator(final FrameLayout fl)
	{
		this.fl = fl;
		
		ObjectAnimator oa = ObjectAnimator.ofInt(this, radiuProperty, radius,endradius);
		oa.setDuration(500);
		oa.setInterpolator(new DecelerateInterpolator());
		
		ObjectAnimator oa2 = ObjectAnimator.ofFloat(this,"alpha", 1,0);
		oa2.setDuration(500);
		oa2.setInterpolator(new DecelerateInterpolator());

		AnimatorSet set  = new AnimatorSet();
		
		set.play(oa).before(oa2);
		
		set.start();
		
		oa.addListener(new AnimatorListenerAdapter() 
		{
			@Override
			public void onAnimationEnd(Animator animation) 
			{
				fl.removeViewAt(1);
				
				if(beginplay == false)
				{
					GameKit.start(-1);
					beginplay = true;
				}
			}
		});
		
		oa2.addListener(new AnimatorListenerAdapter()  
		{
			@Override
			public void onAnimationEnd(Animator animation) 
			{
				fl.removeViewAt(1);
				radius = 0;
			}
		});
	}
	
	private Property<myRelativeLayout, Integer> radiuProperty = new Property<myRelativeLayout, Integer>(Integer.class,"radius")
	{
		@Override
		public Integer get(myRelativeLayout object) 
		{
			return object.radius;
		}
		
		@Override  
        public void set(myRelativeLayout object, Integer value) 
		{  
            object.radius = value;
            
            invalidate();
		}
	};
}

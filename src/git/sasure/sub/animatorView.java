package git.sasure.sub;

import git.sasure.Kit.GameKit;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Property;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class animatorView extends View 
{
	int radius  = 0;
	private Paint paint;
	private int pointX;
	private int pointY;
	
	public animatorView(Context context)
	{
		super(context);
		setEnabled(false);
		
		paint = new Paint();
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		
	//	canvas.save();
		canvas.drawCircle(pointX, pointY, radius, paint);
	//	canvas.restore();
		
		super.onDraw(canvas);
	}
	
	public void startforeAnimator(Point point)
	{
		pointX = point.x;
		pointY = point.y;
		
		AnimatorSet set = new AnimatorSet();  
        //��ӱ仯����  
        set.playTogether(  
                //�뾶�仯  
                ObjectAnimator.ofInt(this, radiuProperty, 0, GameKit.PieceWidth - GameKit.PieceWidth / 4),
                //��ɫ�仯 ��ɫ��͸��  
                ObjectAnimator.ofObject(this, mForegroundColorProperty, new ArgbEvaluator(), Color.BLACK, Color.TRANSPARENT)  
        );  
        // ����ʱ��  
        set.setDuration(500);  
        //�ȿ����  
        set.setInterpolator(new DecelerateInterpolator());  
        set.start();  
	}
	
	private Property<animatorView, Integer> radiuProperty = new Property<animatorView, Integer>(Integer.class,"radius")
	{
		@Override
		public Integer get(animatorView object)
		{
			return object.radius;
		}
		
		@Override  
        public void set(animatorView object, Integer value) 
		{  
            object.radius = value;
            
            invalidate();
		}
	};
	

	
    private Property<animatorView, Integer> mForegroundColorProperty = new Property<animatorView, Integer>(Integer.class, "bg_color") {  
        @Override  
        public Integer get(animatorView object) {  
            return object.paint.getColor();  
        }  
  
        @Override  
        public void set(animatorView object, Integer value) { 
            object.paint.setColor(value);  
        }  
    };  
}

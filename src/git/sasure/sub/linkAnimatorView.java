package git.sasure.sub;

import git.sasure.Kit.GameKit;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

public class linkAnimatorView extends View 
{
	private Paint paint;
	private int pointX;
	private int pointY;
	private int radius; 
	
	public linkAnimatorView(Context context)
	{
		super(context);
		setEnabled(false);
		
		paint = new Paint();
		paint.setAntiAlias(true);
	}
	
	public void setPaintColor(int color)
	{
		paint.setColor(color);
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		canvas.save();
		canvas.drawCircle(pointX, pointY, radius, paint);
		canvas.restore();
		
		radius = 0;
	}
	
	public Point getPoint()
	{
		return new Point(pointX, pointY);
	}
	
	public void setPoint(Point point)
	{
		pointX = point.x;
		pointY = point.y;
		
		radius = GameKit.PieceWidth / 4;
		
		postInvalidate();
	}
}

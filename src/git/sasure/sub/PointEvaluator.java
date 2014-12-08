package git.sasure.sub;

import android.animation.TypeEvaluator;
import android.graphics.Point;

public class PointEvaluator implements TypeEvaluator<Point> 
{
	@Override
	public Point evaluate(float fraction, Point startValue, Point endValue) 
	{
	//	Point current  = new Point();
		
//		current.x = (int) (Math.abs);
//		
//		current.y = (int) (Math.abs(startValue.y - endValue.y) * fraction);
		
		int x = (int) ((endValue.x - startValue.x) * fraction);
		int y =(int) ((endValue.y - startValue.y) * fraction);
		
		return new Point(startValue.x + x, startValue.y + y);
	}

}

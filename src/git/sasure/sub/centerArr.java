package git.sasure.sub;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import git.sasure.Abs.SquareArrangement;
import git.sasure.Kit.GameKit;

/**
 * 其产生的方块排列顺序如：
 * | ****** |
 * | ****** |
 * | ****** |
 * @author Sasure
 * @version 1.0
 */
public class centerArr extends SquareArrangement {

	@Override
	protected List<Point> createArrangement() 
	{
		List<Point> notNullPoints = new ArrayList<>();
		
		for(int i = 1;i < GameKit.GameXN - 1;++i)
			for(int j = 1;j < GameKit.GameYN - 1;++j)
				notNullPoints.add(new Point(i, j));
		
		return notNullPoints;
	}

}

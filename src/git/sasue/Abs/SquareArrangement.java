package git.sasue.Abs;

import git.sasue.Kit.GameKit;

import java.util.List;

import android.graphics.Point;

/**
 * 本类为抽象类，通过继承其产生不同的排列效果
 * 
 * @author Sasure
 * @version 1.0
 */
public abstract class SquareArrangement 
{
	/**
	 * 子类必须实现的方法，可以产生不同的方块排列效果
	 * @return 有方块的集合,这里用point.x代表i，point.y代表j
	 */
	protected abstract List<Point> createArrangement();
	
	/**
	 * 将子类实现的newArrangement方法 得到的List<Point>转换为int[][]
	 * @return pieces
	 */
	public int[][] createPieces()
	{
		int[][] pieces = initPieces();
		
		List<Point> notNULLPoints = createArrangement();
		List<Integer> SqureValues = GameKit.getValues(notNULLPoints.size());
		
		for(int i = 0;i < notNULLPoints.size() && i < SqureValues.size();++i)
		{
			Point point = notNULLPoints.get(i);
			
			pieces[point.x][point.y] = SqureValues.get(i);
		}
		
		return pieces;
	}
	
	/**
	 * 将pieces[][]初始化为零
	 * @return pieces
	 */
	private int[][] initPieces()
	{
		int[][] pieces = new int[GameKit.GameXN][GameKit.GameYN];
		
		for(int i = 0;i < pieces.length;++i)
			for(int j = 0;j < pieces[i].length;++j)
				pieces[i][j] = 0;
		
		return pieces;
	}
}

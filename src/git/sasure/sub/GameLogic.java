package git.sasure.sub;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Point;
import android.util.Log;
import git.sasure.Abs.GameService;
import git.sasure.Abs.SquareArrangement;
import git.sasure.Kit.GameKit;

/**
 * 实现了GameService接口，定义自己的游戏规则
 * @author Sasure
 * @version 1.0
 */
public class GameLogic implements GameService 
{
	private int[][] pieces = null;//本游戏的数据模型，每一个元素代表练练看里的每一个方块，0即为有图，非零即有图
	private List<SquareArrangement> myArrs = null;//保存不同的方块排列样式
	
	public GameLogic()
	{
		myArrs = new ArrayList<>();
		
		myArrs.add(new centerArr());
	}
	
	/**
	 * 传入myArrs.size()范围之外的将随机用其中一个排列样式
	 */
	@Override
	public void start(int i)
	{
		if(i > myArrs.size() - 1 || i < myArrs.size() -1)
		{
			Random rand = new Random();
			
			pieces = myArrs.get(rand.nextInt(myArrs.size() - 1)).createPieces();
		}
		else
		{
			pieces = myArrs.get(i).createPieces();
		}
	}

	@Override
	public int[][] getPieces() 
	{
		return pieces;
	}

	@Override
	public boolean hasPieces()
	{
		for(int i = 0;i < pieces.length;++i)
			for(int j = 0;j < pieces[i].length;++j)
				if(pieces[i][j] != 0)
					return true;
		
		return false;
	}

	@Override
	public int[] findPiece(float X, float Y) 
	{
		return GameKit.findPiece(X,Y);
	}

	@Override
	public List<Point> link(int[] first, int[] second) 
	{
	//	Log.i("test1", "first:["+first[0]+","+first[1]+"]"+" second:["+second[0]+","+second[1]+"]");
		if(first[0] == second[0] && first[1] == second[1])
			return null;
		
		if(pieces[first[0]][first[1]] != pieces[second[0]][second[1]])
			return null;
		
		if(first[1] == second[1] && horizon(first, second))
			return GameKit.changeToList(first,second);
		
		if(first[0] == second[0] && vertical(first, second))
			return GameKit.changeToList(first,second);
		
		int[] corner = getCornerPoint(first, second);
		
		
		if(corner != null)
		{
	//		Log.i("test3", "corner:["+corner[0]+","+corner[1]+"]");
			return GameKit.changeToList(first,corner,second);
		}
		List<Point> leftChenel = getLeftCheList(first, 0);
		List<Point> reghtChenek = getRightChenel(first, GameKit.GameXN);
		List<Point> upChenel = getUpChenel(first, 0);
		List<Point> downChenel = getDownChenel(first, GameKit.GameYN);
		
		for(int i = 0;i < leftChenel.size();++i)
		{
			int[] corner1 = new int[]{leftChenel.get(i).x,leftChenel.get(i).y};
			int[] corner2 = getCornerPoint(corner1, second);
			
			if(corner2 != null)
			{
		//		Log.i("test3", "corner:["+corner1[0]+","+corner1[1]+"]");
		//		Log.i("test3", "corner:["+corner2[0]+","+corner2[1]+"]");
				return GameKit.changeToList(first,corner1,corner2,second);
			}
		}
		
		for(int i = 0;i < reghtChenek.size();++i)
		{
			int[] corner1 = new int[]{reghtChenek.get(i).x,reghtChenek.get(i).y};
			int[] corner2 = getCornerPoint(corner1, second);
			
			if(corner2 != null)
			{
		//		Log.i("test3", "corner:["+corner1[0]+","+corner1[1]+"]");
		//		Log.i("test3", "corner:["+corner2[0]+","+corner2[1]+"]");
				return GameKit.changeToList(first,corner1,corner2,second);
			}
		}
		
		for(int i = 0;i < upChenel.size();++i)
		{
			int[] corner1 = new int[]{upChenel.get(i).x,upChenel.get(i).y};
			int[] corner2 = getCornerPoint(corner1, second);
			
			if(corner2 != null)
			{
		//		Log.i("test3", "corner:["+corner1[0]+","+corner1[1]+"]");
		//		Log.i("test3", "corner:["+corner2[0]+","+corner2[1]+"]");
				return GameKit.changeToList(first,corner1,corner2,second);
			}
		}
		
		for(int i = 0;i < downChenel.size();++i)
		{
			int[] corner1 = new int[]{downChenel.get(i).x,downChenel.get(i).y};
			int[] corner2 = getCornerPoint(corner1, second);
			
			if(corner2 != null)
			{
		//		Log.i("test3", "corner:["+corner1[0]+","+corner1[1]+"]");
		//		Log.i("test3", "corner:["+corner2[0]+","+corner2[1]+"]");
				return GameKit.changeToList(first,corner1,corner2,second);
			}
				
		}
		
		return null;
	}
	
	private boolean horizon(int[] first,int[] second)
	{
		int x_start = first[0] < second[0] ? first[0] : second[0];
		int x_end = first[0] > second[0] ? first[0] : second[0];
		int y = first[1];
		
		for(int x = x_start + 1;x < x_end;++x)
			if(pieces[x][y] != 0)
				return false;
		
		return true;
	}
	
	private boolean vertical(int[] first,int[] second)
	{
		int y_start = first[1] < second[1] ? first[1] : second[1];
		int y_end = first[1] > second[1] ? first[1] : second[1];
		int x = first[0];
		
		for(int y = y_start + 1;y < y_end;++y)
			if(pieces[x][y] != 0)
				return false;
		
		return true;
	}
	
	private int[] getCornerPoint(int[] first,int[] second)
	{
		int[] corner1 = new int[]{first[0],second[1]};
		int[] corner2 = new int[]{second[0],first[1]};
		
		if(pieces[corner1[0]][corner1[1]] == 0)
			if(vertical(first, corner1) && horizon(corner1, second))
				return corner1;
		
		if (pieces[corner2[0]][corner2[1]] == 0) 
			if(horizon(first, corner2) && vertical(corner2, second))
				return corner2;
		
		return null;
	}
	
	/**由于二维数组不是可变长的，所以用Point封装pieces的i和j信息，point.x = i,point.y = j
	 * 
	 * @param current
	 * @param min
	 * @return 返回该方块左通道
	 */
	private List<Point> getLeftCheList(int[] current,int min)
	{
		List<Point> result = new ArrayList<>();
		
		for(int i = current[0] - 1;i >= min;--i)
		{
			if(pieces[i][current[1]] != 0)
				return result;
			
			result.add(new Point(i,current[1]));
		}
		
		return result;
	}
	
	/**由于二维数组不是可变长的，所以用Point封装pieces的i和j信息，point.x = i,point.y = j
	 * 
	 * @param current
	 * @param max
	 * @return 返回当前方块的右通道
	 */
	private List<Point> getRightChenel(int[] current,int max)
	{
		List<Point> result = new ArrayList<>();
		
		for(int i = current[0] + 1;i <= max - 1;++i)
		{
			if(pieces[i][current[1]] != 0)
				return result;
			
			result.add(new Point(i,current[1]));
		}
		
		return result;
	}
	
	/**由于二维数组不是可变长的，所以用Point封装pieces的i和j信息，point.x = i,point.y = j
	 * 
	 * @param current
	 * @param mim
	 * @return 返回当前方块的上通道
	 */
	private List<Point> getUpChenel(int[] current,int min)
	{
		List<Point> result = new ArrayList<>();
		
		for(int j = current[1] - 1;j >= min;--j)
		{
			if(pieces[current[0]][j] != 0)
				return result;
			
			result.add(new Point(current[0],j));
		}
		
		return result;
	}
	
	/**由于二维数组不是可变长的，所以用Point封装pieces的i和j信息，point.x = i,point.y = j
	 * 
	 * @param current
	 * @param max
	 * @return 返回当前方块的下通道
	 */
	private List<Point> getDownChenel(int[] current,int max)
	{
		List<Point> result = new ArrayList<>();
		
		for(int j = current[1] + 1;j <= max - 1;++j)
		{
			if(pieces[current[0]][j] != 0)
				return result;
			
			result.add(new Point(current[0],j));
		}
		
		return result;
	}
}

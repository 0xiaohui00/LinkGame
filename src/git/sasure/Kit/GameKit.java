package git.sasure.Kit;

import git.sasure.Abs.SquareArrangement;
import git.sasure.linkgame.Piece;
import git.sasure.linkgame.R;
import git.sasure.sub.GameView;
import git.sasure.sub.centerArr;
import git.sasure.sub.fullArr;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.graphics.Point;
import android.util.Log;


/**
 * 
 * @author Sasure
 *@version 1.0 
 */
public class GameKit
{
	private static List<Integer> imageValues = getImageValue();
	private static List<SquareArrangement> myArrs = null;
	private static GameView gameView = null;
	
	public static int Game_X_begin = 0;
	public static int Game_Y_begin = 0;
	public static int GameWidth = 0;
	public static int GameHeight = 0;
	public static int GameXN = 7;//连连看横排有GameXN个方块
	public static int GameYN = 8;//连连看竖排有GameYN个方块
	public static int PieceWidth = 0;//每个方块的宽度
	public static int PieceHeidth = 0;//每个方块的高度
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static int defoultbackcolor = 0;

	//加载排列方式
	static
	{
		myArrs = new ArrayList<>();
		
		myArrs.add(new centerArr());
		myArrs.add(new fullArr());
	}
	
	
	public static void setGameView(GameView gameView)
	{
		GameKit.gameView = gameView;
	}
	
	public static int[][] start(int i)
	{
		int[][] pieces;
		
		if(i > myArrs.size() - 1 || i < 0)
		{
			Random rand = new Random();
			int loca = rand.nextInt(myArrs.size());
			
		//	Log.i("test", myArrs.size() + "");
		//	Log.i("test", loca + "");
			pieces = myArrs.get(loca).createPieces();
		}
		else
		{
			pieces = myArrs.get(i).createPieces();
		}
		
		gameView.setPieces(pieces);
		gameView.postInvalidate();
		
		return pieces;
	}
	
	public static List<Point> link(Piece first, Piece second,int[][] pieces) 
	{
		if(first.i == second.i && first.j == second.j)
			return null;
		
		if(pieces[first.i][first.j] != pieces[second.i][second.j])
			return null;
		
		if(first.j == second.j && horizon(first, second,pieces))
			return changeToList(first,second);
		
		if(first.i == second.i && vertical(first, second,pieces))
			return changeToList(first,second);
		
		Piece corner = getCornerPoint(first, second,pieces);
		
		
		if(corner != null)
		{
			return changeToList(first,corner,second);
		}
		
		List<Piece> leftChenel = getLeftCheList(first, 0,pieces);
		List<Piece> reghtChenel = getRightChenel(first, GameKit.GameXN,pieces);
		List<Piece> upChenel = getUpChenel(first, 0,pieces);
		List<Piece> downChenel = getDownChenel(first, GameKit.GameYN,pieces);
		
		for(int i = 0;i < leftChenel.size();++i)
		{
			Piece corner1 = new Piece(leftChenel.get(i).i,leftChenel.get(i).j);
			Piece corner2 = getCornerPoint(corner1, second,pieces);
			
			if(corner2 != null)
			{
				return changeToList(first,corner1,corner2,second);
			}
		}
		
		for(int i = 0;i < reghtChenel.size();++i)
		{
			Piece corner1 = new Piece(reghtChenel.get(i).i,reghtChenel.get(i).j);
			Piece corner2 = getCornerPoint(corner1, second,pieces);
			
			if(corner2 != null)
			{
				return changeToList(first,corner1,corner2,second);
			}
		}
		
		for(int i = 0;i < upChenel.size();++i)
		{
			Piece corner1 = new Piece(upChenel.get(i).i,upChenel.get(i).j);
			Piece corner2 = getCornerPoint(corner1, second,pieces);
			
			if(corner2 != null)
			{
				return changeToList(first,corner1,corner2,second);
			}
		}
		
		for(int i = 0;i < downChenel.size();++i)
		{
			Piece corner1 = new Piece(downChenel.get(i).i,downChenel.get(i).j);
			Piece corner2 = getCornerPoint(corner1, second,pieces);
			
			if(corner2 != null)
			{
				return changeToList(first,corner1,corner2,second);
			}
				
		}
		
		return null;
	}
	
	private static boolean horizon(Piece first,Piece second,int[][] pieces)
	{
		int x_start = first.i < second.i ? first.i : second.i;
		int x_end = first.i > second.i ? first.i : second.i;
		int y = first.j;
		
		for(int x = x_start + 1;x < x_end;++x)
			if(pieces[x][y] != 0)
				return false;
		
		return true;
	}
	
	private static boolean vertical(Piece first,Piece second,int[][] pieces)
	{
		int y_start = first.j < second.j ? first.j : second.j;
		int y_end = first.j > second.j ? first.j : second.j;
		int x = first.i;
		
		for(int y = y_start + 1;y < y_end;++y)
			if(pieces[x][y] != 0)
				return false;
		
		return true;
	}
	
	private static Piece getCornerPoint(Piece first,Piece second,int[][] pieces)
	{
		Piece corner1 = new Piece(first.i,second.j);
		Piece corner2 = new Piece(second.i,first.j);
		
		if(pieces[corner1.i][corner1.j] == 0)
			if(vertical(first, corner1,pieces) && horizon(corner1, second,pieces))
				return corner1;
		
		if (pieces[corner2.i][corner2.j] == 0) 
			if(horizon(first, corner2,pieces) && vertical(corner2, second,pieces))
				return corner2;
		
		return null;
	}
	
	/**由于二维数组不是可变长的，所以用Point封装pieces的i和j信息，point.x = i,point.y = j
	 * 
	 * @param current
	 * @param min
	 * @return 返回该方块左通道
	 */
	private static List<Piece> getLeftCheList(Piece current,int min,int[][] pieces)
	{
		List<Piece> result = new ArrayList<>();
		
		for(int i = current.i - 1;i >= min;--i)
		{
			if(pieces[i][current.j] != 0)
				return result;
			
			result.add(new Piece(i,current.j));
		}
		
		return result;
	}
	
	/**由于二维数组不是可变长的，所以用Point封装pieces的i和j信息，point.x = i,point.y = j
	 * 
	 * @param current
	 * @param max
	 * @return 返回当前方块的右通道
	 */
	private static List<Piece> getRightChenel(Piece current,int max,int[][] pieces)
	{
		List<Piece> result = new ArrayList<>();
		
		for(int i = current.i + 1;i <= max - 1;++i)
		{
			if(pieces[i][current.j] != 0)
				return result;
			
			result.add(new Piece(i,current.j));
		}
		
		return result;
	}
	
	/**由于二维数组不是可变长的，所以用Point封装pieces的i和j信息，point.x = i,point.y = j
	 * 
	 * @param current
	 * @param mim
	 * @return 返回当前方块的上通道
	 */
	private static List<Piece> getUpChenel(Piece current,int min,int[][] pieces)
	{
		List<Piece> result = new ArrayList<>();
		
		for(int j = current.j - 1;j >= min;--j)
		{
			if(pieces[current.i][j] != 0)
				return result;
			
			result.add(new Piece(current.i,j));
		}
		
		return result;
	}
	
	/**由于二维数组不是可变长的，所以用Point封装pieces的i和j信息，point.x = i,point.y = j
	 * 
	 * @param current
	 * @param max
	 * @return 返回当前方块的下通道
	 */
	private static List<Piece> getDownChenel(Piece current,int max,int[][] pieces)
	{
		List<Piece> result = new ArrayList<>();
		
		for(int j = current.j + 1;j <= max - 1;++j)
		{
			if(pieces[current.i][j] != 0)
				return result;
			
			result.add(new Piece(current.i,j));
		}
		
		return result;
	}
	
	/**
	 * 获取连连看的所有以p_开头的图片的ID
	 * @return List<Integer>
	 */
	private static List<Integer> getImageValue()
	{
		try 
		{
			Field[] imageFields = R.color.class.getFields();
			List<Integer> imageValues = new ArrayList<>();
			
			for(Field field : imageFields)
				if(field.getName().startsWith("p_"))
				{
					imageValues.add(field.getInt(R.color.class));
				}
			return imageValues;
		}
		catch(Exception e)
		{
			Log.e("Sasure", "获取图片ID出现错误");
			
			return null;
		}
	}

	/**
	 * 随机从imageValues中获取size个图片ID,加倍并洗牌后
	 * 返回结果为图片ID的集合
	 * 
	 * @param size 需要获取的个数
	 * @return size个图片ID的集合
	 */
	public static List<Integer> getValues(int size)
	{
		if(size % 2 == 0) 
			size /= 2;
		else 
		{
			Log.w("Sasure", "size值应该是偶数！！");
			size = (size - 1) /2;
		}
		
		Random rand = new Random();
		List<Integer> result = new ArrayList<>();
		
		for(int i = 0;i < size;i++)
		{
			int index = rand.nextInt(imageValues.size() - 1);
			result.add(imageValues.get(index));
		}
		
		result.addAll(result);
		Collections.shuffle(result);

		return result;
	}
	
//	/**
//	 * 获取选中框的图片
//	 */
//	public static Bitmap getCheckedBox(Context context)
//	{
//		Bitmap box = BitmapFactory.decodeResource(context.getResources(), R.drawable.checkedbox);
//		
//		return box;
//	}
	
	/**通过二维数组的i，j获取该方块中心点在屏幕上的坐标
	 * 
	 * @param i
	 * @param j
	 * @return point
	 */
	public static Point getGameViewPoint(int i, int j) 
	{
	//	Log.i("test","begin:" + Game_X_begin + "  " + Game_Y_begin);
	//	Log.i("test", "piece" + PieceWidth +" " + PieceHeidth);
		return new Point(i * PieceWidth + PieceWidth / 2,j * PieceHeidth + PieceHeidth / 2);
	}

	public static Point getScreenPoint(int i,int j)
	{
		return new Point(Game_X_begin + i * PieceWidth + PieceWidth / 2,Game_Y_begin + j * PieceHeidth + PieceHeidth / 2);
	}
	
	public static  boolean hasPieces()
	{
		int[][] pieces = gameView.getPieces();
		
		if(pieces != null)
			for(int i = 0;i < pieces.length;++i)
				for(int j = 0;j < pieces[i].length;++j)
					if(pieces[i][j] != 0)
						return true;
		
		return false;
	}
	
	/**
	 *设置连连看界面相对与屏幕的坐标轴 
	 * @param loca
	 */
	public static void setbeginloca(int[] loca)
	{
		Game_X_begin = loca[0];
		Game_Y_begin = loca[1];
	}

	public static Piece findPiece(float X, float Y) 
	{
		Piece current  = new Piece();
		
		int touchX = (int) X;
		int touchY = (int) Y;
		
		if(touchX < 0 || touchY < 0)
			return null;
		
		if(touchX > GameXN * PieceWidth || touchY > GameYN * PieceHeidth)
			return null;
		
		int indexX = getIndex(touchX, PieceWidth);
		int indexY = getIndex(touchY, PieceHeidth);
		
		if (indexX < 0 || indexY < 0)
		{
			return null;
		}
		
		current.i = indexX;
		current.j = indexY;
		
		return current;
	}
	
	private static int getIndex(int relative, int size)
	{
		// 表示座标relative不在该数组中
		int index = -1;
		// 让座标除以边长, 没有余数, 索引减1
		// 例如点了x座标为20, 边宽为10, 20 % 10 没有余数,
		// index为1, 即在数组中的索引为1(第二个元素)
		if (relative % size == 0)
		{
			index = relative / size - 1;
		}
		else
		{
			// 有余数, 例如点了x座标为21, 边宽为10, 21 % 10有余数, index为2
			// 即在数组中的索引为2(第三个元素)
			index = relative / size;
		}
		return index;
	}
	
//	public static Point getLinkPoint(Piece current)
//	{
//		return new Point(Game_X_begin + current.i * PieceWidth + PieceWidth / 2, Game_Y_begin + current.j * PieceHeidth + PieceHeidth / 2);
//	}
	
	/**
	 * 传入可变长的形参
	 * @param points
	 * @return
	 */
	public static List<Point> changeToList(Piece ... pieces)
	{
		List<Point> ArrPoint = new ArrayList<>();
		
		for(Piece piece : pieces)
		{
			Point point = getScreenPoint(piece.i,piece.j);
			ArrPoint.add(point);
		}
		return ArrPoint;
	}
}

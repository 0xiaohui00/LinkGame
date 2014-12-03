package git.sasure.Kit;

import git.sasure.linkgame.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	public static int Game_X_begin = 0;
	public static int Game_Y_begin = 0;
	public static int GameXN = 7;//连连看横排有GameXN个方块
	public static int GameYN = 8;//连连看竖排有GameYN个方块
	public static int PieceWidth = 100;//每个方块的宽度
	public static int PieceHeidth = 100;//每个方块的高度
	
	/**
	 * 获取连连看的所有以p_开头的图片的ID
	 * @return List<Integer>
	 */
	private static List<Integer> getImageValue()
	{
		try 
		{
			Field[] imageFields = R.drawable.class.getFields();
			List<Integer> imageValues = new ArrayList<>();
			
			for(Field field : imageFields)
				if(field.getName().startsWith("p_"))
				{
					imageValues.add(field.getInt(R.drawable.class));
				//	Log.i("test", field.getInt(R.drawable.class)+"");
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
	
	/**
	 * 获取选中框的图片
	 */
	public static Bitmap getCheckedBox(Context context)
	{
		Bitmap box = BitmapFactory.decodeResource(context.getResources(), R.drawable.checkedbox);
		
		return box;
	}
	
	/**通过二维数组的i，j获取该方块左上角在屏幕上的坐标
	 * 
	 * @param i
	 * @param j
	 * @return point
	 */
	public static Point getPoint(int i, int j) 
	{
		return new Point(Game_X_begin + i * PieceWidth, Game_Y_begin + j * PieceHeidth);
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

	public static int[] findPiece(float X, float Y) 
	{
		int[] current  = new int[2];
		
		int touchX = (int) X - Game_X_begin;
		int touchY = (int) Y - Game_Y_begin;
	//	Log.i("test", "touchX:"+touchX+" touchY:"+touchY);
		
		if(touchX < 0 || touchY < 0)
			return null;
		
		if(touchX > GameXN * PieceWidth || touchY > GameYN * PieceHeidth)
			return null;
		
		int indexX = getIndex(touchX, PieceWidth);
		int indexY = getIndex(touchY, PieceHeidth);
	//	Log.i("test","indexx:" + indexX +"indexy:"+indexY);
		
		if (indexX < 0 || indexY < 0)
		{
			return null;
		}
		
		current[0] = indexX;
		current[1] = indexY;
		
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
	
	private static Point getLinkPoint(int[] current)
	{
		return new Point(Game_X_begin + current[0] * PieceWidth + PieceWidth / 2, Game_Y_begin + current[1] * PieceHeidth + PieceHeidth / 2);
	}
	
	/**
	 * 传入可变长的形参
	 * @param points
	 * @return
	 */
	public static List<Point> changeToList(int[] ... points)
	{
		List<Point> ArrPoint = new ArrayList<>();
		
//		Log.i("test3", "length" + points.length);
		
		for(int[] point : points)
		{
//			Log.i("test3", "point:["+point[0]+","+point[1]+"]");
			Point xy = getLinkPoint(point);
			ArrPoint.add(xy);
	//		Log.i("test3","xy:("+xy.x+","+xy.y+")");
		}
		
//		Log.i("test3", "size"+ArrPoint.size());
		return ArrPoint;
	}
	
	
}

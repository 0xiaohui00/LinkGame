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
	public static int GameXN = 7;//������������GameXN������
	public static int GameYN = 8;//������������GameYN������
	public static int PieceWidth = 100;//ÿ������Ŀ��
	public static int PieceHeidth = 100;//ÿ������ĸ߶�
	
	/**
	 * ��ȡ��������������p_��ͷ��ͼƬ��ID
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
			Log.e("Sasure", "��ȡͼƬID���ִ���");
			
			return null;
		}
	}

	/**
	 * �����imageValues�л�ȡsize��ͼƬID,�ӱ���ϴ�ƺ�
	 * ���ؽ��ΪͼƬID�ļ���
	 * 
	 * @param size ��Ҫ��ȡ�ĸ���
	 * @return size��ͼƬID�ļ���
	 */
	public static List<Integer> getValues(int size)
	{
		if(size % 2 == 0) 
			size /= 2;
		else 
		{
			Log.w("Sasure", "sizeֵӦ����ż������");
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
	 * ��ȡѡ�п��ͼƬ
	 */
	public static Bitmap getCheckedBox(Context context)
	{
		Bitmap box = BitmapFactory.decodeResource(context.getResources(), R.drawable.checkedbox);
		
		return box;
	}
	
	/**ͨ����ά�����i��j��ȡ�÷������Ͻ�����Ļ�ϵ�����
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
	 *���������������������Ļ�������� 
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
		// ��ʾ����relative���ڸ�������
		int index = -1;
		// ��������Ա߳�, û������, ������1
		// �������x����Ϊ20, �߿�Ϊ10, 20 % 10 û������,
		// indexΪ1, ���������е�����Ϊ1(�ڶ���Ԫ��)
		if (relative % size == 0)
		{
			index = relative / size - 1;
		}
		else
		{
			// ������, �������x����Ϊ21, �߿�Ϊ10, 21 % 10������, indexΪ2
			// ���������е�����Ϊ2(������Ԫ��)
			index = relative / size;
		}
		return index;
	}
	
	private static Point getLinkPoint(int[] current)
	{
		return new Point(Game_X_begin + current[0] * PieceWidth + PieceWidth / 2, Game_Y_begin + current[1] * PieceHeidth + PieceHeidth / 2);
	}
	
	/**
	 * ����ɱ䳤���β�
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

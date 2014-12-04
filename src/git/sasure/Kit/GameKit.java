package git.sasure.Kit;

import git.sasure.Abs.SquareArrangement;
import git.sasure.linkgame.R;
import git.sasure.sub.centerArr;

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
	private static List<SquareArrangement> myArrs = null;
	
	public static int Game_X_begin = 0;
	public static int Game_Y_begin = 0;
	public static int GameXN = 7;//������������GameXN������
	public static int GameYN = 8;//������������GameYN������
	public static int PieceWidth = 100;//ÿ������Ŀ��
	public static int PieceHeidth = 100;//ÿ������ĸ߶�

	//�������з�ʽ
	static
	{
		myArrs = new ArrayList<>();
		
		myArrs.add(new centerArr());
	}
	
	public static int[][] start(int i)
	{
		int[][] pieces;
		
		if(i > myArrs.size() - 1 || i < myArrs.size() -1)
		{
			Random rand = new Random();
			
			pieces = myArrs.get(rand.nextInt(myArrs.size() - 1)).createPieces();
			
		}
		else
		{
			pieces = myArrs.get(i).createPieces();
		}
		
		return pieces;
	}
	
	public static List<Point> link(int[] first, int[] second,int[][] pieces) 
	{
		if(first[0] == second[0] && first[1] == second[1])
			return null;
		
		if(pieces[first[0]][first[1]] != pieces[second[0]][second[1]])
			return null;
		
		if(first[1] == second[1] && horizon(first, second,pieces))
			return changeToList(first,second);
		
		if(first[0] == second[0] && vertical(first, second,pieces))
			return changeToList(first,second);
		
		int[] corner = getCornerPoint(first, second,pieces);
		
		
		if(corner != null)
		{
			return changeToList(first,corner,second);
		}
		
		List<Point> leftChenel = getLeftCheList(first, 0,pieces);
		List<Point> reghtChenel = getRightChenel(first, GameKit.GameXN,pieces);
		List<Point> upChenel = getUpChenel(first, 0,pieces);
		List<Point> downChenel = getDownChenel(first, GameKit.GameYN,pieces);
		
		for(int i = 0;i < leftChenel.size();++i)
		{
			int[] corner1 = new int[]{leftChenel.get(i).x,leftChenel.get(i).y};
			int[] corner2 = getCornerPoint(corner1, second,pieces);
			
			if(corner2 != null)
			{
				return changeToList(first,corner1,corner2,second);
			}
		}
		
		for(int i = 0;i < reghtChenel.size();++i)
		{
			int[] corner1 = new int[]{reghtChenel.get(i).x,reghtChenel.get(i).y};
			int[] corner2 = getCornerPoint(corner1, second,pieces);
			
			if(corner2 != null)
			{
				return changeToList(first,corner1,corner2,second);
			}
		}
		
		for(int i = 0;i < upChenel.size();++i)
		{
			int[] corner1 = new int[]{upChenel.get(i).x,upChenel.get(i).y};
			int[] corner2 = getCornerPoint(corner1, second,pieces);
			
			if(corner2 != null)
			{
				return changeToList(first,corner1,corner2,second);
			}
		}
		
		for(int i = 0;i < downChenel.size();++i)
		{
			int[] corner1 = new int[]{downChenel.get(i).x,downChenel.get(i).y};
			int[] corner2 = getCornerPoint(corner1, second,pieces);
			
			if(corner2 != null)
			{
				return changeToList(first,corner1,corner2,second);
			}
				
		}
		
		return null;
	}
	
	private static boolean horizon(int[] first,int[] second,int[][] pieces)
	{
		int x_start = first[0] < second[0] ? first[0] : second[0];
		int x_end = first[0] > second[0] ? first[0] : second[0];
		int y = first[1];
		
		for(int x = x_start + 1;x < x_end;++x)
			if(pieces[x][y] != 0)
				return false;
		
		return true;
	}
	
	private static boolean vertical(int[] first,int[] second,int[][] pieces)
	{
		int y_start = first[1] < second[1] ? first[1] : second[1];
		int y_end = first[1] > second[1] ? first[1] : second[1];
		int x = first[0];
		
		for(int y = y_start + 1;y < y_end;++y)
			if(pieces[x][y] != 0)
				return false;
		
		return true;
	}
	
	private static int[] getCornerPoint(int[] first,int[] second,int[][] pieces)
	{
		int[] corner1 = new int[]{first[0],second[1]};
		int[] corner2 = new int[]{second[0],first[1]};
		
		if(pieces[corner1[0]][corner1[1]] == 0)
			if(vertical(first, corner1,pieces) && horizon(corner1, second,pieces))
				return corner1;
		
		if (pieces[corner2[0]][corner2[1]] == 0) 
			if(horizon(first, corner2,pieces) && vertical(corner2, second,pieces))
				return corner2;
		
		return null;
	}
	
	/**���ڶ�ά���鲻�ǿɱ䳤�ģ�������Point��װpieces��i��j��Ϣ��point.x = i,point.y = j
	 * 
	 * @param current
	 * @param min
	 * @return ���ظ÷�����ͨ��
	 */
	private static List<Point> getLeftCheList(int[] current,int min,int[][] pieces)
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
	
	/**���ڶ�ά���鲻�ǿɱ䳤�ģ�������Point��װpieces��i��j��Ϣ��point.x = i,point.y = j
	 * 
	 * @param current
	 * @param max
	 * @return ���ص�ǰ�������ͨ��
	 */
	private static List<Point> getRightChenel(int[] current,int max,int[][] pieces)
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
	
	/**���ڶ�ά���鲻�ǿɱ䳤�ģ�������Point��װpieces��i��j��Ϣ��point.x = i,point.y = j
	 * 
	 * @param current
	 * @param mim
	 * @return ���ص�ǰ�������ͨ��
	 */
	private static List<Point> getUpChenel(int[] current,int min,int[][] pieces)
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
	
	/**���ڶ�ά���鲻�ǿɱ䳤�ģ�������Point��װpieces��i��j��Ϣ��point.x = i,point.y = j
	 * 
	 * @param current
	 * @param max
	 * @return ���ص�ǰ�������ͨ��
	 */
	private static List<Point> getDownChenel(int[] current,int max,int[][] pieces)
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
	
	public static  boolean hasPieces(int[][] pieces)
	{
		for(int i = 0;i < pieces.length;++i)
			for(int j = 0;j < pieces[i].length;++j)
				if(pieces[i][j] != 0)
					return true;
		
		return false;
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
		
		for(int[] point : points)
		{
			Point xy = getLinkPoint(point);
			ArrPoint.add(xy);
		}
		return ArrPoint;
	}
}

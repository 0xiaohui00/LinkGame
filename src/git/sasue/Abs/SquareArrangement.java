package git.sasue.Abs;

import git.sasue.Kit.GameKit;

import java.util.List;

import android.graphics.Point;

/**
 * ����Ϊ�����࣬ͨ���̳��������ͬ������Ч��
 * 
 * @author Sasure
 * @version 1.0
 */
public abstract class SquareArrangement 
{
	/**
	 * �������ʵ�ֵķ��������Բ�����ͬ�ķ�������Ч��
	 * @return �з���ļ���,������point.x����i��point.y����j
	 */
	protected abstract List<Point> createArrangement();
	
	/**
	 * ������ʵ�ֵ�newArrangement���� �õ���List<Point>ת��Ϊint[][]
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
	 * ��pieces[][]��ʼ��Ϊ��
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

package git.sasure.Abs;

import git.sasure.Kit.GameKit;
import git.sasure.linkgame.Piece;

import java.util.List;

import android.graphics.Point;
import android.util.Log;

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
	protected abstract List<Piece> createArrangement();
	
	/**
	 * ������ʵ�ֵ�newArrangement���� �õ���List<Point>ת��Ϊint[][]
	 * @return pieces
	 */
	public int[][] createPieces()
	{
		int[][] pieces = initPieces();
		
		List<Piece> notNULLPieces= createArrangement();
		List<Integer> SqureValues = GameKit.getValues(notNULLPieces.size());
		
		
		Log.i("test", "n:" + notNULLPieces.size() + "  s:"+SqureValues.size());
		for(int i = 0;i < notNULLPieces.size()  && i < SqureValues.size();++i)
		{
			Piece piece = notNULLPieces.get(i);
			Log.i("test", i + "");
			pieces[piece.i][piece.j] = SqureValues.get(i);
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

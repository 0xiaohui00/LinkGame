package git.sasure.Abs;

import java.util.List;

import android.graphics.Point;

/**��Ϸ�߼��Ľӿ�
 * 
 * @author Sasure
 *@version 1.0
 */
public interface GameService 
{
	/**
	 * ���ƿ�ʼ
	 */
	void start(int i);
	
	/**
	 * ����һ���ӿڷ���, ���ڷ���һ����ά����
	 * 
	 * @return ��ŷ�����Ϣ�Ķ�ά����
	 */
	int[][] getPieces();
	
	/**
	 * �жϲ���int[][]�������Ƿ񻹴��ڷ���ֵ
	 * 
	 * @return �����ʣ����ֵ����true, û�з���false
	 */
	boolean hasPieces();
	
	/**
	 * ������Ļ�����x��y����, ���ҳ��ֱ��Ӧ������ģ��pieces[i][j],int[0]Ϊi,int[1]Ϊj
	 * 
	 * @param X �����Ļ��x����
	 * @param Y �����Ļ��y����
	 * @return ���ض�Ӧ��int[], û�з���null
	 */
	int[] findPiece(float X, float Y);
	
	/**
	 * �ж����������Ƿ��������, ��������, ����List<Point>,����������Ϣ
	 * 
	 * @param first�ǵ�һ�������i��j��Ϣ��int[0]Ϊi,int[1]Ϊj,��ͬ��
	 * @param second�ǵڶ��������i��j��Ϣ
	 * @return �����������������List<Point>, ����������鲻��������, ����null
	 */
	List<Point> link(int[] first,int[] second);
}

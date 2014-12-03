package git.sasure.Abs;

import java.util.List;

import android.graphics.Point;

/**游戏逻辑的接口
 * 
 * @author Sasure
 *@version 1.0
 */
public interface GameService 
{
	/**
	 * 控制开始
	 */
	void start(int i);
	
	/**
	 * 定义一个接口方法, 用于返回一个二维数组
	 * 
	 * @return 存放方块信息的二维数组
	 */
	int[][] getPieces();
	
	/**
	 * 判断参数int[][]数组中是否还存在非零值
	 * 
	 * @return 如果还剩非零值返回true, 没有返回false
	 */
	boolean hasPieces();
	
	/**
	 * 根据屏幕点击的x，y坐标, 查找出分别对应的数据模型pieces[i][j],int[0]为i,int[1]为j
	 * 
	 * @param X 点击屏幕的x坐标
	 * @param Y 点击屏幕的y坐标
	 * @return 返回对应的int[], 没有返回null
	 */
	int[] findPiece(float X, float Y);
	
	/**
	 * 判断两个方块是否可以相连, 可以连接, 返回List<Point>,即连接线信息
	 * 
	 * @param first是第一个方块的i，j信息（int[0]为i,int[1]为j,下同）
	 * @param second是第二个方块的i，j信息
	 * @return 如果可以相连，返回List<Point>, 如果两个方块不可以连接, 返回null
	 */
	List<Point> link(int[] first,int[] second);
}

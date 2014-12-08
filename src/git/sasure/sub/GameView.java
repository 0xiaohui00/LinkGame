package git.sasure.sub;

import git.sasure.Kit.GameKit;
import git.sasure.linkgame.Piece;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

/**
 * 练练看的游戏界面
 * 
 * @author Sasure
 * @version 1.0
 */
public class GameView extends View
{
//	private Paint linkpaint;//画笔
	private Paint piecepaint;
	private Paint selectedpaint;
	private Paint pathpaint;
	private Path path;
	private int[][] pieces;//其每个元素即每个方块，0即无，非0即有，每个非零的值代表一个的图片ID
//private Bitmap checkedBox;//选中框的图片
	private Piece selectedPiece;//选中框的位置
//	private List<Point> links;//连接线的信息
	private Context context;//保留包的信息
	
	/**
	 * 唯一的构造函数
	 */
	public GameView(Context context)
	{
		super(context);
		
		this.context = context;
		
//		linkpaint = new Paint();//初始化画笔并设置画笔属性
//		linkpaint.setColor(Color.GRAY);
//		linkpaint.setStrokeWidth(5);
		
		piecepaint = new Paint();
		piecepaint.setAntiAlias(true); 
		piecepaint.setDither(true); 
		
		selectedpaint = new Paint();
		selectedpaint.setAntiAlias(true); 
		selectedpaint.setColor(Color.GRAY);
		selectedpaint.setDither(true);
		
		pathpaint = new Paint();
		pathpaint.setColor(Color.BLACK);
		pathpaint.setStrokeWidth(2);
		pathpaint.setStyle(Paint.Style.STROKE);
		
	//	checkedBox = GameKit.getCheckedBox(context);//获取选中框的图片
		
		selectedPiece = null;
	//	links = null;
		
//		int[] loca = new int[2];
//		this.getLocationOnScreen(loca);
//		
//		GameKit.setbeginloca(loca);
		
		path = new Path();
		path.moveTo(0, 0);
		path.lineTo( GameKit.GameWidth,0);
		path.lineTo(GameKit.GameWidth,GameKit.GameHeight);
		path.lineTo(0, GameKit.GameHeight);
		path.close();
		
	}
	
	/**设置连接线信息
	 * 
	 * @param links
	 * @return void
	 */
//	public void setLinks(List<Point> links)
//	{
//		this.links = links;
//	}
	
	/**设置方块集合信息
	 * 
	 * @param pieces
	 * @return void
	 */
	public void setPieces(int[][] pieces)
	{
		this.pieces = pieces;
	}
	
	public int[][] getPieces()
	{
		return this.pieces;
	}
	
	/**
	 * 设置选中框位置的方法
	 * @param checkedPoint
	 */
	public void setselectedPiece(Piece selectedPiece)
	{
		this.selectedPiece = selectedPiece;
	}
	
	/**绘制游戏界面
	 * 重写View类的onDraw方法
	 */
	@Override 
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		if(pieces == null)//如果尚无方块的信息，直接返回
			return;
		
		canvas.drawPath(path, pathpaint);
		
		for(int i = 0;i < GameKit.GameXN;++i)
			for(int j = 0;j < GameKit.GameYN;++j)
			{
				if(pieces[i][j] == 0)
					continue;
				
				Point point = GameKit.getGameViewPoint(i, j);
				
				piecepaint.setColor(context.getResources().getColor(pieces[i][j]));
				canvas.drawCircle(point.x, point.y, GameKit.PieceWidth / 4, piecepaint);
			}
		
//		if(links != null)
//			drawLine(canvas);
		
		if(selectedPiece != null)
		{
			Point checked = GameKit.getGameViewPoint(selectedPiece.i, selectedPiece.j);
		//	Log.i("test",checked.x +"checked" + (checked.y +GameKit.Game_Y_begin));
			canvas.drawCircle(checked.x, checked.y, GameKit.PieceWidth / 8, selectedpaint);
		}
	}
	
	/**
	 * 绘制连接线的方法
	 * @param canvas
	 */
//	private void drawLine(Canvas canvas)
//	{
//		for(int i = 0;i < links.size() - 1 && links.size() >= 2;++i)
//		{
//			Point current = links.get(i);
//			Point next = links.get(i + 1);
//
//			canvas.drawLine(current.x, current.y, next.x, next.y, linkpaint);
//		}
//		
//		links = null;
//		postInvalidate();
//	}
}

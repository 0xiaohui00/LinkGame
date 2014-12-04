package git.sasure.linkgame;

import git.sasure.Kit.GameKit;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * 练练看的游戏界面
 * 
 * @author Sasure
 * @version 1.0
 */
public class GameView extends View
{
	private Paint paint;//画笔
	private int[][] pieces;//其每个元素即每个方块，0即无，非0即有，每个非零的值代表一个的图片ID
	private Bitmap checkedBox;//选中框的图片
	private Piece checkedPiece;//选中框的位置
	private List<Point> links;//连接线的信息
	private Context context;//保留包的信息
	
	/**
	 * 唯一的构造函数
	 */
	public GameView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		this.context = context;
		
		paint = new Paint();//初始化画笔并设置画笔属性
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(5);
		
		checkedBox = GameKit.getCheckedBox(context);//获取选中框的图片
		
		checkedPiece = null;
		links = null;
		
		int[] loca = new int[2];
		this.getLocationOnScreen(loca);
		
		GameKit.setbeginloca(loca);
	}
	
	/**设置连接线信息
	 * 
	 * @param links
	 * @return void
	 */
	public void setLinks(List<Point> links)
	{
		this.links = links;
	}
	
	/**设置方块集合信息
	 * 
	 * @param pieces
	 * @return void
	 */
	public void setPieces(int[][] pieces)
	{
		this.pieces = pieces;
	}
	
	/**
	 * 设置选中框位置的方法
	 * @param checkedPoint
	 */
	public void setCheckedPiece(Piece checkedPiece)
	{
		this.checkedPiece = checkedPiece;
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
		
		for(int i = 0;i < GameKit.GameXN;++i)
			for(int j = 0;j < GameKit.GameYN;++j)
			{
				if(pieces[i][j] == 0)
					continue;
				
				Bitmap piece = BitmapFactory.decodeResource(context.getResources(), pieces[i][j]);
				Point point = GameKit.getPoint(i, j);
				canvas.drawBitmap(piece,point.x, point.y, null);
			}
		
		if(links != null)
			drawLine(canvas);
		
		if(checkedPiece != null && checkedBox != null)
		{
			Point checked = GameKit.getPoint(checkedPiece.i, checkedPiece.j);
			canvas.drawBitmap(checkedBox, checked.x, checked.y,null);
		}
	}
	
	/**
	 * 绘制连接线的方法
	 * @param canvas
	 */
	private void drawLine(Canvas canvas)
	{
		for(int i = 0;i < links.size() - 1 && links.size() >= 2;++i)
		{
			Point current = links.get(i);
			Point next = links.get(i + 1);

			canvas.drawLine(current.x, current.y, next.x, next.y, paint);
		}
		
		links = null;
		postInvalidate();
	}
}

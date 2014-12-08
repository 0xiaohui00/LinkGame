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
 * ����������Ϸ����
 * 
 * @author Sasure
 * @version 1.0
 */
public class GameView extends View
{
//	private Paint linkpaint;//����
	private Paint piecepaint;
	private Paint selectedpaint;
	private Paint pathpaint;
	private Path path;
	private int[][] pieces;//��ÿ��Ԫ�ؼ�ÿ�����飬0���ޣ���0���У�ÿ�������ֵ����һ����ͼƬID
//private Bitmap checkedBox;//ѡ�п��ͼƬ
	private Piece selectedPiece;//ѡ�п��λ��
//	private List<Point> links;//�����ߵ���Ϣ
	private Context context;//����������Ϣ
	
	/**
	 * Ψһ�Ĺ��캯��
	 */
	public GameView(Context context)
	{
		super(context);
		
		this.context = context;
		
//		linkpaint = new Paint();//��ʼ�����ʲ����û�������
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
		
	//	checkedBox = GameKit.getCheckedBox(context);//��ȡѡ�п��ͼƬ
		
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
	
	/**������������Ϣ
	 * 
	 * @param links
	 * @return void
	 */
//	public void setLinks(List<Point> links)
//	{
//		this.links = links;
//	}
	
	/**���÷��鼯����Ϣ
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
	 * ����ѡ�п�λ�õķ���
	 * @param checkedPoint
	 */
	public void setselectedPiece(Piece selectedPiece)
	{
		this.selectedPiece = selectedPiece;
	}
	
	/**������Ϸ����
	 * ��дView���onDraw����
	 */
	@Override 
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		if(pieces == null)//������޷������Ϣ��ֱ�ӷ���
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
	 * ���������ߵķ���
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

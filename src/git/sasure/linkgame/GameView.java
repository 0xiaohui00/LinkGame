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
 * ����������Ϸ����
 * 
 * @author Sasure
 * @version 1.0
 */
public class GameView extends View
{
	private Paint paint;//����
	private int[][] pieces;//��ÿ��Ԫ�ؼ�ÿ�����飬0���ޣ���0���У�ÿ�������ֵ����һ����ͼƬID
	private Bitmap checkedBox;//ѡ�п��ͼƬ
	private Piece checkedPiece;//ѡ�п��λ��
	private List<Point> links;//�����ߵ���Ϣ
	private Context context;//����������Ϣ
	
	/**
	 * Ψһ�Ĺ��캯��
	 */
	public GameView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		this.context = context;
		
		paint = new Paint();//��ʼ�����ʲ����û�������
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(5);
		
		checkedBox = GameKit.getCheckedBox(context);//��ȡѡ�п��ͼƬ
		
		checkedPiece = null;
		links = null;
		
		int[] loca = new int[2];
		this.getLocationOnScreen(loca);
		
		GameKit.setbeginloca(loca);
	}
	
	/**������������Ϣ
	 * 
	 * @param links
	 * @return void
	 */
	public void setLinks(List<Point> links)
	{
		this.links = links;
	}
	
	/**���÷��鼯����Ϣ
	 * 
	 * @param pieces
	 * @return void
	 */
	public void setPieces(int[][] pieces)
	{
		this.pieces = pieces;
	}
	
	/**
	 * ����ѡ�п�λ�õķ���
	 * @param checkedPoint
	 */
	public void setCheckedPiece(Piece checkedPiece)
	{
		this.checkedPiece = checkedPiece;
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
	 * ���������ߵķ���
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

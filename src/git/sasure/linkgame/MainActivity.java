package git.sasure.linkgame;

import java.util.List;

import git.sasure.Kit.GameKit;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

/**
 *  整个界面
 * @author Sasure
 * @version 1.0
 *
 */
public class MainActivity extends Activity 
{

	private GameView gameView;
	private Piece selected = null;
	private int[][] pieces;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
	}
	
	private void init()
	{
		gameView = (GameView) findViewById(R.id.gameView);
		
		pieces = GameKit.start(-1);
		gameView.setPieces(pieces);
		gameView.postInvalidate();
		
		gameView.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent e) 
			{				
				if (e.getAction() == MotionEvent.ACTION_DOWN)
				{
					gameViewTouchDown(e);
				}
				if (e.getAction() == MotionEvent.ACTION_UP)
				{
					gameViewTouchUp(e);
				}
				return true;
			}
		});
	}
	
	private void gameViewTouchDown(MotionEvent e)
	{
		Piece current = GameKit.findPiece(e.getX(), e.getY());
		
		if(current == null || pieces[current.i][current.j] == 0)
			return;
		
		gameView.setCheckedPiece(current);
		
		if (selected == null)
		{
			selected = current;
			gameView.postInvalidate();
			
			return;
		}
		
		if(selected != null)
		{
			List<Point> linkInfo = GameKit.link(selected, current,pieces);
			
			if(linkInfo == null)
			{
				selected = current;
				gameView.postInvalidate();
			}
			else
			{
				handleSuccessLink(linkInfo, this.selected,current, pieces);
			}
		}
	}
	
	private void handleSuccessLink(List<Point> linkInfo, Piece selected,
			Piece current, int[][] pieces)  
	{
		
		gameView.setLinks(linkInfo);
		gameView.setCheckedPiece(null);
		gameView.postInvalidate();
		
		pieces[current.i][current.j] = 0;
		pieces[selected.i][selected.j] = 0;
		
		this.selected = null;
	}

	private void gameViewTouchUp(MotionEvent e)
	{
		if(!GameKit.hasPieces(pieces))
		{
			Toast.makeText(getApplicationContext(), "您赢啦！！", Toast.LENGTH_LONG).show();
			
			pieces = GameKit.start(-1);
			gameView.setPieces(pieces);
			gameView.postInvalidate();
		}
	}
	
	
	
}

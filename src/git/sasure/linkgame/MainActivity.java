package git.sasure.linkgame;

import java.util.List;

import git.sasure.Abs.GameService;
import git.sasure.sub.GameLogic;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
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

	GameView gameView;
	GameService gameService;
	int[] selected = null;
	
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
		gameService = new GameLogic();
		
		gameService.start(0);
		gameView.setPieces(gameService.getPieces());
		gameView.postInvalidate();
		
		gameView.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent e) 
			{				
			//	Log.i("test", "e.getx():"+e.getX()+",e.gety():" + e.getY());
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
		int[][] pieces = gameService.getPieces();
		int[] current = gameService.findPiece(e.getX(), e.getY());
		
		if(current == null || pieces[current[0]][current[1]] == 0)
			return;
		
	//	Log.i("test", "current=" + new Point(current[0],current[1]));
	//	Log.i("test", "piece[i][j]="+pieces[current[0]][current[1]]);
		
		gameView.setCheckedPoint(current);
		
		if (selected == null)
		{
		//	Log.i("test", "selected=" + new Point(selected[0],selected[1]));
			selected = current;
			gameView.postInvalidate();
			
			return;
		}
		
		if(selected != null)
		{
			List<Point> linkInfo = gameService.link(selected, current);
			
			if(linkInfo == null)
			{
			//	Log.i("test", "linkInfo = null");
				selected = current;
				gameView.postInvalidate();
			}
			else
			{
			//	Log.i("test", "linkInfo != null");
				handleSuccessLink(linkInfo, this.selected,current, pieces);
			}
		}
	}
	
	private void handleSuccessLink(List<Point> linkInfo, int[] selected,
			int[] current, int[][] pieces)  
	{
	//	Log.i("test4", "linkInfo.size:"+linkInfo.size());
		
		gameView.setLinks(linkInfo);
		gameView.setCheckedPoint(null);
		gameView.postInvalidate();
		
		pieces[current[0]][current[1]] = 0;
		pieces[selected[0]][selected[1]] = 0;
		
		this.selected = null;
	}

	private void gameViewTouchUp(MotionEvent e)
	{
		if(!gameService.hasPieces())
		{
			Toast.makeText(getApplicationContext(), "您赢啦！！", Toast.LENGTH_LONG).show();
			
			gameService.start(0);
			gameView.setPieces(gameService.getPieces());
			gameView.postInvalidate();
		}
	}
	
	
	
}

package git.sasure.linkgame;

import java.util.List;

import git.sasure.Kit.GameKit;
import android.R.integer;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
	private View firstView;
	private View secondView;
	private View thirdView;
	private ImageButton start;
	private myRelativeLayout rl;
	private  FrameLayout fl;
//	private int[][] pieces;
	
	private boolean isbegin = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		fl = new FrameLayout(this);
		fl.setBackgroundColor(Color.BLACK);
		firstView = View.inflate(this, R.layout.activity_main, null);
		secondView = View.inflate(this, R.layout.below, null);
		thirdView = View.inflate(this, R.layout.topside, null);
		
		gameView = (GameView) firstView.findViewById(R.id.gameView);
		GameKit.setGameView(gameView);
		
		start = (ImageButton) thirdView.findViewById(R.id.start);
		rl = (myRelativeLayout) thirdView.findViewById(R.id.third);
		
		fl.addView(firstView,0);
		fl.addView(secondView,1);
		fl.addView(thirdView,2);
		
		setContentView(fl);
		
		start.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				start.setEnabled(false);
				
				rl.startAnimator(fl);
				isbegin = true;
			//	GameKit.start(-1);
			}
		});
		
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
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
	
	
	@Override
	public void onStop()
	{
		super.onStop();
	}
	
	@Override
	public void onResume()
	{
		if(isbegin == true)
			rebegin();
		
		super.onResume();
	}

	private void rebegin()
	{
		rl.setAlpha(1);
		start.setEnabled(true);
		start.setImageResource(R.drawable.rebegin);
		fl.addView(secondView,1);
		fl.addView(thirdView,2);
	}
	
	private void gameViewTouchDown(MotionEvent e)
	{
		Piece current = GameKit.findPiece(e.getX(), e.getY());
		int[][] pieces = gameView.getPieces();
		
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
		fl.setBackgroundColor(getApplicationContext().getResources().getColor(pieces[current.i][current.j]));
		gameView.setLinks(linkInfo);
		gameView.setCheckedPiece(null);
		gameView.postInvalidate();
		
		pieces[current.i][current.j] = 0;
		pieces[selected.i][selected.j] = 0;
		
		this.selected = null;
	}

	private void gameViewTouchUp(MotionEvent e)
	{
		int[][] pieces = gameView.getPieces();
		
		if(!GameKit.hasPieces(pieces))
		{
			Toast.makeText(getApplicationContext(), "您赢啦！！", Toast.LENGTH_LONG).show();
			
			pieces = GameKit.start(-1);
			fl.setBackgroundColor(Color.BLACK);
		}
	}
}

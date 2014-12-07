package git.sasure.linkgame;

import java.util.List;

import git.sasure.Kit.GameKit;
import git.sasure.sub.myFrameLayout;
import git.sasure.sub.myRelativeLayout;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 *  Õû¸ö½çÃæ
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
	private  myFrameLayout fl;
	private int backcolor;
	private int currentcolor;
//	private int[][] pieces;
	
	private boolean isbegin = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
		DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);
        
        GameKit.screenWidth = metric.widthPixels;  // ÆÁÄ»¿í¶È£¨ÏñËØ£©
        GameKit.screenHeight = metric.heightPixels;  // ÆÁÄ»¸ß¶È£¨ÏñËØ£©
        
        GameKit.Game_X_begin = 0;
        
        GameKit.PieceHeidth = GameKit.PieceWidth = ( GameKit.screenWidth - GameKit.Game_X_begin * 2 ) / GameKit.GameXN;
        
		GameKit.GameWidth = GameKit.GameXN * GameKit.PieceWidth;
		GameKit.GameHeight = GameKit.GameYN * GameKit.PieceHeidth;
        
        GameKit.Game_Y_begin = GameKit.screenHeight / 2 - GameKit.GameHeight / 2;
        
		fl = new myFrameLayout(this);
		
		backcolor = getResources().getColor(R.color.backcolor);
		fl.setBackgroundColor(backcolor);
		
		firstView = View.inflate(this, R.layout.activity_main, null);
		secondView = View.inflate(this, R.layout.below, null);
		thirdView = View.inflate(this, R.layout.topside, null);
		
		start = (ImageButton) thirdView.findViewById(R.id.start);
		rl = (myRelativeLayout) thirdView.findViewById(R.id.third);
		RelativeLayout mainlayout = (RelativeLayout) firstView.findViewById(R.id.mainlayout);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(GameKit.GameWidth,GameKit.GameHeight);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		gameView = new GameView(this);
		gameView.setLayoutParams(lp);
		
		mainlayout.addView(gameView);
		
		GameKit.setGameView(gameView);
		
		fl.addView(firstView);
		fl.addView(secondView);
		fl.addView(thirdView);
		
		setContentView(fl);
		
		start.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				start.setEnabled(false);
				isbegin = true;
				
				ObjectAnimator spreadAnimator = ObjectAnimator.ofInt(rl, "radius", rl.getRadius(),rl.getendradius());
				ObjectAnimator disappearAnimator = ObjectAnimator.ofFloat(rl, "alpha", 1 , 0);
				AnimatorSet set = new AnimatorSet();
				set.setInterpolator(new AccelerateDecelerateInterpolator ());
				spreadAnimator.setDuration(500);
				disappearAnimator.setDuration(500);
				
				set.play(spreadAnimator).before(disappearAnimator);
				
				spreadAnimator.addListener(new AnimatorListenerAdapter()
				{
					@Override
					public void onAnimationEnd(Animator animation) 
					{
						fl.removeView(secondView);

						GameKit.start(-1);
					}
				});
				
				disappearAnimator.addListener(new AnimatorListenerAdapter()
				{
					@Override
					public void onAnimationEnd(Animator animation) 
					{
						fl.removeView(rl);
						rl.setAlpha(1);
						rl.resetRadius();
						start.setEnabled(true);
					}
				});
				
				set.start();
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
		start.setImageResource(R.drawable.rebegin);
		fl.addView(secondView);
		fl.addView(thirdView);
	}
	
	private void gameViewTouchDown(MotionEvent e)
	{
		Piece current = GameKit.findPiece(e.getX(), e.getY());
		int[][] pieces = gameView.getPieces();
		
		if(current == null || pieces[current.i][current.j] == 0)
			return;
		
		gameView.setselectedPiece(current);
		
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
		currentcolor = getApplicationContext().getResources().getColor(pieces[current.i][current.j]);
		
		if(backcolor == currentcolor)
		{
			backcolor = currentcolor = getResources().getColor(R.color.backcolor);
		}
		else
		{
			backcolor = currentcolor;
		}
		
		Point point = GameKit.getScreenPoint(current.i, current.j);
		
		fl.startBackAnimator(point, currentcolor);
		
		gameView.setLinks(linkInfo);
		gameView.setselectedPiece(null);
		gameView.postInvalidate();
		
		pieces[current.i][current.j] = 0;
		pieces[selected.i][selected.j] = 0;
		
		this.selected = null;
	}

	private void gameViewTouchUp(MotionEvent e)
	{
		
		if(!GameKit.hasPieces())
		{
			Toast.makeText(getApplicationContext(), "ÄúÓ®À²£¡£¡", Toast.LENGTH_LONG).show();
			
		//	GameKit.start(-1);
			//fl.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.backcolor));
		}
	}
}

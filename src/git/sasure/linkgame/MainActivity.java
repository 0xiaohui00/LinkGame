package git.sasure.linkgame;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import git.sasure.Kit.GameKit;
import git.sasure.sub.GameView;
import git.sasure.sub.PointEvaluator;
import git.sasure.sub.animatorView;
import git.sasure.sub.linkAnimatorView;
import git.sasure.sub.myFrameLayout;
import git.sasure.sub.myRelativeLayout;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.style.LineBackgroundSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  整个界面
 * @author Sasure
 * @version 1.0
 *
 */
public class MainActivity extends Activity implements OnClickListener
{
	private static final int nocreated = 0;//当前尚未开始游戏
	private static final int pause = 1;//当前处于暂停
	private static final int playing = 2;
	private static final int tobegin = 3;
	
//	private static final String MAX = "max";
	
	private static final int defaultTime = 60;
	private static final int reducetime = 0x11;

	private static final int perAdd = 10;
	private static final int perRed = 5;
	private int currentgrade = 0;
	private int maxgrade = 0;
	
	private static  final int  maxshuffle = 2;
	private static final int maxaddtime = 2;
	
	private int shufflecount = 0;
	private  int addtimecount = 0;
	
	private Piece selected = null;
	
	private Vibrator vibrator;
	
	private SharedPreferences preferences;
	private Editor editor;
	
	private View firstView;
	private View secondView;
	private View thirdView;
	private animatorView foreView;
	private linkAnimatorView linkAn;
	
	private ImageView start;//开始
	private ImageView anyelse;//其他
	private ImageView rule;//规则
	private ImageView addTime;//加时
	private ImageView supspend;//暂停
	private ImageView sheffle;//洗牌
	private TextView gradeTextView;//分数
	private TextView maxgradeTextView;//最高分
	private ProgressBar schedule;

	private GameView gameView;
	private myRelativeLayout rl;
	private  myFrameLayout fl;

	private int backcolor;
	private int currentcolor;
	private int state = 0;
	private  Boolean toExit = false;
	
	private Animation traslateAnimation;
//	private Animation traslateAnimation2;
	
	private int time = defaultTime;
	private AlertDialog lostDialog;
	private AlertDialog successDialog;
	private AlertDialog ruleDialog;
	private AlertDialog anyelseDialog;
	private Timer timer;
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what == reducetime)
			{
				if(!GameKit.hasPieces())
				{
					stopTimer();
					successDialog.show();
					return;
				}
					
				schedule.setProgress(time);
				time--;
				
				if(time < 0)
				{
					lostDialog.show();
					MainActivity.this.vibrator.vibrate(1000);
					stopTimer();
					return;
				}
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		init();
		initView();
	}
	
	/**
	 * 初始化数据
	 */
	private void init()
	{
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
		DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);
        
        GameKit.screenWidth = metric.widthPixels;  // 屏幕宽度（像素）
        GameKit.screenHeight = metric.heightPixels;  // 屏幕高度（像素）
        
        GameKit.Game_X_begin = 0;
        
        GameKit.PieceHeidth = GameKit.PieceWidth = ( GameKit.screenWidth - GameKit.Game_X_begin * 2 ) / GameKit.GameXN;
        
		GameKit.GameWidth = GameKit.GameXN * GameKit.PieceWidth;
		GameKit.GameHeight = GameKit.GameYN * GameKit.PieceHeidth;
        
        GameKit.Game_Y_begin = GameKit.screenHeight / 2 - GameKit.GameHeight / 2;
        
        GameKit.defaultbackcolor = backcolor = getResources().getColor(R.color.backcolor);
        
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        
         preferences = getSharedPreferences("game", Context.MODE_PRIVATE);
//        int count = preferences.getInt("count", 0);
         editor = preferences.edit();
 //       Toast.makeText(this, count+"", Toast.LENGTH_LONG).show();
 //       editor.putInt("count", ++count);
 //      editor.commit();
	}

	/**
	 * 初始化界面
	 */
	private void initView()
	{
		fl = new myFrameLayout(this);
		fl.setBackgroundColor(backcolor);
		
		firstView = View.inflate(this, R.layout.activity_main, null);
		secondView = View.inflate(this, R.layout.below, null);
		thirdView = View.inflate(this, R.layout.topside, null);
		foreView = new animatorView(this);
		linkAn = new linkAnimatorView(this);
		
		gameView = new GameView(this);
		
		start = (ImageView) thirdView.findViewById(R.id.start);
		anyelse = (ImageView) secondView.findViewById(R.id.anyelse);
		rule = (ImageView) secondView.findViewById(R.id.rule);
		supspend = (ImageView) firstView.findViewById(R.id.supspend);
//		ring = (ImageView) firstView.findViewById(R.id.ring);
//		vibrate = (ImageView) firstView.findViewById(R.id.vibrate);
		addTime = (ImageView) firstView.findViewById(R.id.addtime);
		sheffle = (ImageView) firstView.findViewById(R.id.sheffle);
		maxgradeTextView =(TextView) firstView.findViewById(R.id.maxgrade);
		gradeTextView =(TextView) firstView.findViewById(R.id.grade);
		schedule = (ProgressBar) firstView.findViewById(R.id.schedule);
//		schedule.setMax(defaultTime);
		
		rl = (myRelativeLayout) thirdView.findViewById(R.id.third);
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(GameKit.GameWidth,GameKit.GameHeight);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		gameView.setLayoutParams(lp);
		((RelativeLayout) firstView.findViewById(R.id.mainlayout)).addView(gameView);
		
		GameKit.setGameView(gameView);

		fl.addView(secondView);
		fl.addView(thirdView);
		
		setContentView(fl);
		
		start.setOnClickListener(this);
		anyelse.setOnClickListener(this);
		rule.setOnClickListener(this);
		supspend.setOnClickListener(this);
//		ring.setOnClickListener(this);
//		vibrate.setOnClickListener(this);
		addTime.setOnClickListener(this);
		sheffle.setOnClickListener(this);
		
		gameView.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent e) 
			{		
				int[][] pieces = gameView.getPieces();
				
				if(pieces == null)
					return true;
					
				if (e.getAction() == MotionEvent.ACTION_DOWN)
				{
					gameViewTouchDown(e);
				}
//				if (e.getAction() == MotionEvent.ACTION_UP)
//				{
//					gameViewTouchUp(e);
//				}
				return true;
			}
		});
		
		View lostView = View.inflate(this, R.layout.overlayout, null);
		lostDialog = createDialog(lostView).setPositiveButton("重新开始", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				time = defaultTime;
				backcolor = GameKit.defaultbackcolor;
				fl.startBackAnimator(new Point(GameKit.screenWidth / 2, 0), GameKit.defaultbackcolor);
				GameKit.start(-1);
				shufflecount = 0;
				addtimecount = 0;
				resetGrade();
				startTime();
				lostDialog.dismiss();
			}
		}).create();
		
		View successView = View.inflate(this, R.layout.successlayout, null);
		successDialog = createDialog(successView).setPositiveButton("继续", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				time = defaultTime;
				backcolor = GameKit.defaultbackcolor;
				
				fl.startBackAnimator(new Point(GameKit.screenWidth / 2, 0), GameKit.defaultbackcolor);
				gameView.setselectedPiece(null);
				GameKit.start(-1);
				startTime();
				successDialog.dismiss();
			}
		}).create();
		
//		LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.rulelayout, null);
		View ruleView = View.inflate(this, R.layout.rulelayout, null);
		
		ruleDialog = createDialog(ruleView).setPositiveButton("确定", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				ruleDialog.dismiss();
			}
		}).create();
		
		View anyelseView = View.inflate(this, R.layout.anyelselayout, null);
		TextView author = (TextView) anyelseView.findViewById(R.id.toweibo);
		TextView address = (TextView) anyelseView.findViewById(R.id.togithub);
		
		author.setOnClickListener(this);
		address.setOnClickListener(this);
		
		anyelseDialog = createDialog(anyelseView).setPositiveButton("确定", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				ruleDialog.dismiss();
			}
		}).create();
		

		
		maxgrade = preferences.getInt("max", 0);
		flashmaxView();
		resetGrade();
		
		traslateAnimation = AnimationUtils.loadAnimation(this, R.anim.viewtranslate);

		thirdView.startAnimation(traslateAnimation);
		secondView.startAnimation(traslateAnimation);
	}
	
//	private AlertDialog.Builder createDialog(String title,String message)
//	{
//		AlertDialog.Builder tmp = new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(false);
//		
//		return tmp;
//	}
	
	private AlertDialog.Builder createDialog(View view)
	{
		AlertDialog.Builder tmp = new AlertDialog.Builder(this).setView(view).setCancelable(false);
		
		return tmp;
	}
	
	private void startTime()
	{
		if(timer == null)
		{
			this.timer = new Timer();
			// 启动计时器 ， 每隔1秒发送一次消息
			this.timer.schedule(new TimerTask()
			{
				public void run()
				{
					handler.sendEmptyMessage(reducetime);
				}
			}, 0, 1000);
		}
	}
	
	private void stopTimer()
	{
		// 停止定时器
		if(timer != null)
		{
			this.timer.cancel();
			this.timer = null;
		}
	}
	
//	private void nextGame()
//	{
//		if(this.timer != null)
//			stopTimer();
//		
//		startTime();
//	}
	
//	private void startGame()
//	{
//		if(this.timer != null)
//			stopTimer();
//		
//		startTime();
//	}
	

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.start:
			start.setEnabled(false);
			state = playing;
			
			thirdView.clearAnimation();
			secondView.clearAnimation();
			
//			rl.clearAnimation();
		//	traslateAnimation.cancel();
			ObjectAnimator spreadAnimator = ObjectAnimator.ofInt(rl, "radius", rl.getRadius(),rl.getendradius());
			ObjectAnimator disappearAnimator = ObjectAnimator.ofFloat(rl, "alpha", 1 , 0);
			AnimatorSet set = new AnimatorSet();
			set.setInterpolator(new DecelerateInterpolator ());
			spreadAnimator.setDuration(500);
			disappearAnimator.setDuration(500);
		
			set.play(spreadAnimator).before(disappearAnimator);
			
			spreadAnimator.addListener(new AnimatorListenerAdapter()
			{
				@Override
				public void onAnimationEnd(Animator animation) 
				{
					fl.removeView(secondView);
				
//					start.setEnabled(false);
					
					fl.addView(firstView);
					fl.addView(foreView);
					fl.addView(linkAn);
					
					schedule.setMax(defaultTime);
					schedule.setProgress(time);
					
					if(!GameKit.hasPieces())
						GameKit.start(-1);
				}
			});
			
			disappearAnimator.addListener(new AnimatorListenerAdapter()
			{
				@Override
				public void onAnimationEnd(Animator animation) 
				{
					fl.removeView(thirdView);
					rl.resetRadius();
					rl.setAlpha(1);
					start.setEnabled(true);
					
					startTime();
				}
			});
			
			set.start();
			break;

		case R.id.anyelse:
			anyelse.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.mainanim));
			anyelseDialog.show();
			break;
		
		case R.id.rule:
			rule.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.mainanim));
			ruleDialog.show();
			break;
			
		case R.id.sheffle:
			if(++shufflecount <= maxshuffle)
			{
				sheffle.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.buttonrotate));
				Toast.makeText(MainActivity.this, "还剩" + (maxshuffle - shufflecount) +"次洗牌机会", Toast.LENGTH_SHORT).show();
				selected = null;
				gameView.setselectedPiece(null);
				GameKit.sheffle();
			}
			else
			{
				Toast.makeText(MainActivity.this, "已经用完机会...", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.addtime:
			if(++addtimecount <= maxaddtime)
			{
				Animation at = AnimationUtils.loadAnimation(MainActivity.this, R.anim.mainanim);
			//	at.setRepeatMode(Animation.REVERSE);
				at.setAnimationListener(new AnimationListener()
				{	
					@Override
					public void onAnimationStart(Animation animation)
					{
						stopTimer();
						time = time + 15 < defaultTime ? time + 15 : defaultTime;
						ObjectAnimator oa = ObjectAnimator.ofInt(schedule, "progress", schedule.getProgress(),time);
						oa.setDuration(300);
						oa.setInterpolator(new DecelerateInterpolator());
						oa.addListener(new AnimatorListenerAdapter() 
						{
							@Override
							public void onAnimationEnd(Animator animation) 
							{
								startTime();
								Toast.makeText(MainActivity.this, "还剩" + (maxaddtime - addtimecount) +"次加时机会", Toast.LENGTH_SHORT).show();
							}
						});
						oa.start();
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) 
					{
					}
				});
				
				addTime.startAnimation(at);
//				handler.sendEmptyMessage(reducetime);
			}
			else
			{
				Toast.makeText(MainActivity.this, "已经用完机会...", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.supspend:
			 pause();
			break;
		
		case R.id.toweibo:
			Uri uri = Uri.parse("http://weibo.com/u/1776221183?from=feed&loc=avatar");
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
			break;
			
		case R.id.togithub:
			Uri uri2 = Uri.parse("https://github.com/Sasure/LinkGame");
			Intent it2 = new Intent(Intent.ACTION_VIEW, uri2);
			startActivity(it2);
			break;
			
		default:
			break;
		}
	}

	private void pause()
	{
		stopTimer();
		state = pause;
		rebegin();
	}
	
	/** 
	 * 菜单、返回键响应 
	 */  
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if(keyCode == KeyEvent.KEYCODE_BACK)  
	       {    
	           exitBy2Click();      //调用双击退出函数  
	       }  
	    return false;  
	}  
	
	/** 
	 * 双击退出函数 
	 */ 
	private void exitBy2Click() 
	{  
	    Timer tExit = null;  
	    
	    if (toExit == false) 
	    {  
	    	if(state == playing)
	    	{
	    		 pause();
	    	}
	    	toExit = true; // 准备退出 
	        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
	        tExit = new Timer();  
	        tExit.schedule(new TimerTask() 
	        {  
	            @Override  
	            public void run() 
	            {  
	            	toExit = false; // 取消退出  
	            }  
	        }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  
	  
	    } else 
	    {  
	        finish();  
	        System.exit(0);  
	    }  
	}  
	
	
	@Override
	public void onPause()
	{
		if(state == playing)
		{
			state = pause;
			stopTimer();
		}
		
		super.onPause();
	}
	
	@Override
	public void onStop()
	{
		if(state == playing)
			state = pause;
		

		super.onStop();
	}
	
//	@Override
//	public void onDestroy()
//	{
//		state = nocreated;
//		start.setImageResource(R.drawable.start);
//		editor.putInt("max", maxgrade);
//		editor.commit();
//		super.onDestroy();
//	}
	
	@Override
	public void onResume()
	{
		if(state != nocreated)
		{
			rebegin();
		}
		
		super.onResume();
	}

//	@Override
//	public void onRestart()
//	{
//		super.onRestart();
//	}
	
	private void rebegin()
	{
		if(state == pause)
		{
			start.setImageResource(R.drawable.rebegin);
			fl.addView(secondView);
			fl.addView(thirdView);
			fl.removeView(firstView);
			fl.removeView(foreView);
			fl.removeView(linkAn);
			
			thirdView.startAnimation(traslateAnimation);
			secondView.startAnimation(traslateAnimation);
			state = tobegin;
		}
	}
	
	private void addGrade(int add)
	{
		currentgrade += add;
		flashcurrentView();
		
		if(maxgrade < currentgrade)
		{
			maxgrade = currentgrade;
			flashmaxView();
		}
	}

	private void reduceGrade(int red)
	{
		if(currentgrade - red >= 0)
		{
			currentgrade -= red;
		}
		
		flashcurrentView();
	}
	
	private void flashmaxView()
	{
		maxgradeTextView.setText("最高："+maxgrade + "");
		
		editor.putInt("max", maxgrade);
		editor.commit();
	}
	
	private void flashcurrentView()
	{
		gradeTextView.setText("目前："+currentgrade +"");
	}
	
	private void resetGrade()
	{
		currentgrade = 0;
		flashcurrentView();
	}
	
	private void gameViewTouchDown(MotionEvent e)
	{
		Piece current = GameKit.findPiece(e.getX(), e.getY());
		int[][] pieces = gameView.getPieces();
		
		if(current == null || pieces[current.i][current.j] == 0)
			return;

		foreView.startforeAnimator(GameKit.getScreenPoint(current.i,current.j));
		
		gameView.setselectedPiece(current);
		
		if (selected == null)
		{
			selected = current;
			gameView.postInvalidate();
			
			return;
		}
		else if(!(selected.i == current.i && selected.j == current.j))
		{
			List<Point> linkInfo = GameKit.link(selected, current,pieces);
			
			if(linkInfo == null)
			{
				this.vibrator.vibrate(150);
			//	selected = current;
				selected = null;
				reduceGrade(perRed);
				gameView.setselectedPiece(null);
				gameView.postInvalidate();
			}
			else
			{
				handleSuccessLink(linkInfo, this.selected,current, pieces);
			}
		}
	}
	
	private void handleSuccessLink(List<Point> linkInfo, final Piece selected,final Piece current, final int[][] pieces)  
	{
		AnimatorSet set = linkAnimatorSet(linkInfo);
		addGrade(perAdd);
		currentcolor = getApplicationContext().getResources().getColor(pieces[current.i][current.j]);
		
		if(backcolor == currentcolor)
		{
			backcolor = currentcolor = GameKit.defaultbackcolor;
			addGrade(perAdd);
		}
		else
		{
			backcolor = currentcolor;
		}
		
		final Point point = GameKit.getScreenPoint(current.i, current.j);
		
		linkAn.setPaintColor(currentcolor);
		
		set.addListener(new AnimatorListenerAdapter() 
		{
			
			@Override
			public void onAnimationStart(Animator animation) 
			{
				pieces[selected.i][selected.j] = 0;
				
				gameView.postInvalidate();
			}
			
			@Override
			public void onAnimationEnd(Animator animation) 
			{
				pieces[current.i][current.j] = 0;
				
				if(pieces[selected.i][selected.j] == 0 && pieces[current.i][current.j] == 0)
					gameView.setselectedPiece(null);
				
				gameView.postInvalidate();
				fl.startBackAnimator(point, currentcolor);
			}
		});

		set.setDuration(200);
		set.setInterpolator(new DecelerateInterpolator());
		
		set.start();
		this.selected = null;
	}

	/**
	 * 将连接线转化成动画
	 * @param linkInfo
	 * @return 动画
	 */
	private AnimatorSet linkAnimatorSet(List<Point> linkInfo)
	{
		AnimatorSet set = new AnimatorSet();
		
		switch(linkInfo.size())
		{
		case 2:
			set.play(perstep(linkInfo.get(0), linkInfo.get(1)));
			return set;
			
		case 3:
		ObjectAnimator oa1 = perstep(linkInfo.get(0), linkInfo.get(1));
		ObjectAnimator oa2 = perstep(linkInfo.get(1), linkInfo.get(2));
		set.play(oa1).before(oa2);
		return set;
		
		case 4:
		ObjectAnimator oa3 = perstep(linkInfo.get(0), linkInfo.get(1));
		ObjectAnimator oa4 = perstep(linkInfo.get(1), linkInfo.get(2));
		ObjectAnimator oa5 = perstep(linkInfo.get(2), linkInfo.get(3));
		set.play(oa3).before(oa4);
		set.play(oa5).after(oa4);
		
		return set;
		}
		return set;
	}
	
	/**
	 * 将两点转化成动画
	 * @param current
	 * @param next
	 * @return
	 */
	private ObjectAnimator perstep(Point current,Point next)
	{
		ObjectAnimator oa = ObjectAnimator.ofObject(linkAn, "point", new PointEvaluator(), current,next);
		
		return oa;
	}

	

	
//	private void gameViewTouchUp(MotionEvent e)
//	{
//		
//		if(!GameKit.hasPieces())
//		{
//			successDialog.show();
//			stopTimer();
//		//	backcolor = currentcolor = GameKit.defoultbackcolor;
//		}
//	}


}

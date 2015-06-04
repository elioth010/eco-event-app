package com.bytesw.consultadecuentas.view.activity;

import java.io.Serializable;
import java.net.ConnectException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.springframework.web.client.ResourceAccessException;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bytesw.consultadecuentas.R;
import com.bytesw.consultadecuentas.bs.service.GetEstadoCuentaTask;
import com.bytesw.consultadecuentas.view.fragment.EstadoCuentaTableFragment;
import com.bytesw.consultadecuentas.view.res.FlexibleScrollViewWithToolbar;
import com.bytesw.consultadecuentas.view.transition.RevealTransition;
import com.bytesw.mobile.demo.eis.dto.EstadoCuentaDTO;
import com.bytesw.mobile.demo.eis.dto.ProductoDTO;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;


public class EstadoCuentaActivity extends AbstractActivity implements ObservableScrollViewCallbacks{

	protected static final String TAG = "EstadoCuentaActivity";
	//private RelativeLayout cardViewHeader;
	//private Drawable mActionBarBackgroundDrawable;
	private Integer originalOrientation = 0;
	//private ImageView banner;
	private Toolbar mToolbar;
	private ProductoDTO producto;
	private List<EstadoCuentaDTO> estadoCuentaDTOs;
	//private NotifyingmScrollView mScrollView;
	private EstadoCuentaTableFragment mTableFragment;
	private DisplayMetrics metrics = new DisplayMetrics();
	
	protected TextView productName;
	protected TextView accountNumber;
	protected TextView productAlias;
//	protected TextView dateAccountState;
	protected TextView moneyAvailable;
	protected TextView moneyAccountant;
	protected TextView moneyReserve;
	protected TextView moneyWithholding;
	private static GetEstadoCuentaTask task;
	private static Handler handler = new Handler();
	//protected Drawable mActionBarBackgroundDrawable;
	protected String date;
	
    private FlexibleScrollViewWithToolbar mScrollView;

    protected ImageView flImage; //Layout that hosts the header image
	
	public static final String EXTRA_EPICENTER = "EXTRA_EPICENTER";
	
	private int count=0;
	
	private int countLinks = 0; 
	
	private int mParallaxImageHeight;
	private int mScrollY = 0; // Keeps track of our scroll.
	private boolean mIsToolbarShown = true;
	private int mToolbarHeight;
	private boolean goingUp = false;
	
	private int mToolbarBackgroundColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_cuenta);
		countLinks = 0;
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		if(task!=null){
			task.cancel(true);
		}
		productName = (TextView) findViewById(R.id.static_card_product_name);
		accountNumber = (TextView) findViewById(R.id.static_card_product_number);
		productAlias = (TextView) findViewById(R.id.static_card_product_alias);
//		dateAccountState = (TextView) findViewById(R.id.static_card_date_account_state);
		moneyAvailable = (TextView) findViewById(R.id.card_product_money_available_value);
		moneyAccountant = (TextView) findViewById(R.id.card_product_money_accountant_value);
		moneyReserve = (TextView) findViewById(R.id.card_product_money_reserve_value);
		moneyWithholding = (TextView) findViewById(R.id.card_product_money_withholding_value);
		
		mToolbarBackgroundColor = getResources().getColor(R.color.primary);
		mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
		
	    mScrollView = (FlexibleScrollViewWithToolbar) findViewById(R.id.scroll_view);

	    flImage = (ImageView) findViewById(R.id.image_header); //Layout that
		
		if(savedInstanceState != null){
			count = savedInstanceState.getInt("count");
			date = savedInstanceState.getString("date");			
		}
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			initTransitions();	

		try {
			Bundle bundle = getIntent().getExtras();
			originalOrientation = bundle.getInt("startOrientation");
			producto = (ProductoDTO) bundle.get("producto");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		mToolbar = (Toolbar) findViewById(R.id.include_toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
				getSupportActionBar().setElevation(0);
			}
			
			TypedValue tv = new TypedValue();
	        if (this.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
	            mToolbarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
	        }
	        
			Log.e("MENU", "toolbar set OK: "+mToolbarHeight);
			if(producto!=null){
				//mToolbar.setTitle(producto.getNombreCuenta());
				mToolbar.setTitle("");
				//mToolbar.setTitleTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Medium);
				//mToolbar.setTitleTextColor(getResources().getColor(R.color.icons));
				//mToolbar.setSubtitle(producto.getNumeroCuenta());
				//mToolbar.setSubtitleTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Small);
				//mToolbar.setSubtitleTextColor(getResources().getColor(R.color.gray_light));
				setBackgroundAlpha(mToolbar, 0.0f, mToolbarBackgroundColor);
				//mToolbar.setAlpha(0f);
				//getSupportActionBar().hide();
				productName.setText(producto.getDescripcionProducto());	
				accountNumber.setText(producto.getNumeroCuenta());
				productAlias.setText(producto.getNombreCuenta());
//				
				Locale locale = new Locale(this.getString(R.string.app_language), this.getString(R.string.app_country));
				NumberFormat guatemalaFormat = NumberFormat.getCurrencyInstance(locale);
				
				moneyAvailable.setText(guatemalaFormat.format(producto.getSaldo1()));
     			moneyAccountant.setText(guatemalaFormat.format(producto.getSaldo2()));
				moneyReserve.setText(guatemalaFormat.format(producto.getSaldo3()));
				moneyWithholding.setText(guatemalaFormat.format(producto.getSaldo4()));
				
			}
		}

		mScrollView.setOnScrollChangedListener(mOnScrollChangedListener);
		mScrollView.setOnTouchListener(new OnTouchListener() {
			
			final int MIN_DISTANCE = 150;
			float downX, downY, upX, upY;

			public final void onRightToLeftSwipe() {
				Log.i(TAG, "RightToLeftSwipe!");
				
			}

			public void onLeftToRightSwipe() {
				Log.i(TAG, "LeftToRightSwipe!");
				EstadoCuentaActivity.this.onLeftSwipe();
			}
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						downX = event.getX();
						downY = event.getY();
						return true;
					}
					
					case MotionEvent.ACTION_UP: {
						upX = event.getX();
						upY = event.getY();
						float deltaX = downX - upX;
						float deltaY = downY - upY;
	
						// swipe horizontal?
						if (Math.abs(deltaX) > MIN_DISTANCE && Math.abs(deltaY) < MIN_DISTANCE) {
							Log.i(TAG, Math.abs(deltaX) +", "+Math.abs(deltaY));
							// left or right
							if (deltaX < 0) {
								this.onLeftToRightSwipe();
								return true;
							}
							if (deltaX > 0) {
								this.onRightToLeftSwipe();
								return true;
							}
						} else {
							Log.i(TAG, "Swipe was only " + Math.abs(deltaX) +", "+Math.abs(deltaY)+ " long, need at least " + MIN_DISTANCE);
						}
					}
				}
				return false;
			}
		});

		configureScrollView();
	}
	
	@SuppressWarnings("deprecation")
	private void configureScrollView() {
        mScrollView.setScrollViewCallbacks(this);
        mScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mScrollView.setOnFlingListener(new FlexibleScrollViewWithToolbar.OnFlingListener() {
            @Override
            public void onFlingStarted() {
                if (goingUp && !mIsToolbarShown) {
                    showFullToolbar(50);
                }
            }

            @Override
            public void onFlingStopped() {

            }
        });

        ViewTreeObserver vtoProduct = productAlias.getViewTreeObserver();
        ViewTreeObserver vtoAccount = accountNumber.getViewTreeObserver();
        ViewTreeObserver vtoProductName = productName.getViewTreeObserver();
        vtoProduct.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                	productAlias.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                	productAlias.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                updateFlexibleSpaceText(0);
            }
        });
        
        vtoAccount.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                	accountNumber.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                	accountNumber.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                updateFlexibleSpaceText(0);
            }
        });
        
        vtoProductName.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                	productName.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                	productName.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                updateFlexibleSpaceText(0);
            }
        });
    }
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initTransitions() {
        TransitionInflater inflater = TransitionInflater.from(this);
        Window window = getWindow();
        RevealTransition reveal = createRevealTransition();
        Transition otherEnterTransition = inflater.inflateTransition(R.transition.master_detail_enter);
        window.setEnterTransition(sequence(reveal, otherEnterTransition));

        Transition otherReturnTransition = inflater.inflateTransition(R.transition.master_detail_return);
        window.setReturnTransition(sequence(otherReturnTransition, reveal.clone()));

        Transition shareTransitionClone = window.getSharedElementReturnTransition().clone();
        //shareTransitionClone.setStartDelay(100);
        window.setSharedElementReturnTransition(shareTransitionClone);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private TransitionSet sequence(Transition... transitions) {
        TransitionSet enterTransition = new TransitionSet();
        enterTransition.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        for (Transition t:transitions) {
            enterTransition.addTransition(t);
        }
        return enterTransition;
    }
       
    private RevealTransition createRevealTransition() {
        Point epicenter = getIntent().getParcelableExtra(EXTRA_EPICENTER);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int bigRadius = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
        RevealTransition reveal = new RevealTransition(epicenter, 0, bigRadius, 500);
        return reveal;
    }
	
	private void onLeftSwipe() {
		if(Build.VERSION.SDK_INT >= 21){
			if (originalOrientation == getResources().getConfiguration().orientation) {
				super.finishAfterTransition();
			} else {
				NavUtils.navigateUpFromSameTask(this);
			}
		}else{
			NavUtils.navigateUpFromSameTask(this);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Handler handler = new Handler();

		handler.postDelayed(new Runnable() 
		{

			@Override
			public void run()

			{
				FragmentManager fm = getSupportFragmentManager();
				if (isDeviceOnline()) {
					mTableFragment = new EstadoCuentaTableFragment();
                    mTableFragment.setEstadoDTOs(fillEstadoCuenta());
					FragmentTransaction ft = fm.beginTransaction();
					ft.replace(R.id.container, mTableFragment);
					ft.commit();
				} else {
					if(isAttached())
						Toast.makeText(getApplicationContext(), "No estas Conectado a internet", Toast.LENGTH_SHORT).show();
				}
				
			}
		}, 500);
	}
	
	

	private FlexibleScrollViewWithToolbar.OnScrollChangedListener mOnScrollChangedListener = new FlexibleScrollViewWithToolbar.OnScrollChangedListener() {
		public void onScrollChanged(ScrollView who, int l, int t, int oldl,int oldt) {
//			final int headerHeight = banner.getHeight() - mToolbar.getHeight();
//			final float ratio = (float) Math.min(Math.max(t, 0), headerHeight)/ headerHeight;
//			int inverseAlpha = 255;
//			final int newAlpha = (int) (ratio * 255);	
//			cardViewHeader.setAlpha(inverseAlpha - newAlpha);
//			System.out.println(newAlpha);
//			mActionBarBackgroundDrawable.setAlpha(newAlpha);
//			mToolbar.setBackground(mActionBarBackgroundDrawable);
//			mToolbar.setAlpha(newAlpha);
//			if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP){
//				float scaleFactor = metrics.density;				
//				getSupportActionBar().setElevation(8 * scaleFactor);
//			}
			
			View view = (View) mScrollView.getChildAt(mScrollView.getChildCount() - 1);
		    int diff = (view.getBottom() - (mScrollView.getHeight() + mScrollView.getScrollY()));

		    // if diff is zero, then the bottom has been reached
		    if (diff == 0) {
		       // Toast.makeText(EstadoCuentaActivity.this, "Esta en el final", Toast.LENGTH_SHORT).show();
		    	addMoreItemsScrollView();
		    	return;
		    }
		}
	};
	
	
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		count = savedInstanceState.getInt("count");
		date = savedInstanceState.getString("date");
	};
	
	private List<EstadoCuentaDTO> fillEstadoCuenta(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date fecha = GregorianCalendar.getInstance().getTime();
		String formatedDate = dateFormat.format(fecha);
		String reportDate = formatedDate.split("/")[0]+formatedDate.split("/")[1];
		//if(date==null){
			//String reportDate = "201302";
			this.date = reportDate;
		//}
		//dateAccountState.setText(formatedDate.split("/")[0]+"/"+formatedDate.split("/")[1]);

		task = new GetEstadoCuentaTask(this, this.getSession().getToken(), this.getSession().getUser(), this.getSession().getTokenType(), this.producto, this.countLinks, date);
		try {
			estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
			estadoCuentaDTOs.addAll(task.execute().get());
		} catch (InterruptedException e) {
			estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
			e.printStackTrace();
		} catch (ExecutionException e) {
			estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
			e.printStackTrace();
		}
		countLinks++;
		return estadoCuentaDTOs;
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		countLinks = 0;
	}
	
	@Override
	public void onBackPressed() {
		task.cancel(true);
		super.onBackPressed();
	}
	
	protected List<EstadoCuentaDTO> fillEstadoCuentaBackMonth(){
		String currentDate = date;
		String month = currentDate.substring(4, 6);
		String newDate="";
		String year = currentDate.substring(0,4);
		int currYear = Integer.parseInt(year);
		int newmonth = Integer.parseInt(month)-1;
		if(newmonth <0){
			newmonth = 12;
			currYear=currYear-1;
		}
		
		if(newmonth < 10){
			newDate = currYear+"0"+newmonth;
		}else{
			newDate = currYear+""+newmonth;
		}
		this.date = newDate;
		task = new GetEstadoCuentaTask(this, this.getSession().getToken(), this.getSession().getUser(), this.getSession().getTokenType(), this.producto, countLinks, newDate);
		
		try {
			estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
			estadoCuentaDTOs.addAll(task.execute().get());
		} catch (InterruptedException e) {
			estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
			e.printStackTrace();
		} catch (ExecutionException e) {
			estadoCuentaDTOs = new ArrayList<EstadoCuentaDTO>();
			e.printStackTrace();
		}
		countLinks++;
		return estadoCuentaDTOs;
		
	}
	
	public void addMoreItemsToFill(){
		handler = new Handler();
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(!mScrollView.canScrollVertically(1)){
					System.out.println("Vengo a Buscar otra vz para terminar de llenar: "+countLinks);
					addMoreItemsScrollView();
					if(!mScrollView.canScrollVertically(1)){
						
						if(countLinks < 6)
							addMoreItemsToFill();
						
						System.out.println("No lleno voy a buscar");
					}
				}
			}
		}, 600);	
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("date", date);
		outState.putInt("count", count);
		outState.putSerializable("data", (Serializable)estadoCuentaDTOs);
		outState.putParcelable("EXTRA_EPICENTER", new Point());
	}
	
	@Override
	public void show(final String message) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(EstadoCuentaActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void handleException(final Exception e) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (e instanceof ResourceAccessException) {
					if (e.getCause() instanceof ConnectException) {
						show(EstadoCuentaActivity.this.getResources().getString(R.string.app_toast_error_server_connection_message));
					}
				}
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			if (Build.VERSION.SDK_INT >= 21) {
				if (originalOrientation == getResources().getConfiguration().orientation) {
					super.finishAfterTransition();
				} else {
					NavUtils.navigateUpFromSameTask(this);
				}
			} else {
				NavUtils.navigateUpFromSameTask(this);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void addMoreItemsScrollView() {
		if(countLinks < 6){
			Handler handler = new Handler();
	
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (isDeviceOnline()) {
						mTableFragment.addMoreItemsTable(getLayoutInflater(), (ViewGroup) findViewById(R.id.container), fillEstadoCuentaBackMonth());
					} else {
						if(isAttached())
							Toast.makeText(getApplicationContext(), "No estas Conectado a internet", Toast.LENGTH_SHORT).show();
						return;
					}
				}

			}, 600);
		}else{
			if(isAttached())
				Toast.makeText(this, "No hay mas datos disponibles", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
		//Store actual scroll state:
        if (mScrollY > scrollY) {
            goingUp = true;
        } else if (mScrollY < scrollY) {
            goingUp = false;
        }

        //If we're close to edge, show toolbar faster
        if (mScrollY - scrollY > 50 && !mIsToolbarShown) {
            showFullToolbar(50); //speed up
        } else if (mScrollY - scrollY > 0 && scrollY <= mParallaxImageHeight && !mIsToolbarShown) {
            showFullToolbar(250);
        }

        //Show or hide full toolbar color, so it will become visible over scrollable content:
        if (scrollY >= mParallaxImageHeight - mToolbarHeight) {
            setBackgroundAlpha(mToolbar, 1, mToolbarBackgroundColor);
        } else {
            setBackgroundAlpha(mToolbar, 0, mToolbarBackgroundColor);
        }

        //Translate flexible image in Y axis
        ViewHelper.setTranslationY(flImage, scrollY / 2);

        //Calculate flexible space alpha based on scroll state
        //float alpha = 1 - (float) Math.max(0, mParallaxImageHeight - (mToolbarHeight) - scrollY) / (mParallaxImageHeight - (mToolbarHeight * 1.5f));
        //setBackgroundAlpha(llTintLayer, a, getResources().getColor(R.color.icons));

        //Store last scroll state
        mScrollY = scrollY;

        //Move the flexible text
        updateFlexibleSpaceText((scrollY));
		
	}

	@Override
	public void onDownMotionEvent() {
		
	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState) {
		//If we're scrolling up, and are too far away from toolbar, hide it:
        if (scrollState == ScrollState.UP) {
            if (mScrollY > mParallaxImageHeight) {
                if (mIsToolbarShown) {
                    hideFullToolbar();
                }
            } else {
                // Don't hide toolbar yet
            }
        } else if (scrollState == ScrollState.DOWN) {
            //Show toolbar as fast as we're starting to scroll down
            if (!mIsToolbarShown) {
                showFullToolbar(250);
            }
        }
	}
	
	private void setBackgroundAlpha(View view, float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        view.setBackgroundColor(a + rgb);
    }
	
	public void showFullToolbar(int duration) {
        mIsToolbarShown = true;

        final AnimatorSet animatorSet = buildAnimationSet(duration, buildAnimation(mToolbar, -mToolbarHeight, 0), buildAnimation(productAlias, -mToolbarHeight, 0));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                updateFlexibleSpaceText(mScrollY); //dirty update fling-fix
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                updateFlexibleSpaceText(mScrollY); //dirty update fling-fix
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
        productName.setVisibility(View.VISIBLE);
        productAlias.setVisibility(View.VISIBLE);
    }

    private ObjectAnimator buildAnimation(View view, float from, float to) {
        return ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, from, to);
    }

    public void hideFullToolbar() {
        mIsToolbarShown = false;
        final AnimatorSet animatorSet = buildAnimationSet(250, buildAnimation(mToolbar, 0, (-mToolbarHeight+(8*metrics.scaledDensity))), buildAnimation(productAlias, 0, (-mToolbarHeight+((8*metrics.scaledDensity)))));
        animatorSet.start();
        
        productName.setVisibility(View.INVISIBLE);
        productAlias.setVisibility(View.INVISIBLE);
        //accountNumber.setVisibility(View.INVISIBLE);
    }

    private AnimatorSet buildAnimationSet(int duration, ObjectAnimator... objectAnimators) {

        AnimatorSet a = new AnimatorSet();
        a.playTogether(objectAnimators);
        a.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.accelerate_decelerate));
        a.setDuration(duration);
        return a;
    }

    /**
     * Scale title view and move it in Flexible space
     * @param scrollY
     */
    private void updateFlexibleSpaceText(final int scrollY) {
    	if (!mIsToolbarShown) return;

        int adjustedScrollY = scrollY;
        if (scrollY < 0) {
            adjustedScrollY = 0;
        } else if (scrollY > mParallaxImageHeight) {
            adjustedScrollY = mParallaxImageHeight;
        }
        float maxScale = 0.3f;
        if(super.getSizeOfScreenInInches() >= 7){
        	maxScale = 1.6f;
        }else{
        	if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
        		maxScale = 1.0f;
        	}
        }
        
        //float maxScale = 0.4f;
        float scale = maxScale * ((float) (mParallaxImageHeight - mToolbarHeight) - adjustedScrollY) / (mParallaxImageHeight - mToolbarHeight);
        if (scale < 0) {
            scale = 0;
        }

        ViewHelper.setPivotX(accountNumber, 0);
        ViewHelper.setPivotY(accountNumber, 0);
        ViewHelper.setScaleX(accountNumber, 1 + scale);
        ViewHelper.setScaleY(accountNumber, 1 + scale);

        int maxTitleTranslation = (int) (mParallaxImageHeight * 0.4f);
        int titleTranslation = (int) (maxTitleTranslation * ((float) scale / maxScale));
        ViewHelper.setTranslationY(accountNumber, titleTranslation);
        
        maxTitleTranslation = (int) ((mParallaxImageHeight) * 0.4f);
        titleTranslation = (int) (maxTitleTranslation * ((float) scale / maxScale));
        
        ViewHelper.setPivotX(productName, 0);
        ViewHelper.setPivotY(productName, 0);
        ViewHelper.setScaleX(productName, 1 + scale);
        ViewHelper.setScaleY(productName, 1 + scale);
        ViewHelper.setTranslationY(productName, titleTranslation);
        
        maxTitleTranslation = (int) ((mParallaxImageHeight) * 0.4f);
        titleTranslation = (int) (maxTitleTranslation * ((float) scale / maxScale));
        
        ViewHelper.setPivotX(productAlias, 0);
        ViewHelper.setPivotY(productAlias, 0);
        ViewHelper.setScaleX(productAlias, 1 + scale);
        ViewHelper.setScaleY(productAlias, 1 + scale);
        ViewHelper.setTranslationY(productAlias, titleTranslation);
    }

	public FlexibleScrollViewWithToolbar getmScrollView() {
		return mScrollView;
	}

	public void setmScrollView(FlexibleScrollViewWithToolbar mScrollView) {
		this.mScrollView = mScrollView;
	}
}
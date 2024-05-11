package ccx.gamestudio.triviamendoza;

import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;

import java.time.LocalDate;

import ccx.gamestudio.triviamendoza.Helpers.SharedResources;
import ccx.gamestudio.triviamendoza.Manager.ResourceManager;
import ccx.gamestudio.triviamendoza.Manager.SFXManager;
import ccx.gamestudio.triviamendoza.Manager.SceneManager;
import ccx.gamestudio.triviamendoza.Menus.MainMenu;
import ccx.gamestudio.triviamendoza.Menus.SplashScreens;


public class TriviaMendozaActivity extends BaseGameActivity {

	// Variables that hold references to the Layout and AdView
	public static final String ADS_BANNER_DEV ="ca-app-pub-3940256099942544/9214589741";
	public static final String ADS_BANNER_PRO ="ca-app-pub-9816948408338668/7932805578";
	public static FrameLayout mFrameLayout;
	public static AdView adView;

	public void ShowAds() {
		this.runOnUiThread(() -> {
			adView.setVisibility(View.VISIBLE);
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
		});
	}

	public void NoShowAds() {
		this.runOnUiThread(() -> {
			adView.setVisibility(View.INVISIBLE);
		});
	}

	@Override
	protected void onSetContentView() {
		final FrameLayout frameLayout = new FrameLayout(this);
		final FrameLayout.LayoutParams frameLayoutLayoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
				Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		adView = new AdView(this);
		adView.setAdUnitId(ADS_BANNER_PRO);
		adView.setVisibility(View.VISIBLE);
		adView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, 320));
		final FrameLayout.LayoutParams adViewLayoutParams =	new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,
				Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		this.mRenderSurfaceView = new RenderSurfaceView(this);
		mRenderSurfaceView.setRenderer(mEngine, this);
		final FrameLayout.LayoutParams surfaceViewLayoutParams =	new FrameLayout.LayoutParams(createSurfaceViewLayoutParams());
		frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
		frameLayout.addView(adView, adViewLayoutParams);
		this.setContentView(frameLayout, frameLayoutLayoutParams);
		mFrameLayout = frameLayout;
	}



	static float MAX_WIDTH_PIXELS = 2388, MAX_HEIGHT_PIXELS = 1080f;
	static float MIN_WIDTH_PIXELS = 1920f, MIN_HEIGHT_PIXELS = 1080f;
	static float DESIGN_WINDOW_WIDTH_PIXELS = 2388;
	static float DESIGN_WINDOW_HEIGHT_PIXELS = 1080f;
	static float DESIGN_WINDOW_WIDTH_INCHES = 5.0813646f;
	static float DESIGN_WINDOW_HEIGHT_INCHES = 2.5511832f;

	// ====================================================
	// VARIABLES
	// ====================================================
	public float cameraWidth;
	public float cameraHeight;
	public float actualWindowWidthInches;
	public float actualWindowHeightInches;
	public TriviaMendozaSmoothCamera mCamera;
	public static int numTimesActivityOpened;


	// ====================================================
	// CREATE ENGINE
	// ====================================================
	// Create and return a Switchable Fixed-Step Engine
	@Override
	public Engine onCreateEngine(final EngineOptions pEngineOptions) {
		return new SwitchableFixedStepEngine(pEngineOptions, 120, false);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		FillResolutionPolicy EngineFillResolutionPolicy = new FillResolutionPolicy() {
			@Override
			public void onMeasure(final Callback pResolutionPolicyCallback,
								  final int pWidthMeasureSpec, final int pHeightMeasureSpec) {
				super.onMeasure(pResolutionPolicyCallback, pWidthMeasureSpec, pHeightMeasureSpec);

				final int measuredWidth = MeasureSpec.getSize(pWidthMeasureSpec);
				final int measuredHeight = MeasureSpec.getSize(pHeightMeasureSpec);

				// Uncomment the following lines to log the pixel values needed
				// for setting up the design-window's values
				//Log.v("Andengine","Design window width & height (in pixels): " + measuredWidth + ", " + measuredHeight);
				//Log.v("Andengine","Design window width & height (in inches):	" + measuredWidth / getResources().getDisplayMetrics().xdpi + ", " + measuredHeight / getResources().getDisplayMetrics().ydpi);

				// Determine the device's physical window size.
				actualWindowWidthInches = measuredWidth / getResources().getDisplayMetrics().xdpi;
				actualWindowHeightInches = measuredHeight / getResources().getDisplayMetrics().ydpi;
				//Log.v("Andengine","Design window width & height (actual): "+actualWindowWidthInches + "," +actualWindowHeightInches);

				// Get an initial width for the camera, and bound it to the
				// minimum or maximum values.
				float actualScaledWidthInPixels = DESIGN_WINDOW_WIDTH_PIXELS * (actualWindowWidthInches / DESIGN_WINDOW_WIDTH_INCHES);
				float boundScaledWidthInPixels = Math.round(Math.max(Math.min(actualScaledWidthInPixels, MAX_WIDTH_PIXELS), MIN_WIDTH_PIXELS));

				// Get the height for the camera based on the width and the
				// height/width ratio of the device
				float boundScaledHeightInPixels = boundScaledWidthInPixels
						* (actualWindowHeightInches / actualWindowWidthInches);
				// If the height is outside of the set bounds, scale the width
				// to match it.
				if (boundScaledHeightInPixels > MAX_HEIGHT_PIXELS) {
					float boundAdjustmentRatio = MAX_HEIGHT_PIXELS / boundScaledHeightInPixels;
					boundScaledWidthInPixels *= boundAdjustmentRatio;
					boundScaledHeightInPixels *= boundAdjustmentRatio;
				} else if (boundScaledHeightInPixels < MIN_HEIGHT_PIXELS) {
					float boundAdjustmentRatio = MIN_HEIGHT_PIXELS / boundScaledHeightInPixels;
					boundScaledWidthInPixels *= boundAdjustmentRatio;
					boundScaledHeightInPixels *= boundAdjustmentRatio;
				}

				cameraHeight = boundScaledHeightInPixels;
				cameraWidth = boundScaledWidthInPixels;
				mCamera.set(0f, 0f, cameraWidth, cameraHeight);

			}
		};

		// Create the Camera and EngineOptions.
		mCamera = new TriviaMendozaSmoothCamera(0, 0, 240, 320, 4000f, 2000f, 0.5f);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR, EngineFillResolutionPolicy, mCamera);


		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getRenderOptions().setDithering(true);
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedMultiSampling(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);

		return engineOptions;
	}


	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) {
		ResourceManager.setup(this,
				(SwitchableFixedStepEngine) this.getEngine(),
				this.getApplicationContext(),
				cameraWidth,
				cameraHeight,
				cameraWidth / DESIGN_WINDOW_WIDTH_PIXELS,
				cameraHeight / DESIGN_WINDOW_HEIGHT_PIXELS);


		/*if (SharedResources.getIntFromSharedPreferences(SharedResources.SHARED_PREFS_ACTIVITY_START_COUNT) == 0) {
			SharedResources.writeIntToSharedPreferences(SharedResources.SHARED_PREFS_ACTIVITY_START_COUNT, 1);
		}

		numTimesActivityOpened = SharedResources.getIntFromSharedPreferences(SharedResources.SHARED_PREFS_ACTIVITY_START_COUNT_RATING) + 1;
		SharedResources.writeIntToSharedPreferences(SharedResources.SHARED_PREFS_ACTIVITY_START_COUNT_RATING, numTimesActivityOpened);

		SharedResources.writeIntToSharedPreferences(SharedResources.SHARED_PREFS_ACTIVITY_START_USER, SharedResources.getIntFromSharedPreferences(SharedResources.SHARED_PREFS_ACTIVITY_START_USER) + 1);
        */
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		ResourceManager.loadMenuResources();
		SceneManager.getInstance().showScene(new SplashScreens());
		//SceneManager.getInstance().showScene(new MainMenu());

		pOnCreateSceneCallback.onCreateSceneFinished(mEngine.getScene());
		SharedResources.writeFloatToSharedPreferences(SharedResources.SHARED_WINDOWS_WIDTH_INCHES, actualWindowWidthInches);


		//InAppLoginGooglePlayGameServices.getInstance().GoogleSignInitializate();
		//SaveGame.getInstance().GetVersion();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			if (!LocalDate.now().toString().equals(SharedResources.getStringFromSharedPreferences(SharedResources.SHARED_DATE))) {
				SharedResources.writeStringToSharedPreferences(SharedResources.SHARED_DATE, LocalDate.now().toString());
				SharedResources.writeIntToSharedPreferences(SharedResources.SHARED_COUNT_DATE, SharedResources.getIntFromSharedPreferences(SharedResources.SHARED_COUNT_DATE) + 1);
				SharedResources.writeIntToSharedPreferences(SharedResources.SHARED_DAY_OFF, 0);
			}
		}

	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	protected void onDestroy() {
		adView.destroy();
		super.onDestroy();
		System.exit(0);
	}

	protected void onPause() {
		adView.pause();
		super.onPause();
		if (isGameLoaded()) {
			SFXManager.pauseMusic();
			//SaveGame.getInstance().SaveUsersData();
		}
	}

	protected void onResume() {
		adView.resume();
		super.onResume();
		System.gc();
		if (isGameLoaded()) {
			//SaveGame.getInstance().SaveUsersData();
			SFXManager.pauseMusic();
			if (SceneManager.getInstance().mCurrentScene != null) {
				/*if (SceneManager.getInstance().mCurrentScene.getClass().equals(GameLevel.class)) {
					((GameLevel) SceneManager.getInstance().mCurrentScene).GamePause();
				}*/
			}
		}
	}
}

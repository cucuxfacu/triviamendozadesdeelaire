package ccx.gamestudio.triviamendoza;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.Objects;

import ccx.gamestudio.triviamendoza.Helpers.SharedResources;
import ccx.gamestudio.triviamendoza.Manager.ResourceManager;
import ccx.gamestudio.triviamendoza.Manager.SFXManager;
import ccx.gamestudio.triviamendoza.Manager.SceneManager;
import ccx.gamestudio.triviamendoza.Menus.LoginMenu;


public class TriviaMendozaActivity extends BaseGameActivity {

	// Variables that hold references to the Layout and AdView
	public static final String ADS_BANNER_DEV ="ca-app-pub-3940256099942544/9214589741";
	public static final String ADS_BANNER_PRO ="ca-app-pub-9816948408338668/7932805578";
	private static final int GOOGLE_SIGN_IN = 200;

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
		adView.setAdUnitId(ADS_BANNER_DEV);
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
	private static FirebaseAuth mAuth;
	private static FirebaseUser user = null;
	private Handler handler;
	private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
	private boolean showOneTapUI = true;

	private static GoogleSignInClient mGoogleSignClient;

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
				float boundScaledHeightInPixels = boundScaledWidthInPixels	* (actualWindowHeightInches / actualWindowWidthInches);
				// If the height is outside of the set bounds, scale the width
				// to match it.
				if (boundScaledHeightInPixels > MAX_HEIGHT_PIXELS)
				{
					float boundAdjustmentRatio = MAX_HEIGHT_PIXELS / boundScaledHeightInPixels;
					boundScaledWidthInPixels *= boundAdjustmentRatio;
					boundScaledHeightInPixels *= boundAdjustmentRatio;

				} else if (boundScaledHeightInPixels < MIN_HEIGHT_PIXELS)
				{
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
		//SceneManager.getInstance().showScene(new SplashScreens());
		SceneManager.getInstance().showScene(new LoginMenu());

		pOnCreateSceneCallback.onCreateSceneFinished(mEngine.getScene());
		SharedResources.writeFloatToSharedPreferences(SharedResources.SHARED_WINDOWS_WIDTH_INCHES, actualWindowWidthInches);


		mAuth = FirebaseAuth.getInstance();
		//InAppLoginGooglePlayGameServices.getInstance().GoogleSignInitializate();
		//SaveGame.getInstance().GetVersion();

		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			if (!LocalDate.now().toString().equals(SharedResources.getStringFromSharedPreferences(SharedResources.SHARED_DATE))) {
				SharedResources.writeStringToSharedPreferences(SharedResources.SHARED_DATE, LocalDate.now().toString());
				SharedResources.writeIntToSharedPreferences(SharedResources.SHARED_COUNT_DATE, SharedResources.getIntFromSharedPreferences(SharedResources.SHARED_COUNT_DATE) + 1);
				SharedResources.writeIntToSharedPreferences(SharedResources.SHARED_DAY_OFF, 0);
			}
		}*/
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}


	public static void SignInGoogle() {
		//LogInGoogle();
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
				.requestIdToken(ResourceManager.getActivity().getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		mGoogleSignClient = GoogleSignIn.getClient(ResourceManager.getActivity(), gso);
		LogOutGooglePlayGame();
		ResourceManager.getActivity().startActivityForResult(mGoogleSignClient.getSignInIntent(), GOOGLE_SIGN_IN);
	}

	public static void LogOutGooglePlayGame() {
		mGoogleSignClient.signOut().addOnCompleteListener(ResourceManager.getActivity(),
                task -> {
                    boolean successful = task.isSuccessful();
                });
		LogOutGoogleAndFirebase();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == GOOGLE_SIGN_IN && data != null) {
			try {
				Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
				GoogleSignInAccount account = task.getResult(ApiException.class);
				if (account != null) {
					user = FirebaseAuth(account);
					Log.v("Usuario", "Usuario google: " + account.getDisplayName());
					//if (user != null) {
					SharedResources.writeStringToSharedPreferences(SharedResources.SHARED_USER_EMAIL, account.getEmail());
					SharedResources.writeStringToSharedPreferences(SharedResources.SHARED_USER_NAME_LASTNAME, account.getDisplayName());

				}
			} catch (ApiException e) {
				ResourceManager.getActivity().runOnUiThread(() ->
						Toast.makeText(ResourceManager.getActivity(), String.valueOf(e),
								Toast.LENGTH_SHORT).show());
				Log.v("Usuario", "Usuario: " + String.valueOf(e));
			}
		}
	}

	private FirebaseUser FirebaseAuth(GoogleSignInAccount account) {
		try {
			AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

			if (mAuth.getCurrentUser() == null) {
				mAuth.signInWithCredential(credential).addOnCompleteListener(ResourceManager.getActivity(), task -> {
					if (task.isSuccessful()) {
						user = mAuth.getCurrentUser();
						SendLoginToFireBase();
						Log.v("Usuario", "Usuario firebase: " + user.getEmail());
					} else {
						String message = Objects.requireNonNull(task.getException()).getMessage();
						Log.v("Usuario", "Usuario error:  " + message);
					}
				});
				user = mAuth.getCurrentUser();
			} else {
				user = mAuth.getCurrentUser();
			}

		} catch (Exception e) {
			Log.v("Usuario", "Usuario Exception:  " + e.getMessage());
		}
		return user;
	}

	public static void LogOutGoogleAndFirebase() {
		FirebaseAuth.getInstance().signOut();
	}
	private static void SendLoginToFireBase() {
		FirebaseAnalytics analitycs = FirebaseAnalytics.getInstance(ResourceManager.getActivity());
		Bundle bundle = new Bundle();
		bundle.putString("Login", "usuario se loguea" );
		analitycs.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
	}


	// ====================================================
	// CICLO DE VIDA
	// ====================================================
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

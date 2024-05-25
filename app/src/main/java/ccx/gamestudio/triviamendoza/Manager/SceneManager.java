package ccx.gamestudio.triviamendoza.Manager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;

import ccx.gamestudio.triviamendoza.Layers.ManagedLayer;
import ccx.gamestudio.triviamendoza.ManagedScene;
import ccx.gamestudio.triviamendoza.Menus.LoginMenu;
import ccx.gamestudio.triviamendoza.Menus.MainMenu;

public class SceneManager {

	private static final SceneManager INSTANCE = new SceneManager();

	// ====================================================
	// VARIABLES
	// ====================================================
	public ManagedScene mCurrentScene;
	private ManagedScene mNextScene;
	private Engine mEngine = ResourceManager.getInstance().engine;
	private int mNumFamesPassed = -1;
	private boolean mLoadingScreenHandlerRegistered = false;

	private IUpdateHandler mLoadingScreenHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			mNumFamesPassed++;
			mNextScene.elapsedLoadingScreenTime += pSecondsElapsed;
			if (mNumFamesPassed == 1) {
				if (mCurrentScene != null) {
					mCurrentScene.onHideManagedScene();
					mCurrentScene.onUnloadManagedScene();
				}
				mNextScene.onLoadManagedScene();
			}

			if (mNumFamesPassed > 1 && mNextScene.elapsedLoadingScreenTime >= mNextScene.minLoadingScreenTime && mNextScene.isLoaded) {
				// Remove the loading screen that was set as a child in the
				// showScene() method.
				mNextScene.clearChildScene();
				mNextScene.onLoadingScreenUnloadAndHidden();
				mNextScene.onShowManagedScene();
				mCurrentScene = mNextScene;
				mNextScene.elapsedLoadingScreenTime = 0f;
				mNumFamesPassed = -1;
				mEngine.unregisterUpdateHandler(this);
				mLoadingScreenHandlerRegistered = false;
			}
		}

		@Override
		public void reset() {
		}
	};
	// Set to TRUE in the showLayer() method if the camera had a HUD before the
	// layer was shown.
	private boolean mCameraHadHud = false;
	// Boolean to reflect whether there is a layer currently shown on the
	// screen.
	public boolean mIsLayerShown = false;
	private Scene mPlaceholderModalScene;
	public ManagedLayer mCurrentLayer;

	// ====================================================
	// CONSTRUCTOR AND INSTANCE GETTER
	// ====================================================
	public SceneManager() {
	}

	public static SceneManager getInstance() {
		return INSTANCE;
	}

	public void showMainMenu() {
		showScene(MainMenu.getInstance());
	}

	public void ShowLoginMenu(){
		showScene(LoginMenu.getInstance());
	}

	public void showScene(final ManagedScene pManagedScene) {
		mEngine.getCamera().set(-ResourceManager.getInstance().cameraWidth / 2f,
				-ResourceManager.getInstance().cameraHeight / 2f,
				ResourceManager.getInstance().cameraWidth / 2f,
				ResourceManager.getInstance().cameraHeight / 2f);
		if (pManagedScene.hasLoadingScreen) {
			pManagedScene.setChildScene(pManagedScene.onLoadingScreenLoadAndShown(), true, true, true);
			if (mLoadingScreenHandlerRegistered) {
				mNumFamesPassed = -1;
				mNextScene.clearChildScene();
				mNextScene.onLoadingScreenUnloadAndHidden();
			} else {

				mEngine.registerUpdateHandler(mLoadingScreenHandler);
				mLoadingScreenHandlerRegistered = true;
			}

			mNextScene = pManagedScene;
			mEngine.setScene(pManagedScene);
			return;
		}

		mNextScene = pManagedScene;
		mEngine.setScene(mNextScene);

		if (mCurrentScene != null) {
			mCurrentScene.onHideManagedScene();
			mCurrentScene.onUnloadManagedScene();
		}

		mNextScene.onLoadManagedScene();
		mNextScene.onShowManagedScene();
		mCurrentScene = mNextScene;
	}

	public void showLayer(final ManagedLayer pLayer, final boolean pSuspendSceneDrawing,
			final boolean pSuspendSceneUpdates, final boolean pSuspendSceneTouchEvents) {

		if (mEngine.getCamera().hasHUD()) {
			mCameraHadHud = true;
		} else {
			mCameraHadHud = false;
			HUD placeholderHud = new HUD();
			mEngine.getCamera().setHUD(placeholderHud);
		}

		if (pSuspendSceneDrawing || pSuspendSceneUpdates || pSuspendSceneTouchEvents) {

			mEngine.getCamera().getHUD().setChildScene(pLayer, pSuspendSceneDrawing, pSuspendSceneUpdates,
					pSuspendSceneTouchEvents);
			mEngine.getCamera().getHUD().setOnSceneTouchListenerBindingOnActionDownEnabled(true);

			if (mPlaceholderModalScene == null) {
				mPlaceholderModalScene = new Scene();
				mPlaceholderModalScene.setBackgroundEnabled(false);
			}

			mCurrentScene.setChildScene(mPlaceholderModalScene, pSuspendSceneDrawing, pSuspendSceneUpdates,
					pSuspendSceneTouchEvents);
		} else {

			mEngine.getCamera().getHUD().setChildScene(pLayer);
		}

		pLayer.setCamera(mEngine.getCamera());

		pLayer.onShowManagedLayer();

		mIsLayerShown = true;

		mCurrentLayer = pLayer;
	}

	// Hides the open layer if one is open.
	public void hideLayer() {
		if (mIsLayerShown) {
			mEngine.getCamera().getHUD().clearChildScene();
			if (mCurrentScene.hasChildScene())
				if (mCurrentScene.getChildScene() == mPlaceholderModalScene)
					mCurrentScene.clearChildScene();
			if (!mCameraHadHud)
				mEngine.getCamera().setHUD(null);
			mIsLayerShown = false;
			mCurrentLayer = null;
		}
	}
}
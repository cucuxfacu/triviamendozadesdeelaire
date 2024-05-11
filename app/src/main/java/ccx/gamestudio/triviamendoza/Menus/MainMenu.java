package ccx.gamestudio.triviamendoza.Menus;

import android.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

/*import ccx.gamestudio.runtime.Helpers.InAppLoginGooglePlayGameServices;
import ccx.gamestudio.runtime.Helpers.InAppReview;
import ccx.gamestudio.runtime.Helpers.SharedResources;

import ccx.gamestudio.runtime.Layers.ExitGameLayer;
import ccx.gamestudio.runtime.Layers.GiftOfDayLayer;
import ccx.gamestudio.runtime.Layers.NotGemsGameLayer;
import ccx.gamestudio.runtime.Layers.OptionsLayer;
import ccx.gamestudio.runtime.Layers.UserTanksLayer;*/
import ccx.gamestudio.triviamendoza.Input.GrowButton;
import ccx.gamestudio.triviamendoza.Manager.ResourceManager;
import ccx.gamestudio.triviamendoza.R;
import ccx.gamestudio.triviamendoza.TriviaMendozaSmoothCamera;

public class MainMenu extends ManagedMenuScene {

	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final MainMenu INSTANCE = new MainMenu();
	private static final float mCameraHeight = ResourceManager.getInstance().cameraHeight;
	private static final float mCameraWidth = ResourceManager.getInstance().cameraWidth;
	private static final float mHalfCameraHeight = (ResourceManager.getInstance().cameraHeight / 2f);
	private static final float mHalfCameraWidth = (ResourceManager.getInstance().cameraWidth / 2f);

	// ====================================================
	// INSTANCE GETTER
	// ====================================================
	public static MainMenu getInstance() {
		return INSTANCE;
	}
	private Entity mHomeMenuScreen;
	private Entity mPowerTankScreen;

	private Sprite mTanAttackkBGSprite;
	private GrowButton mSettingButton;
	private GrowButton mAbout;
	private GrowButton mStore;
	private static Text mCoinText;
	private int intChange = 0;
	private boolean lock = false;
	private GrowButton btnChangePower;
	private GrowButton btnChangeFriction;
	private Sprite mCoinsChengePower;
	private static Text mTextCoinsChangePower;
	private Sprite mCoinsChangeFriction;
	private static Text mTextCoinsChangeFriction;

	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public MainMenu() {
		super(0.001f);
		this.setTouchAreaBindingOnActionDownEnabled(true);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
	}


	// ====================================================
	// METHODS
	// ====================================================

	@Override
	public void onHideScene() {}

	@Override
	public Scene onLoadingScreenLoadAndShown() {
		TriviaMendozaSmoothCamera.setupForMenus();
		final Scene MenuLoadingScene = new Scene();
		this.mTanAttackkBGSprite = new Sprite(0f, 0f, ResourceManager.menuBackgroundTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mTanAttackkBGSprite.setScale(ResourceManager.getInstance().cameraHeight / ResourceManager.menuBackgroundTR.getHeight());
		this.mTanAttackkBGSprite.setPosition((this.mTanAttackkBGSprite.getWidth() * this.mTanAttackkBGSprite.getScaleX()) / 2.3f, (this.mTanAttackkBGSprite.getHeight() * this.mTanAttackkBGSprite.getScaleY()) / 2f);
		this.mTanAttackkBGSprite.setZIndex(-999);
		MenuLoadingScene.attachChild(this.mTanAttackkBGSprite);
		MenuLoadingScene.attachChild(new Text(mHalfCameraWidth, mHalfCameraHeight, ResourceManager.fontDefaultTankAtack60, ResourceManager.getContext().getText(R.string.app_loading), ResourceManager.getActivity().getVertexBufferObjectManager()));
		return MenuLoadingScene;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		this.mTanAttackkBGSprite.detachSelf();
	}

	@Override
	public void onLoadScene() {

		GetInAppPurchase();
		this.mHomeMenuScreen = new Entity(0f, mCameraHeight) {
			boolean hasLoaded = false;
			@Override
			protected void onManagedUpdate(final float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);
				if (!this.hasLoaded) {
					this.hasLoaded = true;
					this.registerEntityModifier(new MoveModifier(0.20f, 0f, 0, 0f, 0f));
				}
			}
		};
		final Sprite MainTitleText = new Sprite(0f, 0f, ResourceManager.menuMainTitleTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		MainTitleText.setPosition(mCameraWidth / 2f, mCameraHeight /2);
		MainTitleText.setScale(0.5f);
		ShowAdBanner();

		/*GrowButton playButton = new GrowButton(mHalfCameraWidth + 750f, mHalfCameraHeight - 350f, ResourceManager.btnGreenCircleTTR) {
			@Override
			public void onClick() {
				if (!lock) {
					//SceneManager.getInstance().showScene(new WorldAndLevelSelectorMenu());
				}
			}
		};
		playButton.setScales(1f, 1.25f);
		final Sprite PlayIcon = new Sprite(0f, 0f, ResourceManager.btnIconsBtnTTR.getTextureRegion(10), ResourceManager.getActivity().getVertexBufferObjectManager());
		PlayIcon.setPosition(playButton.getWidth() / 2f, playButton.getHeight() / 2f);
		playButton.attachChild(PlayIcon);
		this.registerTouchArea(playButton);

		GrowButton mUser = new GrowButton(ResourceManager.getCamera().getWidth() - 100f, ResourceManager.getCamera().getHeight() - 100f, ResourceManager.btnBlueCircleTTR.getTextureRegion(0)) {
			@Override
			public void onClick() {
				//SceneManager.getInstance().showLayer(UserTanksLayer.getInstance(), false, false, true);
			}
		};
		Sprite mIconUser = new Sprite(0f, 0f, ResourceManager.btnIconsBtnTTR.getTextureRegion(9), ResourceManager.getActivity().getVertexBufferObjectManager());
		mIconUser.setPosition(mUser.getWidth() / 2f, mUser.getHeight() / 2f);
		mUser.attachChild(mIconUser);
		this.registerTouchArea(mUser);

		GrowButton mUserLeaderBoard = new GrowButton(mUser.getX(), mUser.getY() - 200f, ResourceManager.btnOrangeCircleTTR.getTextureRegion(1)) {
			@Override
			public void onClick() {
				//InAppLoginGooglePlayGameServices.getInstance().showLeaderboard();
			}
		};
		Sprite mIconLederBoard = new Sprite(mUserLeaderBoard.getWidth() / 2f, mUserLeaderBoard.getHeight() / 2f, ResourceManager.btnIconsBtnTTR.getTextureRegion(23), ResourceManager.getActivity().getVertexBufferObjectManager());
		mUserLeaderBoard.attachChild(mIconLederBoard);
		this.registerTouchArea(mUserLeaderBoard);

		GrowButton mGiftOfTheDay = getGrowButton(mUserLeaderBoard);
		this.registerTouchArea(mGiftOfTheDay);

		Sprite backgroundcoins = new Sprite(ResourceManager.btnCoinTR.getWidth() / 2f + 150f, ResourceManager.getCamera().getHeight() - (ResourceManager.btnGameCoinTR.getHeight() / 4f), ResourceManager.backgroundPoints, ResourceManager.getActivity().getVertexBufferObjectManager());
		backgroundcoins.setWidth(700f);
		backgroundcoins.setScale(0.5f);
		backgroundcoins.setAlpha(0.50f);

		Sprite mCoin = new Sprite(backgroundcoins.getWidth() / 2f - 300f, backgroundcoins.getHeight() / 2f, ResourceManager.btnGameCoinTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		mCoin.setScale(0.7f);
		backgroundcoins.attachChild(mCoin);

		Sprite backgroundGems = new Sprite(backgroundcoins.getX(), backgroundcoins.getY() - 100f, ResourceManager.backgroundPoints, ResourceManager.getActivity().getVertexBufferObjectManager());
		backgroundGems.setWidth(700f);
		backgroundGems.setScale(0.5f);
		backgroundGems.setAlpha(0.50f);

		Sprite mGems = new Sprite(backgroundGems.getWidth() / 2f - 300f, backgroundGems.getHeight() / 2f, ResourceManager.btnGemsTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		mGems.setScale(0.75f);
		backgroundGems.attachChild(mGems);

		mCoinText = new Text(0f, 0f, ResourceManager.fontDefaultTankAtack60, "0", 255, ResourceManager.getActivity().getVertexBufferObjectManager());
		mCoinText.setPosition(backgroundcoins.getWidth() / 2f + 50f, backgroundcoins.getHeight() / 2f);
		mCoinText.setScale(2f);
		backgroundcoins.attachChild(mCoinText);

		//final int gems = SharedResources.getIntFromSharedPreferences(SharedResources.SHARED_PREFS_GEMS);
		Text mGemsText = new Text(0f, 0f, ResourceManager.fontDefaultTankAtack60, "0", 255, ResourceManager.getActivity().getVertexBufferObjectManager());
		mGemsText.setPosition(backgroundGems.getWidth() / 2f + 50f, backgroundGems.getHeight() / 2);
		mGemsText.setScale(2f);
		backgroundGems.attachChild(mGemsText);

		mHomeMenuScreen.attachChild(backgroundcoins);
		mHomeMenuScreen.attachChild(backgroundGems);

		///Setting button
		this.mSettingButton = new GrowButton(ResourceManager.btnBlueCircleTTR.getWidth() / 2f + 50f, ResourceManager.btnBlueCircleTTR.getHeight() / 2f + 50f, ResourceManager.btnBlueCircleTTR.getTextureRegion(0)) {
			@Override
			public void onClick() {
				//SceneManager.getInstance().showLayer(OptionsLayer.getInstance(), false, false, true);
			}
		};
		Sprite mSettingIconButton = new Sprite(0f, 0f, ResourceManager.btnIconsBtnTTR.getTextureRegion(1), ResourceManager.getActivity().getVertexBufferObjectManager());
		mSettingIconButton.setPosition(mSettingButton.getWidth() / 2f, mSettingButton.getHeight() / 2f);
		this.mSettingButton.attachChild(mSettingIconButton);
		this.registerTouchArea(mSettingButton);

		///About button
		this.mAbout = new GrowButton(mSettingButton.getX(), mSettingButton.getY() + 200f, ResourceManager.btnBlueCircleTTR.getTextureRegion(0)) {
			@Override
			public void onClick() {
				ResourceManager.getActivity().runOnUiThread(() -> {
					final AlertDialog.Builder builder = new AlertDialog.Builder(ResourceManager.getActivity())
							.setIcon(R.mipmap.ic_launcher)
							.setTitle(ResourceManager.getContext().getString(R.string.app_name))
							.setMessage("HolaMundo")
							.setPositiveButton(ResourceManager.getContext().getText(R.string.app_back), (dialog, id) -> {
							});
					final AlertDialog alert = builder.create();
					alert.show();
					((TextView) alert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
				});
			}
		};
		Sprite mIconAbout = new Sprite(0f, 0f, ResourceManager.btnIconsBtnTTR.getTextureRegion(12), ResourceManager.getActivity().getVertexBufferObjectManager());
		mIconAbout.setPosition(mAbout.getWidth() / 2f, mAbout.getHeight() / 2f);
		this.mAbout.attachChild(mIconAbout);
		this.registerTouchArea(mAbout);

		///Exit button
		this.mStore = new GrowButton(mAbout.getX(), mAbout.getY() + 200f, ResourceManager.btnBlueCircleTTR.getTextureRegion(0)) {
			@Override
			public void onClick() {
				//SceneManager.getInstance().showScene(new ShopMenu());
			}
		};
		Sprite mIconStore = new Sprite(0f, 0f, ResourceManager.btnIconsBtnTTR.getTextureRegion(3), ResourceManager.getActivity().getVertexBufferObjectManager());
		mIconStore.setPosition(mStore.getWidth() / 2f, mStore.getHeight() / 2f);
		this.mStore.attachChild(mIconStore);
		this.registerTouchArea(mStore);

		///Store button
		GrowButton mExit = new GrowButton(mStore.getX(), mStore.getY() + 200f, ResourceManager.btnRedCircleTTR.getTextureRegion(0)) {
			@Override
			public void onClick() {
				//SceneManager.getInstance().showLayer(ExitGameLayer.getInstance(), false, false, true);
			}
		};
		Sprite mIconExit = new Sprite(0f, 0f, ResourceManager.btnIconsBtnTTR.getTextureRegion(6), ResourceManager.getActivity().getVertexBufferObjectManager());
		mIconExit.setPosition(mExit.getWidth() / 2f, mExit.getHeight() / 2f);
		mExit.attachChild(mIconExit);
		this.registerTouchArea(mExit);*/

		/*this.mHomeMenuScreen.attachChild(playButton);
		this.mHomeMenuScreen.attachChild(mUser);
		this.mHomeMenuScreen.attachChild(mUserLeaderBoard);
		this.mHomeMenuScreen.attachChild(mGiftOfTheDay);
		this.mHomeMenuScreen.attachChild(mSettingButton);
		this.mHomeMenuScreen.attachChild(mAbout);
		this.mHomeMenuScreen.attachChild(mStore);
		this.mHomeMenuScreen.attachChild(mExit);*/



		//InAppReview.getInstance().LoadAppReveview(SharedResources.getIntFromSharedPreferences(SharedResources.SHARED_PREFS_ACTIVITY_START_COUNT_RATING));
		//VersionControl.getInstance().ControlVersion();
		this.mHomeMenuScreen.attachChild(MainTitleText);
		this.attachChild(this.mHomeMenuScreen);
	}

	private void GetInAppPurchase() {
		//ShopMenu.getInstance().SetListenerBillingClient();
		//ShopMenu.getInstance().StartConectionInAppPurchase();
	}


	@NonNull
	private static GrowButton getGrowButton(GrowButton mUserLeaderBoard) {
		GrowButton mGiftOfTheDay = new GrowButton(mUserLeaderBoard.getX(), mUserLeaderBoard.getY() - 200f, ResourceManager.btnGreenCircleTTR.getTextureRegion(0)) {
			@Override
			public void onClick() {
				//SceneManager.getInstance().showLayer(GiftOfDayLayer.getInstance(), false, false, true);
			}
		};
		Sprite mIconGiftOfTheDay = new Sprite(mUserLeaderBoard.getWidth() / 2f, mUserLeaderBoard.getHeight() / 2f, ResourceManager.sackOfCoinsTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		mIconGiftOfTheDay.setScale(0.68f);
		mGiftOfTheDay.attachChild(mIconGiftOfTheDay);
		return mGiftOfTheDay;
	}

	private static void LeaderBoard() {
		/*if(SharedResources.getStringFromSharedPreferences(SharedResources.SHARED_USER_EMAIL).equals("0")||
                SharedResources.getStringFromSharedPreferences(SharedResources.SHARED_USER_EMAIL).isEmpty()) {
			NotGemsGameLayer.getInstance().noGems = 6;
			SceneManager.getInstance().showLayer(NotGemsGameLayer.getInstance(), false, false, true);
		}else {

			//TankAttackActivity.showLeaderboard();
			//SceneManager.getInstance().showLayer(LeaderBoardGameLayer.getInstance(), false, false, true);
		}*/
	}

	@Override
	public void onShowScene() {
		TriviaMendozaSmoothCamera.setupForMenus();
		if(!this.mTanAttackkBGSprite.hasParent()) {
			this.attachChild(this.mTanAttackkBGSprite);
			this.sortChildren();
		}
		ShowAdBanner();
	}

	@Override
	public void onUnloadScene() {}

	public static void RefreshCoins() {
		//mCoinText.setText(String.valueOf(SharedResources.getTotalsCoins()));
	}





	public void RefreshTextCoinsPower(CharSequence text) {
		if(mTextCoinsChangePower !=null) {
			mTextCoinsChangePower.setText(text);
		}
	}

	/*private static void VerificationsDialogueUser() {
		if(SharedResources.getIntFromSharedPreferences(SharedResources.SHARED_PREFS_ACTIVITY_START_USER)%3==0) {
		if (SharedResources.getStringFromSharedPreferences(SharedResources.SHARED_USER_EMAIL).equals("0") &&
				SharedResources.getStringFromSharedPreferences(SharedResources.SHARED_USER_EMAIL).isEmpty()) {
				SceneManager.getInstance().showLayer(UserTanksLayer.getInstance(), false, false, true);
			}
		}
	}*/

	public void ShowAdBanner(){
		//ResourceManager.getActivity().ShowAds();
	}
}
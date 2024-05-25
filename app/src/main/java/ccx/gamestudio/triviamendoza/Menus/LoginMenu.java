package ccx.gamestudio.triviamendoza.Menus;

import android.widget.Toast;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;

import java.security.NoSuchAlgorithmException;

import ccx.gamestudio.triviamendoza.Input.GrowButton;
import ccx.gamestudio.triviamendoza.Manager.ResourceManager;
import ccx.gamestudio.triviamendoza.Manager.SceneManager;
import ccx.gamestudio.triviamendoza.R;
import ccx.gamestudio.triviamendoza.TriviaMendozaActivity;
import ccx.gamestudio.triviamendoza.TriviaMendozaSmoothCamera;

public class LoginMenu extends ManagedMenuScene {

	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final LoginMenu INSTANCE = new LoginMenu();
	private static final float mCameraHeight = ResourceManager.getInstance().cameraHeight;
	private static final float mCameraWidth = ResourceManager.getInstance().cameraWidth;
	private static final float mHalfCameraHeight = (ResourceManager.getInstance().cameraHeight / 2f);
	private static final float mHalfCameraWidth = (ResourceManager.getInstance().cameraWidth / 2f);

	// ====================================================
	// INSTANCE GETTER
	// ====================================================
	public static LoginMenu getInstance() {
		return INSTANCE;
	}
    private Sprite mTriviaBGSprite;

	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public LoginMenu() {
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
		this.mTriviaBGSprite = new Sprite(0f, 0f, ResourceManager.menuBackgroundTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		this.mTriviaBGSprite.setScale(ResourceManager.getInstance().cameraHeight / ResourceManager.menuBackgroundTR.getHeight());
		this.mTriviaBGSprite.setPosition((this.mTriviaBGSprite.getWidth() * this.mTriviaBGSprite.getScaleX()) / 2.3f, (this.mTriviaBGSprite.getHeight() * this.mTriviaBGSprite.getScaleY()) / 2f);
		this.mTriviaBGSprite.setZIndex(-999);
		MenuLoadingScene.attachChild(this.mTriviaBGSprite);
		MenuLoadingScene.attachChild(new Text(mHalfCameraWidth, mHalfCameraHeight, ResourceManager.fontDefault60, ResourceManager.getContext().getText(R.string.app_loading), ResourceManager.getActivity().getVertexBufferObjectManager()));
		return MenuLoadingScene;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		this.mTriviaBGSprite.detachSelf();
	}

	@Override
	public void onLoadScene() {

		GetInAppPurchase();
        Entity mLoginMenuScreen = new Entity(0f, mCameraHeight) {
            boolean hasLoaded = false;

            @Override
            protected void onManagedUpdate(final float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                if (!this.hasLoaded) {
                    this.hasLoaded = true;
                    this.registerEntityModifier(new MoveModifier(0.25f, 0f, mCameraHeight, 0f, 0f));
                }
            }
        };

		final Sprite MainTitleText = new Sprite(0f, 0f, ResourceManager.menuMainTitleTR, ResourceManager.getActivity().getVertexBufferObjectManager());
		MainTitleText.setPosition(mCameraWidth / 2f, mCameraHeight /2);
		MainTitleText.setScale(0.5f);
		ShowAdBanner();

		Text continueWith = new Text(mHalfCameraWidth, mHalfCameraHeight - 200f, ResourceManager.fontDefault32, ResourceManager.getContext().getText(R.string.app_continuewith), 255, new TextOptions(AutoWrap.WORDS, 500, HorizontalAlign.CENTER),ResourceManager.getActivity().getVertexBufferObjectManager());
		continueWith.setColor(1,1,1);
		GrowButton btnLoginGoogle = new GrowButton(mHalfCameraWidth, mHalfCameraHeight - 270f, ResourceManager.btnLoginGoogle) {
			@Override
			public void onClick()  {
				TriviaMendozaActivity.SignInGoogle();
			}
		};
		btnLoginGoogle.setScales(0.4f,0.45f);

		Text continueWithGoogle = new Text(btnLoginGoogle.getWidth() / 2f, btnLoginGoogle.getHeight() / 2f, ResourceManager.fontDefault48, ResourceManager.getContext().getText(R.string.app_continueWithGoogle), 255, new TextOptions(AutoWrap.WORDS, 700, HorizontalAlign.CENTER),ResourceManager.getActivity().getVertexBufferObjectManager());
		continueWithGoogle.setColor(0,0,0);
		btnLoginGoogle.attachChild(continueWithGoogle);

		this.registerTouchArea(btnLoginGoogle);

		GrowButton btnLoginFacebook = new GrowButton(mHalfCameraWidth, mHalfCameraHeight - 346f, ResourceManager.btnLoginFacebook) {
			@Override
			public void onClick() {
				ResourceManager.getActivity().runOnUiThread(() ->
						Toast.makeText(ResourceManager.getActivity(), "Estámos trabajando para que puedas acceder con Facebook",
								Toast.LENGTH_SHORT).show());
			}
		};
		btnLoginFacebook.setScales(0.4f,0.45f);
		Text continueWithFacebook = new Text(btnLoginFacebook.getWidth() / 2f + 20, btnLoginFacebook.getHeight() / 2f, ResourceManager.fontDefault48, ResourceManager.getContext().getText(R.string.app_continueWithFacebook), 255, new TextOptions(AutoWrap.WORDS, 700, HorizontalAlign.CENTER),ResourceManager.getActivity().getVertexBufferObjectManager());
		continueWithFacebook.setColor(0,0,0);
		btnLoginFacebook.attachChild(continueWithFacebook);

		this.registerTouchArea(btnLoginFacebook);

		mLoginMenuScreen.attachChild(continueWith);
		mLoginMenuScreen.attachChild(btnLoginGoogle);
		mLoginMenuScreen.attachChild(btnLoginFacebook);

		//InAppReview.getInstance().LoadAppReveview(SharedResources.getIntFromSharedPreferences(SharedResources.SHARED_PREFS_ACTIVITY_START_COUNT_RATING));
		//VersionControl.getInstance().ControlVersion();

		mLoginMenuScreen.attachChild(MainTitleText);

		this.attachChild(mLoginMenuScreen);
	}

	private void GetInAppPurchase() {
		//ShopMenu.getInstance().SetListenerBillingClient();
		//ShopMenu.getInstance().StartConectionInAppPurchase();
	}

	@Override
	public void onShowScene() {
		TriviaMendozaSmoothCamera.setupForMenus();
		if(!this.mTriviaBGSprite.hasParent()) {
			this.attachChild(this.mTriviaBGSprite);
			this.sortChildren();
		}
		ShowAdBanner();
	}

	@Override
	public void onUnloadScene() {}


	public void ShowAdBanner(){
		//ResourceManager.getActivity().ShowAds();
	}
}
package ccx.gamestudio.triviamendoza;

import org.andengine.entity.scene.Scene;

import ccx.gamestudio.triviamendoza.Manager.ResourceManager;

public abstract class ManagedScene extends Scene {
	
	public final boolean hasLoadingScreen;
	public final float minLoadingScreenTime;
	public float elapsedLoadingScreenTime = 0f;
	public boolean isLoaded = false;
	public boolean willHandle_isLoaded = true;
	public ManagedScene() {
		this(0f);
	}
	
	public ManagedScene(final float pLoadingScreenMinimumSecondsShown) {
		minLoadingScreenTime = pLoadingScreenMinimumSecondsShown;
		hasLoadingScreen = (minLoadingScreenTime > 0f);
	}
	
	public void onLoadManagedScene() {
		if(!isLoaded) {
			onLoadScene();
			if(willHandle_isLoaded)
			{
				isLoaded = true;
				this.setIgnoreUpdate(true);
			}
		}
	}
	
	public void onUnloadManagedScene() {
		if(isLoaded) {
			isLoaded = false;
			ResourceManager.getInstance().engine.runOnUpdateThread(this::onUnloadScene);
		}
	}

	public void onShowManagedScene() {
		this.setIgnoreUpdate(false);
		onShowScene();
	}

	public void onHideManagedScene() {
		this.setIgnoreUpdate(true);
		onHideScene();
	}
	public abstract Scene onLoadingScreenLoadAndShown();
	public abstract void onLoadingScreenUnloadAndHidden();
	public abstract void onLoadScene();
	public abstract void onShowScene();
	public abstract void onHideScene();
	public abstract void onUnloadScene();
}
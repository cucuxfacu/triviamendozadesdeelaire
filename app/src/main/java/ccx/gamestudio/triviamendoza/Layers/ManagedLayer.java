package ccx.gamestudio.triviamendoza.Layers;

import org.andengine.entity.scene.CameraScene;

public abstract class ManagedLayer extends CameraScene {
	
	public static final float mSLIDE_PIXELS_PER_SECONDS = 7500f;
	public boolean mHasLoaded = false;
	public boolean mUnloadOnHidden;

    public ManagedLayer() {
		this(false);
	}
	
	public ManagedLayer(boolean pUnloadOnHidden) {
		mUnloadOnHidden = pUnloadOnHidden;
		this.setBackgroundEnabled(false);
	}

	public void onShowManagedLayer() {
		if(!mHasLoaded) {
			mHasLoaded = true;
			onLoadLayer();
		}
		this.setIgnoreUpdate(false);
		onShowLayer();
	}

	public void onHideManagedLayer() {
		this.setIgnoreUpdate(true);
		onHideLayer();
		if(mUnloadOnHidden) {
			onUnloadLayer();
		}
	}

	public abstract void onLoadLayer();
	public abstract void onShowLayer();
	public abstract void onHideLayer();
	public abstract void onUnloadLayer();

}
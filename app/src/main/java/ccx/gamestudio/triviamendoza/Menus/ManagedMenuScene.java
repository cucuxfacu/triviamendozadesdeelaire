package ccx.gamestudio.triviamendoza.Menus;

import ccx.gamestudio.triviamendoza.ManagedScene;

public abstract class ManagedMenuScene extends ManagedScene {
	public ManagedMenuScene(float pLoadingScreenMinimumSecondsShown) {
		super(pLoadingScreenMinimumSecondsShown);
	}
	@Override
	public void onUnloadManagedScene() {
		if(isLoaded) {
			onUnloadScene();
		}
	}
}
package ccx.gamestudio.triviamendoza.Manager;

import org.andengine.engine.handler.IUpdateHandler;

public class GameManager implements IUpdateHandler {

	public interface GameLevelGoal {
		boolean isLevelCompleted();
		void onLevelCompleted();
		boolean isLevelFailed();
		void onLevelFailed();
	}

	private static final GameManager INSTANCE = new GameManager();

	public GameLevelGoal mGameLevelGoal;


	public GameManager() {
		ResourceManager.getEngine().registerUpdateHandler(this);
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		if(mGameLevelGoal!=null)
			if(mGameLevelGoal.isLevelCompleted()) {
				mGameLevelGoal.onLevelCompleted();
				mGameLevelGoal = null;
			} else if (mGameLevelGoal.isLevelFailed()) {
				mGameLevelGoal.onLevelFailed();
				mGameLevelGoal = null;
			}
	}
	@Override public void reset() {}
	

	
	public static void setGameLevelGoal(GameLevelGoal pGameLevelGoal) {
		INSTANCE.mGameLevelGoal = pGameLevelGoal;
	}
	
	public static void clearGameLevelGoal() {
		INSTANCE.mGameLevelGoal = null;
	}
}
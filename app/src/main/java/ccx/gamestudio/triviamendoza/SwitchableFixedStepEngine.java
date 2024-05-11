package ccx.gamestudio.triviamendoza;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.util.time.TimeConstants;

public class SwitchableFixedStepEngine extends Engine {

	private final long mStepLength;
	private long mSecondsElapsedAccumulator;
	private boolean mFixedStepEnabled = false;
	public int mStepsPerSecond;

	public SwitchableFixedStepEngine(final EngineOptions pEngineOptions, final int pStepsPerSecond, final boolean pFixedStepEnabled) {
		super(pEngineOptions);
		this.mStepLength = TimeConstants.NANOSECONDS_PER_SECOND / pStepsPerSecond;
		mFixedStepEnabled = pFixedStepEnabled;
		mStepsPerSecond = pStepsPerSecond;
	}

	public void EnableFixedStep() {
		mFixedStepEnabled = true;
	}
	
	public void DisableFixedStep() {
		mFixedStepEnabled = false;
	}
	
	@Override
	public void onUpdate(final long pNanosecondsElapsed) throws InterruptedException {
		if(mFixedStepEnabled)
		{
			this.mSecondsElapsedAccumulator += pNanosecondsElapsed;
			final long stepLength = this.mStepLength;
			while(this.mSecondsElapsedAccumulator >= stepLength) {
				super.onUpdate(stepLength);
				this.mSecondsElapsedAccumulator -= stepLength;
			}
		} else {
			super.onUpdate(pNanosecondsElapsed);
		}
	}
}
package ccx.gamestudio.triviamendoza.Input;

import org.andengine.input.touch.TouchEvent;

public abstract class BoundTouchInput {
	
	// ====================================================
	// VARIABLES
	// ====================================================
	private boolean mIsActive = false;
	private int mBoundPointerIndex = -1;
	private float mStartX = 0f;
	private float mStartY = 0f;
	private float mPreviousX = 0f;
	private float mPreviousY = 0f;
	
	// ====================================================
	// ABSTRACT METHODS
	// ====================================================
	protected abstract boolean onActionDown(float pX, float pY);
	protected abstract void onActionMove(float pX, float pY, float pPreviousX, float pPreviousY, float pStartX, float pStartY);
	protected abstract void onActionUp(float pX, float pY, float pPreviousX, float pPreviousY, float pStartX, float pStartY);
	
	// ====================================================
	// METHODS
	// ====================================================
	public boolean onTouch(TouchEvent pTouchEvent) {
		if(mIsActive) {
			if(pTouchEvent.getPointerID() == mBoundPointerIndex)
				if(pTouchEvent.isActionMove()) {
					onActionMove(pTouchEvent.getX(), pTouchEvent.getY(), mPreviousX, mPreviousY, mStartX, mStartY);
					mPreviousX = pTouchEvent.getX();
					mPreviousY = pTouchEvent.getY();
				} else {
					mIsActive = false;
					mBoundPointerIndex = -1;
					onActionUp(pTouchEvent.getX(), pTouchEvent.getY(), mPreviousX, mPreviousY, mStartX, mStartY);
					return true;
				}
			else
				return false;
		} else {
			if(pTouchEvent.isActionDown()) {
				mIsActive = onActionDown(pTouchEvent.getX(), pTouchEvent.getY());
				if(mIsActive) {
					mBoundPointerIndex = pTouchEvent.getPointerID();
					mStartX = pTouchEvent.getX();
					mStartY = pTouchEvent.getY();
					mPreviousX = pTouchEvent.getX();
					mPreviousY = pTouchEvent.getY();
				}
			}
		}
		return mIsActive;
	}
	
	public boolean canCaptureTouch(TouchEvent pTouchEvent) {
		if(!mIsActive || mBoundPointerIndex==pTouchEvent.getPointerID()) return true;
		return false;
	}
	
}
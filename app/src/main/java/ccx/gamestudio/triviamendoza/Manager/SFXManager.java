package ccx.gamestudio.triviamendoza.Manager;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.util.debug.Debug;

import java.io.IOException;

import ccx.gamestudio.triviamendoza.Helpers.SharedResources;


public class SFXManager
{
	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final SFXManager INSTANCE = new SFXManager();
	private static Music mMusicMenu;
	private static Sound mClick;
	private static Sound mClose;
	private static Sound mcoin;
	private static Sound mIntro;
	
	// ====================================================
	// INSTANCE GETTER
	// ====================================================
	public static SFXManager getInstance(){
		return INSTANCE;
	}
	
	// ====================================================
	// VARIABLES
	// ====================================================
	public boolean mSoundsMuted;
	public boolean mMusicMuted;
	public boolean mMusicGameMuted;
	
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	public SFXManager() {
		MusicFactory.setAssetBasePath("sounds/");
		try {
			mMusicMenu = MusicFactory.createMusicFromAsset(ResourceManager.getActivity().getMusicManager(), ResourceManager.getActivity(), "MusicMainMenu.mp3");
			mMusicMenu.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}

		SoundFactory.setAssetBasePath("sounds/");
		try {
			mClick = SoundFactory.createSoundFromAsset(ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity(), "click.mp3");
		} catch (final IOException e) { Debug.e(e); }

		try {
			mClose = SoundFactory.createSoundFromAsset(ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity(), "close.mp3");
		} catch (final IOException e) { Debug.e(e); }

		try {
			mIntro = SoundFactory.createSoundFromAsset(ResourceManager.getActivity().getSoundManager(), ResourceManager.getActivity(), "IntroGame.mp3");
		}
		catch (final IOException e) { Debug.e(e); }
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	private static void setVolumeForAllSounds(final float pVolume) {
		mClick.setVolume(pVolume);
	}
	
	public static boolean isSoundMuted() {
		return getInstance().mSoundsMuted;
	}
	
	public static void setSoundMuted(final boolean pMuted) {
		getInstance().mSoundsMuted = pMuted;
		setVolumeForAllSounds((getInstance().mSoundsMuted? 0f:1f));
		SharedResources.writeIntToSharedPreferences(SharedResources.SHARED_PREFS_SOUNDS_MUTED, (getInstance().mSoundsMuted? 1:0));
	}
	
	public static boolean toggleSoundMuted() {
		getInstance().mSoundsMuted = !getInstance().mSoundsMuted;
		setVolumeForAllSounds((getInstance().mSoundsMuted? 0f:1f));
		SharedResources.writeIntToSharedPreferences(SharedResources.SHARED_PREFS_SOUNDS_MUTED, (getInstance().mSoundsMuted? 1:0));
		return getInstance().mSoundsMuted;
	}
	
	public static boolean isMusicMuted() {
		return getInstance().mMusicMuted;
	}

	public static void setMusicMuted(final boolean pMuted) {
		getInstance().mMusicMuted = pMuted;
		if(getInstance().mMusicMuted) {
			mMusicMenu.pause();
		}
		else {
			mMusicMenu.play();
		}
		SharedResources.writeIntToSharedPreferences(SharedResources.SHARED_PREFS_MUSIC_MUTED, (getInstance().mMusicMuted? 1:0));
	}

	public static boolean toggleMusicMuted() {
		getInstance().mMusicMuted = !getInstance().mMusicMuted;
		if(getInstance().mMusicMuted) {
			mMusicMenu.pause();
		}
		else {
			mMusicMenu.play();
		}
		SharedResources.writeIntToSharedPreferences(SharedResources.SHARED_PREFS_MUSIC_MUTED, (getInstance().mMusicMuted? 1:0));
		return getInstance().mMusicMuted;
	}


	public static void playMusic() {
		if(!isMusicMuted())
		{
			mMusicMenu.play();
		}
	}

	public static void PlayIntro() {
		mIntro.setVolume(1f);
		mIntro.setRate(1f);
		mIntro.play();
	}
	public static void pauseMusic() {
		mMusicMenu.pause();
	}

	public static void resumeMusic() {
		if(!isMusicMuted())
			mMusicMenu.resume();
	}

	public static float getMusicVolume() {
		return mMusicMenu.getVolume();
	}
	
	public static void setMusicVolume(final float pVolume) {
		mMusicMenu.setVolume(pVolume);
	}

	public static void playClick(final float pRate, final float pVolume) {
		playSound(mClick,pRate,pVolume);
	}

	public static void playCloseLayer(final float pRate, final float pVolume) {
		playSound(mClose,pRate,pVolume);
	}

	public static void playCoin(final float pRate, final float pVolume) {
		playSound(mcoin,pRate,pVolume);
	}


	private static void playSound(final Sound pSound, final float pRate, final float pVolume) {
		if(SFXManager.isSoundMuted()) return;
		pSound.setRate(pRate);
		pSound.setVolume(pVolume);
		pSound.play();
	}
}
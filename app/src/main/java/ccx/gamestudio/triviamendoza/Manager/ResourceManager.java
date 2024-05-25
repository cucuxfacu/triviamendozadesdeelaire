package ccx.gamestudio.triviamendoza.Manager;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLES30;

import androidx.annotation.NonNull;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.color.Color;

import ccx.gamestudio.triviamendoza.SwitchableFixedStepEngine;
import ccx.gamestudio.triviamendoza.TriviaMendozaActivity;
import ccx.gamestudio.triviamendoza.TriviaMendozaSmoothCamera;

public class ResourceManager {

	//====================================================
	// CONSTANTS
	//====================================================
	private static final ResourceManager INSTANCE = new ResourceManager();
	private static final TextureOptions mNormalTextureOption = TextureOptions.BILINEAR;
	private static final TextureOptions mStretchableBeamTextureOption = new TextureOptions(GLES30.GL_LINEAR, GLES30.GL_LINEAR, GLES30.GL_CLAMP_TO_EDGE, GLES30.GL_REPEAT, false);;
	private static final TextureOptions mTransparentTextureOption = TextureOptions.BILINEAR;
	private static final TextureOptions mTransparentRepeatingTextureOption = TextureOptions.REPEATING_BILINEAR;

	//====================================================
	// VARIABLES
	//====================================================
	public SwitchableFixedStepEngine engine;
	public Context context;
	public TriviaMendozaActivity activity;
	public float cameraWidth;
	public float cameraHeight;
	public float cameraScaleFactorX;
	public float cameraScaleFactorY;

	//========================= Options Menu =======================//
	public static TextureRegion gameOptionsSperatorTR;
	public static TiledTextureRegion gameMusicTTR;
	public static TiledTextureRegion gameSoundTTR;

	// ======================== Menu Resources ================= //
	public static TextureRegion menuBackgroundTR;
	public static TextureRegion menuMainTitleTR;
	public static TextureRegion btnButtonlarge;
	public static TextureRegion btnGenericButton;


	// =================== Shared Game and Menu Resources ====== //
	public static Font fontDefault24;
	public static Font fontDefault32;
	public static Font fontDefault36;
	public static Font fontDefault48;
	public static Font fontDefault50;
	public static Font fontDefault60;
	private String mPreviousAssetBasePath = "";
	public static  TextureRegion btnLoginGoogle;
	public static  TextureRegion btnLoginFacebook;
	public static  ITextureRegion btnIconsBtnTTR;

	// CONSTRUCTOR
	//====================================================
	public ResourceManager(){
	}

	//====================================================
	// GETTERS & SETTERS
	//====================================================
	public static ResourceManager getInstance(){
		return INSTANCE;
	}

	//====================================================
	// PUBLIC METHODS
	//====================================================
	public static void setup(TriviaMendozaActivity pActivity, SwitchableFixedStepEngine pEngine, Context pContext, float pCameraWidth, float pCameraHeight, float pCameraScaleX, float pCameraScaleY){
		getInstance().activity = pActivity;
		getInstance().engine = pEngine;
		getInstance().context = pContext;
		getInstance().cameraWidth = pCameraWidth;
		getInstance().cameraHeight = pCameraHeight;
		getInstance().cameraScaleFactorX = pCameraScaleX;
		getInstance().cameraScaleFactorY = pCameraScaleY;
	}

	// Loads all menu resources
	public static void loadMenuResources() {
		getInstance().LoadMenuTextures();
		getInstance().LoadFonts();

		loadGameResources();
		/*LevelWonLayer.getInstance().onLoadLayer();
		LevelPauseLayer.getInstance().onLoadLayer();
		OptionsLayer.getInstance().onLoadLayer();
		ExitGameLayer.getInstance().onLoadLayer();
		ShopCoinsAndGemsLayer.getInstance().onLoadLayer();
		WeaponsInGame.getInstance().onLoadLayer();
		ShopTanksLayer.getInstance().onLoadLayer();
		UserTanksLayer.getInstance().onLoadLayer();
		NotGemsGameLayer.getInstance().onLoadLayer();
		FreeCoinsLayer.getInstance().onLoadLayer();
		GiftOfDayLayer.getInstance().onLoadLayer();
		LeaderBoardGameLayer.getInstance().onLoadLayer();
		OptionsNextLevelLayer.getInstance().onLoadLayer();
		DefeatedLayer.getInstance().onLoadLayer();*/
	}

	public static void loadGameResources() {

	}

	public static SwitchableFixedStepEngine getEngine()
	{
		return getInstance().engine;
	}

	public static Context getContext()
	{
		return getInstance().context;
	}

	public static TriviaMendozaActivity getActivity() {
		return getInstance().activity;
	}

	public static TriviaMendozaSmoothCamera getCamera()
	{
		return (TriviaMendozaSmoothCamera) getInstance().engine.getCamera();
	}

	private TextureRegion getLimitableTR(String pTextureRegionPath, TextureOptions pTextureOptions) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = AssetBitmapTextureAtlasSource.create(activity.getAssets(), BitmapTextureAtlasTextureRegionFactory.getAssetBasePath() + pTextureRegionPath);
		final BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), bitmapTextureAtlasSource.getTextureWidth(), bitmapTextureAtlasSource.getTextureHeight(), pTextureOptions);
		final TextureRegion textureRegion = new TextureRegion(bitmapTextureAtlas, 0, 0, bitmapTextureAtlasSource.getTextureWidth(), bitmapTextureAtlasSource.getTextureHeight(), false) {
			@Override
			public void updateUV() {
				super.updateUV();
			}
		};
		bitmapTextureAtlas.addTextureAtlasSource(bitmapTextureAtlasSource, 0, 0);
		bitmapTextureAtlas.load();

		return textureRegion;
	}

	@NonNull
	private TiledTextureRegion getLimitableTTR(String pTiledTextureRegionPath, int pColumns, int pRows, TextureOptions pTextureOptions) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = AssetBitmapTextureAtlasSource.create(activity.getAssets(), BitmapTextureAtlasTextureRegionFactory.getAssetBasePath() + pTiledTextureRegionPath);
		final BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), bitmapTextureAtlasSource.getTextureWidth(), bitmapTextureAtlasSource.getTextureHeight(), pTextureOptions);
		final ITextureRegion[] textureRegions = new ITextureRegion[pColumns * pRows];

		final int tileWidth = bitmapTextureAtlas.getWidth() / pColumns;
		final int tileHeight = bitmapTextureAtlas.getHeight() / pRows;

		for(int tileColumn = 0; tileColumn < pColumns; tileColumn++) {
			for (int tileRow = 0; tileRow < pRows; tileRow++) {
				final int tileIndex = tileRow * pColumns + tileColumn;

				final int x = tileColumn * tileWidth;
				final int y = tileRow * tileHeight;
				textureRegions[tileIndex] = new TextureRegion(bitmapTextureAtlas, x, y, tileWidth, tileHeight, false) {
					@Override
					public void updateUV() {
						super.updateUV();
					}
				};
			}
		}

		final TiledTextureRegion tiledTextureRegion = new TiledTextureRegion(bitmapTextureAtlas, false, textureRegions);
		bitmapTextureAtlas.addTextureAtlasSource(bitmapTextureAtlasSource, 0, 0);
		bitmapTextureAtlas.load();
		return tiledTextureRegion;
	}

	// ============================ LOAD MENU ================= //
	private void LoadMenuTextures(){
		mPreviousAssetBasePath = BitmapTextureAtlasTextureRegionFactory.getAssetBasePath();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("TriviaMendoza/Menu/");


		if(menuBackgroundTR==null) menuBackgroundTR = getLimitableTR("BgMenu.png", mNormalTextureOption);
		if(menuMainTitleTR==null) menuMainTitleTR = getLimitableTR("title.png", mNormalTextureOption);
		if(btnButtonlarge==null) btnButtonlarge = getLimitableTR("buttonlarge.png", mNormalTextureOption);
		if(btnGenericButton==null) btnGenericButton = getLimitableTR("genericbutton.png", mNormalTextureOption);
		if(btnLoginFacebook==null) btnLoginFacebook = getLimitableTR("facebook.png", mNormalTextureOption);
		if(btnLoginGoogle==null) btnLoginGoogle = getLimitableTR("google.png", mNormalTextureOption);
		if(btnIconsBtnTTR == null )btnIconsBtnTTR = getLimitableTTR("Iconsbuttons.png",5,5, mTransparentTextureOption);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(mPreviousAssetBasePath);
	}

	// ============================ LOAD FONTS ========================== //
	private void LoadFonts(){
		if(fontDefault24==null) {
			fontDefault24 = getFont(Typeface.createFromAsset(activity.getAssets(), "Fonts/airborne.ttf"), 24f, true);
			fontDefault24.load();
		}

		if(fontDefault32==null) {
			fontDefault32 = getFont(Typeface.createFromAsset(activity.getAssets(), "Fonts/airborne.ttf"), 32f, true);
			fontDefault32.load();
		}

		if(fontDefault36==null) {
			fontDefault36= getFont(Typeface.createFromAsset(activity.getAssets(), "Fonts/airborne.ttf"), 36f, true);
			fontDefault36.load();
		}

		if(fontDefault48==null) {
			fontDefault48 = getFont(Typeface.createFromAsset(activity.getAssets(), "Fonts/airborne.ttf"), 48f, true);
			fontDefault48.load();
		}

		if(fontDefault50==null) {
			fontDefault50= getFont(Typeface.createFromAsset(activity.getAssets(), "Fonts/airborne.ttf"), 50f, true);
			fontDefault50.load();
		}

		if(fontDefault60==null) {
			fontDefault60= getFont(Typeface.createFromAsset(activity.getAssets(), "Fonts/airborne.ttf"), 60f, true);
			fontDefault60.load();
		}
	}

	public Font getFont(Typeface pTypeface, float pSize, boolean pAntiAlias)
	{
		String DEFAULT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~`!@#$%^&*()-_=+[] {};:'\",<.>?/\\";
		return getFont(pTypeface, pSize, pAntiAlias, DEFAULT_CHARS);
	}

	public Font getFont(Typeface pTypeface, float pSize, boolean pAntiAlias, String pCharsToUse)
	{
		updateTextBounds(pTypeface,pSize,pAntiAlias,pCharsToUse);
		float fontTextureWidth = (float) (mTextBounds.width() - mTextBounds.left);
		float fontTextureHeight = (float) (mTextBounds.height() - mTextBounds.top);
		int fontTextureRows = (int) Math.round((float) Math.sqrt(fontTextureWidth / fontTextureHeight));
		float FONT_TEXTURE_PADDING_RATIO = 1.04f;
		fontTextureWidth = ((fontTextureWidth / fontTextureRows) * FONT_TEXTURE_PADDING_RATIO);
		fontTextureHeight = ((fontTextureHeight * fontTextureRows) * FONT_TEXTURE_PADDING_RATIO);
		return new Font(engine.getFontManager(), new BitmapTextureAtlas(engine.getTextureManager(), (int) fontTextureWidth, (int) fontTextureHeight, BitmapTextureFormat.RGBA_8888, TextureOptions.DEFAULT), pTypeface, pSize, pAntiAlias, Color.WHITE_ARGB_PACKED_INT);
	}

	private final Rect mTextBounds = new Rect();
	private void updateTextBounds(final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final String pCharacterAsString) {
		Paint mPaint = new Paint();
		mPaint.setTypeface(pTypeface);
		mPaint.setTextSize(pSize);
		mPaint.setAntiAlias(pAntiAlias);
		mPaint.getTextBounds(pCharacterAsString, 0, pCharacterAsString.length(), this.mTextBounds);
	}
}
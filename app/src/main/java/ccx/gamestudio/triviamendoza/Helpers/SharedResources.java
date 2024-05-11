package ccx.gamestudio.triviamendoza.Helpers;

import ccx.gamestudio.triviamendoza.Manager.ResourceManager;

public class SharedResources {
    // ====================================================
    // ACCESS TO SHARED RESOURCES
    // ====================================================
    public static final String SHARED_WINDOWS_WIDTH_INCHES="windowswidthinches";
    public static final String SHARED_PREFS_ACTIVITY_START_COUNT = "count";
    public static final String SHARED_PREFS_ACTIVITY_START_USER = "countloadactivyty";
    public static final String SHARED_PREFS_MAIN = "TriviaMendozaSettings";
    public static final String SHARED_PREFS_LEVEL_STARS = "level.stars";
    public static final String SHARED_PREFS_LEVEL_HIGHSCORE = "level.highscore";
    public static final String SHARED_PREFS_HIGHSCORE = "highscore";
    public static final String SHARED_PREFS_MUSIC_MUTED = "mute.music";
    public static final String SHARED_PREFS_SOUNDS_MUTED = "mute.sounds";
    public static final String SHARED_PREFS_MUSIC_VOLUMEN = "volumenmusic";
    public static final String SHARED_PREFS_MUSIC_GAME_VOLUMEN = "volumenmusic";
    public static final String SHARED_PREFS_GEMS = "gems";
    public static final String SHARED_PREFS_COINS = "coins";
    public static final String SHARED_DATE = "date";
    public static final String SHARED_COUNT_DATE = "countdate";
    public static final String SHARED_DAY_OFF = "dayoff";
    public static final String SHARED_PREFS_ACTIVITY_START_COUNT_RATING = "countrating";
    public static final String SHARED_PREFS_RATING_SUCCESS = "rating";
    public static final String SHARED_PREFS_VERSION_CODE = "version";
    public static final String SHARED_USER_EMAIL = "useremail";
    public static final String SHARED_USER_NAME_LASTNAME = "usernamelastname";
    public static final String SHARED_USER_PHOTO = "userphoto";
    public static final String SHARED_AMOUNT_CIEN_GEMS = "amount.ciengems";
    public static final String SHARED_AMOUNT_QUININETAS_GEMS ="amount.quininetasgems";
    public static final String SHARED_AMOUNT_MIL_GEMS ="amount.milgems";

    public static int writeIntToSharedPreferences(final String pStr, final int pValue) {
        ResourceManager.getActivity().getSharedPreferences(SHARED_PREFS_MAIN, 0).edit().putInt(pStr, pValue).apply();
        return pValue;
    }

    public static int getIntFromSharedPreferences(final String pStr) {
        return ResourceManager.getActivity().getSharedPreferences(SHARED_PREFS_MAIN, 0).getInt(pStr, 0);
    }

    public static Float getFloatFromSharedPreferences(final String pStr) {
        return ResourceManager.getActivity().getSharedPreferences(SHARED_PREFS_MAIN, 0).getFloat(pStr, 0.0f);
    }

    public static Float writeFloatToSharedPreferences(final String pStr, final Float pValue) {
        ResourceManager.getActivity().getSharedPreferences(SHARED_PREFS_MAIN, 0).edit().putFloat(pStr, pValue).apply();
        return pValue;
    }

    public static void writeBooleanToSharedPreferences(final String pStr, final boolean pValue) {
        ResourceManager.getActivity().getSharedPreferences(SHARED_PREFS_MAIN, 0).edit().putBoolean(pStr, pValue).apply();
    }

    public static boolean getBooleanFromSharedPreferences(final String pStr) {
        return ResourceManager.getActivity().getSharedPreferences(SHARED_PREFS_MAIN, 0).getBoolean(pStr, false);
    }

    public static int getLevelStars(final int pWorld, final int pLevelNumber) {
        return getIntFromSharedPreferences(SHARED_PREFS_LEVEL_STARS + pWorld + pLevelNumber );
    }

    public static int getTotalsCoins() {
        return getIntFromSharedPreferences(SHARED_PREFS_COINS);
    }
    public static int getTotalsGems() {
        return getIntFromSharedPreferences(SHARED_PREFS_GEMS);
    }

    public static String writeStringToSharedPreferences(final String pStr, final String pValue) {
        ResourceManager.getActivity().getSharedPreferences(SHARED_PREFS_MAIN, 0).edit().putString(pStr, pValue).apply();
        return pValue;
    }

    public static String getStringFromSharedPreferences(final String pStr) {
        return ResourceManager.getActivity().getSharedPreferences(SHARED_PREFS_MAIN, 0).getString(pStr, "0");
    }
}

package com.aol.videonews;

public class Constants {

    public static final String EXTRA_SUCCEEDED = "succeeded";
    public static final String EXTRA_SCREEN_ID = "screen_id";
    public static final String EXTRA_PAGE_ID = "page_id";
    public static final String EXTRA_SUBPAGE_ID = "subpage_id";
    public static final String EXTRA_PLAYLIST_ID = "playlist_id";
    public static final String EXTRA_PLALIST_NAME = "playlist_name";
    public static final String EXTRA_VIDEO = "video";
    public static final String EXTRA_VIDEO_ABRIDGE = "video_abridge";
    public static final String EXTRA_SHOW = "show";
    public static final String EXTRA_SHOW_ID = "show_id";
    public static final String EXTRA_SHOW_TITLE = "show_title";
    public static final String EXTRA_VIDEO_ID = "video_id";
    public static final String EXTRA_VIDEO_TITLE = "video_title";
    public static final String EXTRA_VIDEO_OFFSET_SECONDS = "video_offset_seconds";
    public static final String EXTRA_VIDEO_FINISHED = "video_finished";
    public static final String EXTRA_VIDEO_CONTEXT = "video_context";
    public static final String EXTRA_PROVIDER = "provider";
    public static final String EXTRA_PROVIDER_ACCESS_TOKEN = "provider_acccess_token";
    public static final String EXTRA_PROVIDER_ACCESS_TOKEN_SECRET = "provider_acccess_token_secret";
    public static final String EXTRA_AOL_OAUTH2_CODE = "aol_oauth2_code";
    public static final String EXTRA_DEVICE_ACCESS_TOKEN = "device_access_token";
    public static final String EXTRA_SEARCH_TERM = "search_term";
    public static final String EXTRA_SEARCH_LIMIT = "search_limit";
    public static final String EXTRA_SEARCH_OFFSET = "search_offset";
    public static final String EXTRA_SEARCH_RESULT = "search_result";
    public static final String EXTRA_TAGS = "tags";
    public static final String EXTRA_FAVORITED = "favorited";
    public static final String EXTRA_PLAYLIST = "playlist";

    public static final String BROADCAST_GET_PLAYLIST_DETAIL_DONE = "get_playlist_detail_done";

    public static float VIDEO_ASPECT_RATIO = 0.5625f;

    public static final String OAUTH2_CLIENT_ID = "ao1yDs7IugQ0lHaC";
    public static final String OAUTH2_CLIENT_SECRET = "N3D7DES8XscFNNTBztN2t4cF";
    public static final String OAUTH2_AUTHORIZE_REDIRECT_URI = "aolid://aol.auth";
    public static final String AOL_REGISTRATION_CALLBACK_URL = "http://registration.mobile.aol.com/oauth2/regComplete";

    public static final String COMSCORE_CUSTOMER_C2 = "1000009";
    public static final String COMSCORE_PUBLISHER_SECRET = "602e4df9f54cce62b2eff47013c78008";
    public static final String COMSCORE_APP_NAME = "Video News";

    public static final String AOL_ON_PREFS_NAME = "com.aol.videonews.PREFS";
    public static final String AOL_ON_PREF_KEY_ACCESS_TOKEN = "com.aol.videonews.PREF_ACCESS_TOKEN";
    public static final String AOL_ON_PREF_KEY_DEVICE_UUID = "com.aol.videonews.PREF_DEVICE_UUID";
    public static final String AOL_ON_PREF_KEY_IS_LOGIN_TO_ACCOUNT = "com.aol.videonews.PREF_IS_LOGIN_TO_ACCOUNT";

    public static final String GOOGLE_APP_CLIENT_ID = "148548587253-772loqbo43oev0kgped25jtna306auuj.apps.googleusercontent.com";
    public static final String GOOGLE_PLUS_SCOPE = "https://www.googleapis.com/auth/plus.login";

    public static final int AOL_ON_APP_SID = 577;
    public static final int AOL_ON_APP_SKEY = 363;

    public static final String DEV_API_BASE_URL = "https://feedapi-dev.b2c.on.aol.com/v1.0";
    public static final String STAGING_API_BASE_URL = "https://feedapi-stage.b2c.on.aol.com/v1.0";
    public static final String PRD_API_BASE_URL = "https://feedapi.b2c.on.aol.com/v1.0";

    // default to PROD environment
    public static String API_BASE_URL = PRD_API_BASE_URL;

    public static final String API_APP_ID = "aolon";

    public static final String DEFAULT_IMAGE_FORMAT = "png";
    public static final String DEFAULT_VIDEO_FORMAT = "mp4";
    public static final String DEFAULT_SUBTITLE_LOCALE = "en-us";
    public static final String DEFAULT_SUBTITLE_TYPE = "text/dfxp";
    public static final String DEFAULT_VIDEO_QUALITY = "HD";
}
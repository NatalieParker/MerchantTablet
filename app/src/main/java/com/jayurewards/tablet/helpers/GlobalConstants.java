package com.jayurewards.tablet.helpers;

public class GlobalConstants {

    // Stripe Gateway
    public static final String ACTIVE_STRIPE = "active";
    public static final String TRIAL_STRIPE = "trialing";
    public static final String PAST_DUE_STRIPE = "past_due";
    public static final String INCOMPLETE_STRIPE = "incomplete";
    public static final String INCOMPLETE_EXPIRED_STRIPE = "incomplete_expired";
    public static final String CANCELED_STRIPE = "canceled";
    public static final String UNPAID_STRIPE = "unpaid";

    // Intents/Shared preferences
    public static final String SHARED_PREF = "sharedPref";
    public static final String FIRST_NAME = "firstName";
    public static final String EMAIL = "email";
    public static final String STORE_ID = "storeId";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String MERCHANT_ID = "merchantId";
    public static final String MERCHANT_FIREBASE_UID = "firebaseUid";
    public static final String STRIPE_ID = "stripeId";
    public static final String SUBSCRIPTION_ID = "subscriptionId";
    public static final String STRIPE_STATUS = "stripeStatus";
    public static final String TEAM_USER_ID = "userId";
    public static final String TEAM_NAME = "name";
    public static final String TEAM_COUNTRY_CODE = "countryCode";
    public static final String TEAM_PHONE = "phone";
    public static final String TEAM_USER_FIREBASE_UID = "userFirebaseUid";
    public static final String TEAM_STATUS = "teamStatus";
    public static final String TEAM_ADMIN_LVL = "teamAdminLvl";
    public static final String TEAM_TYPE = "teamType";
    public static final String TEAM_PHOTO_URL = "photoUrl";
    public static final String TEAM_THUMBNAIL_URL = "thumbnailUrl";
    public static final String ADMIN_LEVEL = "adminLevel";
    public static final String PIN_CODE = "pin_code";
    public static final String POINT_AMOUNT = "pointAmount";
    public static final String PT_CONVERT_AMOUNT = "pointConvertAmount";
    public static final String PT_CONVERT_POINTS = "pointConvertPoints";
    public static final String PT_CONVERT_ACTIVATED = "pointConvertActivated";
    public static final String ENTERED_PHONE = "enteredPhone";


    // MySQL date formats
    public static final String MYSQL_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String MYSQL_DATE_FORMAT_LEGACY = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String MYSQL_FIREBASE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ssZ";
    public static final String MYSQL_CUSTOM_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss'Z'";


    // MISC
    public static final String IMAGE_DIRECTORY = "imageDirectory";
    public static final String IMAGE_PATH_CHILD = "imagePathChild";
    public static final String USER_TERMS_AND_CONDITIONS_LINK = "userTermsAndConditionsLink";
    public static final String MERCHANT_TABLET_KEYPAD = "merchant_tablet_keypad";
    public static final String SMS_VERIFY_APPROVED = "approved";

    // Team member status
    public static final String APPROVED = "approved";
    public static final String PENDING = "pending";
    public static final String DENIED = "denied";


    // URLS
    public static final String INTENT_ENDING_URL = "endingUrl";
    public static final String URL_PORTAL_LOGIN_TAIL = "/auth/login";
    public static final String URL_PORTAL_SIGNUP_TAIL = "/auth/signup";
    public static final String WEB_URL_PORTAL_LOGIN = "https://portal.jayu.us/auth/login";
    public static final String WEB_URL_PORTAL_SIGNUP = "https://portal.jayu.us/auth/signup";
    public static final String WEB_URL_PORTAL_FORGOT_PASSWORD = "https://portal.jayu.us/auth/forgot-password";

    public static final String OFFER_TYPE_GENERAL = "general";
    public static final String OFFER_TYPE_SIGNUP = "signup";
    public static final String OFFER_TYPE_BIRTHDAY = "birthday";
    public static final String OFFER_TYPE_REFERRAL = "referral";
    public static final String OFFER_TYPE_PROMOTION = "promotion";
    public static final String OFFER_TYPE_PROMO_HOURS = "promo_hours";

    public static final String[] OFFER_TYPES_ARRAY = {
            OFFER_TYPE_SIGNUP,
            OFFER_TYPE_BIRTHDAY,
            OFFER_TYPE_PROMOTION,
            OFFER_TYPE_REFERRAL,
            OFFER_TYPE_PROMO_HOURS
    };


}

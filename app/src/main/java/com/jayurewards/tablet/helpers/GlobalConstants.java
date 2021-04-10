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
    public static final String MERCHANT_ID = "merchantId";
    public static final String MERCHANT_FIREBASE_UID = "firebaseUid";
    public static final String STRIPE_ID = "stripeId";
    public static final String SUBSCRIPTION_ID = "subscriptionId";
    public static final String STRIPE_STATUS = "stripeStatus";
    public static final String USER_ID = "userId";
    public static final String NAME = "name";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String PHONE = "phone";
    public static final String BIRTHDATE = "birthdate";
    public static final String USER_FIREBASE_UID = "userFirebaseUid";
    public static final String PHOTO_URL = "photoUrl";
    public static final String THUMBNAIL_URL = "thumbnailUrl";
    public static final String ADMIN_LEVEL = "adminLevel";
    public static final String PIN_CODE = "pin_code";
    public static final String POINT_AMOUNT = "pointAmount";

    // MySQL date formats
    public static final String MYSQL_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String MYSQL_DATE_FORMAT_LEGACY = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String MYSQL_FIREBASE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ssZ";
    public static final String MYSQL_CUSTOM_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss'Z'";


    public static final String IMAGE_DIRECTORY = "imageDirectory";
    public static final String IMAGE_PATH_CHILD = "imagePathChild";
    public static final String CHANNEL_DEFAULT = "channelDefault";
    public static final String FCM_RECIPIENT = "fcmRecipient";
    public static final String FCM_RECIPIENT_MERCHANT = "fcmRecipientMerchant";
    public static final String FCM_NOTIFICATION_ID = "fcmNotificationId";
    public static final String FCM_NOTIFICATION_ID_MERCHANT = "fcmNotificationIdMerchant";
    public static final String USER_TERMS_AND_CONDITIONS_LINK = "userTermsAndConditionsLink";
    public static final String POINT_TYPE_GENERAL = "pointTypeGeneral";
    public static final String MERCHANT_TABLET_KEYPAD = "merchant_tablet_keypad";


    // URLS
    public static final String INTENT_ENDING_URL = "endingUrl";
    public static final String URL_PORTAL_LOGIN_TAIL = "/auth/login";
    public static final String URL_PORTAL_SIGNUP_TAIL = "/auth/signup";
    public static final String WEB_URL_PORTAL_LOGIN = "https://portal.jayu.us/auth/login";
    public static final String WEB_URL_PORTAL_SIGNUP = "https://portal.jayu.us/auth/signup";
    public static final String WEB_URL_PORTAL_FORGOT_PASSWORD = "https://portal.jayu.us/auth/forgot-password";


}

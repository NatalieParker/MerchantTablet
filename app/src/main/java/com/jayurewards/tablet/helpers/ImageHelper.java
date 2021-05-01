package com.jayurewards.tablet.helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.jayurewards.tablet.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class ImageHelper {
    private static final String TAG = "Photo Service";

    /**
     * RESIZE AND ROTATE IMAGE IF NEEDED
     * This method is responsible for solving the rotation issue if exist. Also scale the images
     *
     * @param context       The current context
     * @param selectedImage The Image URI
     * @return Bitmap image results
     * @throws IOException
     */
    public Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage) throws IOException {
        int MAX_HEIGHT = 700;
        int MAX_WIDTH = 700;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        if (imageStream != null) {
            imageStream.close();
        }

//        Log.i(TAG, "Options H and W BEFORE: " + options.outHeight + " " + options.outWidth);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

//        Log.i(TAG, "Options H and W AFTER: " + options.outHeight + " " + options.outWidth);
        img = rotateImageIfRequired(img, selectedImage);

        return img;
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding bitmaps using the decode* methods from {@link BitmapFactory}.
     * This implementation calculates the closest inSampleSize that will result in the final decoded bitmap having a width and height equal to or larger than
     * the requested width and height. This implementation does not ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode method with inJustDecodeBounds==true)
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = Math.min(heightRatio, widthRatio);

            // This offers some additional logic in case the image has a strange aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {
        ExifInterface ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    // Rotate the image if necessary
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public Bitmap generateThumbnail(Bitmap img) {
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(img, 150, 150);
//        Log.i(TAG, "Thumbnail w and h: " + thumbnail.getWidth() + " " + thumbnail.getHeight());
        return thumbnail;
    }


    /**
     * CONVERT TO BASE64 AND COMPRESS SIZE
     *
     * @param bitmap The image bitmap
     */

    // Convert to base64 for image upload
    public String uploadImage(Bitmap bitmap) {

        // Prep image for uploading - Convert to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Compress image and reduce size
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

        byte[] byteArray = stream.toByteArray();

        // Get new image size in bytes.
        int imageSize = byteArray.length;

        // Convert byte array to base 64 string for uploading
        String encodedImage = Base64.encodeToString(byteArray, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);

//        Log.i(TAG, "Uploaded Image size: " + imageSize);

        return encodedImage;
    }

    /**
     * SAVE, LOAD, OR DELETE IMAGE FROM INTERNAL STORAGE
     *
     * @param context     Current context
     * @param bitmapImage The image bitmap
     */

    // Save image to internal storage
    public void saveProfileImageInternalStorage(Context context, Bitmap bitmapImage) {
        ContextWrapper contextWrapper = new ContextWrapper(context);

        // Create the directory and path (e.g. 0/data/data/jayu/app_imageDir)
        File directory = contextWrapper.getDir(GlobalConstants.IMAGE_DIRECTORY, Context.MODE_PRIVATE);

        // Create image file
        File myPath = new File(directory, GlobalConstants.IMAGE_PATH_CHILD);

        // Add bitmap through file output stream
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(myPath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Load image from internal storage
    public Bitmap loadProfileImageInternalStorage(Context context) {
        ContextWrapper contextWrapper = new ContextWrapper(context);

        // Create the directory and path (e.g. 0/data/data/jayu/app_imageDir)
        File directory = contextWrapper.getDir(GlobalConstants.IMAGE_DIRECTORY, Context.MODE_PRIVATE);

        try {
            // Create image file
            File myPath = new File(directory, GlobalConstants.IMAGE_PATH_CHILD);

            // Retrieve bitmap image from internal file
            return BitmapFactory.decodeStream(new FileInputStream(myPath));
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Load profile image error: " + e);
            e.printStackTrace();
        }

        // Return default profile bitmap if no image available
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.default_profile);
    }

    // Delete user profile image
    public void deleteProfileImageInternalStorage(Context context) {
        ContextWrapper contextWrapper = new ContextWrapper(context);

        // Create the directory and path (e.g. 0/data/data/jayu/app_imageDir)
        File directory = contextWrapper.getDir(GlobalConstants.IMAGE_DIRECTORY, Context.MODE_PRIVATE);

        // Create image file
        File myPath = new File(directory, GlobalConstants.IMAGE_PATH_CHILD);

        // Delete File
        myPath.delete();
    }

}

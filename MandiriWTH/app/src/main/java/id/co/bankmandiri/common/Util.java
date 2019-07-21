package id.co.bankmandiri.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author hendrawd on 11/10/15.
 */
public class Util {

    /**
     * A helper to hide keyboard because that android design patter itself is bad for hiding keyboard
     *
     * @param context context where the keyboard exist
     * @param view    view that currently has focus
     */
    public static void hideKeyboardFrom(Context context, View view) {
        if (view != null) {
            // view.clearFocus();
            // if (view instanceof EditText) {
            // }
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            // imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            // InputMethodManager.HIDE_NOT_ALWAYS
        }
    }

    /**
     * Not working
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focus = activity.getCurrentFocus();
        if (focus != null)
            inputMethodManager.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showKeyboard(Activity a, View v) {
        InputMethodManager imm = (InputMethodManager) a
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    public static void forceShowKeyboard(Activity a, View v) {
        InputMethodManager imm = (InputMethodManager) a
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    public static boolean isOnline(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void openUrl(String url, Context ctx) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            ctx.startActivity(browserIntent);
        } catch (Exception e) {
            Toast.makeText(ctx, "Error open url!", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isCollide(View v1, View v2) {
        Rect R1 = new Rect(v1.getLeft(), v1.getTop(), v1.getRight(), v1.getBottom());
        Rect R2 = new Rect(v2.getLeft(), v2.getTop(), v2.getRight(), v2.getBottom());
        return R1.intersect(R2);
    }

    /**
     * I added synchronized to make sure that different shares will not override each others
     *
     * @param text         text to share
     * @param extraSubject extra subject/title
     * @param ctx          Activity
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    public synchronized static void shareTextUrl(String text, String extraSubject, Context ctx) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        else
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, extraSubject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        ctx.startActivity(Intent.createChooser(shareIntent, "Share with"));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    public static Intent getShareImageIntent(Context context, String pathToImage) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        else
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        shareIntent.setType("image/*");

        // For a file in shared storage.  For data in private storage, use a ContentProvider.
//        Uri uri = Uri.fromFile(context.getFileStreamPath(pathToImage));
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        //TODO must test it
        //previously Uri uri = Uri.fromFile(context, new File(pathToImage));
        Uri uri = Util.uriFromFile(context, new File(pathToImage));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        return shareIntent;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    public static Intent getShareImageIntent(Uri imageUri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        else
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        return shareIntent;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    public static Intent getShareImageListIntent(Context context, List<String> pathsToImages) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        else
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        shareIntent.setType("image/*");

        // For a file in shared storage.  For data in private storage, use a ContentProvider.
//        Uri uri = Uri.fromFile(context.getFileStreamPath(pathToImage));
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        ArrayList<Uri> imageUriList = new ArrayList<>();

        for (String pathToImage : pathsToImages) {
            imageUriList.add(Util.uriFromFile(context, new File(pathToImage)));
        }

        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriList);
        return shareIntent;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    public static Intent shareImageAndText(Context context, String pathToImage) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        else
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        shareIntent.setType("*/*");

        // For a file in shared storage.  For data in private storage, use a ContentProvider.
//        Uri uri = Uri.fromFile(context.getFileStreamPath(pathToImage));
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        Uri uri = Util.uriFromFile(context, new File(pathToImage));
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared image");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Shared by");

        return shareIntent;
    }

    @SuppressWarnings("deprecation")
    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        } else {
            return false;
        }
    }

    /**
     * return the byte string that can easily readable by human :D
     *
     * @param bytes long of bytes
     * @param si    The International System of Units(SI format) or Binary format
     * @return Example output:
     * <p>
     * SI     BINARY
     * <p>
     * 0:        0 B        0 B
     * 27:       27 B       27 B
     * 999:      999 B      999 B
     * 1000:     1.0 kB     1000 B
     * 1023:     1.0 kB     1023 B
     * 1024:     1.0 kB    1.0 KiB
     * 1728:     1.7 kB    1.7 KiB
     * 110592:   110.6 kB  108.0 KiB
     * 7077888:     7.1 MB    6.8 MiB
     * 452984832:   453.0 MB  432.0 MiB
     * 28991029248:    29.0 GB   27.0 GiB
     * 1855425871872:     1.9 TB    1.7 TiB
     * 9223372036854775807:     9.2 EB    8.0 EiB   (Long.MAX_VALUE)
     */

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format(Locale.US, "%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * example of retrieving file informations and file byte array
     */
//    File f = new File("/data/cat.jpg");
//
//    //Size of file in bytes
//    Long size = f.length();
//
//    //Time of when the file was last modified in microseconds
//    Long lastModified = f.lastModified();
//
//    //The bytes of the file. This gets populated below
//    byte[] fileData = new byte[(int)f.length()];
//
//    try {
//        FileInputStream fis = new FileInputStream(f);
//
//        int offset = 0;
//        int bytesActuallyRead;
//
//        while( (bytesActuallyRead = fis.read(fileData, offset, 1024)) > 0) {
//            offset += bytesActuallyRead;
//        }
//
//    } catch(Exception e) {
//
//    }

    /**
     * example of retrieving file informations and file byte array
     * these code should be used for sharing files, but should be worked too
     */
//    Uri returnUri = returnIntent.getData();
//    Cursor returnCursor =
//            getContentResolver().query(returnUri, null, null, null, null);
//    /*
//     * Get the column indexes of the data in the Cursor,
//     * move to the first row in the Cursor, get the data,
//     * and display it.
//     */
//    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
//    returnCursor.moveToFirst();
//    TextView nameView = (TextView) findViewById(R.id.filename_text);
//    TextView sizeView = (TextView) findViewById(R.id.filesize_text);
//    nameView.setText(returnCursor.getString(nameIndex));
//    sizeView.setText(Long.toString(returnCursor.getLong(sizeIndex)));
    public static String encodeBase64(final String clearText, String password) {
        String lastString = clearText + password;
        return Base64.encodeToString(lastString.getBytes(), Base64.DEFAULT);
    }

    public static String decodeBase64(final String encodedString,
                                      String password) {
        String raw = new String(Base64.decode(encodedString, Base64.DEFAULT));
        if (raw.endsWith(password)) {
            return raw.substring(0, raw.length() - password.length());
        } else {
            return "";
        }
    }

    /**
     * Generate random int with little chances of same int
     * It is better than just Random() class
     *
     * @param min min value(inclusive)
     * @param max max value(inclusive)
     * @return random int
     */
    public static int randInt(int min, int max) {
        return new SecureRandom().nextInt((max - min) + 1) + min;
    }

    /**
     * change the drawable passed to gray scale
     *
     * @param drawable drawable that you want to change
     * @return gray-scaled drawable from the drawable passed
     */
    public static Drawable convertToGrayscale(Drawable drawable) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        drawable.setColorFilter(filter);
        return drawable;
    }

    /**
     * copy a text to android clipboard, support all version of android
     *
     * @param context - android context
     * @param text    - text to be copied
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("deprecation")
    public static void setClipboard(Context context, String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * start activity with image view shared element transition if the version greater than lollipop
     * or no shared element transition if the version below lollipop
     * Don't forget to add
     * android:transitionName="yourPreferredTransitionName"
     * to the shared elements
     *
     * @param context activity
     * @param intent  intent to start
     * @param iv      shared element image view
     */
    public static void startActivityWithSharedElementTransitionIfPossible(Context context, Intent intent, ImageView iv) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            context.startActivity(intent);
        } else {
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    (Activity) context, iv, iv.getTransitionName()).toBundle());
        }
    }

    /**
     * http://stackoverflow.com/questions/3004713/get-content-uri-from-file-path-in-android
     *
     * @param context   android context
     * @param imageFile image file
     * @return uri of image file
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                cursor.close();
                return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
            } else {
                cursor.close();
                return null;
            }
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * old ways to get OS Version, haven't tested it yet if it still working
     *
     * @return
     */
    public static int getOsVersion() {
        try {
            java.lang.reflect.Field osField = Build.VERSION.class.getDeclaredField("SDK_INT");
            osField.setAccessible(true);
            return osField.getInt(Build.VERSION.class);
        } catch (Exception e) {
            return 3;
        }
    }

    /**
     * create thumbnail to the destination file, maximized for low memory device
     *
     * @param originalFile
     * @param thumbnailFile
     * @param desiredWidth
     * @param desiredHeight
     */
    public static void createThumbnailFile(File originalFile, File thumbnailFile,
                                           int desiredWidth, int desiredHeight) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

        bitmapOptions.inJustDecodeBounds = true; // obtain the size of the image, without loading it in memory
        BitmapFactory.decodeFile(originalFile.getAbsolutePath(), bitmapOptions);

        float widthScale = (float) bitmapOptions.outWidth / desiredWidth;
        float heightScale = (float) bitmapOptions.outHeight / desiredHeight;
        float scale = Math.min(widthScale, heightScale);

        int sampleSize = 1;
        while (sampleSize < scale) {
            sampleSize *= 2;
        }
        bitmapOptions.inSampleSize = sampleSize; // this value must be a power of 2,
        // this is why you can not have an image scaled as you would like to have
        bitmapOptions.inJustDecodeBounds = false; // now we want to load the image

        // Let's load just the part of the image necessary for creating the thumbnail, not the whole image
        Bitmap thumbnail = BitmapFactory.decodeFile(originalFile.getAbsolutePath(), bitmapOptions);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(thumbnailFile);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Use the thumbail on an ImageView or recycle it!
        thumbnail.recycle();
    }

    /**
     * create thumbnail to the destination file, maximized for low memory device
     *
     * @param originalFile
     * @param thumbnailFile
     * @param desiredWidth
     */
    public static void createThumbnailFileBasedOnWidth(File originalFile, File thumbnailFile,
                                                       int desiredWidth) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

        bitmapOptions.inJustDecodeBounds = true; // obtain the size of the image, without loading it in memory
        BitmapFactory.decodeFile(originalFile.getAbsolutePath(), bitmapOptions);

        float scale = (float) bitmapOptions.outWidth / desiredWidth;

        int sampleSize = 1;
        while (sampleSize < scale) {
            sampleSize *= 2;
        }
        bitmapOptions.inSampleSize = sampleSize; // this value must be a power of 2,
        // this is why you can not have an image scaled as you would like to have
        bitmapOptions.inJustDecodeBounds = false; // now we want to load the image

        // Let's load just the part of the image necessary for creating the thumbnail, not the whole image
        Bitmap thumbnail = BitmapFactory.decodeFile(originalFile.getAbsolutePath(), bitmapOptions);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(thumbnailFile);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Use the thumbail on an ImageView or recycle it!
        thumbnail.recycle();
    }

    public static void deleteFileRecursive(File dir) {
        deleteDirectoryChildrens(dir);
        dir.delete();
    }

    public static void deleteDirectoryChildrens(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                File temp = new File(dir, aChildren);
                if (temp.isDirectory()) {
                    deleteFileRecursive(temp);
                } else {
                    temp.delete();
                }
            }
        }
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Use FileProvider to get uri of a File
     * It is a must on Android API >= 24 if passing a file uri to other application
     * You must specify file provider on your manifest and provide it in res folder
     *
     * @param context android context
     * @param file    file to get the uri
     * @return Uri object
     */
    public static Uri uriFromFile(Context context, File file) {
        Uri uri;
        try {
            uri = FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider",
                    file);
        } catch (IllegalArgumentException iae) {
            //on samsung test device
            //java.lang.IllegalArgumentException: Failed to find configured root that contains /storage/extSdCard/DCIM/Camera/20160922_164408.jpg
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * Generate unique integer from a text
     *
     * @param text String
     * @return int unique integer
     */
    public static int getUniqueInteger(String text) {
        int hash = text.hashCode();
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(text.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashText = bigInt.toString(10);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            int temp = 0;
            for (int i = 0; i < hashText.length(); i++) {
                char c = hashText.charAt(i);
                temp += (int) c;
            }
            return hash + temp;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    /**
     * Make the status bar color translucent or disable it
     *
     * @param activity        Activity caller
     * @param makeTranslucent boolean translucent or not
     */
    public static void setStatusBarTranslucent(Activity activity, boolean makeTranslucent) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (makeTranslucent) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    /**
     * Change status bar color
     *
     * @param activity Activity caller
     * @param color    ColorInt color to set
     */
    public static void setStatusBarColorInt(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    /**
     * Change status bar color
     *
     * @param activity Activity caller
     * @param color    ColorInt color to set
     */
    public static void setStatusBarColorRes(Activity activity, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, color));
        }
    }

    /**
     * Show/hide a view with a boolean value
     *
     * @param view      View to show/hide
     * @param isVisible boolean indicating visible or invisible
     */
    public static void setVisible(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Memakai alamat untuk membuka google map
     * https://developers.google.com/maps/documentation/urls/android-intents
     *
     * @param context android context
     * @param address alamat
     */
    public static void showOnMap(Context context, String address) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?z=18&q=" + Uri.encode(address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        // Cek apa aplikasi google maps terinstall di phone
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            // kalau terinstall, maka buka aplikasi google maps
            context.startActivity(mapIntent);
        } else {
            // kalau tidak terinstall, maka memunculkan toast dengan pesan "No application to open map"
            Toast.makeText(context.getApplicationContext(), "No application to open map", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Memakai nomor telepon untuk membuka aplikasi telepon default dari android
     *
     * @param context android context
     * @param phone   nomor telepon
     */
    public static void call(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        context.startActivity(intent);
    }
}

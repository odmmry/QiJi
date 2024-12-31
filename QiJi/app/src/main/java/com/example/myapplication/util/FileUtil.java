package com.example.myapplication.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class FileUtil {

     public static String getPathFromUri(final Context context, final Uri uri) {
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            String uuid = UUID.randomUUID().toString();
            File targetDirectory = new File(context.getCacheDir(), uuid);
            targetDirectory.mkdir();
            targetDirectory.deleteOnExit();
            String fileName = getImageName(context, uri);
            String extension = getImageExtension(context, uri);

            if (fileName == null) {
                Log.w("FileUtils", "Cannot get file name for " + uri);
                if (extension == null) extension = ".jpg";
                fileName = "image_picker" + extension;
            } else if (extension != null) {
                fileName = getBaseName(fileName) + extension;
            }
            String filePath = new File(targetDirectory, fileName).getPath();
            File outputFile = saferOpenFile(filePath, targetDirectory.getCanonicalPath());
            try (OutputStream outputStream = new FileOutputStream(outputFile)) {
                copy(inputStream, outputStream);
                return outputFile.getPath();
            }
        } catch (IOException e) {
            return null;
        } catch (SecurityException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    protected static @NonNull File saferOpenFile(@NonNull String path, @NonNull String expectedDir)
            throws IllegalArgumentException, IOException {
        File f = new File(path);
        String canonicalPath = f.getCanonicalPath();
        if (!canonicalPath.startsWith(expectedDir)) {
            throw new IllegalArgumentException(
                    "Trying to open path outside of the expected directory. File: "
                            + f.getCanonicalPath()
                            + " was expected to be within directory: "
                            + expectedDir
                            + ".");
        }
        return f;
    }

    private static String getImageName(Context context, Uri uriImage) {
        try (Cursor cursor = queryImageName(context, uriImage)) {
            if (cursor == null || !cursor.moveToFirst() || cursor.getColumnCount() < 1) return null;
            String unsanitizedImageName = cursor.getString(0);
            return sanitizeFilename(unsanitizedImageName);
        }
    }

    private static Cursor queryImageName(Context context, Uri uriImage) {
        return context
                .getContentResolver()
                .query(uriImage, new String[] {MediaStore.MediaColumns.DISPLAY_NAME}, null, null, null);
    }

    private static String getImageExtension(Context context, Uri uriImage) {
        String extension;

        try {
            if (uriImage.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                final MimeTypeMap mime = MimeTypeMap.getSingleton();
                extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uriImage));
            } else {
                extension =
                        MimeTypeMap.getFileExtensionFromUrl(
                                Uri.fromFile(new File(uriImage.getPath())).toString());
            }
        } catch (Exception e) {
            return null;
        }

        if (extension == null || extension.isEmpty()) {
            return null;
        }

        return "." + sanitizeFilename(extension);
    }

    protected static @Nullable String sanitizeFilename(@Nullable String displayName) {
        if (displayName == null) {
            return null;
        }

        String[] badCharacters = new String[] {"..", "/"};
        String[] segments = displayName.split("/");
        String fileName = segments[segments.length - 1];
        for (String suspString : badCharacters) {
            fileName = fileName.replace(suspString, "_");
        }
        return fileName;
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        final byte[] buffer = new byte[4 * 1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        out.flush();
    }


    private static String getBaseName(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0) {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }


    public static long getFileSize(Context context, Uri uri) {
        long fileSize = 0;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor!= null && cursor.moveToFirst()) {
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            if (sizeIndex!= -1) {
                fileSize = cursor.getLong(sizeIndex);
            }
            cursor.close();
        }
        return fileSize;
    }

    public static String getResourcesUri(Context context, @DrawableRes int id) {
        Resources resources = context.getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }

//    public static String getRealPathFromURI(final Context context, final Uri uri) {
//        String path = "";
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
//        if (cursor != null) {
//            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            path = cursor.getString(index);
//            cursor.close();
//        }
//
//        return path;
//
//        //        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
////
////        // DocumentProvider
////        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
////            // ExternalStorageProvider
////            if (isExternalStorageDocument(uri)) {
////                final String docId = DocumentsContract.getDocumentId(uri);
////                final String[] split = docId.split(":");
////                final String type = split[0];
////
////                if ("primary".equalsIgnoreCase(type)) {
////                    return Environment.getExternalStorageDirectory() + "/" + split[1];
////                }
////
////                // TODO handle non-primary volumes
////            }
////            // DownloadsProvider
////            else if (isDownloadsDocument(uri)) {
////
////                final String id = DocumentsContract.getDocumentId(uri);
////                final Uri contentUri = ContentUris.withAppendedId(
////                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
////
////                return getDataColumn(context, contentUri, null, null);
////            }
////            // MediaProvider
////            else if (isMediaDocument(uri)) {
////                final String docId = DocumentsContract.getDocumentId(uri);
////                final String[] split = docId.split(":");
////                final String type = split[0];
////
////                Uri contentUri = null;
////                if ("image".equals(type)) {
////                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
////                } else if ("video".equals(type)) {
////                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
////                } else if ("audio".equals(type)) {
////                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
////                }
////
////                final String selection = "_id=?";
////                final String[] selectionArgs = new String[] {
////                        split[1]
////                };
////
////                return getDataColumn(context, contentUri, selection, selectionArgs);
////            }
////        }
////        // MediaStore (and general)
////        else if ("content".equalsIgnoreCase(uri.getScheme())) {
////
////            // Return the remote address
////            if (isGooglePhotosUri(uri))
////                return uri.getLastPathSegment();
////
////            return getDataColumn(context, uri, null, null);
////        }
////        // File
////        else if ("file".equalsIgnoreCase(uri.getScheme())) {
////            return uri.getPath();
////        }
////
////        return null;
//    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}

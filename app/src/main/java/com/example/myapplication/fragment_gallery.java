package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;

// 2번째 프래그먼트 (명함 갤러리)

public class fragment_gallery extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    View rootView;

    private RecyclerView recyclerView;
    private GridViewAdapter adapter;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    File filePath;
    List<Gallery> lstGallery;

    private final int CAMERA_REQUEST_CODE = 40;
    private final int GALLERY_REQUEST_CODE = 50;

    public fragment_gallery() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static fragment_gallery newInstance(String param1, String param2) {
        fragment_gallery fragment = new fragment_gallery();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lstGallery = new ArrayList<>();
        lstGallery.add(new Gallery(R.drawable.bw01));
        lstGallery.add(new Gallery(R.drawable.bw02));
        lstGallery.add(new Gallery(R.drawable.bw03));
        lstGallery.add(new Gallery(R.drawable.bw04));
        lstGallery.add(new Gallery(R.drawable.bw05));
        lstGallery.add(new Gallery(R.drawable.bw06));
        lstGallery.add(new Gallery(R.drawable.bw07));
        lstGallery.add(new Gallery(R.drawable.bw08));
        lstGallery.add(new Gallery(R.drawable.bw09));
        lstGallery.add(new Gallery(R.drawable.bw10));
        lstGallery.add(new Gallery(R.drawable.bw11));
        lstGallery.add(new Gallery(R.drawable.bw12));
        lstGallery.add(new Gallery(R.drawable.bw13));
        lstGallery.add(new Gallery(R.drawable.bw14));
        lstGallery.add(new Gallery(R.drawable.bw15));
        lstGallery.add(new Gallery(R.drawable.bw16));
        lstGallery.add(new Gallery(R.drawable.bw17));
        lstGallery.add(new Gallery(R.drawable.bw18));
        lstGallery.add(new Gallery(R.drawable.bw19));
        lstGallery.add(new Gallery(R.drawable.bw20));
        lstGallery.add(new Gallery(R.drawable.bw21));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        GridView gridView = rootView.findViewById(R.id.galleryView);
        adapter = new GridViewAdapter(rootView.getContext(), lstGallery);
        gridView.setAdapter(adapter);


        FloatingActionButton fab = rootView.findViewById(R.id.goToCam);                             // 카메라
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    String dirPath = rootView.getContext().getExternalFilesDir(null).getPath();
                    File dir = new File(dirPath);
                    if(!dir.exists()) {
                        Log.d("MAKE DIR", dir.mkdirs() + "");

                    }

                    filePath = File.createTempFile("IMG", ".jpg", dir);
                    if(!filePath.exists()) {
                        filePath.createNewFile();
                    }

                    Uri photoUri = FileProvider.getUriForFile(rootView.getContext(), "com.example.myapplication.provider", filePath);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        FloatingActionButton gal = rootView.findViewById(R.id.goToGallery);                         // 갤러리
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent();
                newIntent.setType("image/*");
                newIntent.setAction(Intent.ACTION_GET_CONTENT);
                newIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(newIntent, "Select Picture"), GALLERY_REQUEST_CODE);
            }
        });

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if(filePath != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                try {
                    InputStream in = new FileInputStream(filePath);
                    BitmapFactory.decodeStream(in, null, options);
                    in.close();
                    in = null;
                } catch ( Exception e ) {
                    e.printStackTrace();
                }

                final int width = options.outWidth;
                final int height = options.outHeight;


                // width, height 값에 따라 inSaampleSize 값 계산


                BitmapFactory.Options imgOptions = new BitmapFactory.Options();
                imgOptions.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath(), imgOptions);

                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap bmRotated = rotateBitmap(bitmap, orientation);
                Gallery gallery = new Gallery(bmRotated);
                lstGallery.add(gallery);
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                if(data.getClipData() != null) {
                    for (int index = 0; index < data.getClipData().getItemCount(); index++) {
                        Uri uri = data.getClipData().getItemAt(index).getUri();

                        InputStream in = getActivity().getContentResolver().openInputStream(uri);

                        BitmapFactory.Options imgOptions = new BitmapFactory.Options();
                        imgOptions.inSampleSize = 4;

                        Bitmap img = BitmapFactory.decodeStream(in, null, imgOptions);
                        in.close();


                        ExifInterface exif = null;
                        try {
                            exif = new ExifInterface(getPathFromUri(getContext() ,uri));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);

                        Bitmap bmRotated = rotateBitmap(img, orientation);

                        lstGallery.add(new Gallery(bmRotated));
                    }
                    adapter.notifyDataSetChanged();

                } else {
                    Uri uri = data.getData();

                    InputStream in = getActivity().getContentResolver().openInputStream(uri);

                    BitmapFactory.Options imgOptions = new BitmapFactory.Options();
                    imgOptions.inSampleSize = 4;

                    Bitmap img = BitmapFactory.decodeStream(in, null, imgOptions);
                    in.close();


                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(getPathFromUri(getContext() ,uri));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap bmRotated = rotateBitmap(img, orientation);

                    lstGallery.add(new Gallery(bmRotated));
                    adapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

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

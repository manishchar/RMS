package alina.com.rms.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import alina.com.rms.R;

/**
 * Created by alina on 08-03-2018.
 */

public class CameraAndGalleryUtil {

    private AppCompatActivity mContext;
    public static final int REQUEST_TAKE_PHOTO = 1;
    private static String packagePath="alina.com.rms.fileprovider";
    private String mCurrentPhotoPath;
    private Uri photoURI;
    private File temp_path;
    private final int COMPRESS = 90;
    public static int IMAGE_MAX_SIZE=600;
    public CameraAndGalleryUtil(AppCompatActivity mContext)
    {
        this.mContext=mContext;
    }

    public File createImageFile(String img_name) throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = img_name;//"JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void dispatchTakePictureIntent(String img_name) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(img_name);
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                 photoURI = FileProvider.getUriForFile(mContext,
                        packagePath,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                mContext.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public String getPhotoURI()
    {
        return mCurrentPhotoPath;
    }

    public File galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
        return f;
    }


    public void setPic(ImageView mImageView) {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }



    public void deleteFile()
    {
        if(mCurrentPhotoPath!=null) {
            File f = new File(mCurrentPhotoPath);
            File f1=f.getAbsoluteFile();
            Log.e("path",""+f1.getAbsolutePath());
            if (f1.exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean result = false;
                    try {
                        result = Files.deleteIfExists(f1.toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (result)
                    {
                        Log.e("delete file oreo","Yes");
                    }
                    else {
                        Log.e("delete file oreo","No");
                    }
                }
                else if(f1.delete())
                {
                    Log.e("delete file cache","Yes");
                }
                else {
                    Log.e("delete file cache","NO");
                }
            }

        }
    }

    public void deleteFile(File file)
    {
        if(file!=null) {
            File f = file.getAbsoluteFile();
            if (f.exists()) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean result = false;
                    try {
                        result = Files.deleteIfExists(file.toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (result)
                    {
                        Log.e("delete file oreo","Yes");
                    }
                    else {
                        Log.e("delete file oreo","No");
                    }
                }
               else if(f.delete())
               {
                   Log.e("delete file","Yes");
               }
               else {
                   Log.e("delete file","No");
               }
            }

        }
    }



    public String saveGalaryImageOnLitkat(Bitmap bitmap,String file_name) {
        try {
            File cacheDir;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                cacheDir = new File(Environment.getExternalStorageDirectory(), mContext.getResources().getString(R.string.app_name));
            else
                cacheDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!cacheDir.exists())
                cacheDir.mkdirs();
            String filename = file_name+ ".png";
            File file = new File(cacheDir, filename);
            temp_path = file.getAbsoluteFile();

            if(!file.exists())
            {
                file.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(file);
            Bitmap bitmap1=getResizedBitmap(bitmap, IMAGE_MAX_SIZE);
            bitmap1.setHasAlpha(true);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, COMPRESS, out);

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public String saveGalaryImageOnLitkat1(Bitmap bitmap,String file_name) {
        try {
            File cacheDir;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                cacheDir = new File(Environment.getExternalStorageDirectory(), mContext.getResources().getString(R.string.app_name));
            else
                cacheDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!cacheDir.exists())
                cacheDir.mkdirs();
            String filename = file_name+ ".jpg";
            File file = new File(cacheDir, filename);
            temp_path = file.getAbsoluteFile();

            if(!file.exists())
            {
                file.createNewFile();
            }

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static final int SELECT_PICTURE = 1221;

    private String selectedImagePath;

    public void openGallery()
    {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
      /*  mContext.startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                SELECT_PICTURE);*/
        mContext.startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                SELECT_PICTURE);
    }
    /*
       * helper to retrieve the path of an image URI
      */
    public  String getPath(Uri uri,String img_name) throws IOException {
        // just some safety built in

        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
        return saveGalaryImageOnLitkat(bitmap,img_name);


    }



    public static int byteSizeOf(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
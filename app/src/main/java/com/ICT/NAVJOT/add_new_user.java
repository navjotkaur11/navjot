package com.ICT.NAVJOT;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ICT.NAVJOT.R;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class add_new_user extends Fragment {

    EditText fnameEdit, lnameEdit;
    Button submitBtn, resetBtn;

    DBHelper db;
    File photofile = null;
    String mCurrentPhotoPath;
    public static final String IMAGE_DIRECTORY_NAME = String.valueOf((R.string.app_name));
    Uri imageUri;
    Spinner sp;
    String activity_type;
    TrackerGPS gps;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CODE_CAMERA_ID = 106;
    String TAG="MainActivity";
    String picturePath_id ="";
    Bitmap bitmapProfileImage3 = null;
    public static String lastCompressedImageFileName="";
    Uri selectedImageUri;
    File selectedImageFile3 = null;
    public static TextView date;
    EditText datevalue;
    EditText title,place,duration,comment;
    ImageView img;
    String[] string_ref = new String[]{
            "  Work",
            "  Study",
            "  Leisure",
            "  Sport "
    };


    ListView nameList;
    Button registerBtn;
    Cursor cursor;
    String currentaddress;


    public add_new_user() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add New Customer");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_new_user, container, false);
  //      setTitle("Log New Activity");
        setcurrentlatlongadd();

        title=(EditText)view.findViewById(R.id.ed_title);
        date=(TextView)view.findViewById(R.id.txt_date);
        sp=(Spinner)view.findViewById(R.id.spinner3);
        ArrayAdapter aa3 = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, string_ref);
        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(aa3);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activity_type= sp.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        img=(ImageView)view.findViewById(R.id.imageView);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectimage();
            }
        });
        datevalue=(EditText)view.findViewById(R.id.txt_datevalue);

        place=(EditText)view.findViewById(R.id.ed_place);
        place.setText(currentaddress);
        duration=(EditText)view.findViewById(R.id.ed_duration);
        comment=(EditText)view.findViewById(R.id.ed_comment);
        final DBHelper  db=new DBHelper(getContext());
        Button btn_save=(Button)view.findViewById(R.id.button);
        Button btn_cancal=(Button)view.findViewById(R.id.button2);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().isEmpty())
                {
                    title.setError("Enter Title.");
                    title.requestFocus();
                }
                else if(datevalue.getText().toString().isEmpty())
                {
                    datevalue.setError("Enter Date");
                    datevalue.requestFocus();
                }
                else if(place.getText().toString().isEmpty())
                {
                    place.setError("Enter Address");
                    place.requestFocus();
                }
                else if(duration.getText().toString().isEmpty())
                {
                    duration.setError("Enter Duration");
                    duration.requestFocus();
                }
                else if(comment.getText().toString().isEmpty())
                {
                    comment.setError("Enter comment");
                    comment.requestFocus();
                }
                else if(picturePath_id.trim()=="")
                {
                    Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
                }
                else

                    try{
                        db.open();
                        String tit = title.getText().toString();
                        String dat = datevalue.getText().toString();
                        String typ = activity_type.toString();
                        String pla = place.getText().toString();
                        String dur = duration.getText().toString();
                        String com = comment.getText().toString();
                        String imgpath = picturePath_id;
                        long val= db.insertdata(tit,dat,typ,pla,dur,com,imgpath);
                        db.close();
                        Toast.makeText(getContext(), Long.toString(val),Toast.LENGTH_LONG).show();
                        //     Toast.makeText(getBaseContext(),"Inserted",Toast.LENGTH_SHORT).show();
                  //      finish();


                    }catch (Exception e){
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

            }
        });
        return  view;
    }


    public static boolean checkAppVersion()
    {
        Boolean isM=false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            isM=true;
        }
        else
        {
            isM=false;
        }
        return isM;
    }
    private void selectimage(){

        final CharSequence[] items={"Camera","Gallery","Cancel"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Select Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            //        @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //      try{
                if(items[i].equals("Camera"))
                {

                    if(checkAppVersion())
                    {
                        if( ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED) {


                            Intent callcameraapplicationintent = new Intent();
                            callcameraapplicationintent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                            photofile = createImageFile();
                            String authorities = getContext().getPackageName() + ".fileprovider";
                            imageUri = FileProvider.getUriForFile(getContext(), authorities, photofile);
                            // imageUri = AppUtills.getOutputMediaFileUri(MEDIA_TYPE_IMAGE, TAG);
                            callcameraapplicationintent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                            startActivityForResult(callcameraapplicationintent, REQUEST_CODE_CAMERA_ID);
                            // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            dialog.dismiss();
                        }else {
                            Intent callcameraapplicationintent = new Intent();
                            callcameraapplicationintent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                            photofile = createImageFile();
                            String authorities = getContext().getPackageName() + ".fileprovider";
                            imageUri = FileProvider.getUriForFile(getContext(), authorities, photofile);
                            // imageUri = AppUtills.getOutputMediaFileUri(MEDIA_TYPE_IMAGE, TAG);
                            callcameraapplicationintent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                            startActivityForResult(callcameraapplicationintent, REQUEST_CODE_CAMERA_ID);
                            // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            dialog.dismiss();
                        }

                    }
                    else
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        imageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, TAG);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        //       if(AppUtills.showLogs) Log.v(TAG,"captureimage...."+imageUri);
                        startActivityForResult(intent, REQUEST_CODE_CAMERA_ID);
                        // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        dialog.dismiss();
                    }



                } else if (items[i].equals("Gallery")) {


                    Intent ir= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(ir,RESULT_LOAD_IMAGE);


                } else if (items[i].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    public static Uri getOutputMediaFileUri(int type,String PageName)
    {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                //    if(AppUtills.showLogs)Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "+ IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + time + ".jpg");
        }
        else
        {
            return null;
        }

//        if(AppUtills.showLogs)Log.v(PageName + "  mediaFile", "" + mediaFile);
//        if(AppUtills.showLogs) Log.v(PageName+"  uri is",""+ Uri.fromFile(mediaFile));
        //       if(AppUtills.showLogs)Log.v(PageName+"  uri is path",""+Uri.fromFile(mediaFile).getPath());

        return  Uri.fromFile(mediaFile);
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {

            e.printStackTrace();
            // String s= String.valueOf(e);
            //Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();

        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {


            if (data != null) {
                picturePath_id = getPath(data.getData(), getContext());

                if (picturePath_id.length() > 0) {
                    //           if (AppUtills.showLogs) Log.d("filepath", "filepath " + picturePath_id);
                    selectedImageUri = data.getData();
                    pickImageFromGallery3(picturePath_id);
                }
            }

        }


        else if (requestCode == REQUEST_CODE_CAMERA_ID && resultCode==RESULT_OK)
        {
            //         if(AppUtills.showLogs)Log.v(TAG, "onActivityResult REQUEST_CODE_CAMERA_ID called...");
            if (checkAppVersion()) {
                selectedImageUri = imageUri;

                pickImageFromGallery3(photofile.getPath());

                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(getContext(),
                        new String[]{imageUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            }else {
                selectedImageUri=imageUri;
                pickImageFromGallery3(imageUri.getPath());
            }
        }
    }
    public static String getPath(Uri uri, Context context)
    {
        if (uri == null)
            return null;

        //    if(AppUtills.showLogs)Log.d("URI", uri + "");
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor =context.getContentResolver().query(uri, projection,null, null, null);
        if (cursor != null)
        {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String temp = cursor.getString(column_index);
            cursor.close();
            //     if(AppUtills.showLogs)Log.v("temp",""+temp);
            return temp;
        }
        else
            return null;
    }
    public void pickImageFromGallery3(String imagePath3) {
        try {
            picturePath_id = imagePath3;
            Matrix matrix = new Matrix();
            int rotate = 0;

            File imageFile = new File(imagePath3.toString());
            selectedImageFile3 = imageFile;
            Log.e(TAG, "Image Size before comress : " + imageFile.length());

            try {
                ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate -= 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate -= 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate -= 90;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            matrix.postRotate(rotate);
            Bitmap user_image = BitmapFactory.decodeFile(imagePath3);
            Log.e(TAG, "original user_image.getWidth()" + user_image.getWidth() + " user_image.getHeight()" + user_image.getHeight());

            if (imageFile.length() > 10000) {
                Bitmap bitmap = compressImage(imagePath3);
                Log.e(TAG, "compress bitmap.getWidth()" + bitmap.getWidth() + " bitmap.getHeight()" + bitmap.getHeight());

                File fileSize = new File(lastCompressedImageFileName);
                selectedImageFile3 = fileSize;
                Log.e(TAG, "Image Size after in what comress : " + fileSize.length());
                if (fileSize.length() > 5000000) {
                    //              AppUtills.alertDialog(getContext(), "Maximum image size limit less then 500KB.");
                } else {
                    picturePath_id = lastCompressedImageFileName;
                    bitmapProfileImage3 = bitmap;
                    img.setImageBitmap(bitmap);
                }
            } else {
                int width = 0, height = 0;
                if (user_image.getWidth() > 250) {
                    width = 250;
                } else {
                    width = user_image.getWidth();
                }
                if (user_image.getHeight() > 250) {
                    height = 250;
                } else {
                    height = user_image.getHeight();
                }

                Bitmap bitmap = Bitmap.createBitmap(user_image, 0, 0, width, height, matrix, true);
                Log.e(TAG, "compress bitmap.getWidth()" + bitmap.getWidth() + " bitmap.getHeight()" + bitmap.getHeight());

                bitmapProfileImage3 = bitmap;
                img.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //       AppUtills.sendExceptionMail(d_my_account.this, e, TAG);
            //String s= String.valueOf(e);
            // Toast.makeText(this, String.valueOf(e), Toast.LENGTH_SHORT).show();
            //AppUtills.showExceptionDialog(MyAccountActivity.this);
        }
    }
    public static Bitmap compressImage(String filePath)
    {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 250.0f;
        float maxWidth = 250.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        lastCompressedImageFileName = getFilename();
        try
        {
            out = new FileOutputStream(lastCompressedImageFileName);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 30, out);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return scaledBitmap;
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    public static String getFilename() {
        //File file = new File(Environment.getExternalStorageDirectory().getPath(),"RCM/Images");
        File file = new File(Environment.getExternalStorageDirectory().getPath(),IMAGE_DIRECTORY_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    private void setcurrentlatlongadd() {
        gps = new TrackerGPS(getContext());
        double latitude = gps.getLatitude();
        double longitude =gps.getLongitude();

        // \n is for new line
        //    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

        if(latitude!=0 || latitude!=0.0 || longitude!=0 || longitude!=0.0)
        {
            Geocoder geocoder = new Geocoder(getContext(), Locale.ENGLISH);
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                currentaddress = returnedAddress.getAddressLine(0);
                Toast.makeText(getContext(), currentaddress, Toast.LENGTH_SHORT).show();

            }
            else{
                currentaddress="No Address";
                Toast.makeText(getContext(), "No Address", Toast.LENGTH_SHORT).show();
            }

        }else { Toast.makeText(getContext(), "No Address", Toast.LENGTH_SHORT).show();}


    }

}

package practice.com.dailyselfie;

import android.app.AlarmManager;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

/***
 * Main activity that is shown when application is initiates
 * load existing images and didplay appropriate buttons
 */

public class DailyActivity extends ActionBarActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mImageView;
    private ArrayList<PhotoData> mListOfImages;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private ListView mListView;
    private ListAdapter mListAdapter;
    private File mphotoFile;
    private final int TWO_MINUTE_INTERVAL = 60000*60;
    static final String IMAGE_ID = "ImageUrl";
    static final String FILE_NAME_EXTENSION = "_JPEG";

    Stack<String> mListOfPaths = new Stack<String>();



    private File storageDir = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);



        mListView = (ListView) findViewById(R.id.listView);

        storageDir =  getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);



        attachClickListener();

        attachLongClickListener();

        displayImages();

        mListAdapter = new ListAdapter(DailyActivity.this, mListOfImages);
        mListView.setAdapter(mListAdapter);

        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);



    }

    private void attachLongClickListener() {

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (mActionMode != null) {
                    return false;
                }

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = startActionMode(mActionModeCallback);
                mActionMode.setTag(mListOfImages.get(position));
                view.setSelected(true);
                return true;
            }



        });
    }


    private  ActionMode mActionMode = null;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_actions, menu);


            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.deleteAll:
                    deleteCurrentItem(mode);

                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    // deletes a chosen item
    private void deleteCurrentItem(ActionMode mode) {

        File myPath = storageDir;

        try {

            for (File f : myPath.listFiles()) {

                PhotoData photoToRemove = (PhotoData)mode.getTag();

                if(f.getAbsolutePath().equals(photoToRemove.getFile().getAbsolutePath())){
                    f.delete();

                    mListOfImages.remove(photoToRemove);
                    mListAdapter.notifyDataSetChanged();
                }

            }
        } catch (Exception ex) {
            Log.w("Error", ex.getMessage());
        }



    }

    private void attachClickListener() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                try {

                    Intent myIntent = new Intent(getApplicationContext(), DisplayActivity.class);
                    myIntent.putExtra(IMAGE_ID, mListOfImages.get(position).getFile().getAbsolutePath());
                    startActivity(myIntent);
                } catch (Exception e) {
                    // TODO: handle exception
                    String data = e.getMessage();
                }


            }
        });


    }

    private void startAlarm() {

        alarmMgr.cancel(alarmIntent);

       // Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+AlarmManager.INTERVAL_DAY,
                AlarmManager.INTERVAL_DAY, alarmIntent);

    }

    //load images from file and display them
    private void displayImages() {

        File myPath = storageDir;

        mListOfImages = new ArrayList<PhotoData>();

        try {

            for (File f : myPath.listFiles()) {
                PhotoData newPhoto = new PhotoData(f);
                mListOfImages.add(newPhoto);
            }


        } catch (Exception ex) {
            Log.w("Error", ex.getMessage());
        }

        mListAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_daily, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            capturePhoto();


            return true;
        }

        if(id == R.id.deleteAll){
            deleteFiles();

            displayImages();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // open default camera and capture image
    private void capturePhoto() {

        // open camera application

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        // Create the File where the photo should go
        mphotoFile = null;
        try {
            mphotoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File

        }
        // Continue only if the File was successfully created
        if (mphotoFile != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(mphotoFile));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }


    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private File createImageFile() throws IOException {

        // Create an image file name
        String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+FILE_NAME_EXTENSION;

        // String imageFileName = DateFormat.getDateInstance().format(new Date()).toString()+FILE_NAME_EXTENSION;


        if (!isExternalStorageWritable()){

            storageDir = getApplicationContext().getFilesDir();

        }




     File image =  File.createTempFile(
                imageFileName,  /* prefix */
               ".jpg",         /* suffix */
              storageDir      /* directory */
        );




        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            mListOfImages.add(new PhotoData(mphotoFile));
            mListAdapter.notifyDataSetChanged();
            //displayImages();

        }

        if (resultCode == RESULT_CANCELED) {

            mphotoFile.delete();
        }
    }

   
    private void deleteFiles() {


        File myPath = storageDir;

        try {

            for (File f : myPath.listFiles()) {

                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

                //  if (bitmap == null)
                f.delete();


            }
        } catch (Exception ex) {
            Log.w("Error", ex.getMessage());
        }

    }




    @Override protected void onStop(){
        super.onStop();

        startAlarm();


    }

}





// testing purpose

/*  public class DownloaderTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            displayImages();

            return null;

        }
    }*/
  /*  private void deleteFiles() {


        File myPath = getExternalFilesDir(null);

        try {

            for (File f : myPath.listFiles()) {

                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

              //  if (bitmap == null)
                    f.delete();


            }
        } catch (Exception ex) {
            Log.w("Error", ex.getMessage());
        }

    }
  */


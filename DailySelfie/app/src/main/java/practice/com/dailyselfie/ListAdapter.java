package practice.com.dailyselfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 *List adapter - displays view for each row of the list
 */
public class ListAdapter extends ArrayAdapter<PhotoData> {


    private final Activity context;

    private final ArrayList<PhotoData> listOfCapturedImages;

    public ListAdapter(Activity context, ArrayList<PhotoData> listOfCapturedImages) {
        super(context,R.layout.list_item, listOfCapturedImages);

        this.context=context;
        this.listOfCapturedImages = listOfCapturedImages;

    }

    @Override
    public void notifyDataSetChanged(){

        Collections.sort(listOfCapturedImages, dateComparator);
        super.notifyDataSetChanged();

    }

    Comparator<PhotoData> dateComparator = new Comparator<PhotoData>(){

        public int compare(PhotoData obj1, PhotoData obj2){

            int result = obj1.getDateCreated().compareTo(obj2.getDateCreated());


            if (result > 0) {
                return -1;
            } else if (result == 0){
                return 0;
            }

            return 1;

        }

    };


    public View getView(int position,View view,ViewGroup parent) {

        View rowView = view;

        if(rowView == null){

            LayoutInflater inflater=context.getLayoutInflater();
            rowView =inflater.inflate(R.layout.list_item, null,true);

            ViewHolder holder = new ViewHolder();
            holder.mLinearLayout = (LinearLayout)rowView.findViewById(R.id.thumbnail);


            holder.mImageView = (ImageView)  holder.mLinearLayout.findViewById(R.id.img);
            holder.mTextView = (TextView) rowView.findViewById(R.id.txt);

            rowView.setTag(holder);



        }

        ViewHolder holder = (ViewHolder)rowView.getTag();


        if(listOfCapturedImages.size() != 0){

            // load view holder from async task

            AsyncTask<PhotoData, Integer, Bitmap> loadBitmap = new LoadBitMapImage(holder);
            loadBitmap.execute(listOfCapturedImages.get(position));
        }

        return rowView;

    };


    private String getFileName(File f){

        int index = f.getName().indexOf(DailyActivity.FILE_NAME_EXTENSION);

        return f.getName().substring(0,index).toUpperCase();
    }

    static class ViewHolder{
        LinearLayout mLinearLayout;
        ImageView mImageView;
        TextView mTextView;

    }

    public class LoadBitMapImage extends AsyncTask<PhotoData, Integer, Bitmap>{

        private ViewHolder mHolder;

        private PhotoData mPhotoDataToDisplay;

        public LoadBitMapImage(ViewHolder holder){
            mHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(PhotoData... params) {

             mPhotoDataToDisplay = params[0];

            Bitmap bitmap = BitmapFactory.decodeFile(mPhotoDataToDisplay.getFile().getAbsolutePath());


            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            mHolder.mImageView.setImageBitmap(result);
            mHolder.mTextView.setText(mPhotoDataToDisplay.getName());
        }

    }
}


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

/*
 *List adapter - displays view for each row of the list
 */
public class ListAdapter extends ArrayAdapter<String> {


    private final Activity context;

    private final ArrayList<String> listOfCapturedImages;

    public ListAdapter(Activity context, ArrayList<String> listOfCapturedImages) {
        super(context,R.layout.list_item, listOfCapturedImages);

        this.context=context;
        this.listOfCapturedImages = listOfCapturedImages;

    }

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

            int index = listOfCapturedImages.size() -1;
            File f = new File(listOfCapturedImages.get(position));

            holder.mTextView.setText(getFileName(f));

            // load image from async task

            AsyncTask<String, Integer, Bitmap> loadBitmap = new LoadBitMapImage(holder.mImageView);
            loadBitmap.execute(listOfCapturedImages.get(position));


        }




        return rowView;

    };


    private String getFileName(File f){

        int index = f.getName().indexOf("_JPEG");

        return f.getName().substring(0,index).toUpperCase();
    }

    static class ViewHolder{
        LinearLayout mLinearLayout;
        ImageView mImageView;
        TextView mTextView;

    }

    public class LoadBitMapImage extends AsyncTask<String, Integer, Bitmap>{

        private ImageView mImageView;

        public LoadBitMapImage(ImageView imageview){
            mImageView = imageview;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String path = params[0];

            Bitmap bitmap = BitmapFactory.decodeFile(path);

            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            mImageView.setImageBitmap(result);

        }

    }
}


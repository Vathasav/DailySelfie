package practice.com.dailyselfie;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by vathasav on 20/04/15.
 * Displays bitmap in a large view
 */
public class DisplayActivity extends Activity {

    ImageView imageView = null;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_layout);

            Bundle extras = getIntent().getExtras();
            String path = extras.getString(DailyActivity.IMAGE_ID);
            Bitmap bitmap = BitmapFactory.decodeFile(path);


            imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);



        }

}

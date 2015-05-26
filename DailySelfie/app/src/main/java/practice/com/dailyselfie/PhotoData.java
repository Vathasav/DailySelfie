package practice.com.dailyselfie;

import java.io.File;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Messi on 13/05/15.
 */
public class PhotoData  {

    private String name = null;

    private File mFile  = null;

    public Date getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(Date mDateCreated) {
        this.mDateCreated = mDateCreated;
    }

    private Date mDateCreated = null;

    private String map_location = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return mFile;
    }

    public  PhotoData(File file){
        this.mFile = file;
      //  this.name = getFileName(mFile);
        String fileName = getFileName(mFile);

        try {
            mDateCreated = new SimpleDateFormat("yyyyMMdd_HHmmss").parse(fileName);//format(new Date())SimpleDateFormat..getDateInstance().parse(fileName);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.name = DateFormat.getDateInstance().format(mDateCreated).toString();
    }
    public String getMap_location() {
        return map_location;
    }

    public void setMap_location(String map_location) {
        this.map_location = map_location;
    }

    private String getFileName(File f){

        int index = f.getName().indexOf(DailyActivity.FILE_NAME_EXTENSION);

        return f.getName().substring(0,index).toUpperCase();
    }

}

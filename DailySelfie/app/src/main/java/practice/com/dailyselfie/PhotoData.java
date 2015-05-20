package practice.com.dailyselfie;

import java.io.File;
import java.net.URI;

/**
 * Created by Messi on 13/05/15.
 */
public class PhotoData extends File {

    private String name = null;

    private String fileLocation = null;

    private String map_location = null;

    public PhotoData(URI uri) {
        super(uri);
    }

    public PhotoData(String dirPath, String name) {
        super(dirPath, name);
    }

    public PhotoData(String path) {
        super(path);
    }

    public PhotoData(File dir, String name) {
        super(dir, name);
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getMap_location() {
        return map_location;
    }

    public void setMap_location(String map_location) {
        this.map_location = map_location;
    }


}

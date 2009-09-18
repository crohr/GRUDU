package diet.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class XmlFilter extends FileFilter {
    public boolean accept(File f) {
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equalsIgnoreCase("xml"))
                return true;
            else
                return false;
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "XML files";
    }
    
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}

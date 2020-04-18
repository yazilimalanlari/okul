package app;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author birisi
 */
public class Config {
    public static String DB_USER = "root";
    public static String DB_PASS = "root";
    
    public Config(javax.swing.JFrame app){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int x = (screenSize.width - app.getWidth()) / 2;
        int y = (screenSize.height - app.getHeight()) / 2;
        app.setLocation(x, y);
    }
}

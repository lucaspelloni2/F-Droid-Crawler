import Config.ConfigurationManager;
import Crawler.*;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {

        String extension = "apk";
        //String extension = "_src.tar.gz";  // If you want to retrieve the source code of the apps
        SAXParserXML parser = new SAXParserXML();
        Filter filter = new Filter(parser.getAPKSLinksFromXMLFile());
        ArrayList<String> filteredList = filter.filterFDroidWithDataSet(DatasetParser.getPackagesFromCSV(), extension);
        Crawler.downloadAll(filteredList, ConfigurationManager.getInstance().getAPKDestinationDir());

    }
}

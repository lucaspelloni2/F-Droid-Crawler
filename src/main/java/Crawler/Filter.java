package Crawler;

import Config.ConfigurationManager;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by LuckyP on 03.08.17.
 */
public class Filter {

    private ArrayList<String> apkLinks;

    public Filter(ArrayList<String> apkLinks) {
        this.apkLinks = apkLinks;
    }

    /**
     * The stream filters the APK-Links from the XML File. After filtering, it contains the name of all packages and
     * their apk version, which are available on the FDroid XML file.
     * With this method the stream gets initialized
     *
     * @return (stream of String[] containing at the first position the name of the package, and the second the APK version)
     */
    private Stream<String[]> getStream() {
        return this.apkLinks.stream()
                .filter(link -> link.contains("_") && link.contains(".apk"))
                .map(link -> link.substring(ConfigurationManager.getInstance().getFDroidAPILink().length()).split("_"));
    }

    /**
     * It compares the data_set provided in the <config.properties> file with the FDroidRepo XML file and returns
     * an ArrayList of filtered data (based on the extension file)
     *
     * @param CSV_DATASET    (given dataset)
     * @param file_extension (given file extension)
     * @return (An ArrayList of filtered strings containing links for downloading data from the FDroid repo)
     */
    public ArrayList<String> filterFDroidWithDataSet(ArrayList<String> CSV_DATASET, String file_extension) {
        ArrayList<String> filteredList = new ArrayList<>();
        CSV_DATASET.forEach(csvApp -> {
            //final boolean[] isLastVersion = {true};
            getStream().forEach(x -> {
                String fDroidApp = x[0];
                if (fDroidApp.equals(csvApp)) { // &&isLastVersion[0]) {
                    //isLastVersion[0] = false;
                    int apkVersion = Integer.valueOf(x[1].split("\\.")[0]);
                    filteredList.add(ConfigurationManager.getInstance().getFDroidAPILink() + fDroidApp + "_" + apkVersion + "." + file_extension);
                }
            });
        });
        return filteredList;
    }

}

package Crawler;


import Config.ConfigurationManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * This Class DatasetParser is in charge of parsing the dataset located in the directory specified in the <config.properties> file
 *
 * @author Lucas Pelloni
 * @version 1.0
 * @since 18.12.2017
 */
public abstract class DatasetParser {

    private static final String DELIMITER = ",";
    private static final String DATASET_DIRECTORY_PATH = ConfigurationManager.getInstance().getCSVDirectory();
    private static ArrayList<String> dataFromCSV = new ArrayList<>();
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    ;

    /**
     * Stream gets initialized for this class
     *
     * @throws IOException
     */
    private static Stream<String[]> getStream() throws IOException {
        return Files.lines(Paths.get(DATASET_DIRECTORY_PATH))
                .filter(line -> !line.startsWith("Name APP"))
                .map(line -> line.split(DELIMITER));
    }

    /**
     * Parses the CSV file specified in the <config.properties> file and returns a list of packages
     *
     * @return a list of packages
     * @throws IOException
     */
    public static ArrayList<String> getPackagesFromCSV() throws IOException {
        dataFromCSV.clear();
        getStream().forEach(line_split -> {
            String csvPackageLink = line_split[2];
            String csvPackageName = csvPackageLink.split("packages")[1].replace("/", "");
            dataFromCSV.add(csvPackageName);
        });
        return dataFromCSV;
    }


    /**
     * Parses the CSV file and returns a HashMap which contains all apps (key)
     * associated with their initial/final review dates (values)
     *
     * @return a list of all packages with their initial-final review dates
     * @throws IOException
     */
    public static HashMap<String, ArrayList<Date>> getPackagesWithDatesFromCSV() throws IOException {
        HashMap<String, ArrayList<Date>> packagesWithDates = new HashMap<>();
        getStream().forEach(line_split -> {
            ArrayList<Date> dates = new ArrayList<>();
            String package_name = line_split[0];
            try {
                Date oldest_date = format.parse(line_split[2]);
                Date newest_date = format.parse(line_split[3]);
                dates.add(0, oldest_date);
                dates.add(1, newest_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            packagesWithDates.put(package_name, dates);
        });
        return packagesWithDates;
    }
}

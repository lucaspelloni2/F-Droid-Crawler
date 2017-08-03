package Crawler;


import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public abstract class Crawler {

    private static final int BUFFER_SIZE = 4096;

    /**
     * Downloads the file and returns a boolean flag
     *
     * @param fileURL the file URL
     * @param destDir the download directory
     * @return a boolean flag
     * @throws IOException
     */
    private static boolean downloadFile(String fileURL, String destDir)
            throws IOException {

        boolean hasBeenDownloaded = false;

        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = destDir + File.separator + fileName;

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println(fileURL + " downloaded");
            hasBeenDownloaded = true;
        } else {
            System.err.println(fileURL + " failed to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
        return hasBeenDownloaded;
    }

    public static void downloadAll(ArrayList<String> filteredFDroidRepo, String dir) {
        for (String apkLink : filteredFDroidRepo) {
            try {
                boolean flag = downloadFile(apkLink, dir);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // direct unzip files
                if (apkLink.endsWith(".tar.gz") && flag) {

                    String zipFileName = dir + "/" + apkLink.substring(25);

                    File sourceFile = new File(zipFileName);
                    File destDir = new File(zipFileName.replace("_src.tar.gz", ""));

                    // extract the archive
                    Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
                    archiver.extract(sourceFile, destDir);

                    // delete the extracted archive
                    sourceFile.delete();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
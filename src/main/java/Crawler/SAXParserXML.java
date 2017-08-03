package Crawler;

import Config.ConfigurationManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


// https://github.com/guardianproject/fdroid-repo/tree/master/repo
// https://f-droid.org/repo/{fileapk}
public class SAXParserXML {

    private ConfigurationManager config;
    private DateFormat format;


    public SAXParserXML() {
        this.config = ConfigurationManager.getInstance();
        this.format = new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * Returns an ArrayList<String> with ALL APKS Links (it needs to be filtered)
     *
     * @return
     */
    public ArrayList<String> getAPKSLinksFromXMLFile() {
        ArrayList<String> apks = new ArrayList<>();

        try {
            File XMLFileName = new File(this.config.getFDroidRepoDirectory());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(XMLFileName);

            doc.getDocumentElement().normalize();

            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("package");

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                //System.out.println(temp);
                Node nNode = nodeList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getTextContent());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String apk = eElement.getElementsByTagName("apkname").item(0).getTextContent();
                    apks.add(this.config.getFDroidAPILink() + apk);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        Collections.sort(apks);
        return apks;
    }

    /**
     * Returns an ArrayList<String> with ALL APKS Links (it need to be filtered)
     *
     * @return
     */
    public HashMap<String, Date> getAPKSWithDateFromXMLFile() {
        HashMap<String, Date> apksWithDate = new HashMap<>();

        try {
            File XMLFileName = new File(this.config.getFDroidRepoDirectory());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(XMLFileName);
            doc.getDocumentElement().normalize();

            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList_package = doc.getElementsByTagName("package");
            for (int temp = 0; temp < nodeList_package.getLength(); temp++) {
                //System.out.println(temp);
                Node nNode = nodeList_package.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getTextContent());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String apk = eElement.getElementsByTagName("apkname").item(0).getTextContent();
                    String toParse = eElement.getElementsByTagName("added").item(0).getTextContent();
//                    System.out.println("APK: "+apk + " ADDED: " + added);
                    Date addedDate = format.parse(toParse);
                    apksWithDate.put(this.config.getFDroidAPILink() + apk, addedDate);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return apksWithDate;
    }



}
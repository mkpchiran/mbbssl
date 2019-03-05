package process;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by chiranz on 5/15/17.
 */

public class Process {

    boolean isalive = false;
    String baseUrl = "http://www.srilankamedicalcouncil.org";

    String registry = "";
    String initials = "";
    String last_name = "";
    String other_name = "";
    String reg_no = "";
    String nic = "";
    String part_of_address = "";

    public ArrayList<Model> getDataset() {
        return dataset;
    }

    public void setDataset(ArrayList<Model> dataset) {
        this.dataset = dataset;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    private ArrayList<Model> dataset = new ArrayList<>();

    private String requestUrl = "http://www.srilankamedicalcouncil.org/registry.php?" +
            "registry=%s&" +
            "initials=%s&" +
            "last_name=%s&" +
            "other_name=%s&" +
            "reg_no=%s&" +
            "nic=%s&" +
            "part_of_address=%s&" +
            "search=Search";

    public String createUrl(String registry,
                            String initials,
                            String last_name,
                            String other_name,
                            String reg_no,
                            String nic,
                            String part_of_address) {
        if (registry.contains("SEC29")) {
            registry = "5";                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
        } else if (registry.equals("ACT15")) {
            registry = "3";
        } else if (reg_no.equals("Dental")) {
            registry = "4";
        } else if (registry.equals("SEC41")) {
            registry = "6";
        }
        return String.format(requestUrl, registry, initials, last_name, other_name, reg_no, nic, part_of_address);
    }

    public Document getDocument(String registry,
                                String initials,
                                String last_name,
                                String other_name,
                                String reg_no,
                                String nic,
                                String part_of_address) throws IOException {


        if (registry.equals("SEC29")) {
            registry = "5";
        } else if (registry.equals("ACT15")) {
            registry = "3";
        } else if (reg_no.equals("Dental")) {
            registry = "4";
        } else if (registry.equals("SEC41")) {
            registry = "6";
        }

        String url = this.createUrl(
                registry,
                initials,
                last_name,
                other_name,
                reg_no,
                nic,
                part_of_address);
        return getDocument(url);
    }

    public Document getDocument(String documentUrl) throws IOException {
        Document doc = Jsoup.parse(new URL(documentUrl).openStream(), null, documentUrl);
        return doc;
    }

    public ArrayList<Model> getPageData(Document doc) throws IOException {

        String baseUrl = "http://www.srilankamedicalcouncil.org";

        Elements data = new Elements();
        //doc.getElementById("r_table").select("tr");
        Element r_table = doc.getElementById("r_table");

        if (r_table != null) {
            data = r_table.select("tr");
        }
        for (Element element : data) {

            Model model = new Model();
            if (element.select("td").size() > 0) {
                model.setReg_No(element.select("td").get(1).text());//reg id
                model.setReg_Date(element.select("td").get(2).text());//reg date
                model.setFull_Name(element.select("td").get(3).text());//name
                model.setAddress(element.select("td").get(4).text());//address
                model.setQualifications(element.select("td").get(5).text());//qualifications

                dataset.add(model);
            }
        }

        return dataset;

    }

    public String getNextPageUrl(Document doc) {

        Element p_n_links = doc.getElementById("p_n_links");//.select("a");
        Element next = p_n_links.getElementById("next");
        return baseUrl + next.attr("href");
    }

    public String getPreviousPageUrl(Document doc) {

        Element p_n_links = doc.getElementById("p_n_links");//.select("a");
        Element previous = p_n_links.getElementById("previous");
        return baseUrl + previous.attr("href");
    }

    public String getNumberedPageUrl(Document doc, int pageNumber) {

        Element p_n_links = doc.getElementById("p_n_links");//.select("a");
        Element numberedPage = p_n_links.getElementsContainingOwnText(String.valueOf(pageNumber)).first();
        return baseUrl + numberedPage.attr("href");
    }

    public Document getNextDoc(Document homeDocument) throws IOException {

        return getDocument(getNextPageUrl(homeDocument));

    }

    public Document getPreviousDoc(Document homeDocument) throws IOException {

        return getDocument(getPreviousPageUrl(homeDocument));

    }

    public Document getNumberedDoc(Document homeDocument, int pageNumber) throws IOException {

        return getDocument(getNumberedPageUrl(homeDocument, pageNumber));

    }

    public boolean getHasPrev(Document doc) {
        Element p_n_links = doc.getElementById("p_n_links");
        if (p_n_links == null)
            return false;
        Element previous = p_n_links.getElementById("previous");

        if (previous != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getHasNext(Document doc) {
        Element p_n_links = doc.getElementById("p_n_links");
        if (p_n_links == null)
            return false;
        Element next = p_n_links.getElementById("next");

        if (next != null) {
            return true;
        } else {
            return false;
        }
    }

    public int getNumberOfDoctors(Document doc) {
        String val = "0";
        Elements h2 = doc.getElementsByTag("h2");
        if (h2 != null)
            if (h2.get(0).text().split(" ").length == 3)
                val = h2.get(0).text().split(" ")[1];

        return Integer.parseInt(val);
    }

    public static void main(String[] args) {

    }
}


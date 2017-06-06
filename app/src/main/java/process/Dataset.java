package process;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Created by chiranz on 5/16/17.
 */

public class Dataset {
public static Model model;

    public static Model getModel() {
        return model;
    }

    public static void setModel(Model model) {
        Dataset.model = model;
    }

    static int numOfDoc=0;

    public static int getNumOfDoc() {
        return numOfDoc;
    }

    public static void setNumOfDoc(int numOfDoc) {
        Dataset.numOfDoc = numOfDoc;
    }

    static ArrayList<Model> models;

    static Document doc;

    public static boolean isNextOk() {
        return isNextOk;
    }

    public static void setIsNextOk(boolean isNextOk) {
        Dataset.isNextOk = isNextOk;
    }

    public static String getUrl() {
        return url;
    }

    public static boolean isPrevOk=false;

    public static boolean isNextOk=true;

    public static boolean isPrevOk() {
        return isPrevOk;
    }

    public static void setIsPrevOk(boolean isPrevOk) {
        Dataset.isPrevOk = isPrevOk;
    }

    public static void setUrl(String url) {
        Dataset.url = url;
    }

    static String url;

    private Dataset() {

    }

    public static ArrayList<Model> getModels(){

        if(null==Dataset.models){

            return new ArrayList<>();
        }else{
            return Dataset.models;
        }

    }
    public static  void setModels(ArrayList<Model> models){

        Dataset.models=models;
    }

    public static Document getDoc() {
        return doc;
    }

    public static void setDoc(Document doc) {
        Dataset.doc = doc;
    }
}

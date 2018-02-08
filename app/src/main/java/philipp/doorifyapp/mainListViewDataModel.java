package philipp.doorifyapp;

/**
 * Created by Philipp on 13.12.2017.
 */

public class mainListViewDataModel {

    private String name, zugriff;

    public mainListViewDataModel(String name, String zugriff){
        this.name = name;
        this.zugriff = zugriff;
    }

    public String getName(){
        return this.name;
    }

    public String getZugriff(){
        return this.zugriff;
    }



}

package com.stratagile.qlink.entity;

import com.stratagile.qlink.R;

/**
 * Created by huzhipeng on 2018/2/8.
 */

public class CountryEntity {
    private String name;
    private int type;
    private String countryImage;

    public String getCountryImage() {
        return countryImage;
    }  //north_korea

    public void setCountryImage(String countryImage) {
        this.countryImage = countryImage;
    }

    public static final String DATA = "[{\"name\":\"A\",\"type\":1},{\"name\":\"B\",\"type\":1},{\"name\":\"C\",\"type\":1},{\"name\":\"D\",\"type\":1}," +
            "{\"name\":\"E\",\"type\":1},{\"name\":\"F\",\"type\":1},{\"name\":\"G\",\"type\":1},{\"name\":\"H\",\"type\":1},{\"name\":\"J\",\"type\":1}," +
            "{\"name\":\"K\",\"type\":1},{\"name\":\"L\",\"type\":1},{\"name\":\"M\",\"type\":1},{\"name\":\"N\",\"type\":1},{\"name\":\"P\",\"type\":1}," +
            "{\"name\":\"Q\",\"type\":1},{\"name\":\"R\",\"type\":1},{\"name\":\"S\",\"type\":1},{\"name\":\"T\",\"type\":1},{\"name\":\"U\",\"type\":1},{\"name\":\"V\",\"type\":1},{\"pys\":\"China\",\"name\":\"China\",\"countryImage\":\"china\"}, " +
            "{\"pys\":\"japan\",\"name\":\"Japan\",\"countryImage\":\"japan\"},{\"pys\":\"Singapore\",\"name\":\"singapore\",\"countryImage\":\"singapore\"}" +
            ",{\"pys\":\"United_States\",\"name\":\"United_States\",\"countryImage\":\"united_states\"},{\"name\":\"korea\",\"countryImage\":\"korea\"}" +
            ",{\"name\":\"India\",\"countryImage\":\"india\"},{\"name\":\"Afghanistan\",\"countryImage\":\"afghanistan\"},{\"name\":\"Canada\",\"countryImage\":\"canada\"}" +
            ",{\"name\":\"Germany\",\"countryImage\":\"germany\"},{\"name\":\"Hong_Kong\",\"countryImage\":\"hong_kong\"},{\"name\":\"North_Korea\",\"countryImage\":\"north_korea\"}" +
            ",{\"name\":\"Philippines\",\"countryImage\":\"philippines\"},{\"name\":\"Vietnam\",\"countryImage\":\"vietnam\"},{\"name\":\"Thailand\",\"countryImage\":\"thailand\"}" +
            ",{\"name\":\"malaysia\",\"countryImage\":\"malaysia\"},{\"name\":\"Indonesia\",\"countryImage\":\"indonesia\"},{\"name\":\"Iran\",\"countryImage\":\"iran\"}" +
            ",{\"name\":\"Israel\",\"countryImage\":\"israel\"},{\"name\":\"Saudi_Arabia\",\"countryImage\":\"saudi_arabia\"},{\"name\":\"Qatar\",\"countryImage\":\"qatar\"}" +
            ",{\"name\":\"Kuwait\",\"countryImage\":\"kuwait\"},{\"name\":\"The United Arab Emirates\",\"countryImage\":\"united_arab_emirates\"},{\"name\":\"Turkey\",\"countryImage\":\"turkey\"}" +
            ",{\"name\":\"Finland\",\"countryImage\":\"finland\"},{\"name\":\"Sweden\",\"countryImage\":\"sweden\"},{\"name\":\"Norway\",\"countryImage\":\"norway\"}" +
            ",{\"name\":\"Denmark\",\"countryImage\":\"denmark\"},{\"name\":\"Russia\",\"countryImage\":\"russia\"},{\"name\":\"Ukraine\",\"countryImage\":\"ukraine\"}" +
            ",{\"name\":\"Hungary\",\"countryImage\":\"hungary\"}" +
            ",{\"name\":\"United Kingdom\",\"countryImage\":\"united_kingdom\"}" +
            ",{\"name\":\"Ireland\",\"countryImage\":\"ireland\"},{\"name\":\"Netherlands\",\"countryImage\":\"netherlands\"}" +
            ",{\"name\":\"Luxembourg\",\"countryImage\":\"luxembourg\"},{\"name\":\"France\",\"countryImage\":\"france\"},{\"name\":\"Monaco\",\"countryImage\":\"monaco\"}" +
            ",{\"name\":\"Romanian\",\"countryImage\":\"romania\"},{\"name\":\"Greece\",\"countryImage\":\"greece\"},{\"name\":\"Slovenia\",\"countryImage\":\"slovenia\"}" +
            ",{\"name\":\"Croatia\",\"countryImage\":\"croatia\"},{\"name\":\"Italy\",\"countryImage\":\"italy\"},{\"name\":\"Spain\",\"countryImage\":\"spain\"}" +
            ",{\"name\":\"Portugal\",\"countryImage\":\"portugal\"},{\"name\":\"Australia\",\"countryImage\":\"australian\"},{\"name\":\"New Zealand\",\"countryImage\":\"new_zealand\"}" +
            ",{\"name\":\"Cuba\",\"countryImage\":\"cuba\"},{\"name\":\"Mexico\",\"countryImage\":\"mexico\"},{\"name\":\"Brazil\",\"countryImage\":\"brazil\"}" +
            ",{\"name\":\"Chile\",\"countryImage\":\"chile\"},{\"name\":\"Uruguay\",\"countryImage\":\"uruguay\"}" +
            ",{\"name\":\"Paraguay\",\"countryImage\":\"paraguay\"},{\"name\":\"Korea\",\"countryImage\":\"korea\"},{\"name\":\"poland\",\"countryImage\":\"poland\"}" +
            ",{\"name\":\"Czech\",\"countryImage\":\"czech_republic\"}]";

    //,{"name":"W","type":1}," +
     //       "{"name":"X","type":1},{"name":"Y","type":1},{"name":"Z","type":1}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

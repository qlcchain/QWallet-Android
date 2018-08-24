package com.stratagile.qlink.entity;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by huzhipeng on 2018/3/13.
 */

public class ContinentAndCountry {

    private List<ContinentBean> continent;

    public List<ContinentBean> getContinent() {
        return continent;
    }

    public void setContinent(List<ContinentBean> continent) {
        this.continent = continent;
    }

    public static class ContinentBean {
        /**
         * continent : asia
         * country : [{"name":"China","countryImage":"china"},{"name":"Japan","countryImage":"japan"},{"name":"singapore","countryImage":"singapore"},{"pys":"United_States","name":"United_States","countryImage":"united_states"},{"name":"korea","countryImage":"korea"},{"name":"India","countryImage":"india"},{"name":"Afghanistan","countryImage":"afghanistan"},{"name":"Canada","countryImage":"canada"},{"name":"Germany","countryImage":"germany"},{"name":"Hong_Kong","countryImage":"hong_kong"},{"name":"North_Korea","countryImage":"north_korea"},{"name":"Philippines","countryImage":"philippines"},{"name":"Vietnam","countryImage":"vietnam"},{"name":"Thailand","countryImage":"thailand"},{"name":"malaysia","countryImage":"malaysia"},{"name":"Indonesia","countryImage":"indonesia"},{"name":"Iran","countryImage":"iran"},{"name":"Israel","countryImage":"israel"},{"name":"Saudi_Arabia","countryImage":"saudi_arabia"},{"name":"Qatar","countryImage":"qatar"},{"name":"Kuwait","countryImage":"kuwait"},{"name":"The United Arab Emirates","countryImage":"united_arab_emirates"},{"name":"Turkey","countryImage":"turkey"},{"name":"Finland","countryImage":"finland"},{"name":"Sweden","countryImage":"sweden"},{"name":"Norway","countryImage":"norway"},{"name":"Denmark","countryImage":"denmark"},{"name":"Russia","countryImage":"russia"},{"name":"Ukraine","countryImage":"ukraine"},{"name":"Hungary","countryImage":"hungary"},{"name":"United Kingdom","countryImage":"united_kingdom"},{"name":"Ireland","countryImage":"ireland"},{"name":"Netherlands","countryImage":"netherlands"},{"name":"Luxembourg","countryImage":"luxembourg"},{"name":"France","countryImage":"france"},{"name":"Monaco","countryImage":"monaco"},{"name":"Romanian","countryImage":"romania"},{"name":"Greece","countryImage":"greece"},{"name":"Slovenia","countryImage":"slovenia"},{"name":"Croatia","countryImage":"croatia"},{"name":"Italy","countryImage":"italy"},{"name":"Spain","countryImage":"spain"},{"name":"Portugal","countryImage":"portugal"},{"name":"Australia","countryImage":"australian"},{"name":"New Zealand","countryImage":"new_zealand"},{"name":"Cuba","countryImage":"cuba"},{"name":"Mexico","countryImage":"mexico"},{"name":"Brazil","countryImage":"brazil"},{"name":"Chile","countryImage":"chile"},{"name":"Uruguay","countryImage":"uruguay"},{"name":"Paraguay","countryImage":"paraguay"},{"name":"Korea","countryImage":"korea"},{"name":"poland","countryImage":"poland"},{"name":"Czech","countryImage":"czech_republic"}]
         */

        private String continent;
        private List<CountryBean> country;

        public String getContinent() {
            return continent;
        }

        public void setContinent(String continent) {
            this.continent = continent;
        }

        public List<CountryBean> getCountry() {
            return country;
        }

        public void setCountry(List<CountryBean> country) {
            this.country = country;
        }

        public static class CountryBean implements  Comparable<CountryBean>{
            public CountryBean(String name, String countryImage) {
                this.name = name;
                this.countryImage = countryImage;
                this.isCheck = false;
            }

            public CountryBean(String name, String countryImage, boolean isCheck) {
                this.name = name;
                this.countryImage = countryImage;
                this.isCheck = isCheck;
            }

            public CountryBean() {
            }

            /**

             * name : China

             * countryImage : china
             * pys : United_States
             */

            private String name;
            private String countryImage;
            private boolean isCheck;

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCountryImage() {
                return countryImage;
            }

            public void setCountryImage(String countryImage) {
                this.countryImage = countryImage;
            }

            @Override
            public int compareTo(@NonNull CountryBean o) {
                    return getName().compareTo(o.getName());
            }

        }
    }
}

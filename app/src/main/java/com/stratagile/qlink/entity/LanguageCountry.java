package com.stratagile.qlink.entity;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by zl on 2018/07/02.
 */

public class LanguageCountry {

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
         * country : [{"name":"China","localLanguage":"china"},{"name":"Japan","localLanguage":"japan"},{"name":"singapore","localLanguage":"singapore"},{"pys":"United_States","name":"United_States","localLanguage":"united_states"},{"name":"korea","localLanguage":"korea"},{"name":"India","localLanguage":"india"},{"name":"Afghanistan","localLanguage":"afghanistan"},{"name":"Canada","localLanguage":"canada"},{"name":"Germany","localLanguage":"germany"},{"name":"Hong_Kong","localLanguage":"hong_kong"},{"name":"North_Korea","localLanguage":"north_korea"},{"name":"Philippines","localLanguage":"philippines"},{"name":"Vietnam","localLanguage":"vietnam"},{"name":"Thailand","localLanguage":"thailand"},{"name":"malaysia","localLanguage":"malaysia"},{"name":"Indonesia","localLanguage":"indonesia"},{"name":"Iran","localLanguage":"iran"},{"name":"Israel","localLanguage":"israel"},{"name":"Saudi_Arabia","localLanguage":"saudi_arabia"},{"name":"Qatar","localLanguage":"qatar"},{"name":"Kuwait","localLanguage":"kuwait"},{"name":"The United Arab Emirates","localLanguage":"united_arab_emirates"},{"name":"Turkey","localLanguage":"turkey"},{"name":"Finland","localLanguage":"finland"},{"name":"Sweden","localLanguage":"sweden"},{"name":"Norway","localLanguage":"norway"},{"name":"Denmark","localLanguage":"denmark"},{"name":"Russia","localLanguage":"russia"},{"name":"Ukraine","localLanguage":"ukraine"},{"name":"Hungary","localLanguage":"hungary"},{"name":"United Kingdom","localLanguage":"united_kingdom"},{"name":"Ireland","localLanguage":"ireland"},{"name":"Netherlands","localLanguage":"netherlands"},{"name":"Luxembourg","localLanguage":"luxembourg"},{"name":"France","localLanguage":"france"},{"name":"Monaco","localLanguage":"monaco"},{"name":"Romanian","localLanguage":"romania"},{"name":"Greece","localLanguage":"greece"},{"name":"Slovenia","localLanguage":"slovenia"},{"name":"Croatia","localLanguage":"croatia"},{"name":"Italy","localLanguage":"italy"},{"name":"Spain","localLanguage":"spain"},{"name":"Portugal","localLanguage":"portugal"},{"name":"Australia","localLanguage":"australian"},{"name":"New Zealand","localLanguage":"new_zealand"},{"name":"Cuba","localLanguage":"cuba"},{"name":"Mexico","localLanguage":"mexico"},{"name":"Brazil","localLanguage":"brazil"},{"name":"Chile","localLanguage":"chile"},{"name":"Uruguay","localLanguage":"uruguay"},{"name":"Paraguay","localLanguage":"paraguay"},{"name":"Korea","localLanguage":"korea"},{"name":"poland","localLanguage":"poland"},{"name":"Czech","localLanguage":"czech_republic"}]
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
            /**
             * name : China
             * localLanguage : china
             * pys : United_States
             */

            private String name;
            private String localLanguage;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLocalLanguage() {
                return localLanguage;
            }

            public void setLocalLanguage(String localLanguage) {
                this.localLanguage = localLanguage;
            }

            @Override
            public int compareTo(@NonNull CountryBean o) {
                    return getName().compareTo(o.getName());
            }

        }
    }
}

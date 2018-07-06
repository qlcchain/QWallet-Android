package com.stratagile.qlink.entity;

import java.util.List;

/**
 * Created by huzhipeng on 2018/2/7.
 */

public class GoogleResult {

    /**
     * results : [{"address_components":[{"long_name":"Jing Lian Lu","short_name":"Jing Lian Lu","types":["route"]},{"long_name":"Yuhua Qu","short_name":"Yuhua Qu","types":["political","sublocality","sublocality_level_1"]},{"long_name":"Changsha Shi","short_name":"Changsha Shi","types":["locality","political"]},{"long_name":"Hunan Sheng","short_name":"Hunan Sheng","types":["administrative_area_level_1","political"]},{"long_name":"China","short_name":"CN","types":["country","political"]},{"long_name":"410004","short_name":"410004","types":["postal_code"]}],"formatted_address":"Jing Lian Lu, Yuhua Qu, Changsha Shi, Hunan Sheng, China, 410004","geometry":{"bounds":{"northeast":{"lat":28.1147553,"lng":113.0199563},"southwest":{"lat":28.1143368,"lng":113.0184188}},"location":{"lat":28.1145413,"lng":113.0191859},"location_type":"GEOMETRIC_CENTER","viewport":{"northeast":{"lat":28.1158950302915,"lng":113.0205365302915},"southwest":{"lat":28.11319706970849,"lng":113.0178385697085}}},"place_id":"ChIJkTYx_ZXKIDQROU7mzhXAHxY","types":["route"]},{"address_components":[{"long_name":"Yuhua","short_name":"Yuhua","types":["political","sublocality","sublocality_level_1"]},{"long_name":"Changsha","short_name":"Changsha","types":["locality","political"]},{"long_name":"Hunan","short_name":"Hunan","types":["administrative_area_level_1","political"]},{"long_name":"China","short_name":"CN","types":["country","political"]}],"formatted_address":"Yuhua, Changsha, Hunan, China","geometry":{"bounds":{"northeast":{"lat":28.1896525,"lng":113.1867471},"southwest":{"lat":27.9127051,"lng":112.9839559}},"location":{"lat":28.13771,"lng":113.038017},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":28.1896525,"lng":113.1867471},"southwest":{"lat":27.9127051,"lng":112.9839559}}},"place_id":"ChIJc_eWZifLIDQRorbexLGCzCw","types":["political","sublocality","sublocality_level_1"]},{"address_components":[{"long_name":"Changsha","short_name":"Changsha","types":["locality","political"]},{"long_name":"Hunan","short_name":"Hunan","types":["administrative_area_level_1","political"]},{"long_name":"China","short_name":"CN","types":["country","political"]}],"formatted_address":"Changsha, Hunan, China","geometry":{"bounds":{"northeast":{"lat":28.6616124,"lng":114.2612646},"southwest":{"lat":27.8480724,"lng":111.8962565}},"location":{"lat":28.228209,"lng":112.938814},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":28.4369499,"lng":113.3432007},"southwest":{"lat":27.9153089,"lng":112.7059937}}},"place_id":"ChIJxWQcnvM1JzQRgKbxoZy75bE","types":["locality","political"]},{"address_components":[{"long_name":"410004","short_name":"410004","types":["postal_code"]},{"long_name":"Yuhua","short_name":"Yuhua","types":["political","sublocality","sublocality_level_1"]},{"long_name":"Changsha","short_name":"Changsha","types":["locality","political"]},{"long_name":"Hunan","short_name":"Hunan","types":["administrative_area_level_1","political"]},{"long_name":"China","short_name":"CN","types":["country","political"]}],"formatted_address":"Yuhua, Changsha, Hunan, China, 410004","geometry":{"bounds":{"northeast":{"lat":28.1536538,"lng":113.025027},"southwest":{"lat":28.0906031,"lng":112.9773704}},"location":{"lat":28.1436635,"lng":112.9986675},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":28.1536538,"lng":113.025027},"southwest":{"lat":28.1031228,"lng":112.9773704}}},"place_id":"ChIJ9Uqj-KpKJzQRb9pn9p4ROzs","types":["postal_code"]},{"address_components":[{"long_name":"Hunan","short_name":"Hunan","types":["administrative_area_level_1","political"]},{"long_name":"China","short_name":"CN","types":["country","political"]}],"formatted_address":"Hunan, China","geometry":{"bounds":{"northeast":{"lat":30.1263628,"lng":114.2612646},"southwest":{"lat":24.6363232,"lng":108.7908408}},"location":{"lat":27.6252995,"lng":111.8568586},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":30.1263628,"lng":114.2612646},"southwest":{"lat":24.6363232,"lng":108.7908408}}},"place_id":"ChIJTTiHaRi6IDQR5Syu5AuRIcw","types":["administrative_area_level_1","political"]},{"address_components":[{"long_name":"China","short_name":"CN","types":["country","political"]}],"formatted_address":"China","geometry":{"bounds":{"northeast":{"lat":53.56097399999999,"lng":134.7728099},"southwest":{"lat":17.9996,"lng":73.4994136}},"location":{"lat":35.86166,"lng":104.195397},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":53.56097399999999,"lng":134.7728099},"southwest":{"lat":17.9996,"lng":73.4994136}}},"place_id":"ChIJwULG5WSOUDERbzafNHyqHZU","types":["country","political"]}]
     * status : OK
     */

    private String status;
    private List<ResultsBean> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * address_components : [{"long_name":"Jing Lian Lu","short_name":"Jing Lian Lu","types":["route"]},{"long_name":"Yuhua Qu","short_name":"Yuhua Qu","types":["political","sublocality","sublocality_level_1"]},{"long_name":"Changsha Shi","short_name":"Changsha Shi","types":["locality","political"]},{"long_name":"Hunan Sheng","short_name":"Hunan Sheng","types":["administrative_area_level_1","political"]},{"long_name":"China","short_name":"CN","types":["country","political"]},{"long_name":"410004","short_name":"410004","types":["postal_code"]}]
         * formatted_address : Jing Lian Lu, Yuhua Qu, Changsha Shi, Hunan Sheng, China, 410004
         * geometry : {"bounds":{"northeast":{"lat":28.1147553,"lng":113.0199563},"southwest":{"lat":28.1143368,"lng":113.0184188}},"location":{"lat":28.1145413,"lng":113.0191859},"location_type":"GEOMETRIC_CENTER","viewport":{"northeast":{"lat":28.1158950302915,"lng":113.0205365302915},"southwest":{"lat":28.11319706970849,"lng":113.0178385697085}}}
         * place_id : ChIJkTYx_ZXKIDQROU7mzhXAHxY
         * types : ["route"]
         */

        private String formatted_address;
        private GeometryBean geometry;
        private String place_id;
        private List<AddressComponentsBean> address_components;
        private List<String> types;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public List<AddressComponentsBean> getAddress_components() {
            return address_components;
        }

        public void setAddress_components(List<AddressComponentsBean> address_components) {
            this.address_components = address_components;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public static class GeometryBean {
            /**
             * bounds : {"northeast":{"lat":28.1147553,"lng":113.0199563},"southwest":{"lat":28.1143368,"lng":113.0184188}}
             * location : {"lat":28.1145413,"lng":113.0191859}
             * location_type : GEOMETRIC_CENTER
             * viewport : {"northeast":{"lat":28.1158950302915,"lng":113.0205365302915},"southwest":{"lat":28.11319706970849,"lng":113.0178385697085}}
             */

            private BoundsBean bounds;
            private LocationBean location;
            private String location_type;
            private ViewportBean viewport;

            public BoundsBean getBounds() {
                return bounds;
            }

            public void setBounds(BoundsBean bounds) {
                this.bounds = bounds;
            }

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public String getLocation_type() {
                return location_type;
            }

            public void setLocation_type(String location_type) {
                this.location_type = location_type;
            }

            public ViewportBean getViewport() {
                return viewport;
            }

            public void setViewport(ViewportBean viewport) {
                this.viewport = viewport;
            }

            public static class BoundsBean {
                /**
                 * northeast : {"lat":28.1147553,"lng":113.0199563}
                 * southwest : {"lat":28.1143368,"lng":113.0184188}
                 */

                private NortheastBean northeast;
                private SouthwestBean southwest;

                public NortheastBean getNortheast() {
                    return northeast;
                }

                public void setNortheast(NortheastBean northeast) {
                    this.northeast = northeast;
                }

                public SouthwestBean getSouthwest() {
                    return southwest;
                }

                public void setSouthwest(SouthwestBean southwest) {
                    this.southwest = southwest;
                }

                public static class NortheastBean {
                    /**
                     * lat : 28.1147553
                     * lng : 113.0199563
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public static class SouthwestBean {
                    /**
                     * lat : 28.1143368
                     * lng : 113.0184188
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }

            public static class LocationBean {
                /**
                 * lat : 28.1145413
                 * lng : 113.0191859
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class ViewportBean {
                /**
                 * northeast : {"lat":28.1158950302915,"lng":113.0205365302915}
                 * southwest : {"lat":28.11319706970849,"lng":113.0178385697085}
                 */

                private NortheastBeanX northeast;
                private SouthwestBeanX southwest;

                public NortheastBeanX getNortheast() {
                    return northeast;
                }

                public void setNortheast(NortheastBeanX northeast) {
                    this.northeast = northeast;
                }

                public SouthwestBeanX getSouthwest() {
                    return southwest;
                }

                public void setSouthwest(SouthwestBeanX southwest) {
                    this.southwest = southwest;
                }

                public static class NortheastBeanX {
                    /**
                     * lat : 28.1158950302915
                     * lng : 113.0205365302915
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public static class SouthwestBeanX {
                    /**
                     * lat : 28.11319706970849
                     * lng : 113.0178385697085
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }
        }

        public static class AddressComponentsBean {
            /**
             * long_name : Jing Lian Lu
             * short_name : Jing Lian Lu
             * types : ["route"]
             */

            private String long_name;
            private String short_name;
            private List<String> types;

            public String getLong_name() {
                return long_name;
            }

            public void setLong_name(String long_name) {
                this.long_name = long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public List<String> getTypes() {
                return types;
            }

            public void setTypes(List<String> types) {
                this.types = types;
            }
        }
    }
}

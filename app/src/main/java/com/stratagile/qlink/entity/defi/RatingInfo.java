package com.stratagile.qlink.entity.defi;

import com.stratagile.qlink.entity.BaseBack;

public class RatingInfo extends BaseBack {

    /**
     * score : 10
     * rating : 7
     * weight : 0.04
     * projectName : Maker
     * qlcAmount : 2334.45966356
     */

    private int score;
    private String rating;
    private String weight;
    private String projectName;
    private String qlcAmount;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getQlcAmount() {
        return qlcAmount;
    }

    public void setQlcAmount(String qlcAmount) {
        this.qlcAmount = qlcAmount;
    }
}

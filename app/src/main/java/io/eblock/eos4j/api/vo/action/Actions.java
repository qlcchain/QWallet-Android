package io.eblock.eos4j.api.vo.action;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Actions {

    private List<Action> actions;

    @JsonProperty("last_irreversible_block")
    private Integer lastIrreversibleBlock;

    @JsonProperty("actions")
    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public Integer getLastIrreversibleBlock() {
        return lastIrreversibleBlock;
    }

    public void setLastIrreversibleBlock(Integer lastIrreversibleBlock) {
        this.lastIrreversibleBlock = lastIrreversibleBlock;
    }
}

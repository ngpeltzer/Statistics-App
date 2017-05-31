package com.statiticsapp.Model;

import java.util.List;

/**
 * Created by Augusto Herbel on 26/5/2017.
 */

public class Stem {

    private int stem;
    private List<Integer> leafs;

    public Stem(int stem) {
        this.stem = stem;
    }

    public int getStem() { return stem; }

    public List<Integer> getLeafs() { return leafs; }

    public void setLeafs(List<Integer> leafs) { this.leafs = leafs; }
}

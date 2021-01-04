package com.example.beermanager.beans;

import java.util.ArrayList;

public class Recipe {

    public String name, type, brewer, style ;
    public Integer batchSize;
    public ArrayList<Yeast> yeasts ;
    public ArrayList<Hop> hops;
    public ArrayList<Fermentable> fermentables ;

}

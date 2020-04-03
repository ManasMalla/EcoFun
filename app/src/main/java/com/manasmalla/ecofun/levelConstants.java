package com.manasmalla.ecofun;

import android.util.Log;

import java.util.Arrays;

public class levelConstants {
    public final static int level1 = 500;
    public final static int level2 = 1000;
    public final static int level3 = 1500;
    public final static int level4 = 2000;
    public final static int level5 = 5000;
    public final static int level6 = 8000;
    public final static int level7 = 10000;
    public final static int level8 = 16000;
    public final static int level9 = 32000;
    public final static int level10 = 50000;
    public final static int level11 = 100000;
    public final static int level12 = 120000;
    public final static int level13 = 150000;
    public final static int level14 = 200000;
    public final static int level15 = 400000;
    public final static int level16 = 500000;
    public final static int level17 = 750000;

    public static final String levelIndia = "Lyon";
    public static final String levelChina = "Po";
    public static final String levelIndonesia = "Blu";
    public static final String levelDesert = "Kangaroo";
    public static final String levelOutback = "Platypus";
    public static final String levelAntarctica = "Snowy";
    public static final String levelEgypt = "Camello";
    public static final String levelMadagascar = "Chamelyon";
    public static final String levelFrance = "Rhino";
    public static final String levelUK = "Squirry";
    public static final String levelCalifornia = "Byson";
    public static final String levelCanada = "Beavy";
    public static final String levelArgentina = "Jaguar";
    public static final String levelAmazon = "Macaw";
    public static final String levelAtlanticOcean= "Wall-E";
    public static final String levelPacificOcean = "BWale";
    public static final String levelArctic = "Penny";

    public static final String[] levelIndiaArray = {"India", levelIndia, String.valueOf(R.drawable.lion)};
    public static final String[] levelChinaArray = {"China", levelChina, String.valueOf(R.drawable.panda)};
    public static final String[] levelIndonesiaArray = {"Indonesia", levelIndonesia, String.valueOf(R.drawable.elephant)};
    public static final String[] levelDesertArray = {"Australian \nDesert", levelDesert, String.valueOf(R.drawable.kangaroo)};
    public static final String[] levelForestArray = {"Australian \nForest", levelOutback, String.valueOf(R.drawable.platy)};
    public static final String[] levelAntarcticaArray = {"Antarctica", levelAntarctica, String.valueOf(R.drawable.polar_bear)};
    public static final String[] levelArcticArray = {"Arctic", levelArctic, String.valueOf(R.drawable.penguin)};
    public static final String[] levelCanadaArray = {"Canada", levelCanada, String.valueOf(R.drawable.beaver)};
    public static final String[] levelCaliforniaArray = {"California", levelCalifornia, String.valueOf(R.drawable.byson)};
    public static final String[] levelArgentinaArray = {"Argentina", levelArgentina, String.valueOf(R.drawable.jaguar)};
    public static final String[] levelAmazonArray = {"The Amazon \nRainforest", levelAmazon, String.valueOf(R.drawable.macaw)};
    public static final String[] levelAtlanticOceanArray = {"Atlantic Ocean", levelAtlanticOcean, String.valueOf(R.drawable.walrus)};
    public static final String[] levelPacificOceanArray = {"Pacific Ocean", levelPacificOcean, String.valueOf(R.drawable.whale)};
    public static final String[] levelFranceArray = {"France", levelFrance, String.valueOf(R.drawable.rhino)};
    public static final String[] levelUKArray = {"U.K.", levelUK, String.valueOf(R.drawable.squirell)};
    public static final String[] levelEgyptArray = {"Egypt", levelEgypt, String.valueOf(R.drawable.camel)};
    public static final String[] levelMadagascarArray = {"Madagascar", levelMadagascar, String.valueOf(R.drawable.chameleon)};

    public String[] levelArray(String presentUserLevel, int pinID) {

        String userLevel = "TEST";
        if (presentUserLevel.matches("Asia")) {
            if (pinID == 1) {
                userLevel = "India";
            } else if (pinID == 2) {
                userLevel = "China";
            }
        } else if (presentUserLevel.matches("Africa")) {
            if (pinID == 1) {
                userLevel = "Egypt";
            } else if (pinID == 2) {
                userLevel = "Madagascar";
            }
        } else if (presentUserLevel.matches("Australia")) {
            if (pinID == 1) {
                userLevel = "Indonesia";
            } else if (pinID == 2) {
                userLevel = "Australian Desert";
            } else if (pinID == 3) {
                userLevel = "Australian Forest";
            }
        } else if (presentUserLevel.matches("North America")) {
            if (pinID == 1) {
                userLevel = "California";
            } else if (pinID == 2) {
                userLevel = "Canada";
            }
        } else if (presentUserLevel.matches("South America")) {
            if (pinID == 1) {
                userLevel = "Argentina";
            } else if (pinID == 2) {
                userLevel = "Amazon Rainforest";
            }
        } else if (presentUserLevel.matches("Atlantic Ocean")) {
            userLevel = "Atlantic Ocean";
        } else if (presentUserLevel.matches("Pacific Ocean")) {
            userLevel = "Pacific Ocean";
        } else if (presentUserLevel.matches("Europe")) {
            if (pinID == 1) {
                userLevel = "France";
            } else if (pinID == 2) {
                userLevel = "U.K.";
            }
        } else if (presentUserLevel.matches("Arctic")) {
            userLevel = "Arctic";
        } else if (presentUserLevel.matches("Antarctica")) {
            userLevel = "Antarctica";
        }

        if (userLevel.matches("India")) {
            return levelIndiaArray;
        } else if (userLevel.matches("China")) {
            return levelChinaArray;
        } else if (userLevel.matches("Indonesia")) {
            return levelIndonesiaArray;
        } else if (userLevel.matches("Australian Desert")) {
            return levelDesertArray;
        } else if (userLevel.matches("Australian Forest")) {
            return levelForestArray;
        } else if (userLevel.matches("Antarctica")) {
            return levelAntarcticaArray;
        } else if (userLevel.matches("Arctic")) {
            return levelArcticArray;
        } else if (userLevel.matches("Canada")) {
            return levelCanadaArray;
        } else if (userLevel.matches("California")) {
            return levelCaliforniaArray;
        } else if (userLevel.matches("Argentina")) {
            return levelArgentinaArray;
        } else if (userLevel.matches("Amazon Rainforest")) {
            return levelAmazonArray;
        } else if (userLevel.matches("Egypt")) {
            return levelEgyptArray;
        } else if (userLevel.matches("Madagascar")) {
            return levelMadagascarArray;
        } else if (userLevel.matches("France")) {
            return levelFranceArray;
        } else if (userLevel.matches("U.K.")) {
            return levelUKArray;
        } else if (userLevel.matches("Pacific Ocean")) {
            return levelPacificOceanArray;
        } else if (userLevel.matches("Atlantic Ocean")) {
            return levelAtlanticOceanArray;
        }

        return levelIndiaArray;

    }

}
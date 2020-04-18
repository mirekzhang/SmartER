package com.example.mirek.smarter;

import android.provider.BaseColumns;

/**
 * Created by Mirek on 29/04/2018.
 */

public class DBStructure {
    public static abstract class tableEntry implements BaseColumns {
        public static final String TABLE_NAME = "usage";
        public static final String COLUMN_USAGE_ID = "usageid";
        public static final String COLUMN_RES_ID = "resid";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_HOURS = "hours";
        public static final String COLUMN_FRIDGE_USAGE = "fridgeusage";
        public static final String COLUMN_AC_USAGE = "acusage";
        public static final String COLUMN_WM_USAGE = "wmusage";
        public static final String COLUMN_TEMPERATURE = "temperature";
    }
}

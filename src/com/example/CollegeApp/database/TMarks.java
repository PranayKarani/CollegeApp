package com.example.CollegeApp.database; // 23 Dec, 06:18 AM

public class TMarks {

    public final static String table_name = "Marks";

    public final static String s_id = TStudent.s_id;
    public static final String m_sub1 = "Java";
    public static final String m_sub2 = "Maths";
    public static final String m_sub3 = "Multimedia";
    public static final String m_total = "total";

    public String createTable() {
        return "CREATE TABLE " + table_name + " ( " +
                s_id + " INTEGER, " +
                m_sub1 + " INTEGER, " +
                m_sub2 + " INTEGER, " +
                m_sub3 + " INTEGER, " +
                m_total + " INTEGER," +
                "FOREIGN KEY("+s_id+") REFERENCES " + TStudent.table_name+"("+TStudent.s_id+")"+
                " )";

    }

}

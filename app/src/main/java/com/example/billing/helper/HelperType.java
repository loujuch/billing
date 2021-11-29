package com.example.billing.helper;

import android.annotation.SuppressLint;

import com.example.billing.model.DetailIO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HelperType {

    private final static int offset=8;
    private final static int offsetDouble=16;
    private final static int mask=0xff;

    public final static int YEAR_HOUR=1;
    public final static int MONTH_MINTER=2;
    public final static int DAY_SECOND=3;

    public final static boolean CALENDAR=false;
    public final static boolean TIME=true;

    private final static String[] WEEK_STR={"星期天","星期一","星期二","星期三","星期四","星期五","星期六"};

    @SuppressLint("DefaultLocale")
    public static String getCalendarString(int whole) {
        return String.format("%d年%d月%d日",whole>>>offsetDouble,(whole>>>offset)&mask,whole&mask);
    }

    public static int getWhole(int YearHour, int MonthMinter, int DaySecond) {
        return (YearHour<<offsetDouble)|(MonthMinter<<offset)|DaySecond;
    }

    public static int getPart(int whole, int type) {
        switch (type) {
            case YEAR_HOUR:return whole>>>offsetDouble;
            case MONTH_MINTER:return (whole>>>offset)&mask;
            case DAY_SECOND:return whole&mask;
            default:return -1;
        }
    }

    public static int StoI(String s) {
        if(s==null)return -1;
        int j=0;
        for(int i=0;i<s.length();++i) {
            char c=s.charAt(i);
            if(c==' ')continue;
            if(!(s.charAt(i)>='0'&&s.charAt(i)<='9'))return -1;
            j*=10;
            j+=c-'0';
        }
        return j;
    }

    public static String getWeekStr(int week) {
        if(week<0||week>=WEEK_STR.length)return "";
        return WEEK_STR[week];
    }

    @SuppressLint("DefaultLocale")
    public static String getCalendar(int whole, int type) {
        String s="";
        if(type==YEAR_HOUR)s=String.format("%d年",whole>>>offsetDouble);
        if(type==MONTH_MINTER)s=String.format("%d月",(whole>>>offset)&mask);
        if(type==DAY_SECOND)s=String.format("%d日",(whole&mask));
        return s;
    }

    @SuppressLint("DefaultLocale")
    public static String getTime(int whole) {
        return String.format("%02d:%02d:%02d",whole>>>offsetDouble,(whole>>>offset)&mask,whole&mask);
    }

    public static DetailIO getType(List<DetailIO> tmp) {
        if(tmp==null||tmp.size()==0)return null;
        if(tmp.size()==1)return tmp.get(0);
        DetailIO mid=HelperIO.getOutputDetailIO(tmp.get(0).getIo(),HelperIO.YEAR);
        mid.setCalendar(tmp.get(0).getCalendar());
        mid.setTime(tmp.get(0).getTime());
        mid.setWeek(tmp.get(0).getWeek());
        mid.setPrice(tmp.get(0).getPrice());
        int len=tmp.size();
        for(int i=1;i<len;++i) {
            if(mid.getCalendar()!=tmp.get(i).getCalendar()||mid.getTime()!=tmp.get(i).getTime()
                    ||mid.getIo()!=tmp.get(i).getIo())return null;
            mid.setPrice(mid.getPrice()+tmp.get(i).getPrice());
            HelperIO.RemoveDetailById(tmp.get(i).getId());
        }
        return mid;
    }

    public static double StoD(String s) {
        double tmp=0;
        double mid=1;
        boolean flag=true;
        for(int i=0;i<s.length();++i) {
            if(s.charAt(i)==' ')continue;
            if(s.charAt(i)=='.') {
                flag=false;
                continue;
            }
            if(flag)tmp*=10;
            else mid*=0.1;
            if(!(s.charAt(i)>='0'&&s.charAt(i)<='9'))break;
            tmp+=(s.charAt(i)-'0')*mid;
        }
        return tmp;
    }

    public static int getTime(int year, int month, int day, int off, int type) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            @SuppressLint("DefaultLocale") Date start=sdf.parse(String.format("%04d-%02d-%02d",year,month,day));
            Calendar tmp =Calendar.getInstance();
            assert start != null;
            tmp.setTime(start);
            tmp.add(Calendar.DAY_OF_MONTH,off);
            if(type==DAY_SECOND)return tmp.get(Calendar.DATE);
            if(type==MONTH_MINTER)return tmp.get(Calendar.MONTH)+1;
            if(type==YEAR_HOUR)return tmp.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @SuppressLint("DefaultLocale")
    public static String getTimeString(int year, int month, int day, int off) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start=sdf.parse(String.format("%04d-%02d-%02d",year,month,day));
            Calendar tmp =Calendar.getInstance();
            assert start != null;
            tmp.setTime(start);
            tmp.add(Calendar.DAY_OF_MONTH,off);
            return String.format("%04d年%02d月%02d日",
                    tmp.get(Calendar.YEAR),tmp.get(Calendar.MONTH)+1,tmp.get(Calendar.DATE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}

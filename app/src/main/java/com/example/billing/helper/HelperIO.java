package com.example.billing.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;

import com.example.billing.model.DetailIO;
import com.example.billing.model.oneSign;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HelperIO {

    public final static int INCOME=0;
    public final static int PAY=1;

    public final static int YEAR=1;
    public final static int MONTH=2;
    public final static int DAY=3;
    public final static int TIME=4;

    public static DetailIO getOutputDetailIO(int io, int type) {
        DetailIO tmp=new DetailIO();
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=0;
        int day=0;
        int time=-1;
        int week=-1;
        if(type>=MONTH)month=calendar.get(Calendar.MONTH)+1;
        if(type>=DAY) {
            day=calendar.get(Calendar.DATE);
            week=calendar.get(Calendar.DAY_OF_WEEK)-1;
        }
        if(type>=TIME) {
            time=HelperType.getWhole(calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
        }
        int cal=HelperType.getWhole(year,month,day);
        tmp.setIo(io);
        tmp.setTime(time);
        tmp.setCalendar(cal);
        tmp.setPrice(0);
        tmp.setExtra("");
        tmp.setWeek(week);
        tmp.setMainLabel(-1);
        tmp.setSecondLabel(-1);
        return tmp;
    }

    public static DetailIO getInputDetailIO(int io, int year) {
        int calendar=HelperType.getWhole(year,0,0);
        @SuppressLint("DefaultLocale") List<DetailIO>tmp=LitePal.where(
                "calendar = ? and time = ? and io = ?",
                String.format("%d",calendar),"-1",String.format("%d",io)).find(DetailIO.class);
        return HelperType.getType(tmp);
    }

    public static DetailIO getInputDetailIO(int io, int year, int month) {
        int calendar=HelperType.getWhole(year,month,0);
        @SuppressLint("DefaultLocale") List<DetailIO>tmp=LitePal.where(
                "calendar = ? and time = ? and io = ?",
                String.format("%d",calendar),"-1",String.format("%d",io)).find(DetailIO.class);
        return HelperType.getType(tmp);
    }

    public static DetailIO getInputDetailIO(int io, int year, int month, int day) {
        if(io==HelperIO.INCOME)return null;
        int calendar=HelperType.getWhole(year,month,day);
        @SuppressLint("DefaultLocale") List<DetailIO>tmp=LitePal.where(
                "calendar = ? and time = ? and io = ?",
                String.format("%d",calendar),"-1",String.format("%d",io)).find(DetailIO.class);
        return HelperType.getType(tmp);
    }

    @SuppressLint("DefaultLocale")
    public static List<DetailIO> getInputDetailIOs(int year, int month) {
        int calendarL=HelperType.getWhole(year,month,1);
        int calendarR=HelperType.getWhole(year,month,31);
        return LitePal.where("calendar >= ? and calendar <= ? and time >= ?",
                String.format("%d",calendarL),String.format("%d",calendarR),"0").
                order("calendar desc, time desc").find(DetailIO.class);
    }

    @SuppressLint("DefaultLocale")
    public static List<DetailIO> getInputDetailIOs(int yearL, int monthL, int dayL,
                                                   int yearR, int monthR, int dayR) {
        int calendarL=HelperType.getWhole(yearL,monthL,dayL);
        int calendarR=HelperType.getWhole(yearR,monthR,dayR);
        if(calendarL>calendarR) {
            calendarR^=calendarL;
            calendarL^=calendarR;
            calendarR^=calendarL;
        }
        return LitePal.where("calendar >= ? and calendar <= ? and time >= ?",
                String.format("%d",calendarL),String.format("%d",calendarR),"0").
                order("calendar desc, time desc").find(DetailIO.class);
    }

    @SuppressLint("DefaultLocale")
    public static List<Entry> getInputDetailIOSum(int yearL, int monthL, int dayL,
                                                       int yearR, int monthR, int dayR, int io) {
        List<Entry>out=new ArrayList<>();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            int x=0;
            Date start=sdf.parse(String.format("%04d-%02d-%02d",yearL,monthL,dayL));
            Date end=sdf.parse(String.format("%04d-%02d-%02d",yearR,monthR,dayR));
            Calendar tmp =Calendar.getInstance();
            assert start != null;
            tmp.setTime(start);
            while(true) {
                assert end != null;
                if (!(start.getTime()<=end.getTime())) break;
                start=tmp.getTime();
                int year=tmp.get(Calendar.YEAR);
                int month=tmp.get(Calendar.MONTH)+1;
                int day=tmp.get(Calendar.DATE);
                out.add(new Entry(x, LitePal.where("calendar = ? and time >= ? and io = ?",
                        String.format("%d",HelperType.getWhole(year,month,day)),"0",String.format("%d",io)).
                        sum(DetailIO.class,"price",float.class)));
                tmp.add(Calendar.DAY_OF_MONTH, 1);
                ++x;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }

    @SuppressLint("DefaultLocale")
    public static List<PieEntry> getInputDetailIOSignMainSum(ArrayList<oneSign>tmp, int io, int timeL, int timeR) {
        List<PieEntry>out=new ArrayList<>();
        for(oneSign i:tmp) {
            String mid=i.getName();
            if(mid.length()>2)mid=mid.substring(0,2)+mid.substring(7);
            out.add(new PieEntry(LitePal.where("calendar >= ? and calendar <= ? and " +
                            "time >= ? and io = ? and mainLabel = ?",
                    String.format("%d",timeL),String.format("%d",timeR),"0",
                    String.format("%d",io),String.format("%d",i.getId())).
                    sum(DetailIO.class,"price",float.class),mid));
        }
        out.add(new PieEntry(LitePal.where("calendar >= ? and calendar <= ? and " +
                        "time >= ? and io = ? and mainLabel <= ?",
                String.format("%d",timeL),String.format("%d",timeR),"0",
                String.format("%d",io),"0").
                sum(DetailIO.class,"price",float.class),"未定义"));
        return out;
    }

    public static boolean Save(DetailIO tmp, double price, int mainLabel, int secondLabel, String extra) {
        tmp.setPrice(price);
        tmp.setMainLabel(mainLabel);
        tmp.setSecondLabel(secondLabel);
        tmp.setExtra(extra);
        return tmp.save();
    }

    public static DetailIO getInputDetailIOById(int id) {
        if (id<0) {
            return null;
        }
        return LitePal.find(DetailIO.class,id);
    }

    public static int UpdateDetailById(int id, double price,int type, int io) {
        if (id<0) {
            DetailIO tmp=getOutputDetailIO(io,type);
            if(!Save(tmp,price,-1,-1,""))return -1;
            return tmp.getId();
        }
        ContentValues value=new ContentValues();
        value.put("price",price);
        LitePal.update(DetailIO.class,value,id);
        return id;
    }

    public static void UpdateDetailByCalendar(int io, int year, double offset) {
        DetailIO y=getInputDetailIO(io, year);
        y.setPrice(y.getPrice()+offset);
        y.update(y.getId());
    }

    public static void UpdateDetailByCalendar(int io, int year, int month, double offset) {
        DetailIO m=getInputDetailIO(io, year, month);
        m.setPrice(m.getPrice()+offset);
        m.update(m.getId());
    }

    public static void UpdateDetailByCalendar(int io, int year, int month,int day, double offset) {
        DetailIO d=getInputDetailIO(io, year, month, day);
        assert d != null;
        d.setPrice(d.getPrice()+offset);
        d.update(d.getId());
    }

    public static void UpdateDetailByCalendar(int io, double offset, int whole) {
        int year=HelperType.getPart(whole,HelperType.YEAR_HOUR);
        int month=HelperType.getPart(whole,HelperType.MONTH_MINTER);
        int day=HelperType.getPart(whole,HelperType.DAY_SECOND);
        if(io==HelperIO.PAY)UpdateDetailByCalendar(io,year,month,day,offset);
        UpdateDetailByCalendar(io,year,month,offset);
        UpdateDetailByCalendar(io,year,offset);
    }

    public static void UpdateDetail(int id, double price, int main, int second, String extra) {
        ContentValues value=new ContentValues();
        value.put("extra",extra);
        value.put("price",price);
        value.put("mainLabel",main);
        value.put("secondLabel",second);
        LitePal.update(DetailIO.class,value,id);
    }

    @SuppressLint("DefaultLocale")
    public static void UpdateDetailByLabel(int fromMain, int fromSecond, int toMain, int toSecond) {
        ContentValues values=new ContentValues();
        values.put("mainLabel",toMain);
        values.put("secondLabel",toSecond);
        LitePal.updateAll(DetailIO.class,values,
                "mainLabel = ? and secondLabel = ?",
                String.format("%d",fromMain),
                String.format("%d",fromSecond));
    }

    public static void RemoveDetailById(int id) {
        LitePal.delete(DetailIO.class,id);
    }

    public static void DeleteDatabase() {
        LitePal.deleteDatabase("io");
    }

    @SuppressLint("DefaultLocale")
    public static List<PieEntry> getInputDetailIOSignSecondSum(ArrayList<oneSign> tmp,
                                                               int io, int timeL, int timeR, int id) {
        if(tmp==null||tmp.isEmpty())return null;
        List<PieEntry>out=new ArrayList<>();
        for(oneSign i:tmp) {
            String mid=i.getName();
            if(mid.length()>2)mid=mid.substring(0,2)+mid.substring(7);
            out.add(new PieEntry(LitePal.where("calendar >= ? and calendar <= ? and " +
                            "time >= ? and io = ? and mainLabel = ? and secondLabel = ?",
                    String.format("%d",timeL),String.format("%d",timeR),"0",
                    String.format("%d",io),String.format("%d",id),String.format("%d",i.getId())).
                    sum(DetailIO.class,"price",float.class),mid));
        }
        out.add(new PieEntry(LitePal.where("calendar >= ? and calendar <= ? and " +
                        "time >= ? and io = ? and mainLabel = ? and secondLabel <= ?",
                String.format("%d",timeL),String.format("%d",timeR),"0",
                String.format("%d",io),String.format("%d",id),"0").
                sum(DetailIO.class,"price",float.class),"未定义"));
        return out;
    }
}

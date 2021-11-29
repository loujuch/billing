package com.example.billing.helper;


import com.example.billing.model.DetailIO;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.Calendar;

public class MyApplication extends LitePalApplication {
    private int week;
    private int day;
    private int month;
    private int year;
    private int yearPayId;
    private int monthPayId;
    private int dayPayId;
    private int yearIncomeId;
    private int monthIncomeId;
    private double yearPay;
    private double monthPay;
    private double dayPay;
    private double yearIncome;
    private double monthIncome;
    private double allSaving;
    private double allUseAble;
    private int payId;
    private int incomeId;
    private Sign billing;
    private Sign budget;

    @Override
    public void onCreate() {
        super.onCreate();

        LitePal.initialize(this);
        LitePal.getDatabase();

        Calendar calendar=Calendar.getInstance();

        init();

        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DATE);
        week=calendar.get(Calendar.DAY_OF_WEEK)-1;

        DetailIO yearI=HelperIO.getInputDetailIO(HelperIO.INCOME,year);
        DetailIO yearO=HelperIO.getInputDetailIO(HelperIO.PAY,year);
        DetailIO monthI=HelperIO.getInputDetailIO(HelperIO.INCOME,year,month);
        DetailIO monthO=HelperIO.getInputDetailIO(HelperIO.PAY,year,month);
        DetailIO dayO=HelperIO.getInputDetailIO(HelperIO.PAY,year,month,day);

        if(yearI!=null) {
            yearIncomeId=yearI.getId();
            yearIncome=yearI.getPrice();
        }

        if(yearO!=null) {
            yearPayId=yearO.getId();
            yearPay=yearO.getPrice();
        }

        if(monthI!=null) {
            monthIncomeId=monthI.getId();
            monthIncome=monthI.getPrice();
        }

        if (monthO!=null) {
            monthPayId=monthO.getId();
            monthPay=monthO.getPrice();
        }

        if (dayO!=null) {
            dayPayId=dayO.getId();
            dayPay=dayO.getPrice();
        }
    }

    public void init() {
        billing=null;
        budget=null;

        allUseAble=0;
        allSaving=0;
        payId=0;
        incomeId=0;

        yearIncome=0;
        monthIncome=0;
        yearPay=0;
        monthPay=0;
        dayPay=0;

        yearIncomeId=-1;
        monthIncomeId=-1;
        yearPayId=-1;
        monthPayId=-1;
        dayPayId=-1;

        if(!HelperFile.judgeFile(HelperFile.ID))HelperFile.CreateDefault(this,HelperFile.ID);
        ArrayList<String>tmp=HelperFile.Read(this,HelperFile.ID);
        if(tmp==null)return;
        payId=HelperType.StoI(tmp.get(0));
        incomeId=HelperType.StoI(tmp.get(1));
        allSaving=HelperType.StoD(tmp.get(2));
        allUseAble=HelperType.StoD(tmp.get(3));
    }

    public void Update(int io, double offset) {
        Update();
        Update(io,offset,getYear(),getMonth(),getDay());
    }

    public void Update(int io, double offset, int whole) {
        Update();
        Update(io,offset,
                HelperType.getPart(whole,HelperType.YEAR_HOUR),
                HelperType.getPart(whole,HelperType.MONTH_MINTER),
                HelperType.getPart(whole,HelperType.DAY_SECOND));
    }

    public void Update(int io, double offset, int yearIn, int monthIn, int dayIn) {
        if(io==HelperIO.PAY) {
            if(yearIn==getYear()) {
                setYearPay(getYearPay() + offset);
                setYearPayId((HelperIO.UpdateDetailById(getYearPayId(), getYearPay(),
                        HelperIO.YEAR, HelperIO.PAY)));
                if(monthIn==getMonth()) {
                    setMonthPay(getMonthPay() + offset);
                    setMonthPayId((HelperIO.UpdateDetailById(getMonthPayId(), getMonthPay(),
                            HelperIO.MONTH, HelperIO.PAY)));
                    if(dayIn==getDay()) {
                        setDayPay(getDayPay() + offset);
                        setDayPayId((HelperIO.UpdateDetailById(getDayPayId(), getDayPay(),
                                HelperIO.DAY, HelperIO.PAY)));
                    } else {
                        HelperIO.UpdateDetailByCalendar(HelperIO.PAY,yearIn,monthIn,dayIn,offset);
                    }
                } else {
                    HelperIO.UpdateDetailByCalendar(HelperIO.PAY,yearIn,monthIn,offset);
                }
            } else {
                HelperIO.UpdateDetailByCalendar(HelperIO.PAY,yearIn,offset);
            }
        } else {
            if(yearIn==getYear()) {
                setYearIncome(getYearIncome() + offset);
                setYearIncomeId((HelperIO.UpdateDetailById(getYearIncomeId(), getYearIncome(),
                        HelperIO.YEAR, HelperIO.INCOME)));
                if(monthIn==getMonth()) {
                    setMonthIncome(getMonthIncome() + offset);
                    setMonthIncomeId((HelperIO.UpdateDetailById(getMonthIncomeId(), getMonthIncome(),
                            HelperIO.MONTH, HelperIO.INCOME)));
                } else {
                    HelperIO.UpdateDetailByCalendar(HelperIO.INCOME,yearIn,monthIn,offset);
                }
            } else {
                HelperIO.UpdateDetailByCalendar(HelperIO.INCOME,yearIn,offset);
            }
        }
    }

    public void Update() {
        Calendar calendar=Calendar.getInstance();
        if (getDay()!=calendar.get(Calendar.DATE)) {
            setDayPayId(-1);
            setDayPay(0);
            setDay(calendar.get(Calendar.DATE));
            setWeek(calendar.get(Calendar.DAY_OF_WEEK));
            if (getMonth()!=calendar.get(Calendar.MONTH)) {
                setMonthPayId(-1);
                setMonthIncome(-1);
                setMonthPay(0);
                setMonthIncome(0);
                setMonth(calendar.get(Calendar.MONTH));
                if (getYear()!=calendar.get(Calendar.YEAR)) {
                    setYear(calendar.get(Calendar.YEAR));
                    setYearPay(0);
                    setYearIncome(0);
                    setYearIncomeId(-1);
                    setYearPayId(-1);
                }
            }
        }
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYearPayId() {
        return yearPayId;
    }

    public void setYearPayId(int yearPayId) {
        this.yearPayId = yearPayId;
    }

    public int getMonthPayId() {
        return monthPayId;
    }

    public void setMonthPayId(int monthPayId) {
        this.monthPayId = monthPayId;
    }

    public int getDayPayId() {
        return dayPayId;
    }

    public void setDayPayId(int dayPayId) {
        this.dayPayId = dayPayId;
    }

    public int getYearIncomeId() {
        return yearIncomeId;
    }

    public void setYearIncomeId(int yearIncomeId) {
        this.yearIncomeId = yearIncomeId;
    }

    public int getMonthIncomeId() {
        return monthIncomeId;
    }

    public void setMonthIncomeId(int monthIncomeId) {
        this.monthIncomeId = monthIncomeId;
    }

    public double getYearPay() {
        return yearPay;
    }

    public void setYearPay(double yearPay) {
        this.yearPay = yearPay;
    }

    public double getMonthPay() {
        return monthPay;
    }

    public void setMonthPay(double monthPay) {
        this.monthPay = monthPay;
    }

    public double getDayPay() {
        return dayPay;
    }

    public void setDayPay(double dayPay) {
        this.dayPay = dayPay;
    }

    public double getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(double yearIncome) {
        this.yearIncome = yearIncome;
    }

    public double getMonthIncome() {
        return monthIncome;
    }

    public void setMonthIncome(double monthIncome) {
        this.monthIncome = monthIncome;
    }

    public double getAllSaving() {
        return allSaving;
    }

    public void setAllSaving(double allSaving) {
        this.allSaving = allSaving;
    }

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public int getIncomeId() {
        return incomeId;
    }

    public double getAllUseAble() {
        return allUseAble;
    }

    public void setAllUseAble(double allUseAble) {
        this.allUseAble = allUseAble;
    }

    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
    }

    public Sign getBilling() {
        return billing;
    }

    public void setBilling(Sign billing) {
        this.billing = billing;
    }

    public Sign getBudget() {
        return budget;
    }

    public void setBudget(Sign budget) {
        this.budget = budget;
    }
}

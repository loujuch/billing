package com.example.billing.helper;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class HelperFile {

    public final static int BILLING=1;
    public final static int BUDGET=2;
    public final static int ID=3;
    public final static int OUT=4;

    private final static String[]NAME={"Billing","Budget","Id","out.billing"};

    public static void CreateOut(Context context, String[] s) {
        FileOutputStream out;
        BufferedWriter writer=null;
        try {
            out=context.openFileOutput("out.billing", Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            for (String value : s) {
                if (value.equals("")) {
                    writer.write('\n');
                    continue;
                }
                writer.write(String.format("%s\n", value));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer!=null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private static void CreateFile(Context context, String[] s, String name) {
        FileOutputStream out;
        BufferedWriter writer=null;
        try {
            out=context.openFileOutput(name, Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            for(int i=0;i<s.length;++i) {
                if (s[i].equals("")) {
                    writer.write('\n');
                    continue;
                }
                writer.write(String.format("%10d%20.5f%1d%s\n",i+1,0.0,1,s[i]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer!=null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void CreateDefault(Context context, int type) {
        switch (type) {
            case BILLING:
                CreateDefaultBilling(context);
                break;
            case BUDGET:
                CreateDefaultBudget(context);
                break;
            case ID:
                CreateDefaultId(context);
                break;
            default:
                break;
        }
    }

    @SuppressLint("DefaultLocale")
    private static void CreateDefaultBilling(Context context) {
        String[] s = {
                "餐饮", "早餐", "午餐", "晚餐", "夜宵", "零食", "",
                "日常<br/>开销", "住房", "医疗", "交通", "通信", "教育", "",
                "日常<br>用品", "洗漱", "衣物", "家具", "",
                "经济<br/>活动", "投资", "借出", "还债", "赠与", "",
                "娱乐", "电视", "电影", "游戏", "聚会","",
                "其他"
        };
        CreateFile(context, s, "Billing");
    }

    @SuppressLint("DefaultLocate")
    private static void CreateDefaultBudget(Context context) {
        String[] s = {
                "工资","",
                "副业","",
                "投资","",
                "卖出","",
                "还款","",
                "其他"
        };
        CreateFile(context,s,"Budget");
    }

    @SuppressLint("DefaultLocale")
    private static void CreateDefaultId(Context context) {
        int pay=32;
        int income=12;
        double saving=0;
        double UseAble=0;
        if(judgeFile(BILLING))pay=getMax(context,BILLING);
        if(judgeFile(BUDGET))income=getMax(context,BUDGET);
        FileOutputStream out;
        BufferedWriter writer=null;
        try {
            out=context.openFileOutput("Id", Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(String.format("%d\n",pay));
            writer.write(String.format("%d\n",income));
            writer.write(String.format("%f\n",saving));
            writer.write(String.format("%f\n",UseAble));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer!=null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean FileIsExist(String s) {
        try {
            File file=new File(s);
            if(!file.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean DeleteFile(int type) {
        if(type<=0||type>NAME.length)return false;
        @SuppressLint("SdCardPath") String name="/data/data/com.example.billing/files/"+NAME[type-1];
        if(!FileIsExist(name))return false;
        File file=new File(name);
        return file.delete();
    }

    @SuppressLint("SdCardPath")
    public static boolean judgeFile(int type) {
        if(type<=0||type>NAME.length)return false;
        return FileIsExist("/data/data/com.example.billing/files/"+NAME[type-1]);
    }

    private static void Save(Context context,ArrayList<String>date, String name) {
        FileOutputStream out;
        BufferedWriter writer=null;
        try {
            int len=date.size();
            out=context.openFileOutput(name, Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            for(int i=0;i<len;++i) {
                writer.write(date.get(i)+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer!=null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void Save(Context context,ArrayList<String>data, int type) {
        if(type>NAME.length||type<=0)return;
        Save(context,data,NAME[type-1]);
    }

    @SuppressLint("DefaultLocale")
    public static void Save(Context context, int mainId, int secondId, double saving, double use) {
        ArrayList<String>data=new ArrayList<>();
        data.add(String.format("%d",mainId));
        data.add(String.format("%d",secondId));
        data.add(String.format("%.5f",saving));
        data.add(String.format("%.5f",use));
        Save(context,data,"Id");
    }

    private static ArrayList<String> Read(Context context, String name) {
        FileInputStream in;
        BufferedReader reader=null;
        ArrayList<String>tmp=new ArrayList<>();
        try {
            in=context.openFileInput(name);
            reader=new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line=reader.readLine())!=null) {
                tmp.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader!=null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tmp;
    }

    public static ArrayList<String> Read(Context context, int type) {
        if(type>NAME.length)return null;
        return Read(context,NAME[type-1]);
    }

    private static int getMax(Context context, String name) {
        FileInputStream in;
        BufferedReader reader=null;
        int max=-1;
        try {
            in=context.openFileInput(name);
            reader=new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line=reader.readLine())!=null) {
                if(line.length()<10)continue;
                int tmp=HelperType.StoI(line.substring(0,10));
                if(tmp>max)max=tmp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader!=null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return max;
    }

    public static int getMax(Context context, int type) {
        switch (type) {
            case BILLING:return getMax(context, "Billing");
            case BUDGET:return getMax(context, "Budget");
            default:return -1;
        }
    }
}

package com.example.billing.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.example.billing.R;
import com.example.billing.component.MyRadioGroup;
import com.example.billing.component.MyShowBudget;
import com.example.billing.model.oneSign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sign  {

    private int main_value;
    private int second_value;
    private final HashMap<Integer, Integer>main_map;
    private final HashMap<Integer, Integer>second_map;
    private final ArrayList<oneSign>main;
    private final ArrayList<ArrayList<oneSign>>second;

    public Sign(Context context,int type) {
        int site = 0;
        main_value=-1;
        second_value=-1;
        boolean flag = true;
        main = new ArrayList<>();
        main_map = new HashMap<>();
        second = new ArrayList<>();
        second_map = new HashMap<>();
        if(!HelperFile.judgeFile(type))HelperFile.CreateDefault(context,type);
        ArrayList<String>tmp=HelperFile.Read(context,type);
        assert tmp != null;
        int len=tmp.size();
        for(int i=0;i<len;++i) {
            if(tmp.get(i).equals("")) {
                ++site;
                flag=true;
                continue;
            }
            String mid=tmp.get(i);
            if(flag) {
                flag=false;
                main.add(new oneSign(mid));
                second.add(new ArrayList<>());
            } else {
                second.get(site).add(new oneSign(mid));
            }
        }
    }

    public ArrayList<oneSign> getMain() {
        return main;
    }

    public int add(String s,int id) {
        if(main_value==-1)return -1;
        for(oneSign o:second.get(main_value)) {
            if(o.getName().equals(s)) return 1;
        }
        second.get(main_value).add(new oneSign(id,s));
        return 0;
    }

    public String getMainLabel(int mv) {
        for(oneSign tmp:main) {
            if(tmp.getId()==mv)return tmp.getName();
        }
        return "未定<br/>义";
    }

    public String getSecondLabel(int mv) {
        for(ArrayList<oneSign>mid:second) {
            for (oneSign tmp:mid) {
                if(tmp.getId()==mv)return tmp.getName();
            }
        }
        return "未定<br/>义";
    }

    public int getMainId(String s) {
        if(s==null)return -1;
        if(s.length()>2)s=s.substring(0,2)+"<br/>"+s.substring(2);
        if(s.equals("未定<br/>义"))return 0;
        for(oneSign i:main) {
            if (i.getName().equals(s)) {
                return i.getId();
            }
        }
        return -1;
    }

    public ArrayList<oneSign> getSecond(int id) {
        if(id<=0)return null;
        int len=main.size();
        for(int i=0;i<len;++i) {
            if(main.get(i).getId()==id) {
                return second.get(i);
            }
        }
        return null;
    }

    public int getMainId(int mv) {
        if(mv<=0)return -1;
        int len=main.size();
        int site=-1;
        for(int i=0;i<len;++i) {
            if(main.get(i).getId()==mv) {
                site=i;
                break;
            }
        }
        if(site==-1)return -1;
        for(Map.Entry<Integer,Integer> entry:main_map.entrySet()) {
            if(entry.getValue().equals(site)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public int getSecondId(int mv) {
        if(mv<=0)return -1;
        int len=second.get(main_value).size();
        int site=-1;
        for(int i=0;i<len;++i) {
            if(second.get(main_value).get(i).getId()==mv) {
                site=i;
                break;
            }
        }
        if(site==-1)return -1;
        for(Map.Entry<Integer,Integer> entry:second_map.entrySet()) {
            if(entry.getValue().equals(site)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public void setLabel(Context context, int m, int s, MyRadioGroup group, MyRadioGroup other,
                         MyShowBudget mainShow, MyShowBudget secondShow) {
        showMain(context,group,other,mainShow,secondShow);
        main_value=m;
        second_value=s;
        if(m!=-1) {
            int n=getMainId(m);
            if(n==-1)return;
            group.check(n);
            n=getSecondId(s);
            if(n==-1)return;
            other.check(n);
        }
    }

    @SuppressLint("DefaultLocale")
    public void showMain(Context context, MyRadioGroup group, MyRadioGroup other,
                         MyShowBudget mainShow, MyShowBudget secondShow) {
        main_value=-1;
        second_value=-1;
        int len=main.size();
        for(int i=0;i<len;++i) {
            @SuppressLint("InflateParams") RadioButton tmp=(RadioButton) LayoutInflater.
                    from(context).inflate(R.layout.sign,null);
            tmp.setText(Html.fromHtml(main.get(i).getName()));
            int mid= View.generateViewId();
            main_map.put(mid,i);
            tmp.setId(mid);
            group.addView(tmp);
        }
        group.setOnCheckedChangeListener((radioGroup, i) -> {
            main_value=main_map.get(i);
            mainShow.setText(String.format("%.2f",main.get(main_value).getAble()));
            mainShow.Show();
            secondShow.unShow();
            showSecond(context,other,secondShow);
        });
    }

    @SuppressLint("DefaultLocale")
    public void showSecond(Context context, MyRadioGroup group, MyShowBudget show) {
        if (group==null)return;
        group.removeAllViews();
        second_map.clear();
        second_value=-1;
        int len=second.get(main_value).size();
        for(int i=0;i<len;++i) {
            if(second.get(main_value).get(i).getType()==0)continue;
            @SuppressLint("InflateParams") RadioButton tmp=(RadioButton) LayoutInflater.
                    from(context).inflate(R.layout.sign,null);
            tmp.setText(Html.fromHtml(second.get(main_value).get(i).getName()));
            int mid= View.generateViewId();
            second_map.put(mid,i);
            tmp.setId(mid);
            group.addView(tmp);
        }
        group.setOnCheckedChangeListener((radioGroup, i) -> {
            second_value=second_map.get(i);
            show.setText(String.format("%.2f",second.get(main_value).get(second_value).getAble()));
            show.Show();
        });
    }

    public void showMainHide(Context context, MyRadioGroup group, MyRadioGroup other) {
        group.removeAllViews();
        other.removeAllViews();
        main_value=-1;
        second_value=-1;
        int len=main.size();
        for(int i=0;i<len;++i) {
            @SuppressLint("InflateParams") RadioButton tmp=(RadioButton) LayoutInflater.
                    from(context).inflate(R.layout.sign,null);
            tmp.setText(Html.fromHtml(main.get(i).getName()));
            int mid= View.generateViewId();
            main_map.put(mid,i);
            tmp.setId(mid);
            group.addView(tmp);
        }
        group.setOnCheckedChangeListener((radioGroup, i) -> {
            main_value=main_map.get(i);
            showSecondHide(context,other);
        });
    }

    @SuppressLint("DefaultLocale")
    public void showSecondHide(Context context, MyRadioGroup group) {
        if(group==null)return;
        group.removeAllViews();
        second_map.clear();
        second_value=-1;
        int len=second.get(main_value).size();
        for(int i=0;i<len;++i) {
            if(second.get(main_value).get(i).getType()==1)continue;
            @SuppressLint("InflateParams") RadioButton tmp=(RadioButton) LayoutInflater.
                    from(context).inflate(R.layout.sign,null);
            tmp.setText(Html.fromHtml(second.get(main_value).get(i).getName()));
            int mid= View.generateViewId();
            second_map.put(mid,i);
            tmp.setId(mid);
            group.addView(tmp);
        }
        group.setOnCheckedChangeListener((radioGroup, i) -> second_value=second_map.get(i));
    }

    @SuppressLint("DefaultLocale")
    public void showMain(Context context, MyRadioGroup group, MyRadioGroup other) {
        group.removeAllViews();
        if(other!=null)other.removeAllViews();
        main_value=-1;
        second_value=-1;
        int len=main.size();
        for(int i=0;i<len;++i) {
            @SuppressLint("InflateParams") RadioButton tmp=(RadioButton) LayoutInflater.
                    from(context).inflate(R.layout.sign,null);
            tmp.setText(Html.fromHtml(main.get(i).getName()));
            int mid= View.generateViewId();
            main_map.put(mid,i);
            tmp.setId(mid);
            group.addView(tmp);
        }
        group.setOnCheckedChangeListener((radioGroup, i) -> {
            main_value=main_map.get(i);
            showSecond(context,other);
        });
    }

    @SuppressLint("DefaultLocale")
    public void showSecond(Context context, MyRadioGroup group) {
        if (group==null)return;
        group.removeAllViews();
        second_map.clear();
        second_value=-1;
        int len=second.get(main_value).size();
        for(int i=0;i<len;++i) {
            if(second.get(main_value).get(i).getType()==0)continue;
            @SuppressLint("InflateParams") RadioButton tmp=(RadioButton) LayoutInflater.
                    from(context).inflate(R.layout.sign,null);
            tmp.setText(Html.fromHtml(second.get(main_value).get(i).getName()));
            int mid= View.generateViewId();
            second_map.put(mid,i);
            tmp.setId(mid);
            group.addView(tmp);
        }
        group.setOnCheckedChangeListener((radioGroup, i) -> second_value=second_map.get(i));
    }

    public int getMainValue() {
        if(main_value<0||main_value>=main.size())return -1;
        return main.get(main_value).getId();
    }

    public int getSecondValue() {
        if(main_value<0||main_value>=main.size()||
                second_value<0||second_value>=second.get(main_value).size())return -1;
        return second.get(main_value).get(second_value).getId();
    }

    public double getPrice() {
        if(main_value==-1&&second_value==-1)return 0;
        if(second_value==-1) {
            return main.get(main_value).getAble();
        } else {
            return second.get(main_value).get(second_value).getAble();
        }
    }

    public int setPrice(double price) {
        if(main_value==-1&&second_value==-1)return -2;
        if(second_value==-1) {
            double tmp=main.get(main_value).getAble()+price;
            if(tmp<0)return -1;
            main.get(main_value).setAble(tmp);
        } else {
            double tmp=second.get(main_value).get(second_value).getAble()+price;
            if(tmp<0)return -1;
            second.get(main_value).get(second_value).setAble(tmp);
        }
        return 0;
    }

    public int setPrice(double price, int type) {
        if(main_value==-1&&second_value==-1) return -2;
        if (type==HelperIO.PAY) price*=-1;
        if(second_value==-1) {
            double tmp=main.get(main_value).getAble()+price;
            if(tmp<0)return -1;
            main.get(main_value).setAble(tmp);
        } else {
            double tmp=second.get(main_value).get(second_value).getAble()+price;
            if(tmp<0) {
                tmp+=main.get(main_value).getAble();
                if(tmp<0)return -1;
                second.get(main_value).get(second_value).setAble(0);
                main.get(main_value).setAble(tmp);
                return 1;
            }
            second.get(main_value).get(second_value).setAble(tmp);
        }
        return 0;
    }

    public double getDiff(double price, int type) {
        if(main_value==-1&&second_value==-1) return 0;
        if (type==HelperIO.PAY) price*=-1;
        if(second_value==-1) {
            return main.get(main_value).getAble()+price;
        } else {
            return second.get(main_value).get(second_value).getAble()+
                    main.get(main_value).getAble()+price;
        }
    }

    public void clear() {
        main.get(main_value).setAble(0);
        if(second_value>0)second.get(main_value).get(second_value).setAble(0);
    }

    @SuppressLint("DefaultLocale")
    public ArrayList<String> getSignList() {
        int len=main.size();
        ArrayList<String>tmp=new ArrayList<>();
        for(int i=0;i<len;++i) {
            tmp.add(main.get(i).toString());
            int size=second.get(i).size();
            for(int j=0;j<size;++j) {
                tmp.add(second.get(i).get(j).toString());
            }
            tmp.add("");
        }
        return tmp;
    }

    public boolean changeType() {
        if(second_value<0) return false;
        int tmp=second.get(main_value).get(second_value).getType();
        second.get(main_value).get(second_value).setType(tmp>0?0:1);
        return true;
    }

    public boolean setText(String s) {
        if(second_value==-1)return false;
        second.get(main_value).get(second_value).setName(s);
        return true;
    }

    public void delete(int x,int y) {
        if(x==-1||y==-1)return;
        second.get(x).remove(y);
    }

    public int getMain_value() {
        return main_value;
    }

    public int getSecond_value() {
        return second_value;
    }
}

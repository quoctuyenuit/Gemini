package com.example.a15520824.phanmemchuyensau;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuocTuyen on 6/17/2018.
 */

public class DataManager {
    public ArrayList<ActionModel> ListData;
    private final String fileName = "systemFile.txt";
    private Context context;
    private static DataManager _instance;

    public static DataManager Instance(){
        if (_instance == null){
            _instance = new DataManager();
        }

        return _instance;
    }

    DataManager(){
    }

    public void giveContext(Context context){
        this.context = context;
        this.initData();
    }

    private void initData(){
        try {
            this.ListData = FileIO.Instance().readFile(context, this.fileName);
        } catch (Exception ex){
            this.ListData = this.getDefautInit();
            try {
                FileIO.Instance().writeFile(context, this.fileName, this.ListData);
            } catch (Exception e) {
            }
        }
    }

    public boolean checkKey(String key){
        String from = key.toLowerCase();

        for (ActionModel action :
                ListData) {
            if (from.equals(action.getKey()))
                return false;
        }
        return true;
    }

    public void saveDatabase(){
        try {
            FileIO.Instance().writeFile(context, fileName, ListData);
        } catch (Exception e) {
            Toast.makeText(context, "Không thể lưu xuống file!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getActionId(String key){
        for (ActionModel action :
                ListData) {
            if (action.getKey().equals(key))
                return action.getId();
        }

        return "";
    }

    private ArrayList<ActionModel> getDefautInit()
    {
        ArrayList<ActionModel> listDefaultAction = new ArrayList<>();

        ArrayList<PackageInfo> infoList = FileIO.Instance().getInstalledApps(context);



        listDefaultAction.add(new ActionModel("gọi", "Gọi"));
        listDefaultAction.add(new ActionModel("báo thức", "Báo Thức"));
        listDefaultAction.add(new ActionModel("hẹn", "Hẹn"));
        listDefaultAction.add(new ActionModel("gửi", "Gửi"));
        listDefaultAction.add(new ActionModel("tin nhắn", "Tin Nhắn"));
        listDefaultAction.add(new ActionModel("chụp hình", "Chụp Hình"));
        listDefaultAction.add(new ActionModel("tìm", "Tìm"));
        listDefaultAction.add(new ActionModel("đi", "Đi"));
        listDefaultAction.add(new ActionModel("tới", "Tới"));
        listDefaultAction.add(new ActionModel("tìm kiếm", "Tìm Kiếm"));
        listDefaultAction.add(new ActionModel("google", "Google"));
        listDefaultAction.add(new ActionModel("status", "Status"));
        listDefaultAction.add(new ActionModel("check in", "Check in"));
        listDefaultAction.add(new ActionModel("mở tin nhắn", "Mở tin nhắn"));
        listDefaultAction.add(new ActionModel("mở máy tính", "Mở máy tính"));
        listDefaultAction.add(new ActionModel("mở cuộc gọi", "Mở cuộc gọi"));
        listDefaultAction.add(new ActionModel("mở ảnh", "Mở ảnh"));
        listDefaultAction.add(new ActionModel("mở bản đồ", "Mở bản đồ"));
        listDefaultAction.add(new ActionModel("mở báo thức", "Mở báo thức"));
        listDefaultAction.add(new ActionModel("mở youtube", "Mở YouTube"));
        for (PackageInfo info :
                infoList) {
            String name = info.applicationInfo.loadLabel(context.getPackageManager()).toString();
            String id = info.packageName;
            String key = "mở " + name;
            if (name.equals("Zalo") ){
                int a  = 0;
            }
            listDefaultAction.add(new ActionModel(key, name, id));
        }

        return listDefaultAction;
    }
}

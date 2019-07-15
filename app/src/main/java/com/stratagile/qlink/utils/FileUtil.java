package com.stratagile.qlink.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Base64;

import com.socks.library.KLog;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.qlinkcom;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

/**
 * Created by huzhipeng on 2018/3/7.
 */

public class FileUtil {
    public static void removeAllImageAvater(Context context) {
//        File vpn = new File(Environment.getExternalStorageDirectory() + "/QWallet/vpn");
//        String[] vpnchildren = vpn.list();
//        if (vpnchildren != null && vpnchildren.length != 0) {
//            for (int i=0; i<vpnchildren.length; i++) {
//                File chilrenFile = new File(vpn, vpnchildren[i]);
//                chilrenFile.delete();
//            }
//        }
//        if (Calendar.getInstance().getTimeInMillis() - SpUtil.getLong(context, ConstantValue.lastRemoveImageAvaterTime, 0) > 1000 * 60 * 60 * 24 * 7) {
//            String jsonPath =Environment.getExternalStorageDirectory() + "/QWallet/Profile/jsonFile.json";
//            File jsonFile = new File(jsonPath);
//            if (jsonFile.exists()) {
//                jsonFile.delete();
//            }
//
//            SpUtil.putLong(context, ConstantValue.lastRemoveImageAvaterTime, Calendar.getInstance().getTimeInMillis());
//            File dir = new File(Environment.getExternalStorageDirectory() + "/QWallet/image");
//            String[] children = dir.list();
//            if (children != null && children.length != 0) {
//                for (int i=0; i<children.length; i++) {
//                    File chilrenFile = new File(dir, children[i]);
//                    if (chilrenFile.getName().substring(0, chilrenFile.getName().lastIndexOf(".")).equals(SpUtil.getString(context, ConstantValue.myAvaterUpdateTime, ""))) {
//                        continue;
//                    }
//                    chilrenFile.delete();
//                }
//            }
//        }
    }

    /**
     * 保存自己的p2pid到本地sd卡
     */
    public static String saveP2pId2Local(String p2pId) {
        String lastP2pId = getLocalP2pId();
        if ("".equals(lastP2pId)) {
            copyDataFile();
            String jsonPath = Environment.getExternalStorageDirectory() + "/QWallet/backup/p2p.json";
            File jsonFile = new File(jsonPath);

            FileWriter fw = null;
            BufferedWriter out = null;
            try {
                if (!jsonFile.exists()) {
                    jsonFile.createNewFile();
                }
                fw = new FileWriter(jsonFile);
                out = new BufferedWriter(fw);
                out.write(p2pId, 0, p2pId.length());
                out.close();
            } catch (Exception e)
            {
                System.out.println("保存数据异常" + e);
                e.printStackTrace();
            }
            finally
            {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            return "";
        } else {
            if (lastP2pId.equals(p2pId)) {
                return "";
            } else {
                return lastP2pId;
            }
        }
    }

    /**
     * 获取sd卡已经保存的p2pId
     */
    public static String getLocalP2pId() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        String p2pIdJson = "";
        try {
            File file = new File(Environment.getExternalStorageDirectory(),"/QWallet/backup/p2p.json");
            if(!file.exists())
            {
                return p2pIdJson;
            }
            fis = new FileInputStream(file);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
//            String res = new String(buffer);
            p2pIdJson = new String(buffer);
        } catch (IOException  e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
                if (ois != null) ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p2pIdJson;
    }

    /**
     * 复制data文件到backup文件夹
     */
    public static void copyDataFile() {
        copyFile(Environment.getExternalStorageDirectory() + "/QWallet/data", Environment.getExternalStorageDirectory() + "/QWallet/backup/data");
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        KLog.i("复制文件。。。。。。");
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }

    }

    public static String getJson(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }

    /**
     *  保持资产数据到sd卡
     * @param walletAdress 钱包地址
     * @param jsonStr 数据
     */
    public static void saveAssetsData(String walletAdress,String jsonStr) {
        String jsonPath = Environment.getExternalStorageDirectory() + "/QWallet/Assets/"+walletAdress+".json";
        File jsonFile = new File(jsonPath);

        FileWriter fw = null;
        BufferedWriter out = null;
        try {
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
            }
            fw = new FileWriter(jsonFile);
            out = new BufferedWriter(fw);
            out.write(jsonStr, 0, jsonStr.length());
            out.close();
        } catch (Exception e)
        {
            System.out.println("保持资产数据异常" + e);
            e.printStackTrace();
        }
        finally
        {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 读取本地SDk卡资产数据
     * @param walletAdress 钱包地址
     * @return
     */
    public static String readAssetsData(String walletAdress) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                File file = new File(Environment.getExternalStorageDirectory(),"/QWallet/Assets/"+walletAdress+".json");
                if(!file.exists())
                {
                    return  "";
                }
                fis = new FileInputStream(file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                String res = new String(buffer);
//                String res = EncodingUtils.getString(buffer, "UTF-8");
                return res;
            } catch (IOException  e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) fis.close();
                    if (ois != null) ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
    /**
     * 读取本地SDk卡所有资产的钱包名称
     * @return
     */
    public static String getAllWalletNames() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                File file = new File(Environment.getExternalStorageDirectory(),"/QWallet/Assets/");
                if(!file.exists())
                {
                    return  "";
                }
                File[] files = file.listFiles();
                String name = "";
                if(files != null && files.length >0)
                {
                    for (int i=0; i<files.length; i++) {
                        if(name.equals(""))
                        {
                            name += files[i].getName().replace(".json","");
                        }else{
                            name += ","+files[i].getName().replace(".json","");
                        }

                    }
                    return name;
                }
            } catch (Exception  e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) fis.close();
                    if (ois != null) ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
    /**
     * 读取本地SDk卡所有钱包的地址
     * @return
     */
    public static String getAllAddressNames() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                File file = new File(Environment.getExternalStorageDirectory(),"/QWallet/Address/");
                if(!file.exists())
                {
                    return  "";
                }
                File[] files = file.listFiles();
                String name = "";
                if(files != null && files.length >0)                {

                    for (int i=0; i<files.length; i++) {
                        String data = getDataFromFile(files[i]);
                        String addData = data;
                        if(!"".equals(data) && data.length() >= 42)
                        {
                            boolean isBase64 = StringUitl.isBase64(data);
                            if(!isBase64)
                            {
                                try {
                                    FileOutputStream fos = new FileOutputStream(files[i]);
                                    OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                                    byte[] bytes = qlinkcom.AES(data.getBytes(),0);
                                    String encryptPrivateKey = Base64.encodeToString(bytes,Base64.NO_WRAP);
                                    addData = encryptPrivateKey;
                                    osw.write(encryptPrivateKey);
                                    osw.flush();
                                    fos.flush();
                                    osw.close();
                                    fos.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            isBase64 = StringUitl.isBase64(addData);
                            if(isBase64)
                            {
                                byte[] bytesAddData = Base64.decode(addData, Base64.NO_WRAP);
                                byte[] bytes = qlinkcom.AES(bytesAddData,1);
                                String decryptPrivateKey =  new String(bytes);
                                addData =  decryptPrivateKey;
                            }
                            if(!name.contains(addData))
                            {
                                if(name.equals(""))
                                {
                                    name += addData;
                                }else{
                                    name += ","+addData;
                                }
                            }
                        }
                    }
                    return name;
                }
            } catch (Exception  e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) fis.close();
                    if (ois != null) ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
    /**
     * 读取文件数据
     * @param file
     * @return
     */
    public static String getDataFromFile(File file) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                if(!file.exists())
                {
                    return  "";
                }
                fis = new FileInputStream(file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                String res = new String(buffer);
//                String res = EncodingUtils.getString(buffer, "UTF-8");
                return res;
            } catch (IOException  e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) fis.close();
                    if (ois != null) ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 读取文件数据
     * @param file
     * @return
     */
    public static String getStrDataFromFile(File file) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                if(!file.exists())
                {
                    return  "";
                }
                fis = new FileInputStream(file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                String res = new String(buffer);
//                String res = EncodingUtils.getString(buffer, "UTF-8");
                return res;
            } catch (IOException  e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) fis.close();
                    if (ois != null) ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return "";
    }

    /**
     *  保存数据到sd卡
     * @param path 路径包括文件名称
     * @param data 数据
     */
    public static void savaData(String path,String data)
    {
        File walletFile = new File(Environment.getExternalStorageDirectory() + path, "");//"/QWallet/Address/index.txt"
        if (!walletFile.exists()) {
            try {
                walletFile.createNewFile();
                try {
                    FileOutputStream fos = new FileOutputStream(walletFile);
                    OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                    osw.write(data);
                    osw.flush();
                    fos.flush();
                    osw.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileOutputStream fos = new FileOutputStream(walletFile);
                OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
                osw.write(data);
                osw.flush();
                fos.flush();
                osw.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据路径获取sd卡数据
     * @param path
     * @return
     */
    public static String readData(String path) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                File file = new File(Environment.getExternalStorageDirectory(),path);
                if(!file.exists())
                {
                    return  "";
                }
				
                fis = new FileInputStream(file);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                String res = new String(buffer);
//                String res = EncodingUtils.getString(buffer, "UTF-8");
                return res;
            } catch (IOException  e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) fis.close();
                    if (ois != null) ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

}

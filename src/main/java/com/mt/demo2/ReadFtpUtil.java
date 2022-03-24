package com.mt.demo2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpDirEntry;
import sun.net.ftp.FtpProtocolException;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;

@RestController
@RequestMapping("/xml")
public class ReadFtpUtil {
    @RequestMapping("/test")
    public String string() {
        FtpClient ftp = connectFTP("192.168.3.135", 22, "root", "12345qwertcensoft");
        try {
            //获取第一级的文件夹 911....
            List<String> firstNames = getFirstDirName(ftp);

            //遍历,读取文件夹下的文件
//            for (String dirName : firstNames){
//                String filePath = dirName+"\\";
//                List<Map<String, Object>> fileList =  getAllFileName(ftp,dirName);
//                if (fileList!= null && fileList.size()>0){
//                    System.out.println("===="+filePath+"====="+ fileList.toString());
//                    Map<String,Object> mapList = fileList.get(0);
//                    //获取70下的文件
//                    List<String> list70 =  (List<String>)mapList.get(dirName+"_70");
//                    //TODO
//
//                    //获取73下的文件
//                    List<String> list73 =  (List<String>)mapList.get(dirName+"_73");
//                    if(list73!= null && list73.size()>0){
//                        for (String txtName: list73){
//                            String txtPath = filePath+ "73\\"+txtName ;
//                            List<String> countList = readFileContent(txtPath,ftp);
//                            System.out.println("====="+txtPath+"===="+ countList.toString());
//
//                        }
//                    }
//                }
//            }
            return "xxx";

        } catch (Exception e) {
            e.printStackTrace();
            return "yyy";
        }

    }

    /***
     * 连接ftp
     * @param url  必须是ip地址: 10.0.0.1
     * @param port
     * @param username
     * @param password
     * @return
     */
    public static FtpClient connectFTP(String url, int port, String username, String password) {
        //创建ftp
        FtpClient ftp = null;
        try {
            //创建地址
            InetAddress ia = InetAddress.getByName("192.168.3.135");
            InetSocketAddress addr = new InetSocketAddress(ia, port);
            System.out.println("主机IP地址：" + addr.getAddress());
            //连接
            ftp = FtpClient.create();
            ftp.connect(addr);
            //登陆
            ftp.login(username, password.toCharArray());
            ftp.setBinaryType();
        } catch (FtpProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftp;
    }


    public static List<String> getFirstDirName(FtpClient ftp) {
        //获取第一层的文件夹名称: 911.912....
        List<String> list = new LinkedList<>();
        try {

            Iterator<FtpDirEntry> itDirEntry = ftp.listFiles("/home/cendxs/linkmonitor/sites-upload/DbCenter/DLC01A");
            while (itDirEntry.hasNext()) {
                FtpDirEntry ftpEntry = itDirEntry.next();
                FtpDirEntry.Type type = ftpEntry.getType();
                if ("DIR".equals(type.name())){
                    String dirName = ftpEntry.getName();
                    list.add(dirName);
                }
            }

            System.out.println(list.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }


    public static List<Map<String, Object>> getAllFileName(FtpClient ftp, String firstDirName) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new LinkedHashMap<>(); //有序的集合
        try {
            // 获取指定目录下的文件夹集合
            Iterator<FtpDirEntry> itDirEntry = ftp.listFiles("/"+firstDirName);
            List<String> list70 = new LinkedList<>();
            List<String> list73 = new LinkedList<>();
            //遍历
            while (itDirEntry.hasNext()) {
                FtpDirEntry ftpEntry = (FtpDirEntry) itDirEntry.next();
                FtpDirEntry.Type type = ftpEntry.getType();
                String dirName = ftpEntry.getName();
                if (type.name().equals("DIR")) {
                    // //遍历获取70/73
                    if ("70".equals(dirName)) {
                        Iterator<FtpDirEntry> iteratorFile = ftp.listFiles("/" +firstDirName+ "/" + dirName);
                        while (iteratorFile.hasNext()) {
                            FtpDirEntry fileEntry = (FtpDirEntry) iteratorFile.next();
                            String fileName = fileEntry.getName();
                            if (fileName.endsWith(".TXT")) {
                                list70.add(fileName);
                            }
                        }
                        map.put(firstDirName+"_70", list70);
                    } else if ("73".equals(dirName)) {
                        Iterator<FtpDirEntry> iteratorFile = ftp.listFiles("/"+firstDirName + "/" + dirName);
                        while (iteratorFile.hasNext()) {
                            FtpDirEntry fileEntry = (FtpDirEntry) iteratorFile.next();
                            String fileName = fileEntry.getName();
                            if (fileName.endsWith(".TXT")) {
                                list73.add(fileName);
                            }
                        }
                        map.put(firstDirName+"_73", list73);
                    }
                }
            }
            list.add(map);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }


    public static List<String> readFileContent(String ftpFile, FtpClient ftp) throws Exception {
        List<String> list = new ArrayList<String>();
        String str = "";
        InputStream is = null;
        BufferedReader br = null;
        try {
            // 获取ftp上的文件
            is = ftp.getFileStream(ftpFile);
            //转为字节流,在windows测试,解决中文乱码设置为GBK
            br = new BufferedReader(new InputStreamReader(is, "GBK"));
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
            br.close();
        } catch (FtpProtocolException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return list;
    }




}
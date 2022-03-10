package com.celal258.cagoz.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;

public class FileFtp {
    FTPClient client;
    private String ftpServerName = "37.148.210.241";
    private String userName = "kizilapp";
    private String password = "253461Ka";
    public FileFtp() {
        client = new FTPClient();
    }

    /*public void Upload(String name,String path) {
        FileInputStream fis = null;
        try {
            client.connect(ftpServerName,21);
            client.login(userName, password);
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);
            fis = new FileInputStream(new File(path));
            String[] directory=name.split("/");
            String pathDirectory="";
            for(int i =0;i<directory.length-1;i++){

                pathDirectory+=directory[i];
                client.makeDirectory(pathDirectory);
                pathDirectory+="/";
            }

            boolean done = client.storeFile(name, fis);
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }
            client.logout();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }*/
    public void upload(ArrayList<String> name, ArrayList<String> path) {
        FileInputStream fis = null;
        try {
            System.out.println("Bağlanıyor....");
            client.connect(ftpServerName,21);
            client.login(userName, password);
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);
            System.out.println("Bağlandı.");
            for(int j=0;j<name.size();j++){
                fis = new FileInputStream(new File(path.get(j)));
                String[] directory=name.get(j).split("/");
                String pathDirectory="";
                for(int i =0;i<directory.length-1;i++){
                    pathDirectory+=directory[i];
                    client.makeDirectory(pathDirectory);
                    pathDirectory+="/";
                }

                boolean done = client.storeFile(name.get(j), fis);
                if (done) {
                    System.out.println("The first file is uploaded successfully.");
                }
            }

            client.logout();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void Download(String remoteFilePath, String localFilePath)
    {
        FileOutputStream fos =null;
        try {
            client.connect(ftpServerName,21);
            client.login(userName, password);
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);
            fos = new FileOutputStream(localFilePath);

            client.retrieveFile(remoteFilePath,fos);
            client.logout();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void Delete(String path)
    {
        try {
            client.connect(ftpServerName);
            client.login(userName, password);
            boolean exist = client.deleteFile(path);
            if (exist) {
                System.out.println("File '"+ path + "' deleted...");
            }
            else
                System.out.println("File '"+ path + "' doesn't exist...");
            client.logout();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*public void download(String name, String path){
        FileInputStream fis = null;
        try {
            client.connect(ftpServerName,21);
            client.login(userName, password);
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);
            for(int j=0;j<name.size();j++){
                fis = new FileInputStream(new File(path.get(j)));
                String[] directory=name.get(j).split("/");
                String pathDirectory="";
                for(int i =0;i<directory.length-1;i++){

                    pathDirectory+=directory[i];
                    client.makeDirectory(pathDirectory);
                    pathDirectory+="/";
                }

                boolean done = client.retrieveFile(name.get(j), fis);
                if (done) {
                    System.out.println("The first file is uploaded successfully.");
                }
            }

            client.logout();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}

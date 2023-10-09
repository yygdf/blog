package com.iksling.blog.util;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class FtpUtil {
    private static String hostname;

    private static int port;

    private static String address;

    private static String username;

    private static String password;

    @Value("${ftp.hostname:null}")
    public void setHostname(String hostname) {
        FtpUtil.hostname = hostname;
    }

    @Value("${ftp.address:null}")
    public void setAddress(String address) {
        FtpUtil.address = address;
    }

    @Value("${ftp.port:21}")
    public void setPort(int port) {
        FtpUtil.port = port;
    }

    @Value("${ftp.username:null}")
    public void setUsername(String username) {
        FtpUtil.username = username;
    }

    @Value("${ftp.password:null}")
    public void setPassword(String password) {
        FtpUtil.password = password;
    }

    public static String upload(MultipartFile file, String targetAddr, String fullFileName) {
        return uploadFile(file, targetAddr, fullFileName);
    }

    public static void rename(String path, String pathNew) {
        renameFile(path, pathNew);
    }

    public static boolean delete(String url) {
        return deleteFile(url);
    }

    private static String uploadFile(MultipartFile file, String targetAddr, String fullFileName) {
        FTPClient ftpClient = null;
        try {
            ftpClient = getFTPClient();
            String[] paths = targetAddr.split("/");
            for (String path : paths) {
                if (!ftpClient.changeWorkingDirectory(path)) {
                    ftpClient.makeDirectory(path);
                    ftpClient.changeWorkingDirectory(path);
                }
            }
            ftpClient.storeFile(fullFileName, file.getInputStream());
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ftpClient != null) {
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return address + targetAddr + "/" + fullFileName;
    }

    private static boolean deleteFile(String url) {
        FTPClient ftpClient = null;
        try {
            ftpClient = getFTPClient();
            String[] paths = url.split("/");
            int lastIndex = paths.length - 1;
            for (int i = 4; i < lastIndex; i++)
                ftpClient.changeWorkingDirectory(paths[i]);
            boolean flag;
            if (ftpClient.changeWorkingDirectory(paths[lastIndex])) {
                deleteDirectory(ftpClient);
                flag = ftpClient.removeDirectory(paths[lastIndex]);
            } else
                flag = ftpClient.deleteFile(paths[lastIndex]);
            ftpClient.logout();
            return flag;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ftpClient != null) {
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void renameFile(String path, String pathNew) {
        FTPClient ftpClient = null;
        try {
            ftpClient = getFTPClient();
            ftpClient.rename(path, pathNew);
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient != null) {
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void deleteDirectory(FTPClient ftpClient) throws IOException {
        String[] names = ftpClient.listNames();
        for (String name : names) {
            if (ftpClient.changeWorkingDirectory(name))
                deleteDirectory(ftpClient);
            ftpClient.deleteFile(name);
        }
        ftpClient.changeToParentDirectory();
    }

    private static FTPClient getFTPClient() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(hostname, port);
        ftpClient.login(username, password);
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        return ftpClient;
    }
}

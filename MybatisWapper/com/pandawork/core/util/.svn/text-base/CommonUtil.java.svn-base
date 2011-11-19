package com.pandawork.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.commons.fileupload.util.Streams;

import com.pandawork.core.log.LogClerk;

/**
 * 公用助手类
 * 
 */
public class CommonUtil {

    /**
     * 计算字符串长度
     * 
     * @param s
     * @return
     */
    public static int strlen(String s) {
        if (s == null) return 0;
        int len = 0;
        s = s.trim();
        for (int i = 0; i < s.length(); i++) {
            if (s.codePointAt(i) > 255) {
                len += 2;
            } else {
                ++len;
            }
        }
        return len;
    }

    /**
     * 对一个byte数组计算md5值
     * 
     * @param b
     * @return
     */
    public static String md5(byte b[]) {
        int i;
        StringBuffer buf = new StringBuffer("");
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if (i < 0) i += 256;
            if (i < 16) buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }

    /**
     * 对一个字符串计算md5值
     * 
     * @param b
     * @return
     */
    public static String md5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            return md5(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对一个文件计算md5值
     * 
     * @param b
     * @return
     */
    public static String md5(File file) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            byte[] b = new byte[8192];
            int length = -1;
            MessageDigest md = MessageDigest.getInstance("MD5");
            while ((length = fin.read(b)) != -1) {
                md.update(b, 0, length);
            }
            return md5(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos).toLowerCase();
    }

    /**
     * 拷贝文件
     * 
     * @param src
     * @param target
     * @return
     */
    public static boolean copyFile(File src, File target) {
        try {
            Streams.copy(new FileInputStream(src), new FileOutputStream(target),
                    true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogClerk.errLog.error(e);
            return false;
        }
    }

    private static Random rn = new Random();

    private static int rand(int lo, int hi) {
        int n = hi - lo + 1;
        int i = rn.nextInt() % n;
        if (i < 0) i = -i;
        return lo + i;
    }

    public static String randomstring(int lo, int hi) {
        int len = rand(lo, hi);
        byte b[] = new byte[len];
        for (int i = 0; i < len; i++) {
            int mux = rn.nextInt() % 3;
            if (mux < 0) mux = -mux;
            switch (mux) {
                case 0:
                    b[i] = (byte) rand('a', 'z');
                    break;
                case 1:
                    b[i] = (byte) rand('A', 'Z');
                    break;
                case 2:
                    b[i] = (byte) rand('0', '9');
                    break;
            }
        }
        return new String(b, 0, len);
    }

    private static char ch[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
            'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1' };

    public static String getPasswordString(int length) {
        if (length > 0) {
            char[] result = new char[length];
            int index = 0, rand = rn.nextInt();
            for (int i = 0; i < length % 5; i++) {
                result[index++] = ch[(byte) rand & 63];
                rand >>= 6;
            }
            for (int i = length / 5; i > 0; i--) {
                rand = rn.nextInt();
                for (int j = 0; j < 5; j++) {
                    result[index++] = ch[(byte) rand & 63];
                    rand >>= 6;
                }
            }
            return new String(result, 0, length);
        } else if (length == 0) {
            return "";
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String getGetMethod(String field) {
        String fir = field.substring(0, 1);
        return "get" + fir.toUpperCase() + field.substring(1);
    }
    
   public static void main(String[] args){
       File src = new File("D:/TDDOWNLOAD/99_FireWorkflow工作流原理、设计与应用.pdf");
       File target = new File("D:/TDDOWNLOAD/videoplayback.flv0001");
       
       CommonUtil.copyFile(src, target);
   } 
   

}

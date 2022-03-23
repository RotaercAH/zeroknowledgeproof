package org.fisco.bcos.asset.crypto.ecdsasign;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 16进制字符串与byte数组转换
 * @author Administrator
 *
 */
public final class HexUtil {
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public HexUtil() {
    }

    /**
     * byte数组转16进制字符串
     * @param bytes
     * @return
     */
    public static String encodeHexString(byte[] bytes) {
        int nBytes = bytes.length;
        char[] result = new char[2 * nBytes];
        int j = 0;
        byte[] var4 = bytes;
        int var5 = bytes.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            byte aByte = var4[var6];
            result[j++] = HEX[(240 & aByte) >>> 4];
            result[j++] = HEX[15 & aByte];
        }

        return new String(result);
    }

    /**
     * 16进制字符串转byte数组
     * @param s 字符串
     * @return
     */
    public static byte[] decode(CharSequence s) {
        int nChars = s.length();
        if (nChars % 2 != 0) {
            throw new IllegalArgumentException("Hex-encoded string must have an even number of characters");
        } else {
            byte[] result = new byte[nChars / 2];

            for(int i = 0; i < nChars; i += 2) {
                int msb = Character.digit(s.charAt(i), 16);
                int lsb = Character.digit(s.charAt(i + 1), 16);
                if (msb < 0 || lsb < 0) {
                    throw new IllegalArgumentException("Detected a Non-hex character at " + (i + 1) + " or " + (i + 2) + " position");
                }

                result[i / 2] = (byte)(msb << 4 | lsb);
            }

            return result;
        }
    }

    public static List<byte[]> bytetobyte32list(byte[] in){
        int x = in.length/32;
        int y = in.length%32;
        List<byte[]> out = new ArrayList<>();
        ByteBuffer bb = ByteBuffer.wrap(in);
        for(int i=0; i<x; i++){
            byte[] temp = new byte[32];
            bb.get(temp, 0, temp.length);
            out.add(temp);
        }
        byte[] other = new byte[32];
        bb.get(other, 0, y);
        out.add(other);
        return out;
    }
    public static byte[] byte32listtobyte(List<byte[]> in) {
        int leng = 0;
        for (int i = 31; i >=0; i--) {
            if (in.get(in.size() - 1)[i] != 0) {
                leng = i + 1;
                break;
            }
        }
        byte[] other = new byte[leng];
        ByteBuffer b = ByteBuffer.wrap(in.get(in.size() - 1));
        b.get(other, 0, leng);
        if (in.size() == 1) {
            return other;
        } else if (in.size() == 2) {
            return byteMerger(in.get(0), other);
        } else {
            byte[] out = byteMerger(in.get(0), in.get(1));
            for (int i = 3; i < in.size(); i++) {
                out = byteMerger(out, in.get(i));
            }
            out = byteMerger(out, other);
            return out;
        }
    }

    public static byte intToByte(int num){
        return (byte) (num&0xff);
    }

    public static byte[] intToByte(int[] num){
        byte[] result = new byte[32];
        for(int i = 0;i<num.length;i++){
            result[i] = (byte) (num[i]&0xff);
        }
        return result;
    }

    public static int[] byteToInt(byte[] bytes){
        int[] result = new int[32];
        for(int i= 0; i<bytes.length;i++){
            result[i] = bytes[i]&0xff;
        }
        return result;
    }

    public static byte[] tobyte32(byte[] in){
        byte[] result = new  byte[32];
        System.arraycopy(in, 0, result, 32-in.length, in.length);
        return result;
    }

    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
            byte[] bt3 = new byte[bt1.length+bt2.length];
            System.arraycopy(bt1, 0, bt3, 0, bt1.length);
            System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
            return bt3;
        }
    public static byte[] byteCombine(List<byte[]> list) {
        if (list.size() == 1) {
            return list.get(0);
        } else {
            byte[] result = byteMerger(list.get(0), list.get(1));
            for (int i = 2; i < list.size(); i++) {
                result = byteMerger(result, list.get(i));
            }
            return result;
        }
    }
}
package com.tqmall.data.monk.common.utils;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ClipboardDataUtil {
    /**
     * test class
     *
     * @param args
     */
    public static void main(String[] args) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();//获取系统剪贴板
        try {

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 獲取剪切板中圖片的base64字符串
     *
     *
     */
    public static String getBase64StrFromClipBoard() {
//        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();//获取系统剪贴板
        String str = null;
        try {
            BufferedImage image = getImageFromClipboard();
            if(image == null){
                return "";
            }
            str = Object2Base64(image);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            e.getMessage();
        }
        return str;
    }

    /**
     * 从指定的剪切板中获取文本内容
     * 本地剪切板使用 Clipborad cp = new Clipboard("clip1"); 来构造
     * 系统剪切板使用  Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
     * 剪切板的内容   getContents(null); 返回Transferable
     */
    protected static String getClipboardText() throws Exception{
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();//获取系统剪贴板
        // 获取剪切板中的内容
        Transferable clipT = clip.getContents(null);
        if (clipT != null) {
            // 检查内容是否是文本类型
            if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor))
                return (String)clipT.getTransferData(DataFlavor.stringFlavor);
        }
        return null;
    }

    //往剪切板写文本数据
    protected static void setClipboardText(Clipboard clip, String writeMe) {
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

    // 从剪切板读取图像
    public static BufferedImage getImageFromClipboard() throws Exception{
        Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable cc = sysc.getContents(null);
        if (cc == null)
            return null;
        else if(cc.isDataFlavorSupported(DataFlavor.imageFlavor))
            return (BufferedImage)cc.getTransferData(DataFlavor.imageFlavor);
        return null;
    }
    // 写图像到剪切板
    protected static void setClipboardImage2(final Image image) {
        Transferable trans = new Transferable(){

            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] { DataFlavor.imageFlavor };
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }

            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if(isDataFlavorSupported(flavor))
                    return image;
                throw new UnsupportedFlavorException(flavor);
            }
        };
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
    }
    // 判断剪切板是否有数据
    public static boolean getClipboardInfo(){
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();//获取系统剪贴板
        // 获取剪切板中的内容
        Transferable clipT = clip.getContents(null);
        if (clipT != null) {
            return true;
        }
        return false;
    }
    // object to Base64 Code
    public static String Object2Base64(BufferedImage image) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", bo);
            bytes = bo.toByteArray();
            bo.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String encodeStr = encoder.encode(bytes);
        return encodeStr;
    }
}
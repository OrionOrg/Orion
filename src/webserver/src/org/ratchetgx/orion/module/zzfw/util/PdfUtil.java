package org.ratchetgx.orion.module.zzfw.util;

import java.awt.Image;  
import java.awt.Rectangle;  
import java.awt.image.BufferedImage;  
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FilenameFilter;  
import java.io.FileOutputStream;  
import java.io.IOException;
import java.io.InputStream;  
import java.io.RandomAccessFile;  
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;  
import java.nio.channels.FileChannel;  
import java.util.Arrays;  
import java.util.Comparator; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;    
import com.sun.image.codec.jpeg.JPEGCodec;    
import com.sun.image.codec.jpeg.JPEGImageEncoder;  
import com.sun.pdfview.PDFFile;  
import com.sun.pdfview.PDFPage;  

@Component 
public class PdfUtil{ 	
	//	日志
    private Logger log = LoggerFactory.getLogger(this.getClass());     
    
	/**
	 * 通过Url 读取pdf文件，并保存至本地中
	 * @param urlstr
	 * @param position
	 */
	public void getPdfByUrl(String urlstr,PdfPosition position){
		try {
			 URL url = new URL(urlstr);
			 URLConnection  conn = (URLConnection) url.openConnection();
			 
		     conn.setDoInput(true);  
		     conn.setDoOutput(true); 		     
		     conn.connect();// 握手 		    
		     
		     File dir = new File(position.getPdfPath());
			 if (!dir.exists()){
				dir.mkdirs();
			 }
			 
			 File f = new File(position.getPdfPath() + File.separator + position.getFileName() + ".pdf");

			 FileOutputStream fos = new FileOutputStream(f);
			 DataOutputStream out = new DataOutputStream(fos);
			 DataInputStream in = new DataInputStream(conn.getInputStream());
			 byte[] buffer = new byte[4096];
			 int count = 0;
			 while ((count = in.read(buffer)) > 0){
				out.write(buffer, 0, count);
			 }
			
			 out.close();
			 out.flush();
			 in.close();			
		}catch (MalformedURLException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		} 
		
        pdf2Images(position);
	} 
      
    /** 
     * PDF文档读取.  
     * @param filePath -- 待读取PDF文件的路径. 
     * @return null 或者 PDFFile instance. 
     */  
    private PDFFile getPdfFile(String filePath){  
        try {  
            //load a pdf file from byte buffer.   
            File file = new File(filePath);  
            RandomAccessFile raf = new RandomAccessFile(file, "r");  
            FileChannel channel = raf.getChannel();  
            ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,  
                    channel.size());  
            PDFFile pdfFile = new PDFFile(buf);  
  
            return pdfFile;  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
        return null;  
    }
    
    /**
     * 获取图片名称列表
     * @param position
     * @return
     */
    public String[] getImageNames(PdfPosition position){
	  	  //将转换后图片存放于path路径下     
	      String path = position.getImagePath()+  File.separator;  
	      File filePath = new File(path);  
	  	  //取得当前文件夹下的所有jpg格式的文件名.   
	      String[] imageNames  = filePath.list(new ImageFilter());  
	      log.debug("imageNames:"+imageNames);
	      if(imageNames!=null){
	    	  if(imageNames.length>0){
	    		  //对文件名排序.  
	    		  Arrays.sort(imageNames,new FileNameComparator()); 
	    	  }
	      }	     
	      
	      return imageNames;    	
    }

    /**
     * 删除指定文件夹下所有文件    
     * @param path文件夹完整绝对路径
     * @return
     */
     public static void delFolder(String folderPath) {
         try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
         } catch (Exception e) {
           e.printStackTrace(); 
         }
     }

    /**
     * 删除指定文件夹下所有文件    
     * @param path文件夹完整绝对路径
     * @return
     */
    public static boolean delAllFile(String path) {
       boolean flag = false;
       File file = new File(path);
       if (!file.exists()) {
    	   return flag;
       }
       if (!file.isDirectory()) {
         return flag;
       }
       String[] tempList = file.list();
       File temp = null;
       for (int i = 0; i < tempList.length; i++) {
          if (path.endsWith(File.separator)) {
             temp = new File(path + tempList[i]);
          } else {
              temp = new File(path + File.separator + tempList[i]);
          }
          if (temp.isFile()) {
             temp.delete();
          }
          if (temp.isDirectory()) {
             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
             delFolder(path + "/" + tempList[i]);//再删除空文件夹
             flag = true;
          }
       }
       return flag;
   }

      
    /** 
     * PDF文档按页转换为图片. 
     * @param pdfFile -- PDFFile instance 
     * @param imageSavePath -- 图片保存路径. 
     * @param fileName -- 保存图片文件夹名称. 
     */   
    private boolean pdf2Images(PdfPosition position){  
    	
    	PDFFile pdfFile = getPdfFile(position.getPdfPath()+ File.separator + position.getFileName() + ".pdf");  
    	
    	if(pdfFile == null ) { //待转换文档不存在，返回false.   
            return false;  
        }  
          
        //将转换后图片存放于path路径下     
        String path = position.getImagePath();  
        
        File filePath = new File(path);  
        if(filePath.exists()){ //判断以文件名命名的文件夹是否存在.   
        	delFolder(path);  
        }         
        filePath.mkdirs();
        
        log.debug("sfnie:wadasdas");
        
        String imPath = position.getImagePath()+ File.separator;  
        File imageFilePath = new File(imPath);  
        //取得当前文件夹下的所有jpg格式的文件名.   
        String[] imageNames = imageFilePath.list(new ImageFilter());       
        log.debug("sfnie:测试证明");
        
        //log.debug("sfnie:测试证明"+imageNames.length);
        //if(imageNames.length == 0) {  //当前文件夹下没有文件.   
        	  log.debug("sfnie:测试证明2222");
            //将pdf文档按页转为图片.   
            String imagePath = "";  
            try {  
                //对转换页数进行限制,最多只转换前maxPage页.   
                int pages = pdfFile.getNumPages();  
                  
                for (int i = 1; i <= pages; i++) {  
                    // draw the page to an image   
                    PDFPage page = pdfFile.getPage(i);  
                    // get the width and height for the doc at the default zoom   
                    Rectangle rect = new Rectangle(0,   
                                                   0,   
                                                   (int) page.getBBox().getWidth(),   
                                                   (int) page.getBBox().getHeight());  
                    // generate the image   
                    Image img = page.getImage(rect.width, rect.height, // width & height   
                                              rect, // clip rect   
                                              null, // null for the ImageObserver   
                                              true, // fill background with white   
                                              true // block until drawing is done   
                                              );  
                                                  
                    BufferedImage tag = new BufferedImage(rect.width,   
                                                          rect.height,  
                                                          BufferedImage.TYPE_INT_RGB);  
                      
                    tag.getGraphics().drawImage(img,   
                                                0,  
                                                0,  
                                                rect.width,  
                                                rect.height,  
                                                null);  
                      
                      
                    imagePath = imPath + i + ".jpg";  
                    FileOutputStream out = new FileOutputStream(imagePath);  // 输出到文件流.   
                    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
                    encoder.encode(tag);        // JPEG编码.   
                    out.close();  
                }                 
            }catch (Exception ex) {  
                ex.printStackTrace();  
                return false;  
            }  
        //}  
        
        return true;  
    }  
         
      
    //图片jpg过滤器类   
    class ImageFilter implements FilenameFilter {  
         public boolean isImageFile(String fileName){  
              if(fileName.toLowerCase().endsWith("jpg")) {  
                  return true;  
              }else {  
                  return false;  
              }         
         }  
           
         public ImageFilter() {}  
           
         public boolean accept(File dir,String name){  
             return isImageFile(name);  
          }  
    }  
      
    //文件名称比较类   
  
    class FileNameComparator implements Comparator {  
        public final int compare(Object first, Object second) {  
           String[] fir = ((String)first).split("\\.");  
           String[] sec = ((String)second).split("\\.");  
             
           int firstPage = Integer.parseInt(fir[0]);  
           int secondPage = Integer.parseInt(sec[0]);  
           int diff = firstPage - secondPage;  
           if (diff > 0)  
            return 1;  
           if (diff < 0)  
            return -1;  
           else  
            return 0;  
        }  
    }  
}  

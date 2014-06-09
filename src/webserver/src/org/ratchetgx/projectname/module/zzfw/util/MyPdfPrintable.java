package org.ratchetgx.projectname.module.zzfw.util;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.Toolkit;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.text.AttributedString;

import javax.swing.JApplet;

import com.lowagie.text.Rectangle;
import com.sun.pdfview.PDFPage;



public class MyPdfPrintable  implements Printable{
   /** *//**
   * @param Graphic指明打印的图形环境
   * @param PageFormat指明打印页格式（页面大小以点为计量单位，1点为1英才的1/72，1英寸为25.4毫米。A4纸大致为595×842点）
   * @param pageIndex指明页号
   **/
   public  PDFPage page =null;  
   
   public int print(Graphics gra, PageFormat pf, int pageIndex) throws PrinterException {
       System.out.println("pageIndex="+pageIndex);
       Component c = null;
      //转换成Graphics2D
      Graphics2D g2 = (Graphics2D) gra;
      //设置打印颜色为黑色
      g2.setColor(Color.black);

      //打印起点坐标
      double x = pf.getImageableX();
      double y = pf.getImageableY();
       
      switch(pageIndex){
         case 0:
           Image src = Toolkit.getDefaultToolkit().getImage("F:/ZZFW/08011183/xjzm-yw/images/1.jpg");
           g2.drawImage(src,(int)x,(int)y,c);
           int img_Height=src.getHeight(c);
           int img_width=src.getWidth(c);
          
           g2.drawImage(src,(int)x,(int)(y+img_Height+11),c);
           
         return PAGE_EXISTS;
         default:
         return NO_SUCH_PAGE;
      }
      
   }
   
   public PDFPage getPdf(){	   
	   return this.page;
   }
   
   public void setPdf(PDFPage pf){	   
	   this.page =pf;
   }
}
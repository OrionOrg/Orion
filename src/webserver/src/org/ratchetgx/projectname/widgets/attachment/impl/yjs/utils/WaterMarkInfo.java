package org.ratchetgx.projectname.widgets.attachment.impl.yjs.utils;

public class WaterMarkInfo {
	//待添加水印文档
	private String dirPath = "";
	//添加水印图片
	private String imgPath = "";
    //添加水印的x位置
	private int xPosition = 0;
	//添加水印的y位置
	private int yPosition = 0;
	//添加图片水印的透明度
	private String transparency = "1";
	//添加水印起始页
	private int startPage=-1;
	//添加水印结束页
	private int endPage=-1;
	
	public String getDirPath() {
		return dirPath;
	}
	
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}
	
	public String getImgPath() {
		return imgPath;
	}
	
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	public int getXPosition() {
		return xPosition;
	}
	
	public void setXPosition(int position) {
		xPosition = position;
	}
	
	public int getYPosition() {
		return yPosition;
	}
	
	public void setYPosition(int position) {
		yPosition = position;
	}

	public String getTransparency() {
		return transparency;
	}

	public void setTransparency(String transparency) {
		this.transparency = transparency;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	 
}

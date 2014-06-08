package org.ratchetgx.orion.module.zzfw.util;

public class PdfPosition{
	//pdf目录路径
	private String pdfPath = "";
	// 文件名称
	private String fileName = "";
	
	//生成图片目录路径
	private String imagePath = "";

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getPdfPath() {
		return pdfPath;
	}

	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}
}

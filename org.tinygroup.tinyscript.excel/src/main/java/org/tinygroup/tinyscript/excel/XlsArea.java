package org.tinygroup.tinyscript.excel;

/**
 * XLS格式描述
 * @author yancheng11334
 *
 */
public class XlsArea {

	private int dataX; //数据(包括标题)开始X坐标。
	private int dataY; //数据(包括标题)开始Y坐标。
	private int width; //数据列数
	private int height;//数据行数
	private boolean printTilte; //是否打印标题头

	public int getDataX() {
		return dataX;
	}

	public void setDataX(int dataX) {
		this.dataX = dataX;
	}

	public int getDataY() {
		return dataY;
	}

	public void setDataY(int dataY) {
		this.dataY = dataY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isPrintTilte() {
		return printTilte;
	}

	public void setPrintTilte(boolean printTilte) {
		this.printTilte = printTilte;
	}
	
}

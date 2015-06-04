package com.bytesw.consultadecuentas.view.beans;

public class ItemBean {

	private int icon;
	private String title;
	private String detail;
	private boolean stateIcon;

	public ItemBean() {
		super();
	}

	public ItemBean(int icon, String title, String detail, boolean statusIcon) {
		super();
		this.icon = icon;
		this.title = title;
		this.detail = detail;
		this.stateIcon = statusIcon;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public boolean getStateIcon() {
		return stateIcon;
	}

	public void setStateIcon(boolean stateIcon) {
		this.stateIcon = stateIcon;
	}

}

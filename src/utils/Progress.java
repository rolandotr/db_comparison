package utils;

import java.io.Serializable;

public final class Progress implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4592743677692574642L;
	
	
	
	private long total;
	private long progress;
	
	public Progress(long total){
		this.total = total;
		progress = 0;
	}
	
	public void addProgress(){
		progress++;
		if (progress > total) throw new RuntimeException("progress = "+progress+" is greater than total =" + total);
	}
	
	public void addProgress(long progress){
		this.progress += progress;
		if (progress > total) throw new RuntimeException("progress = "+progress+" is greater than total =" + total);
	}
	
	public void printProgress(){
		System.out.println("Progress = " + ((double)progress/total)*100+" %");
	}
	
	public void setTotal(long total){
		this.total = total;
	}
}

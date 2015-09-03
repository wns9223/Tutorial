package edu.sfsu.cs.orange.ocr.intent.adt;

import java.io.Serializable;

public class ProductIntent implements Serializable{
	private String Url;
	private String name;
	private String company;
	
	//Set
	public void setUrl(String url){
		this.Url = url;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setCompany(String company){
		this.company = company;
	}
	
	
	
	//Get
	public String getUrl(){
		return Url;
	}
	public String getName(){
		return name;
	}
	public String getCompany(){
		return company;
	}

}

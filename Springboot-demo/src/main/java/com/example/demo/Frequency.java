package com.example.demo;

public enum Frequency {
		
	WEEKDAYS(1,"Weekdays"),DAILY(2,"Daily"),WEEKLY(3,"Weekly"),MONTHLY(4,"Monthly"),ONCE(0,"Once");
	
	public static int getFrequencyType(String typeName)
	{
		int freqType = 0;
		if(typeName != null)
		{
		for(Frequency freqElement: values())
		{
			if(freqElement.getTypeName().equals(typeName))
			{
				freqType = freqElement.getType();
			}
		}
		}
		return freqType;
	}
	
	public static String getFreqenceTypeName(int type)
	{
		String freqType = "Once";

		for(Frequency freqElement: values())
		{
			if(freqElement.getType() == type)
			{
				freqType = freqElement.getTypeName();
			}
			 
		}
		return freqType;
	}
	
	Frequency(int type, String typeName) 
	{
		this.type = type;
		this.typeName = typeName;
	}
	
	int type;
	String typeName;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}

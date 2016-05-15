package com.wora.test;

public class PatternTest {
	
	public static void main(String[] args) {
		
		String pattern = "if contains %alican%";
		String text = "alican akkus";
		
		
		String patternSeq[] = pattern.split(" ");
		
		for(int i=0; i< patternSeq.length; i++){
			String ptr = patternSeq[i];
			
			if(ptr.equalsIgnoreCase("if")){
				String secondPtr = patternSeq[i+1];
				if(secondPtr.equalsIgnoreCase("equals")){
					String thirdPtr = patternSeq[i+2];
					if(thirdPtr.startsWith("%") || thirdPtr.endsWith("%")){
						if(text.equalsIgnoreCase(thirdPtr.substring(1, thirdPtr.length()-1))){
							System.out.println("Equalssss");
						}else{
							System.out.println("Not Equalsss");
						}
					}
				}else if(secondPtr.equals("contains")){
					String thirdPtr = patternSeq[i+2];
					if(thirdPtr.startsWith("%") || thirdPtr.endsWith("%")){
						if(text.contains(thirdPtr.substring(1, thirdPtr.length()-1))){
							System.out.println("Containss");
						}else{
							System.out.println("Not Containss");
						}
					}
				}
				
			}
		}
		
	}

}

package genreal;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import startup.*;

public class EmployeeReader {

	public String line;
	public double linedouble;
	public int Stage;
	public File file = new File("src\\info\\emloyees.txt");
	public Data data;
	public int Test;
	
	public ArrayList<String> FirstName = new ArrayList<String>();
	public ArrayList<String> SecondName = new ArrayList<String>();
	public ArrayList<String> GCSE = new ArrayList<String>();
	public ArrayList<String> ALevel = new ArrayList<String>();
	public ArrayList<String> UniversitySubject = new ArrayList<String>();
	public ArrayList<Boolean> UniversityPassed = new ArrayList<Boolean>();
	public ArrayList<String> UniversityGradeTemp = new ArrayList<String>();
	public ArrayList<Boolean> PHD = new ArrayList<Boolean>();
	public ArrayList<String> IntelligenceTemp = new ArrayList<String>();
	public ArrayList<String> UniversityGrade = new ArrayList<String>();
	public ArrayList<Integer> Intelligence = new ArrayList<Integer>();
	public ArrayList<String> Questions = new ArrayList<String>();
	public ArrayList<String> Answers = new ArrayList<String>();
	public ArrayList<String> Overall = new ArrayList<String>();
	
	public ArrayList<Integer> FinalEmployeesTemp = new ArrayList<Integer>();
	public ArrayList<Integer> FinalEmployees = new ArrayList<Integer>();
	
	public Boolean finished = false;
	public BufferedReader reader;
	
	public EmployeeReader(Data parent)
	{
		
		data = parent;
		
		reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("emloyees.txt")));
		
		reademyloyeelist();
		
	}
	
	public void pickemployees(int min, int max)
	{
		
		FinalEmployeesTemp.clear();
		FinalEmployees.clear();
		
		System.out.println(min + " " + max);
		
		for(int read=0;read<Intelligence.size();read++)
		{
			if(Intelligence.get(read) <= max*10 && Intelligence.get(read) >= min*10)
			{
				
				FinalEmployeesTemp.add(read);
				
			}
		}
		
		if(FinalEmployeesTemp.size() < 5)
		{
			
			for(int fill=0;fill<FinalEmployeesTemp.size();fill++)
			{
				
				System.out.println((int)(Math.random() * FinalEmployeesTemp.size()));
				FinalEmployees.add(FinalEmployeesTemp.get(fill));
				
			}
			
		}
		int random;
		int item = 0;
		while(FinalEmployees.size() < 5 && FinalEmployeesTemp.size() > 5)
		{
			
			random = (int) (Math.random()*5);
			if(random == 1)
			{
				
				FinalEmployees.add(item);
				
			}
			
			item += 1;
			
			if(item > FinalEmployeesTemp.size())
			{
				
				item = 0;
				
			}
		}
		
		System.out.println(FinalEmployeesTemp);
		data.waituntilshow = (int) (100 + (Math.random()*150));
		data.hiringtocome = true;
		
	}
	
    //Reads using BufferedReader a list of all employees
	public void reademyloyeelist()
	{
			 Stage = 1;	
			
			 //Gets the next line
       	     try {
       		      line = reader.readLine();
       	 	     } catch (IOException e) {
       	 							      e.printStackTrace();
       	 							     }
       	     
					while(!finished)
					{
						 //Stage 9 already gets it in order to check if completed so Stage 1 will have been loaded
    	                 if(Stage != 1)
    	                 {
    	                	 //Gets the next line
    	                	 try {
    	                		 line = reader.readLine();
    	                	 	} catch (IOException e) {
    	                	 							e.printStackTrace();
    	                	 							}
    	                 }
						//Checks if its the last line
						if(line == null)
						{
								System.out.println("You may have entered details to employees.txt wrongly");
						        finished = true;
						}
						 
						//Switch clause to see which list it should add to 
						switch(Stage)
						{
						case(1):
						FirstName.add(line.toString());	 
						break;
						
						case(2):
						SecondName.add(line.toString());	 
						break;
						
						case(3):
						IntelligenceTemp.add(line.toString());	 
						break;
						
						case(4):
						Questions.add(line.toString());	 
						break;
						
						case(5):
						Questions.add(line.toString());	 
						break;
						
						case(6):
						Questions.add(line.toString());	 
						break;
						
						case(7):
						Answers.add(line.toString());	 
						break;
							
						case(8):
						Answers.add(line.toString());	 
						break;
							
						case(9):
						Answers.add(line.toString());	 
						break;
						
						case(10):
						Overall.add(line.toString());	 
						break;
						 
						}
						//Quick test to see if has reached a new employee
						if(Stage == 10)
						{
							Stage = 1;
							
							//Gets the next line
   	                        try {
   	                	    	 line = reader.readLine();
   	                	 		} catch (IOException e) {
   	                	 								  e.printStackTrace();
   	                	 							    }
							//Checks if its the last line
							if(line == null)
						   	{
						    		finished = true;
						   	}
						}
						else
						{
							Test += 1;
							Stage += 1;
						}
	
       }	
	   //Converts from string to there wanted integer and double form
	   for(int Done=0;IntelligenceTemp.size()>Done;Done++)
	   {
		   
		   Intelligence.add(Integer.parseInt(IntelligenceTemp.get(Done)));
		   createemployeegrades(Integer.parseInt(IntelligenceTemp.get(Done)));
		   
	   }
	   
	   for(int Done=0;UniversityGradeTemp.size()>Done;Done++)
	   {
		   
		   UniversityGrade.add((UniversityGradeTemp.get(Done)));
		   
	   }
	   
	}
	
	public void createemployeegrades(int intelligence)
	{	 
		if(intelligence <= 20)
		{
			GCSE.add("F");
		}else{
			if(intelligence <= 30)
			{
				GCSE.add("E");
			}else{
				if(intelligence <= 40)
				{
					GCSE.add("D");
				}else{
					if(intelligence <= 50)
					{
						GCSE.add("C");
					}else{
						if(intelligence <= 60)
						{
							GCSE.add("B");
						}else{
							if(intelligence <= 70)
							{
								GCSE.add("A");
							}else{
								if(intelligence <= 100)
								{
									GCSE.add("A*");
								}
							}
						}
					}
				}
			}
		}
		
		if(intelligence <= 25)
		{
			ALevel.add("F");
		}else{
			if(intelligence <= 35)
			{
				ALevel.add("E");
			}else{
				if(intelligence <= 45)
				{
					ALevel.add("D");
				}else{
					if(intelligence <= 55)
					{
						ALevel.add("C");
					}else{
						if(intelligence <= 65)
						{
							ALevel.add("B");
						}else{
							if(intelligence <= 75)
							{
								ALevel.add("A");
							}else{
								if(intelligence <= 100)
								{
									ALevel.add("A*");
								}
							}
						}
					}
				}
			}
		}
		
		if(intelligence <= 50)
		{
			UniversitySubject.add("NONE");	 
		}else{
			if(String.valueOf(intelligence).substring(0, 1) == "5" || String.valueOf(intelligence).substring(0, 1) == "7" || String.valueOf(intelligence).substring(0, 1) == "9")
			{
				UniversitySubject.add("Science");	 
			}else{
				UniversitySubject.add("Maths");	 
			}
		} 
		
	    if(intelligence >= 60)
		{
		       UniversityPassed.add(true);
		}
	    else
	    {
		       UniversityPassed.add(false);
		}
	    
	    if(intelligence < 60)
		{
	    	UniversityGradeTemp.add("Fail");
		}else{
			if(intelligence <= 70)
			{
				UniversityGradeTemp.add("Pass");
			}else{
				if(intelligence <= 75)
				{
					UniversityGradeTemp.add("3.0");
				}else{
					if(intelligence <= 80)
					{
						UniversityGradeTemp.add("2.2");
					}else{
						if(intelligence <= 85)
						{
							UniversityGradeTemp.add("2.1");
						}else{
							if(intelligence <= 90)
							{
								UniversityGradeTemp.add("1.0");
							}else{
								if(intelligence <= 100)
								{
									UniversityGradeTemp.add("PHD");
								}
						}
					}
				}
			}
		}} 
		
	}
}

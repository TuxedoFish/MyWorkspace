package gamehub;

import startup.Data;

public class Employee {

	Data data;
	
	public Employee(Data parent)
	{
		
		data = parent;
		
	}
	
	public void Update()
	{
		data.buffergraphics2D.drawImage(data.employee,0,0,null);
		if(data.buttons.testbutton(data.close2,data.mousexpos,data.mouseypos))
		{
			
			if(data.released)
			{
				
				data.buffergraphics2D.drawImage(data.closetouchedbutton,400,2,null);
				
			}else{
				
				data.buffergraphics2D.drawImage(data.closepressedbutton,400,2,null);
				
			}
			
		}else{
			data.buffergraphics2D.drawImage(data.closebutton,400,2,null);
			
		}
		data.string.DrawStringWhite(data.reader.FirstName.get(data.reader.FinalEmployees.get(0)), 290, 35,0);
		data.string.DrawStringWhite(data.reader.SecondName.get(data.reader.FinalEmployees.get(0)), 300, 60,0);
		data.string.DrawString("GCSE Grade :" + data.reader.GCSE.get(data.reader.FinalEmployees.get(0)), 250, 95,0);
		data.string.DrawString("ALevel Grade :" + data.reader.ALevel.get(data.reader.FinalEmployees.get(0)), 250, 130,0);
		data.string.DrawStringWhite("University", 290, 160,0);
		data.string.DrawString("Course :" + data.reader.UniversitySubject.get(data.reader.FinalEmployees.get(0)), 250, 200,0);
		data.string.DrawString("Passed :" + data.reader.UniversityPassed.get(data.reader.FinalEmployees.get(0)), 250, 230,0);
		data.string.DrawString("Grade :" + String.valueOf(data.reader.UniversityGrade.get(data.reader.FinalEmployees.get(0))), 250, 255,0);
		data.string.DrawStringWhite("Overview", 290, 285,0);	
		data.string.DrawString(data.reader.Overall.get(data.reader.FinalEmployees.get(0)), 240, 315,415);
		
	}
	
}

package gov.nysenate.opendirectory.utils;

import gov.nysenate.opendirectory.models.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;

public class FrontPagePeople {
	private Date lastUpdate;
	private ArrayList<Person> frontPagePeople;
	
	public FrontPagePeople(Request self) {
		setFrontPagePeople(self);
	}
	
	public ArrayList<Person> getFrontPagePeople(Request self) {
		Date currentDate = new Date();
		
		//if new initialization or if time of last update is >= 15 minutes then update people
		if(lastUpdate == null || 
				(lastUpdate.getHours() == currentDate.getHours() && currentDate.getMinutes()-lastUpdate.getMinutes() >= 15) ||
				(lastUpdate.getHours() != currentDate.getHours() && (lastUpdate.getMinutes()%15 + currentDate.getMinutes() >= 15))) {
			setFrontPagePeople(self);
		}
		
		return frontPagePeople;
	}
	
	private void setFrontPagePeople(Request self) {
		lastUpdate = new Date();
		List<Person> people = self.solrSession.loadPeopleBySortedQuery("otype:person AND frontPage:true", "modified", false);
		
		ArrayList<Person> updatedPeople = new ArrayList<Person>();
		
		for(Person person:people) {
			if(person.getPicture() != null && !person.getPicture().equals("")) {
				updatedPeople.add(person);
			}
			
			if(updatedPeople.size() > 20) {
				break;
			}
		}
		
		Random gen = new Random();
		
		while(updatedPeople.size() > 4) {
			updatedPeople.remove(gen.nextInt(updatedPeople.size()));
		}
		
		frontPagePeople = updatedPeople;
	}
	
	
}

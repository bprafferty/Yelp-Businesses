import realtimeweb.simplebusiness.SimpleBusiness;
import realtimeweb.simplebusiness.domain.Business;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Businesses {

    public static void fileWriter(LinkedList<Business>[] sorted, String[] types, String filename, double[] averages) {
		PrintWriter output = openOutput(filename);
		for (int i = 0; i < sorted.length; i++) {
			output.println("\nAbove Average Ratings for Business in Category: " + types[i] );
			output.println("\n\tBusiness Name  --  Business Rating  --  Business Location");
			for (int j = 0; j < sorted[i].size(); j++) {
				if (sorted[i].get(j).getRating() > averages[i]) {
					String location = sorted[i].get(j).getLocation();
					String name = sorted[i].get(j).getName();
					double rating = sorted[i].get(j).getRating();
					location = removeBrackets(location);
					
					output.println("\n\t" + name + "  --  " + rating + "  --  " + location);
				}
				
			}
			
		}
		output.close();
		
    }
    
    public static PrintWriter openOutput(String filename) {
    		PrintWriter openFile = null;
    		try {
    			File textFile = new File(filename);
    			openFile = new PrintWriter(textFile);
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    		return openFile;
    }
    
	public static LinkedList<Business>[] sortLinked(LinkedList<Business>[] linked){
    		for (int i = 0; i < linked.length; i++) {
    			Collections.sort((List<Business>)linked[i], new Comparator<Business>() {
    				public int compare (Business b1, Business b2) {
    					if (b1.getRating() > b2.getRating()) {
    						return 1;
    					}
    					else if (b1.getRating() < b2.getRating()) {
    						return -1;
    					}
    					else {
    						return 0;
    					}
    				}
    			});
    			Collections.reverse(linked[i]);
    		}
    		return linked;
    }
    
    public static double[] dataPrinter(LinkedList<Business>[] sorted, String[] types) {
    		double[] arrayAvg = new double[sorted.length];
    		for (int i = 0; i < sorted.length; i++) {
    			double total = 0;
    			System.out.println("\nBusiness Category: " + types[i] );
    			System.out.println("\n\tBusiness Name  --  Business Rating  --  Business Location");
    			for (int j = 0; j < sorted[i].size(); j++) {
    				String location = sorted[i].get(j).getLocation();
    				String name = sorted[i].get(j).getName();
    				double rating = sorted[i].get(j).getRating();
    				
    				location = removeBrackets(location);
    				total += rating;
    				System.out.println("\n\t" + name + "  --  " + rating + "  --  " + location);
    			}
    			arrayAvg[i] = total/sorted[i].size();
    			System.out.println("\nAverage rating for " + types[i] + ": " + arrayAvg[i]);
    		}
    		return arrayAvg;
    }
    
    public static String removeBrackets(String location) {
    		return location.replaceAll("[\\[\\]]", "");
    }
    
    public static LinkedList<Business> myList(List<Business> b){
    		LinkedList<Business> myLinkedList = new LinkedList<Business>();
    		myLinkedList.addAll(b);
    		return myLinkedList;
    }
    
    @SuppressWarnings("unchecked")
	public static void main(String[] args){
    		SimpleBusiness offYelp = new SimpleBusiness("cache.json");
    		
        String[] businessType = {"Mexican food", "Chinese food", "Italian food"};
		String[] businessLocation = {"Kansas City, MO", "Blacksburg, VA", "Atlanta, GA"}; 
		
        LinkedList<Business>[] linked = new LinkedList[businessType.length];
        
        for (int i = 0; i < businessType.length; i++) {
        		String type = businessType[i];
        		ArrayList<Business> businesses = new ArrayList<Business>();
        		for (int j = 0; j < businessLocation.length; j++) {
        			String city = businessLocation[j];
        			businesses.addAll(offYelp.search(type, city));
        		}
        		linked[i] = myList(businesses);
        }
        
        LinkedList<Business>[] sorted = sortLinked(linked);
        
        double[] averages = dataPrinter(sorted, businessType);
        
        String filename = "bus.csv";
        fileWriter(sorted, businessType, filename, averages);
        
    }
    
}
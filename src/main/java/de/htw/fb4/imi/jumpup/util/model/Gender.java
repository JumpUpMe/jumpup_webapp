package de.htw.fb4.imi.jumpup.util.model;

import java.util.LinkedHashMap;
import java.util.Map;

public enum Gender {

	MAN, WOMAN, LADYBOY;
	
	protected Map<String, Object> storedMap;
	
	/**
     * Get all genders
     * 
     * Should be used by JSF selectitems primarly.
     * 
     * @return {@link Map} of label - gender elements.
     */
    public Map<String, Object> getAllGenders()
    {
        if (null == this.storedMap) {
            this.fillGenders();
        }
        
        return storedMap;
    }
    
    private void fillGenders()
    {
        this.storedMap = new LinkedHashMap<String, Object>();
        
        // return a map of english label as key and the enumeration value itself as value
        this.storedMap.put("Man", MAN);
        this.storedMap.put("Woman", WOMAN);        
        this.storedMap.put("Ladyboy", LADYBOY);        
    }
}

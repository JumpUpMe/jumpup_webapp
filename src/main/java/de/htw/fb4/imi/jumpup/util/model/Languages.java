/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.util.model;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Languages enumeration</p>
 *
 * @since 12.2014
 *
 */
public enum Languages {
	GERMAN, ENGLISH; 
	
	protected Map<String, Languages> storedMap;
	
	/**
	 * Get all languages.
	 * 
	 * Should be used by JSF selectitems primarly.
	 * 
	 * @return {@link Map} of label - Language elements.
	 */
	public Map<String, Languages> getAllLanguages()
	{
	    if (null == this.storedMap) {
	        this.fillLanguages();
	    }
	    return storedMap;
	}
	
    private void fillLanguages()
    {
        this.storedMap = new HashMap<String, Languages>();
        
        // return a map of english label as key and the enumeration value itself as value
        this.storedMap.put("German", GERMAN);
        this.storedMap.put("English", ENGLISH);        
    }
	
}

/**
*******************************************************************************
* Copyright (C) 1996-2004, International Business Machines Corporation and    *
* others. All Rights Reserved.				                                  *
*******************************************************************************
*
* $Source: /xsrl/Nsvn/icu/icu4jni/src/classes/com/ibm/icu4jni/charset/CharsetProviderICU.java,v $ 
* $Date: 2004/12/29 01:12:53 $ 
* $Revision: 1.12 $
*
*******************************************************************************
*/ 

package com.ibm.icu4jni.charset;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.*;
import java.util.Iterator;
import com.ibm.icu4jni.converters.NativeConverter;

public final class CharsetProviderICU extends CharsetProvider{
    
    /**
     * Constructs a CharsetProviderICU object 
     * @stable ICU 2.4
     */
    public CharsetProviderICU(){
    }
    
    /**
     * Constructs a charset for the given charset name
     * @param charsetName charset name
     * @return charset objet for the given charset name
     * @stable ICU 2.4
     */
    public final Charset charsetForName(String charsetName) {
	    // get the canonical name	 
        String icuCanonicalName = NativeConverter.getICUCanonicalName(charsetName);	     
        // create the converter object and return it
        if(icuCanonicalName.length()==0){
        	// this would make the Charset API to throw 
        	// unsupported encoding exception
        	return null;
        }else{
	       String[] aliases = (String[])NativeConverter.getAliases(charsetName);	
           String canonicalName = NativeConverter.getJavaCanonicalName(icuCanonicalName);
	       return (new CharsetICU(canonicalName,icuCanonicalName, aliases));
	    }
    }
    
    /**
     * Adds an entry to the given map whose key is the charset's 
     * canonical name and whose value is the charset itself. 
     * @param map a map to receive charset objects and names
     * @stable ICU 2.4
     */
    public final void putCharsets(Map map) {
        // Get the available converter canonical names and aliases	  
        String[] charsets = NativeConverter.getAvailable();        
        for(int i=0; i<charsets.length;i++){
	    // get the ICU aliases for a converter	  
	    String[] aliases = NativeConverter.getAliases(charsets[i]);            
	    // store the charsets and aliases in a Map    
	    if (!map.containsKey(charsets[i])){
		      map.put(charsets[i], aliases);
	        }
        }
    }
    /**
     * Class that implements the iterator for charsets
     * @stable ICU 2.4
     */
    protected final class CharsetIterator implements Iterator{
      private String[] names;
      private int currentIndex;
      protected CharsetIterator(String[] strs){
    	names = strs;
    	currentIndex=0;
      }
      public boolean hasNext(){
    	return (currentIndex< names.length);
      }
      public Object next(){
    	if(currentIndex<names.length){
    	      return charsetForName(names[currentIndex++]);
    	}else{
    	      throw new NoSuchElementException();
    	}
      }
      public void remove(){
        if(currentIndex==0){
	       throw new IllegalStateException();
        }else{
	       names = null;
	       currentIndex=0;
        }
      }
    }
      

    /**
     * Returns an iterator for the available charsets
     * @return Iterator the charset name iterator
     * @stable ICU 2.4
     */
    public final Iterator charsets(){
          String[] charsets = NativeConverter.getAvailable();
          Iterator iter = new CharsetIterator(charsets);
          return iter;
    }
     
}

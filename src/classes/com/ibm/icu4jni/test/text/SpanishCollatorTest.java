/**
*******************************************************************************
* Copyright (C) 1996-2005, International Business Machines Corporation and    *
* others. All Rights Reserved.                                                *
*******************************************************************************
*
*******************************************************************************
*/

package com.ibm.icu4jni.test.text;

import java.util.Locale;
import com.ibm.icu4jni.text.Collator;
import com.ibm.icu4jni.text.CollationAttribute;
import com.ibm.icu4jni.test.TestFmwk;

/**
* Testing class for Spanish collator
* Mostly following the test cases for ICU
* @author Syn Wee Quek
* @since jan 23 2001
*/
public final class SpanishCollatorTest extends TestFmwk
{ 
  
  // constructor ===================================================
  
  /**
  * Constructor
  */
  public SpanishCollatorTest() throws Exception
  {
    m_collator_ = Collator.getInstance(new Locale("es", "ES"));
  }
  
  // public methods ================================================

  /**
  * Test with primary collation strength
  */
  public void TestPrimary() throws Exception
  {
    m_collator_.setStrength(CollationAttribute.VALUE_PRIMARY);
    for (int i = 5; i < 9; i ++)
      CollatorTest.doTest(this, m_collator_, SOURCE_TEST_CASE_[i], 
                          TARGET_TEST_CASE_[i], EXPECTED_TEST_RESULT_[i]);
  }

  /**
  * Test with tertiary collation strength
  */
  public void TestTertiary() throws Exception
  {
    m_collator_.setStrength(CollationAttribute.VALUE_TERTIARY);
    for (int i = 0; i < 5 ; i ++)
      CollatorTest.doTest(this, m_collator_, SOURCE_TEST_CASE_[i], 
                          TARGET_TEST_CASE_[i], EXPECTED_TEST_RESULT_[i]);
  }
  
  // private variables =============================================
  
  /**
  * RuleBasedCollator for testing
  */
  private Collator m_collator_;
 
  /**
  * Source strings for testing
  */
  private static final String SOURCE_TEST_CASE_[] = 
  {
    "\u0061\u006c\u0069\u0061\u0073",
    "\u0045\u006c\u006c\u0069\u006f\u0074",
    "\u0048\u0065\u006c\u006c\u006f",
    "\u0061\u0063\u0048\u0063",
    "\u0061\u0063\u0063",
    "\u0061\u006c\u0069\u0061\u0073",
    "\u0061\u0063\u0048\u0063",
    "\u0061\u0063\u0063",
    "\u0048\u0065\u006c\u006c\u006f"
  };

  /**
  * Target strings for testing
  */
  private final String TARGET_TEST_CASE_[] = 
  {
    "\u0061\u006c\u006c\u0069\u0061\u0073",
    "\u0045\u006d\u0069\u006f\u0074",
    "\u0068\u0065\u006c\u006c\u004f",
    "\u0061\u0043\u0048\u0063",
    "\u0061\u0043\u0048\u0063",
    "\u0061\u006c\u006c\u0069\u0061\u0073",
    "\u0061\u0043\u0048\u0063",
    "\u0061\u0043\u0048\u0063",
    "\u0068\u0065\u006c\u006c\u004f"
  };

  /**
  * Comparison result corresponding to above source and target cases
  */
  private final int EXPECTED_TEST_RESULT_[] = 
  {
    Collator.RESULT_LESS,
    Collator.RESULT_LESS,
    Collator.RESULT_GREATER,
    Collator.RESULT_LESS,
    Collator.RESULT_LESS,
    Collator.RESULT_LESS,
    Collator.RESULT_EQUAL,
    Collator.RESULT_LESS,
    Collator.RESULT_EQUAL                                               
  };
}


/**
************************************************************************
* Copyright (c) 1997-2005, International Business Machines
* Corporation and others.  All Rights Reserved.
************************************************************************
*/

package com.ibm.icu4jni.test.text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.ibm.icu4jni.test.TestFmwk;
import com.ibm.icu4jni.text.Normalizer;




public class ConformanceTest extends TestFmwk {

    Normalizer normalizer;

    static final String TEST_SUITE_FILE =
        "./src/classes/com/ibm/icu4jni/test/text/NormalizationTest.txt";
    static final String FILE = "NormalizationTest.txt";
    public static void main(String[] args) throws Exception {
        new ConformanceTest().run(args);
    }

    public ConformanceTest() {
        // Doesn't matter what the string and mode are; we'll change
        // them later as needed.
       // normalizer = new Normalizer("", Normalizer.UNORM_NFC);
    }

    /**
     * Test the conformance of Normalizer to
     * http://www.unicode.org/unicode/reports/tr15/conformance/Draft-TestSuite.txt.
     * This file must be located at the path specified as TEST_SUITE_FILE.
     */
    public void TestConformance() throws Exception{
        BufferedReader input = null;
        String line = null;
        String[] fields = new String[5];
        StringBuffer buf = new StringBuffer();
        int passCount = 0;
        int failCount = 0;
        InputStream is = null;
        try {
            is = new FileInputStream(TEST_SUITE_FILE);
        } catch (Exception ex) {
        }
        if (is == null) {
            try {
                is = getClass().getResourceAsStream(FILE);
            }
            catch (Exception e) {
                // will fail below
                e.printStackTrace();
            }    
        }
         try {
            input = new BufferedReader(new InputStreamReader(is),64*1024);
            for (int count = 0;;++count) {
                line = input.readLine();
                if (line == null) break;
                if (line.length() == 0) continue;

                // Expect 5 columns of this format:
                // 1E0C;1E0C;0044 0323;1E0C;0044 0323; # <comments>

                // Skip comments
                if (line.charAt(0) == '#' || line.charAt(0)=='@') continue;

                // Parse out the fields
                hexsplit(line, ';', fields, buf);
                if (checkConformance(fields, line)) {
                    ++passCount;
                } else {
                    ++failCount;
                }
                if ((count % 1000) == 999) {
                    logln("Line " + (count+1));
                }
            }
        } catch (IOException ex) {
            try {
                input.close();
            } catch (Exception ex2) {}
            ex.printStackTrace();
            throw new IllegalArgumentException("Couldn't read file "
              + ex.getClass().getName() + " " + ex.getMessage()
              + " line = " + line
              );
        }

        if (failCount != 0) {
            errln("Total: " + failCount + " lines failed, " +
                  passCount + " lines passed");
        } else {
            logln("Total: " + passCount + " lines passed");
        }
    }

    /**
     * Verify the conformance of the given line of the Unicode
     * normalization (UTR 15) test suite file.  For each line,
     * there are five columns, corresponding to field[0]..field[4].
     *
     * The following invariants must be true for all conformant implementations
     *  c2 == NFC(c1) == NFC(c2) == NFC(c3)
     *  c3 == NFD(c1) == NFD(c2) == NFD(c3)
     *  c4 == NFKC(c1) == NFKC(c2) == NFKC(c3) == NFKC(c4) == NFKC(c5)
     *  c5 == NFKD(c1) == NFKD(c2) == NFKD(c3) == NFKD(c4) == NFKD(c5)
     *
     * @param field the 5 columns
     * @param line the source line from the test suite file
     * @return true if the test passes
     */
    private boolean checkConformance(String[] field, String line) throws Exception{
        boolean pass = true;
        //StringBuffer buf = new StringBuffer(); // scratch
        String out;
        int i=0;

        for (i=0; i<5; ++i) {
            if (i<3) {
                out = Normalizer.normalize(field[i], Normalizer.UNORM_NFC);
                pass &= assertEqual("C", field[i], out, field[1], "c2!=C(c" + (i+1));
            /* out = iterativeNorm(field[i], Normalizer.UNORM_NFC, buf, +1);
                pass &= assertEqual("C(+1)", field[i], out, field[1], "c2!=C(c" + (i+1));
                out = iterativeNorm(field[i], Normalizer.UNORM_NFC, buf, -1);
                pass &= assertEqual("C(-1)", field[i], out, field[1], "c2!=C(c" + (i+1));
                */
                out = Normalizer.normalize(field[i], Normalizer.UNORM_NFD);
                pass &= assertEqual("D", field[i], out, field[2], "c3!=D(c" + (i+1));
            /* out = iterativeNorm(field[i], Normalizer.UNORM_NFD, buf, +1);
                pass &= assertEqual("D(+1)", field[i], out, field[2], "c3!=D(c" + (i+1));
                out = iterativeNorm(field[i], Normalizer.UNORM_NFD, buf, -1);
                pass &= assertEqual("D(-1)", field[i], out, field[2], "c3!=D(c" + (i+1));
                */
            }
            out = Normalizer.normalize(field[i], Normalizer.UNORM_NFKC);
            pass &= assertEqual("KC", field[i], out, field[3], "c4!=KC(c" + (i+1));
        /* out = iterativeNorm(field[i], Normalizer.UNORM_NFKC, buf, +1);
            pass &= assertEqual("KD(+1)", field[i], out, field[3], "c4!=KC(c" + (i+1));
            out = iterativeNorm(field[i], Normalizer.UNORM_NFKC, buf, -1);
            pass &= assertEqual("KD(-1)", field[i], out, field[3], "c4!=KC(c" + (i+1));
            */

            out = Normalizer.normalize(field[i], Normalizer.UNORM_NFKD);
            pass &= assertEqual("KD", field[i], out, field[4], "c5!=KD(c" + (i+1));
            /*out = iterativeNorm(field[i], Normalizer.UNORM_NFKD, buf, +1);
            pass &= assertEqual("KD(+1)", field[i], out, field[4], "c5!=KD(c" + (i+1));
            out = iterativeNorm(field[i], Normalizer.UNORM_NFKD, buf, -1);
            pass &= assertEqual("KD(-1)", field[i], out, field[4], "c5!=KD(c" + (i+1));
            */
        }
            
        if (!pass) {
            errln("FAIL: " + line);
        }     
       
        return pass;
    }

    /**
     * Do a normalization using the iterative API in the given direction.
     * @param buf scratch buffer
     * @param dir either +1 or -1
     */
    /*
    private String iterativeNorm(String str, int mode,
                                 StringBuffer buf, int dir) throws Exception{
        normalizer.setText(str);
        normalizer.setMode(mode);
        buf.setLength(0);
        char ch;
        if (dir > 0) {
            for (ch = normalizer.first(); ch != Normalizer.DONE;
                 ch = normalizer.next()) {
                buf.append(ch);
            }
        } else {
            for (ch = normalizer.last(); ch != Normalizer.DONE;
                 ch = normalizer.previous()) {
                buf.insert(0, ch);
            }
        }
        return buf.toString();

        return null;
    }
    */
    /**
     * @param op name of normalization form, e.g., "KC"
     * @param s string being normalized
     * @param got value received
     * @param exp expected value
     * @param msg description of this test
     * @return true if got == exp
     */
    private boolean assertEqual(String op, String s, String got,
                                String exp, String msg) {
        if (exp.equals(got)) {
            return true;
        }
        errln(TestFmwk.escape("      " + msg + ") " + op + "(" + s + ")=" + got +
                             ", exp. " + exp));
        return false;
    }

    /**
     * Split a string into pieces based on the given delimiter
     * character.  Then, parse the resultant fields from hex into
     * characters.  That is, "0040 0400;0C00;0899" -> new String[] {
     * "\u0040\u0400", "\u0C00", "\u0899" }.  The output is assumed to
     * be of the proper length already, and exactly output.length
     * fields are parsed.  If there are too few an exception is
     * thrown.  If there are too many the extras are ignored.
     *
     * @param buf scratch buffer
     */
    private static void hexsplit(String s, char delimiter,
                                 String[] output, StringBuffer buf) {
        int i;
        int pos = 0;
        for (i=0; i<output.length; ++i) {
            int delim = s.indexOf(delimiter, pos);
            if (delim < 0) {
                throw new IllegalArgumentException("Missing field in " + s);
            }
            // Our field is from pos..delim-1.
            buf.setLength(0);
            
            String toHex = s.substring(pos,delim);
            pos = delim;
            int index = 0;
            int len = toHex.length();
            while(index< len){
                if(toHex.charAt(index)==' '){
                    index++;
                }else{
                    int spacePos = toHex.indexOf(' ', index);
                    if(spacePos==-1){
                        appendInt(buf,toHex.substring(index,len),s);
                        spacePos = len;
                    }else{
                        appendInt(buf,toHex.substring(index, spacePos),s);
                    }
                    index = spacePos+1;
                }
            }
            
            if (buf.length() < 1) {
                throw new IllegalArgumentException("Empty field " + i + " in " + s);
            }
            output[i] = buf.toString();
            ++pos; // Skip over delim
        }
    }
    public static void appendInt(StringBuffer buf, String strToHex, String s){
        int hex = Integer.parseInt(strToHex,16);
        if (hex < 0 ) {
            throw new IllegalArgumentException("Out of range hex " +
                                                hex + " in " + s);
        }else if (hex > 0xFFFF){
            buf.append((char)((hex>>10)+0xd7c0)); 
            buf.append((char)((hex&0x3ff)|0xdc00));
        }else{
            buf.append((char) hex);
        }
    }
            
    // Specific tests for debugging.  These are generally failures
    // taken from the conformance file, but culled out to make
    // debugging easier.  These can be eliminated without affecting
    // coverage.

    public void _hideTestCase6() throws Exception{
        _testOneLine("0385;0385;00A8 0301;0020 0308 0301;0020 0308 0301;");
    }

    public void _testOneLine(String line) throws Exception{
        String[] fields = new String[5];
        StringBuffer buf = new StringBuffer();
        // Parse out the fields
        hexsplit(line, ';', fields, buf);
        checkConformance(fields, line);
    }
}

package waditu.tushare.common;

import static waditu.tushare.common.Utility.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by Raymond on 26/11/2016.
 */
public class UtilityTest {
    @Test
    public void testCodeToSymbol() {
        assertEquals("sh000001", _codeToSymbol("sh"));
        assertEquals("sz399001", _codeToSymbol("sz"));
        assertEquals("sz399300", _codeToSymbol("hs300"));

        assertEquals("", _codeToSymbol(null));
        assertEquals("", _codeToSymbol(""));

        assertEquals("sh600183", _codeToSymbol("600183"));
        assertEquals("sh500183", _codeToSymbol("500183"));
        assertEquals("sh900183", _codeToSymbol("900183"));

        assertEquals("sz002561", _codeToSymbol("002561"));
        assertEquals("sz300118", _codeToSymbol("300118"));
    }

}

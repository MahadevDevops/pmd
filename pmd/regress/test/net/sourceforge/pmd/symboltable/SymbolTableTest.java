/*
 * User: tom
 * Date: Jun 19, 2002
 * Time: 11:09:06 AM
 */
package test.net.sourceforge.pmd.symboltable;

import junit.framework.TestCase;
import net.sourceforge.pmd.*;
import net.sourceforge.pmd.symboltable.SymbolTable;
import net.sourceforge.pmd.symboltable.Symbol;
import net.sourceforge.pmd.ast.JavaParser;
import net.sourceforge.pmd.ast.ASTCompilationUnit;

import java.util.HashMap;
import java.io.InputStreamReader;
import java.io.Reader;

public class SymbolTableTest extends TestCase {

    private static final Symbol FOO = new Symbol("foo", 10);

    public void testAdd() {
        SymbolTable s = new SymbolTable();
        s.add(FOO);
        try {
            s.add(FOO);
        } catch (RuntimeException e) {
            return; // cool
        }
        throw new RuntimeException("Should have thrown RuntimeException");
    }

    public void testParent() {
        SymbolTable parent = new SymbolTable();
        SymbolTable child = new SymbolTable(parent, 1);
        assertEquals(child.getParent(), parent);
    }

    public void testParentContains() {
        SymbolTable parent = new SymbolTable();
        SymbolTable child = new SymbolTable(parent, 1);
        child.add(new Symbol("bar", 12));
        child.add(new Symbol("baz", 12));
        assertTrue(!parent.getUnusedSymbols().hasNext());
        assertTrue(child.getUnusedSymbols().hasNext());
    }

    public void testRecordUsage() {
        SymbolTable s = new SymbolTable();
        s.add(FOO);
        assertTrue(s.getUnusedSymbols().hasNext());
        s.recordPossibleUsageOf(FOO, null);
        assertTrue(!s.getUnusedSymbols().hasNext());
    }

    public void testRecordPossibleUsage() {
        SymbolTable parent = new SymbolTable();
        SymbolTable child = new SymbolTable(parent, 1);
        child.recordPossibleUsageOf(new Symbol("bar", 10), null);
        assertTrue(!parent.getUnusedSymbols().hasNext());
    }

    public void testRecordPossibleUsage2() {
        SymbolTable s = new SymbolTable();
        s.recordPossibleUsageOf(new Symbol("bar", 10), null);
        assertTrue(!s.getUnusedSymbols().hasNext());
    }

    public void testRecordUsageParent() {
        SymbolTable parent = new SymbolTable();
        parent.add(FOO);
        SymbolTable child = new SymbolTable(parent, 1);
        assertEquals(FOO, parent.getUnusedSymbols().next());
    }

    public void testRecordUsageParent2() {
        SymbolTable parent = new SymbolTable();
        parent.add(FOO);
        SymbolTable child = new SymbolTable(parent, 1);
        child.recordPossibleUsageOf(FOO, null);
        assertTrue(!parent.getUnusedSymbols().hasNext());
    }

}

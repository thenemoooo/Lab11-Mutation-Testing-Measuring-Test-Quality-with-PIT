package mn.edu.must.sqat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScholarshipCalculatorTest {

    ScholarshipCalculator calc;

    @BeforeEach
    void setUp() { calc = new ScholarshipCalculator(); }

    @Test
    void letterGrade_returnsExactGradesForAllRanges() {
        assertEquals("A", calc.letterGrade(95));
        assertEquals("B", calc.letterGrade(85));
        assertEquals("C", calc.letterGrade(75));
        assertEquals("D", calc.letterGrade(65));
        assertEquals("F", calc.letterGrade(30));
    }

    @Test
    void letterGrade_boundaries() {
        assertEquals("F", calc.letterGrade(0));
        assertEquals("F", calc.letterGrade(59.99));
        assertEquals("D", calc.letterGrade(60));
        assertEquals("D", calc.letterGrade(69.99));
        assertEquals("C", calc.letterGrade(70));
        assertEquals("C", calc.letterGrade(79.99));
        assertEquals("B", calc.letterGrade(80));
        assertEquals("B", calc.letterGrade(89.99));
        assertEquals("A", calc.letterGrade(90));
        assertEquals("A", calc.letterGrade(100));
    }

    @Test
    void letterGrade_invalidInputs_throw() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-0.01));
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(100.01));
    }

    @Test
    void gpa_returnsExactAveragesForAllRanges() {
        assertEquals(3.5, calc.gpa(new double[]{95, 85}), 1e-9);
        assertEquals(1.5, calc.gpa(new double[]{75, 65}), 1e-9);
        assertEquals(0.0, calc.gpa(new double[]{40}), 1e-9);
    }

    @Test
    void gpa_usesEveryLetterGrade() {
        assertEquals(4.0, calc.gpa(new double[]{100}), 1e-9);
        assertEquals(3.0, calc.gpa(new double[]{89.99}), 1e-9);
        assertEquals(2.0, calc.gpa(new double[]{79.99}), 1e-9);
        assertEquals(1.0, calc.gpa(new double[]{69.99}), 1e-9);
        assertEquals(0.0, calc.gpa(new double[]{59.99}), 1e-9);
    }

    @Test
    void gpa_invalidInputs_throw() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[]{}));
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[]{-0.01}));
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[]{100.01}));
    }

    @Test
    void scholarship_returnsExactAwardsForAllRules() {
        assertEquals(100, calc.scholarship(3.9, 16, false));
        assertEquals(70, calc.scholarship(3.5, 13, false));
        assertEquals(50, calc.scholarship(3.1, 5, false));
        assertEquals(0, calc.scholarship(2.0, 1, false));
        assertEquals(0, calc.scholarship(4.0, 20, true));
    }

    @Test
    void scholarship_fullAwardBoundaries() {
        assertEquals(100, calc.scholarship(3.8, 15, false));
        assertEquals(70, calc.scholarship(3.8, 14, false));
        assertEquals(70, calc.scholarship(3.79, 15, false));
        assertEquals(70, calc.scholarship(3.79, 14, false));
    }

    @Test
    void scholarship_secondAwardBoundaries() {
        assertEquals(70, calc.scholarship(3.4, 12, false));
        assertEquals(50, calc.scholarship(3.4, 11, false));
        assertEquals(50, calc.scholarship(3.39, 12, false));
        assertEquals(50, calc.scholarship(3.39, 11, false));
    }

    @Test
    void scholarship_baseAwardAndInputBoundaries() {
        assertEquals(50, calc.scholarship(3.0, 0, false));
        assertEquals(0, calc.scholarship(2.99, 100, false));
        assertEquals(0, calc.scholarship(0.0, 0, false));
        assertEquals(50, calc.scholarship(4.0, 0, false));
    }

    @Test
    void scholarship_violationTakesPrecedence() {
        assertEquals(0, calc.scholarship(3.8, 15, true));
        assertEquals(0, calc.scholarship(4.0, 20, true));
    }

    @Test
    void scholarship_invalidInputs_throw() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(-0.01, 15, false));
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(4.01, 15, false));
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(3.0, -1, false));
    }
}

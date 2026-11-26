package mn.edu.must.sqat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScholarshipCalculatorAiTest {

    private final ScholarshipCalculator calculator = new ScholarshipCalculator();

    @Test
    void letterGradeReturnsExactGradeForEachRange() {
        assertEquals("A", calculator.letterGrade(95));
        assertEquals("B", calculator.letterGrade(85));
        assertEquals("C", calculator.letterGrade(75));
        assertEquals("D", calculator.letterGrade(65));
        assertEquals("F", calculator.letterGrade(30));
    }

    @Test
    void letterGradeRejectsScoresOutsideZeroToOneHundred() {
        assertThrows(IllegalArgumentException.class, () -> calculator.letterGrade(-1));
        assertThrows(IllegalArgumentException.class, () -> calculator.letterGrade(101));
    }

    @Test
    void letterGradeHandlesInclusiveEndpointsAndImmediateNeighbors() {
        assertEquals("F", calculator.letterGrade(0));
        assertEquals("F", calculator.letterGrade(59.99));
        assertEquals("D", calculator.letterGrade(60));
        assertEquals("D", calculator.letterGrade(69.99));
        assertEquals("C", calculator.letterGrade(70));
        assertEquals("C", calculator.letterGrade(79.99));
        assertEquals("B", calculator.letterGrade(80));
        assertEquals("B", calculator.letterGrade(89.99));
        assertEquals("A", calculator.letterGrade(90));
        assertEquals("A", calculator.letterGrade(100));
    }

    @Test
    void gpaReturnsExactAveragesForRepresentativeScores() {
        assertEquals(3.5, calculator.gpa(new double[]{95, 85}), 1e-9);
        assertEquals(1.5, calculator.gpa(new double[]{75, 65}), 1e-9);
        assertEquals(0.0, calculator.gpa(new double[]{40}), 1e-9);
    }

    @Test
    void gpaMapsEveryLetterToItsDocumentedPointValue() {
        assertEquals(4.0, calculator.gpa(new double[]{100}), 1e-9);
        assertEquals(3.0, calculator.gpa(new double[]{89.99}), 1e-9);
        assertEquals(2.0, calculator.gpa(new double[]{79.99}), 1e-9);
        assertEquals(1.0, calculator.gpa(new double[]{69.99}), 1e-9);
        assertEquals(0.0, calculator.gpa(new double[]{59.99}), 1e-9);
    }

    @Test
    void gpaRejectsMissingOrInvalidScores() {
        assertThrows(IllegalArgumentException.class, () -> calculator.gpa(null));
        assertThrows(IllegalArgumentException.class, () -> calculator.gpa(new double[]{}));
        assertThrows(IllegalArgumentException.class, () -> calculator.gpa(new double[]{-1}));
        assertThrows(IllegalArgumentException.class, () -> calculator.gpa(new double[]{101}));
    }

    @Test
    void scholarshipReturnsExactAwardForEachRule() {
        assertEquals(100, calculator.scholarship(3.9, 16, false));
        assertEquals(70, calculator.scholarship(3.5, 13, false));
        assertEquals(50, calculator.scholarship(3.1, 5, false));
        assertEquals(0, calculator.scholarship(2.0, 1, false));
        assertEquals(0, calculator.scholarship(4.0, 20, true));
    }

    @Test
    void scholarshipHandlesExactRuleThresholdsAndRangeEndpoints() {
        assertEquals(100, calculator.scholarship(3.8, 15, false));
        assertEquals(70, calculator.scholarship(3.4, 12, false));
        assertEquals(50, calculator.scholarship(3.0, 0, false));
        assertEquals(50, calculator.scholarship(4.0, 0, false));
        assertEquals(0, calculator.scholarship(0.0, 0, false));
    }

    @Test
    void scholarshipChecksGpaConditionsWhenCreditsReachTheHigherTier() {
        assertEquals(70, calculator.scholarship(3.79, 15, false));
        assertEquals(50, calculator.scholarship(3.39, 12, false));
    }

    @Test
    void scholarshipChecksCreditConditionsWhenGpaReachesTheHigherTier() {
        assertEquals(70, calculator.scholarship(3.8, 14, false));
        assertEquals(70, calculator.scholarship(3.79, 14, false));
        assertEquals(50, calculator.scholarship(3.4, 11, false));
        assertEquals(50, calculator.scholarship(3.39, 11, false));
    }

    @Test
    void scholarshipViolationOverridesOtherwiseValidAwards() {
        assertEquals(0, calculator.scholarship(3.8, 15, true));
        assertEquals(0, calculator.scholarship(4.0, 20, true));
    }

    @Test
    void scholarshipRejectsInvalidGpaAndCredits() {
        assertThrows(IllegalArgumentException.class, () -> calculator.scholarship(-0.01, 15, false));
        assertThrows(IllegalArgumentException.class, () -> calculator.scholarship(4.01, 15, false));
        assertThrows(IllegalArgumentException.class, () -> calculator.scholarship(3.0, -1, false));
    }
}

package xyz.projectplay.jisho.models;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class ConceptTest {

    private Concept concept;

    @Before
    public void setUp() throws Exception {
        concept = new Concept();
    }

    @Test
    public void getReading() throws Exception {
        String expected = "hello";
        concept.setReading(expected);
        assertEquals(expected, concept.getReading());
    }

    @Test
    public void getFurigana() throws Exception {
        List<String> expected = new ArrayList<>();
        expected.add("he");
        expected.add("l");
        expected.add("lo");
        concept.setFurigana(expected);
        assertEquals(expected, concept.getFurigana());
    }

    @Test
    public void getTag() throws Exception {
        String expected = "common";
        concept.setTag(expected);
        assertEquals(expected, concept.getTag());
    }

    @Test
    public void getMeanings() throws Exception {
        List<String> expected = new ArrayList<>();
        expected.add("hello");
        expected.add("good day");
        expected.add("daytime greeting");
        concept.setMeanings(expected);
        assertEquals(expected, concept.getMeanings());
    }

}
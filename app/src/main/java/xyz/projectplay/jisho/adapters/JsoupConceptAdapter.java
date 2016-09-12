package xyz.projectplay.jisho.adapters;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import xyz.projectplay.jisho.models.Concept;

public class JsoupConceptAdapter {

    private JsoupConceptAdapter() {}

    public static Concept parseConcept(Document doc) {
        return parseConcepts(doc).get(0);
    }

    public static List<Concept> parseConcepts(Document doc) {
        Elements elements = doc.select("div.concept_light.clearfix");
        List<Concept> concepts = new ArrayList<>();
        for (Element element : elements) {
            // TODO: 9/11/16 names
            if (element.parent().className().equals("names"))
                continue;

            Concept concept = new Concept();

            // set reading
            Elements reading = element.getElementsByClass("text");
            if (!reading.isEmpty()) {
                concept.setReading(reading.first().text());
            }

            // set furigana
            Elements furigana = element.getElementsByClass("kanji");
            if (!reading.isEmpty()) {
                List<String> conceptFurigana = new ArrayList<>();
                for (Element e : furigana) {
                    conceptFurigana.add(e.childNodes().get(0).attr("text"));
                }
                concept.setFurigana(conceptFurigana);
            }

            // set tag
            Elements tag = element.getElementsByClass("success");
            if (!tag.isEmpty()) {
                concept.setTag(tag.first().text());
            }

            // set meaning
            Elements meanings = element.getElementsByClass("meaning-definition");
            if (!meanings.isEmpty()) {
                List<String> conceptMeanings = new ArrayList<>();
                for (Element meaning : meanings) {
                    String conceptMeaning = "";
                    for (Element child : meaning.children()) {
                        conceptMeaning = conceptMeaning.concat(child.childNodes().get(0).attr("text"));
                    }
                    conceptMeanings.add(conceptMeaning);
                }
                concept.setMeanings(conceptMeanings);
            }

            concepts.add(concept);
        }
        return concepts;
    }
}

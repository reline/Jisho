package com.github.reline.jisho;

import com.github.reline.jisho.util.VerbInflectionUtils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by surjo on 24/04/17.
 */

public class VerbInflectionTest {

    @Test
    public void testIchidanInflectionAffirmative() {
        String root = "食べる";
        VerbInflectionUtils utils = new VerbInflectionUtils(root, VerbInflectionUtils.TYPE_ICHIDAN);

        assertEquals(utils.getNonPast().getAffirmative(), "食べる");
        assertEquals(utils.getNonPastPolite().getAffirmative(), "食べます");
        assertEquals(utils.getPast().getAffirmative(), "食べた");
        assertEquals(utils.getPastPolite().getAffirmative(), "食べました");
        assertEquals(utils.getTeForm().getAffirmative(), "食べて");
        assertEquals(utils.getPotential().getAffirmative(), "食べられる");
        assertEquals(utils.getPassive().getAffirmative(), "食べられる");
        assertEquals(utils.getCausative().getAffirmative(), "食べさせる");
        assertEquals(utils.getCausativePassive().getAffirmative(), "食べさせられる");
        assertEquals(utils.getImperative().getAffirmative(), "食べろ");
    }

    @Test
    public void testIchidanInflectionNegative() {
        String root = "食べる";
        VerbInflectionUtils utils = new VerbInflectionUtils(root, VerbInflectionUtils.TYPE_ICHIDAN);

        assertEquals(utils.getNonPast().getNegative(), "食べない");
        assertEquals(utils.getNonPastPolite().getNegative(), "食べません");
        assertEquals(utils.getPast().getNegative(), "食べなかった");
        assertEquals(utils.getPastPolite().getNegative(), "食べませんでした");
        assertEquals(utils.getTeForm().getNegative(), "食べなくて");
        assertEquals(utils.getPotential().getNegative(), "食べられない");
        assertEquals(utils.getPassive().getNegative(), "食べられない");
        assertEquals(utils.getCausative().getNegative(), "食べさせない");
        assertEquals(utils.getCausativePassive().getNegative(), "食べさせられない");
        assertEquals(utils.getImperative().getNegative(), "食べるな");
    }

    @Test
    public void testGodanInflectionAffirmative() {
        String root = "行く";
        VerbInflectionUtils utils = new VerbInflectionUtils(root, VerbInflectionUtils.TYPE_GODAN);

        assertEquals(utils.getNonPast().getAffirmative(), "行く");
        assertEquals(utils.getNonPastPolite().getAffirmative(), "行きます");
        assertEquals(utils.getPast().getAffirmative(), "行った");
        assertEquals(utils.getPastPolite().getAffirmative(), "行きました");
        assertEquals(utils.getTeForm().getAffirmative(), "行って");
        assertEquals(utils.getPotential().getAffirmative(), "行ける");
        assertEquals(utils.getPassive().getAffirmative(), "行かれる");
        assertEquals(utils.getCausative().getAffirmative(), "行かせる");
        assertEquals(utils.getCausativePassive().getAffirmative(), "行かせられる");
        assertEquals(utils.getImperative().getAffirmative(), "行け");
    }

    @Test
    public void testGodanInflectionNegative() {
        String root = "行く";
        VerbInflectionUtils utils = new VerbInflectionUtils(root, VerbInflectionUtils.TYPE_GODAN);

        assertEquals(utils.getNonPast().getNegative(), "行かない");
        assertEquals(utils.getNonPastPolite().getNegative(), "行きません");
        assertEquals(utils.getPast().getNegative(), "行かなかった");
        assertEquals(utils.getPastPolite().getNegative(), "行きませんでした");
        assertEquals(utils.getTeForm().getNegative(), "行かなくて");
        assertEquals(utils.getPotential().getNegative(), "行けない");
        assertEquals(utils.getPassive().getNegative(), "行かれない");
        assertEquals(utils.getCausative().getNegative(), "行かせない");
        assertEquals(utils.getCausativePassive().getNegative(), "行かせられない");
        assertEquals(utils.getImperative().getNegative(), "行くな");
    }

    @Test
    public void testKuruInflectionNegative() {
        String root = "来る";
        VerbInflectionUtils utils = new VerbInflectionUtils(root, VerbInflectionUtils.TYPE_KURU_VERB);

        assertEquals(utils.getNonPast().getNegative(), "来ない");
        assertEquals(utils.getNonPastPolite().getNegative(), "来ません");
        assertEquals(utils.getPast().getNegative(), "来なかった");
        assertEquals(utils.getPastPolite().getNegative(), "来ませんでした");
        assertEquals(utils.getTeForm().getNegative(), "来なくて");
        assertEquals(utils.getPotential().getNegative(), "来られない");
        assertEquals(utils.getPassive().getNegative(), "来られない");
        assertEquals(utils.getCausative().getNegative(), "来させない");
        assertEquals(utils.getCausativePassive().getNegative(), "来させられない");
        assertEquals(utils.getImperative().getNegative(), "来るな");
    }

    @Test
    public void testKuruInflectionAffirmative() {
        String root = "来る";
        VerbInflectionUtils utils = new VerbInflectionUtils(root, VerbInflectionUtils.TYPE_KURU_VERB);

        assertEquals(utils.getNonPast().getAffirmative(), "来る");
        assertEquals(utils.getNonPastPolite().getAffirmative(), "来ます");
        assertEquals(utils.getPast().getAffirmative(), "来た");
        assertEquals(utils.getPastPolite().getAffirmative(), "来ました");
        assertEquals(utils.getTeForm().getAffirmative(), "来て");
        assertEquals(utils.getPotential().getAffirmative(), "来られる");
        assertEquals(utils.getPassive().getAffirmative(), "来られる");
        assertEquals(utils.getCausative().getAffirmative(), "来させる");
        assertEquals(utils.getCausativePassive().getAffirmative(), "来させられる");
        assertEquals(utils.getImperative().getAffirmative(), "来い");
    }

    @Test
    public void testSuruInflectionAffirmative() {
        String root = "する";
        VerbInflectionUtils utils = new VerbInflectionUtils(root, VerbInflectionUtils.TYPE_SURU_VERB);

        assertEquals(utils.getNonPast().getAffirmative(), "する");
        assertEquals(utils.getNonPastPolite().getAffirmative(), "します");
        assertEquals(utils.getPast().getAffirmative(), "した");
        assertEquals(utils.getPastPolite().getAffirmative(), "しました");
        assertEquals(utils.getTeForm().getAffirmative(), "して");
        assertEquals(utils.getPotential().getAffirmative(), "できる");
        assertEquals(utils.getPassive().getAffirmative(), "される");
        assertEquals(utils.getCausative().getAffirmative(), "させる");
        assertEquals(utils.getImperative().getAffirmative(), "しろ");
    }

    @Test
    public void testSuruInflectionNegative() {
        String root = "する";
        VerbInflectionUtils utils = new VerbInflectionUtils(root, VerbInflectionUtils.TYPE_SURU_VERB);

        assertEquals(utils.getNonPast().getNegative(), "しない");
        assertEquals(utils.getNonPastPolite().getNegative(), "しません");
        assertEquals(utils.getPast().getNegative(), "しなかった");
        assertEquals(utils.getPastPolite().getNegative(), "しませんでした");
        assertEquals(utils.getPotential().getNegative(), "できない");
        assertEquals(utils.getImperative().getNegative(), "するな");
    }
}

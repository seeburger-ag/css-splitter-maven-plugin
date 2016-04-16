/*
 * CSS Splitter Maven Plugin
 * http://www.gabrys.biz/projects/css-splitter-maven-plugin/
 *
 * Copyright (c) 2015 Adam Gabryś
 *
 * This file is licensed under the BSD 3-Clause (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *      https://raw.githubusercontent.com/gabrysbiz/css-splitter-maven-plugin/master/src/main/resources/license.txt
 */
package biz.gabrys.maven.plugins.css.splitter.split;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import biz.gabrys.maven.plugins.css.splitter.counter.AnyRuleCounter;
import biz.gabrys.maven.plugins.css.splitter.css.types.NodeRule;
import biz.gabrys.maven.plugins.css.splitter.css.types.StyleSheet;

//TODO add tests
public class Splliter {

    private final int limit;
    private final RulesSplitter<NodeRule> splitter;

    public Splliter(final int limit) {
        this.limit = limit;
        splitter = new RulesSplitter<NodeRule>(new AnyRuleCounter(), new AnyRuleSplitter());
    }

    public List<StyleSheet> split(final StyleSheet stylesheet) {
        final List<List<NodeRule>> rulesMatrix = splitToMatrix(stylesheet.getRules());
        return convertToSheets(rulesMatrix);
    }

    private List<List<NodeRule>> splitToMatrix(final List<NodeRule> rules) {
        final List<List<NodeRule>> rulesMatrix = new LinkedList<List<NodeRule>>();
        List<NodeRule> currentRules = rules;
        while (!currentRules.isEmpty()) {
            final RulesContainer<NodeRule> container = splitter.split(currentRules, limit);
            rulesMatrix.add(container.before);
            currentRules = container.after;
        }
        return rulesMatrix;
    }

    private List<StyleSheet> convertToSheets(final List<List<NodeRule>> rulesMatrix) {
        final List<StyleSheet> sheets = new ArrayList<StyleSheet>(rulesMatrix.size());
        for (final List<NodeRule> sheetRules : rulesMatrix) {
            sheets.add(new StyleSheet(sheetRules));
        }
        return sheets;
    }
}

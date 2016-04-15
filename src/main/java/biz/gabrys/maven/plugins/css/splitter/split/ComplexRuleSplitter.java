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

import java.util.LinkedList;
import java.util.List;

import biz.gabrys.maven.plugins.css.splitter.counter.RuleCounter;
import biz.gabrys.maven.plugins.css.splitter.counter.StyleRuleCounter;
import biz.gabrys.maven.plugins.css.splitter.css.types.ComplexRule;
import biz.gabrys.maven.plugins.css.splitter.css.types.StyleRule;

class ComplexRuleSplitter extends AbstractRuleSplitter<ComplexRule> {

    private final RuleCounter counter;
    private final RuleSplitter<StyleRule> splitter;
    private final NeighborsManager neighborsManager;

    ComplexRuleSplitter() {
        this(new StyleRuleCounter(), new StyleRuleSplitter(), new NeighborsManager());
    }

    // for tests
    ComplexRuleSplitter(final RuleCounter counter, final RuleSplitter<StyleRule> splitter, final NeighborsManager neighborsManager) {
        super(ComplexRule.class);
        this.counter = counter;
        this.splitter = splitter;
        this.neighborsManager = neighborsManager;
    }

    @Override
    protected SplitResult<ComplexRule> split2(final ComplexRule rule, final int splitAfter) {
        final RulesContainer container = splitRules(rule.getRules(), splitAfter);
        final ComplexRule first = new ComplexRule(rule.getType(), rule.getSelectors(), container.first);
        final ComplexRule second = new ComplexRule(rule.getType(), rule.getSelectors(), container.second);
        neighborsManager.fill(rule, first, second);
        return new SplitResult<ComplexRule>(first, second);
    }

    RulesContainer splitRules(final List<StyleRule> rules, final int splitAfter) {
        final RulesContainer container = new RulesContainer();
        final ValueAndIndex info = new ValueAndIndex(splitAfter);
        processBeforeSplitPoint(rules, container.first, info);
        if (info.value != 0) {
            processSplitPoint(rules, container, info);
        }
        processAfterSplitPoint(rules, container.second, info);
        return container;
    }

    private void processBeforeSplitPoint(final List<StyleRule> rules, final List<StyleRule> beforeSplitPointRules,
            final ValueAndIndex info) {
        while (true) {
            final StyleRule styleRule = rules.get(info.index);
            final int count = counter.count(styleRule);
            final int odds = info.value - count;
            if (odds < 0) {
                break;
            }
            beforeSplitPointRules.add(styleRule);
            ++info.index;
            info.value = odds;
        }
    }

    private void processSplitPoint(final List<StyleRule> rules, final RulesContainer container, final ValueAndIndex info) {
        final StyleRule styleRule = rules.get(info.index);
        ++info.index;
        final SplitResult<StyleRule> result = splitter.split(styleRule, info.value);
        container.first.add(result.getFirst());
        container.second.add(result.getSecond());
    }

    private void processAfterSplitPoint(final List<StyleRule> rules, final List<StyleRule> afterSplitPointRules, final ValueAndIndex info) {
        if (info.index < rules.size()) {
            afterSplitPointRules.addAll(rules.subList(info.index, rules.size()));
        }
    }

    private static class ValueAndIndex {

        private int value;
        private int index;

        ValueAndIndex(final int value) {
            this.value = value;
        }
    }

    static class RulesContainer {

        protected final List<StyleRule> first = new LinkedList<StyleRule>();
        protected final List<StyleRule> second = new LinkedList<StyleRule>();
    }
}

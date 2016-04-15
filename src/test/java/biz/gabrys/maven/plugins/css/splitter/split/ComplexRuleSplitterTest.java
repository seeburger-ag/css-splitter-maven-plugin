package biz.gabrys.maven.plugins.css.splitter.split;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import biz.gabrys.maven.plugins.css.splitter.counter.RuleCounter;
import biz.gabrys.maven.plugins.css.splitter.css.types.ComplexRule;
import biz.gabrys.maven.plugins.css.splitter.css.types.StyleRule;
import biz.gabrys.maven.plugins.css.splitter.split.ComplexRuleSplitter.RulesContainer;

public final class ComplexRuleSplitterTest {

    @SuppressWarnings("unchecked")
    @Test
    public void split2_ruleIsSplittable_returnsResultWithRuleParts() {
        final RuleCounter counter = Mockito.mock(RuleCounter.class);
        final RuleSplitter<StyleRule> ruleSplitter = Mockito.mock(RuleSplitter.class);
        final NeighborsManager neighborsManager = Mockito.mock(NeighborsManager.class);
        final ComplexRuleSplitter complexSplitter = Mockito.spy(new ComplexRuleSplitter(counter, ruleSplitter, neighborsManager));

        final ComplexRule rule = Mockito.mock(ComplexRule.class);
        final String type = "@type";
        Mockito.when(rule.getType()).thenReturn(type);

        final List<String> selectors = Arrays.asList("div", "p");
        Mockito.when(rule.getSelectors()).thenReturn(selectors);

        final StyleRule rule1 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule1)).thenReturn(2);
        final StyleRule rule2 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule2)).thenReturn(2);
        final List<StyleRule> rules = Arrays.asList(rule1, rule2);
        Mockito.when(rule.getRules()).thenReturn(rules);

        final SplitResult<ComplexRule> result = complexSplitter.split2(rule, 2);

        final ComplexRule firstRule = result.getFirst();
        Assert.assertNotNull("First rule should not be equal to null", firstRule);
        final List<String> firstRuleSelectors = firstRule.getSelectors();
        Assert.assertEquals("First rule selectors quantity", selectors.size(), firstRuleSelectors.size());
        Assert.assertTrue("First rule selectors list", selectors.containsAll(firstRuleSelectors));
        final List<StyleRule> firstRuleChildren = firstRule.getRules();
        Assert.assertEquals("First children rules list size", 1, firstRuleChildren.size());
        Assert.assertTrue("First children rules should contain rule1", rule1 == firstRuleChildren.get(0));

        final ComplexRule secondRule = result.getSecond();
        Assert.assertNotNull("Second rule should not be equal to null", secondRule);
        final List<String> secondRuleSelectors = secondRule.getSelectors();
        Assert.assertEquals("Second rule selectors quantity", selectors.size(), secondRuleSelectors.size());
        Assert.assertTrue("Second rule selectors list", selectors.containsAll(secondRuleSelectors));
        final List<StyleRule> secondRuleChildren = secondRule.getRules();
        Assert.assertEquals("Second children rules list size", 1, secondRuleChildren.size());
        Assert.assertTrue("Second children rules should contain rule2", rule2 == secondRuleChildren.get(0));

        Mockito.verify(complexSplitter).splitRules(rules, 2);
        Mockito.verify(neighborsManager).fill(rule, firstRule, secondRule);
        Mockito.verifyNoMoreInteractions(neighborsManager);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void splitRules_splitBetweenRules() {
        final RuleCounter counter = Mockito.mock(RuleCounter.class);
        final RuleSplitter<StyleRule> ruleSplitter = Mockito.mock(RuleSplitter.class);
        final NeighborsManager neighborsManager = Mockito.mock(NeighborsManager.class);
        final ComplexRuleSplitter complexSplitter = new ComplexRuleSplitter(counter, ruleSplitter, neighborsManager);

        final StyleRule rule1 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule1)).thenReturn(2);
        final StyleRule rule2 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule2)).thenReturn(2);
        final StyleRule rule3 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule3)).thenReturn(2);
        final StyleRule rule4 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule4)).thenReturn(2);
        final List<StyleRule> rules = Arrays.asList(rule1, rule2, rule3, rule4);

        final RulesContainer container = complexSplitter.splitRules(rules, 4);

        Assert.assertEquals("First rules list size", 2, container.first.size());
        Assert.assertTrue("First rules should contain rule1", rule1 == container.first.get(0));
        Assert.assertTrue("First rules should contain rule2", rule2 == container.first.get(1));
        Assert.assertEquals("Second rules list size", 2, container.second.size());
        Assert.assertTrue("Second rules should contain rule3", rule3 == container.second.get(0));
        Assert.assertTrue("Second rules should contain rule4", rule4 == container.second.get(1));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void splitRules_splitRuleFromEnd() {
        final RuleCounter counter = Mockito.mock(RuleCounter.class);
        final RuleSplitter<StyleRule> ruleSplitter = Mockito.mock(RuleSplitter.class);
        final NeighborsManager neighborsManager = Mockito.mock(NeighborsManager.class);
        final ComplexRuleSplitter complexSplitter = new ComplexRuleSplitter(counter, ruleSplitter, neighborsManager);

        final StyleRule rule1 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule1)).thenReturn(2);
        final StyleRule rule2 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule2)).thenReturn(6);
        final List<StyleRule> rules = Arrays.asList(rule1, rule2);

        final StyleRule first = Mockito.mock(StyleRule.class);
        final StyleRule second = Mockito.mock(StyleRule.class);
        Mockito.when(ruleSplitter.split(rule2, 2)).thenReturn(new SplitResult<StyleRule>(first, second));

        final RulesContainer container = complexSplitter.splitRules(rules, 4);

        Assert.assertEquals("First rules list size", 2, container.first.size());
        Assert.assertTrue("First rules should contain rule1", rule1 == container.first.get(0));
        Assert.assertTrue("First rules should contain first", first == container.first.get(1));
        Assert.assertEquals("Second rules list size", 1, container.second.size());
        Assert.assertTrue("Second rules should contain second", second == container.second.get(0));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void splitRules_splitRuleFromMiddle() {
        final RuleCounter counter = Mockito.mock(RuleCounter.class);
        final RuleSplitter<StyleRule> ruleSplitter = Mockito.mock(RuleSplitter.class);
        final NeighborsManager neighborsManager = Mockito.mock(NeighborsManager.class);
        final ComplexRuleSplitter complexSplitter = new ComplexRuleSplitter(counter, ruleSplitter, neighborsManager);

        final StyleRule rule1 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule1)).thenReturn(2);
        final StyleRule rule2 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule2)).thenReturn(4);
        final StyleRule rule3 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule3)).thenReturn(2);
        final List<StyleRule> rules = Arrays.asList(rule1, rule2, rule3);

        final StyleRule first = Mockito.mock(StyleRule.class);
        final StyleRule second = Mockito.mock(StyleRule.class);
        Mockito.when(ruleSplitter.split(rule2, 2)).thenReturn(new SplitResult<StyleRule>(first, second));

        final RulesContainer container = complexSplitter.splitRules(rules, 4);

        Assert.assertEquals("First rules list size", 2, container.first.size());
        Assert.assertTrue("First rules should contain rule1", rule1 == container.first.get(0));
        Assert.assertTrue("First rules should contain first", first == container.first.get(1));
        Assert.assertEquals("Second rules list size", 2, container.second.size());
        Assert.assertTrue("Second rules should contain second", second == container.second.get(0));
        Assert.assertTrue("Second rules should contain rule3", rule3 == container.second.get(1));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void splitRules_splitRuleFromBegin() {
        final RuleCounter counter = Mockito.mock(RuleCounter.class);
        final RuleSplitter<StyleRule> ruleSplitter = Mockito.mock(RuleSplitter.class);
        final NeighborsManager neighborsManager = Mockito.mock(NeighborsManager.class);
        final ComplexRuleSplitter complexSplitter = new ComplexRuleSplitter(counter, ruleSplitter, neighborsManager);

        final StyleRule rule1 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule1)).thenReturn(6);
        final StyleRule rule2 = Mockito.mock(StyleRule.class);
        Mockito.when(counter.count(rule2)).thenReturn(2);
        final List<StyleRule> rules = Arrays.asList(rule1, rule2);

        final StyleRule first = Mockito.mock(StyleRule.class);
        final StyleRule second = Mockito.mock(StyleRule.class);
        Mockito.when(ruleSplitter.split(rule1, 4)).thenReturn(new SplitResult<StyleRule>(first, second));

        final RulesContainer container = complexSplitter.splitRules(rules, 4);

        Assert.assertEquals("First rules list size", 1, container.first.size());
        Assert.assertTrue("First rules should contain first", first == container.first.get(0));
        Assert.assertEquals("Second rules list size", 2, container.second.size());
        Assert.assertTrue("Second rules should contain second", second == container.second.get(0));
        Assert.assertTrue("Second rules should contain rule2", rule2 == container.second.get(1));
    }
}

package biz.gabrys.maven.plugins.css.splitter.split;

import java.util.Collections;

import org.junit.Test;
import org.mockito.Mockito;

import biz.gabrys.maven.plugins.css.splitter.css.types.StyleProperty;
import biz.gabrys.maven.plugins.css.splitter.css.types.StyleRule;
import biz.gabrys.maven.plugins.css.splitter.css.types.UnknownRule;

public final class AbstractRuleSplitterTest {

    @Test(expected = IllegalArgumentException.class)
    public void split_typeIsInvalid_throwsException() {
        final RuleSplitterImpl splitter = new RuleSplitterImpl();
        splitter.split(new StyleRule(Collections.<String>emptyList(), Collections.<StyleProperty>emptyList()), 2);
    }

    @Test
    public void split_typeIsValid_executesConvert2() {
        final RuleSplitterImpl splitter = Mockito.spy(new RuleSplitterImpl());
        final UnknownRule rule = new UnknownRule("code");
        final int splitAfter = 3;
        splitter.split(rule, splitAfter);
        Mockito.verify(splitter).split2(rule, splitAfter);
    }

    private static class RuleSplitterImpl extends AbstractRuleSplitter<UnknownRule> {

        protected RuleSplitterImpl() {
            super(UnknownRule.class);
        }

        @Override
        protected SplitResult<UnknownRule> split2(final UnknownRule rule, final int splitAfter) {
            // do nothing
            return null;
        }
    }
}

package biz.gabrys.maven.plugins.css.splitter.split;

import org.junit.Test;
import org.mockito.Mockito;

import biz.gabrys.maven.plugins.css.splitter.css.types.NodeRule;

public final class AbstractRuleSplitterTest {

    @Test(expected = IllegalArgumentException.class)
    public void split_typeIsInvalid_throwsException() {
        final RuleSplitterImpl splitter = new RuleSplitterImpl();
        splitter.split(new NotSupportedNodeRule(), 2);
    }

    @Test
    public void split_typeIsValid_executesConvert2() {
        final RuleSplitterImpl splitter = Mockito.spy(new RuleSplitterImpl());
        final SupportedNodeRule rule = new SupportedNodeRule();
        final int splitAfter = 3;
        splitter.split(rule, splitAfter);
        Mockito.verify(splitter).split2(rule, splitAfter);
    }

    private static class RuleSplitterImpl extends AbstractRuleSplitter<SupportedNodeRule> {

        RuleSplitterImpl() {
            super(SupportedNodeRule.class);
        }

        @Override
        protected SplitResult<SupportedNodeRule> split2(final SupportedNodeRule rule, final int splitAfter) {
            // do nothing
            return null;
        }
    }

    private static class SupportedNodeRule extends NodeRule {

    }

    private static class NotSupportedNodeRule extends NodeRule {

    }
}

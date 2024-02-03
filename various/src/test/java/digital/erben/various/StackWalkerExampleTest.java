package digital.erben.various;

import org.junit.jupiter.api.Test;

class StackWalkerExampleTest {

    @Test
    public void giveStalkWalker_whenWalkingTheStack_thenShowStackFrames() {
        new StackWalkerExample().methodOne();
    }

    @Test
    public void giveStalkWalker_whenInvokingFindCaller_thenFindCallingClass() {
        new StackWalkerExample().findCaller();
    }
}
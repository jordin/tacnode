package in.jord.tacnode.util;

/**
 * Created by Jordin on 8/10/2017.
 * Jordin is still best hacker.
 */
// Sorry
public interface NConsumer {
    // 0 doesn't use a Runnable because we look for an "accept" method in the lambda.

    interface Zero extends NConsumer {
        void accept();
    }

    // 1: Consumer
    // 2: BiConsumer

    interface Three<A, B, C> extends NConsumer {
        void accept(A a, B b, C c);
    }

    interface Four<A, B, C, D> extends NConsumer {
        void accept(A a, B b, C c, D d);
    }
}

package me.jordin.tacnode.wrappers.string;

import java.util.Iterator;

/**
 * Created by Jordin on 8/15/2017.
 * Jordin is still best hacker.
 */
public abstract class StringWrapper {
    private static final String SPACE = " ";

    private String underlying;

    public StringWrapper(Iterator<String> iterator) {
        if (iterator.hasNext()) {
            StringBuilder builder = new StringBuilder();
            while (iterator.hasNext()) {
                builder.append(SPACE);
                builder.append(iterator.next());
            }

            this.underlying = builder.toString().substring(SPACE.length());
        } else {
            this.underlying = "";
        }
    }

    public String get() {
        return this.underlying;
    }

    @Override
    public String toString() {
        return this.underlying;
    }

}

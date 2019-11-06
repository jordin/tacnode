package in.jord.tacnode.util;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by Jordin on 8/9/2017.
 * Jordin is still best hacker.
 */
public class CommonSuggestions {
    public static final List<String> TILDE = ImmutableList.of("~");
    public static final List<String> ZERO_POINT_ZERO = ImmutableList.of("0.0");
    public static final List<String> ZERO = ImmutableList.of("0");

    public static final List<String> ACCEPTABLE_TRUE_VALUES = ImmutableList.of(
            "true", "t", "yes", "y", "accept", "acceptable", "allow", "allowed", "affirmative", "okay", "yea", "yeah",
            "aye", "byallmeans", "certainly", "definitely", "gladly", "naturally", "ofcourse", "course", "yep",
            "undoubtedly", "1", "1.0", "10", "100", "100%", "5/7");

    public static final List<String> COMMON_BOOLEAN_VALUES = ImmutableList.of("true", "false"); // in the spec
}

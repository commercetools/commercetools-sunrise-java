package common.templates;

import com.github.jknack.handlebars.ValueResolver;

import java.lang.reflect.Method;

/**
 * Based on the {@link com.github.jknack.handlebars.context.JavaBeanValueResolver} by edgar.espina,
 * but using a non-cached version of {@link com.github.jknack.handlebars.context.MethodValueResolver}.
 * Further info: https://github.com/jknack/handlebars.java/issues/306
 */
public class NonCachedJavaBeanValueResolver extends NonCachedMethodValueResolver {

    /**
     * The 'is' prefix.
     */
    private static final String IS_PREFIX = "is";

    /**
     * The 'get' prefix.
     */
    private static final String GET_PREFIX = "get";

    /**
     * The default value resolver.
     */
    public static final ValueResolver INSTANCE = new NonCachedJavaBeanValueResolver();

    @Override
    public boolean matches(final Method method, final String name) {
        boolean isStatic = isStatic(method);
        boolean isPublic = isPublic(method);
        boolean isGet = method.getName().equals(javaBeanMethod(GET_PREFIX, name));
        boolean isBoolGet = method.getName().equals(javaBeanMethod(IS_PREFIX, name));
        int parameterCount = method.getParameterTypes().length;

        return !isStatic && isPublic && parameterCount == 0 && (isGet || isBoolGet);
    }

    /**
     * Convert the property's name to a JavaBean read method name.
     *
     * @param prefix The prefix: 'get' or 'is'.
     * @param name The unqualified property name.
     * @return The javaBean method name.
     */
    private static String javaBeanMethod(final String prefix,
                                         final String name) {
        StringBuilder buffer = new StringBuilder(prefix);
        buffer.append(name);
        buffer.setCharAt(prefix.length(), Character.toUpperCase(name.charAt(0)));
        return buffer.toString();
    }

    @Override
    protected String memberName(final Method member) {
        String name = member.getName();
        if (name.startsWith(GET_PREFIX)) {
            name = name.substring(GET_PREFIX.length());
        } else if (name.startsWith(IS_PREFIX)) {
            name = name.substring(IS_PREFIX.length());
        } else {
            return name;
        }
        if (name.length() > 0) {
            return Character.toLowerCase(name.charAt(0)) + name.substring(1);
        }
        return member.getName();
    }
}

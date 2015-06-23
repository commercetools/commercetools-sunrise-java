package common.templates;

import com.github.jknack.handlebars.ValueResolver;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Based on the {@link com.github.jknack.handlebars.context.MemberValueResolver} by edgar.espina,
 * but removing the internal cache used for saving {@link Member members}.
 * Further info: https://github.com/jknack/handlebars.java/issues/306
 */
public abstract class NonCachedMemberValueResolver<M extends Member> implements ValueResolver {

        @Override
        public final Object resolve(final Object context, final String name) {
            M member = find(context.getClass(), name);
            if (member == null) {
                // No luck, move to the next value resolver.
                return UNRESOLVED;
            }
            // Mark as accessible.
            if (member instanceof AccessibleObject) {
                ((AccessibleObject) member).setAccessible(true);
            }
            return invokeMember(member, context);
        }

        @Override
        public Object resolve(final Object context) {
            return UNRESOLVED;
        }

        /**
         * Find a {@link Member} in the given class.
         *
         * @param clazz The context's class.
         * @param name The attribute's name.
         * @return A {@link Member} or null.
         */
        protected final M find(final Class<?> clazz, final String name) {
            Set<M> members = members(clazz);
            for (M member : members) {
                if (matches(member, name)) {
                    return member;
                }
            }
            return null;
        }

        /**
         * Invoke the member in the given context.
         *
         * @param member The class member.
         * @param context The context object.
         * @return The resulting value.
         */
        protected abstract Object invokeMember(M member, Object context);

        /**
         * True, if the member matches the one we look for.
         *
         * @param member The class {@link Member}.
         * @param name The attribute's name.
         * @return True, if the member matches the one we look for.
         */
        public abstract boolean matches(M member, String name);

        /**
         * True if the member is public.
         *
         * @param member The member object.
         * @return True if the member is public.
         */
        protected boolean isPublic(final M member) {
            return Modifier.isPublic(member.getModifiers());
        }

        /**
         * True if the member is private.
         *
         * @param member The member object.
         * @return True if the member is private.
         */
        protected boolean isPrivate(final M member) {
            return Modifier.isPrivate(member.getModifiers());
        }

        /**
         * True if the member is protected.
         *
         * @param member The member object.
         * @return True if the member is protected.
         */
        protected boolean isProtected(final M member) {
            return Modifier.isProtected(member.getModifiers());
        }

        /**
         * True if the member is static.
         *
         * @param member The member object.
         * @return True if the member is statuc.
         */
        protected boolean isStatic(final M member) {
            return Modifier.isStatic(member.getModifiers());
        }

        /**
         * Creates a key using the context and the attribute's name.
         *
         * @param context The context object.
         * @param name The attribute's name.
         * @return A unique key from the given parameters.
         */
        private String key(final Object context, final String name) {
            return context.getClass().getName() + "#" + name;
        }

        /**
         * List all the possible members for the given class.
         *
         * @param clazz The base class.
         * @return All the possible members for the given class.
         */
        protected abstract Set<M> members(Class<?> clazz);

        @Override
        public Set<Map.Entry<String, Object>> propertySet(final Object context) {
            notNull(context, "The context is required.");
            if (context instanceof Map) {
                return Collections.emptySet();
            } else if (context instanceof Collection) {
                return Collections.emptySet();
            }
            Set<M> members = members(context.getClass());
            Map<String, Object> propertySet = new LinkedHashMap<String, Object>();
            for (M member : members) {
                String name = memberName(member);
                propertySet.put(name, resolve(context, name));
            }
            return propertySet.entrySet();
        }

        /**
         * Get the name for the given member.
         *
         * @param member A class member.
         * @return The member's name.
         */
        protected abstract String memberName(M member);
    }

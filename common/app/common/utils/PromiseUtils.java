package common.utils;

import play.libs.F;

import java.util.function.BiFunction;

public class PromiseUtils {

    public static <A, B, R> F.Promise<R> combine(final F.Promise<A> aPromise, final F.Promise<B> bPromise, final BiFunction<A, B, F.Promise<R>> function) {
        return aPromise.zip(bPromise).flatMap(tuple -> function.apply(tuple._1, tuple._2));
    }
}

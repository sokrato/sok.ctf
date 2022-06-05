package io.github.sokrato;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("io.github.sokrato.ctf.core"));
        IFn apply = Clojure.var("clojure.core", "apply");
        IFn main = Clojure.var("io.github.sokrato.ctf.core", "-main");
        apply.invoke(main, Arrays.asList(args));
    }
}

package io.github.sokrato;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Main m = new Main();
        m.crack(args);
        // m.jwt();
    }

    private void crack(String[] args) {
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("io.github.sokrato.ctf.core"));
        IFn apply = Clojure.var("clojure.core", "apply");
        IFn main = Clojure.var("io.github.sokrato.ctf.core", "-main");
        apply.invoke(main, Arrays.asList(args));
    }

    private void jwt() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", "admin");

        byte[] key = "iN2UwNmMtMjhmNi0xMWV".getBytes();
        //Strings.repeat("abcd", 8).getBytes();
        // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Imd1ZXN0In0.Eb3ePRN_yJsUX5zoQddxITZ14yS0LOfun7lul8dtDm0

        String sign = JWT.create()
                .withPayload(payload)
                .sign(Algorithm.HMAC256(key));
        System.out.println(sign);
    }
}

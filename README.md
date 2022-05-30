# CTF utilities

## build
```shell
clojure -Tdev uberjar
```

## run
```shell
java -jar target/sok.ctf-0.0.1-standalone.jar method=sha256 prefix=abc123 target=5c4a4e
# or
clojure -M:dev method=sha56 prefix=abc123 target=5c4a4e
```
package io.github.sokrato.util;

import java.util.Optional;

public interface Generator<T> {
    Optional<T> next();
}

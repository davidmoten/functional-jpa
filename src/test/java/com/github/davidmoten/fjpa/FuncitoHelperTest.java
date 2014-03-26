package com.github.davidmoten.fjpa;

import static com.github.davidmoten.fjpa.FuncitoHelper.c;
import static com.github.davidmoten.fjpa.FuncitoHelper.f;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Function;

public class FuncitoHelperTest {

    @Test
    public void test() {
        Function<Document, String> toId = f(c(Document.class).getId());
        assertEquals("a", toId.apply(new Document("a")));
    }

}

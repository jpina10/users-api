package com.users.api.util.guards;

import com.users.api.util.validator.Text;

public class TextGuard extends BaseGuard<Text> {

    public TextGuard(Text value) {
        super(value);
    }

    public void againstNullOrWhitespace(String message) {
        against(value::isNullOrWhitespace, message);
    }
}

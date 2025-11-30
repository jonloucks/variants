package io.github.jonloucks.variants.impl;

import static io.github.jonloucks.variants.api.Checks.textCheck;
import static java.lang.Character.isWhitespace;

final class TrimTextImpl {
    CharSequence trim() {
        if (length == 0) {
            return "";
        }
        
        int start = 0;
        int end = length - 1;
        
        while (start < length && isWhitespace(text.charAt(start))) {
            ++start;
        }
        
        while (end >= start && isWhitespace(text.charAt(end))) {
            --end;
        }
        
        if (start == 0 && end == length-1) {
            return text;
        }
        
        if (start > end) {
            return "";
        } else {
            return text.subSequence(start, end + 1);
        }  
    }
    
    TrimTextImpl(CharSequence text) {
        this.text = textCheck(text);
        this.length = text.length();
    }
    
    private final CharSequence text;
    private final int length;
}

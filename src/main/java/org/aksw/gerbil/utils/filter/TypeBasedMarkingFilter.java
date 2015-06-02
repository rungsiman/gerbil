/**
 * The MIT License
 * Copyright (c) 2014 Agile Knowledge Engineering and Semantic Web (AKSW) (usbeck@informatik.uni-leipzig.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.aksw.gerbil.utils.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.aksw.gerbil.transfer.nif.TypedMarking;

public class TypeBasedMarkingFilter<T extends TypedMarking> extends AbstractMarkingFilter<T> {

    private Set<String> types = new HashSet<String>();
    private boolean acceptTypes;

    public TypeBasedMarkingFilter(boolean acceptTypes, String... types) {
        this.types.addAll(Arrays.asList(types));
        this.acceptTypes = acceptTypes;
    }

    @Override
    public boolean isMarkingGood(T marking) {
        Set<String> types = marking.getTypes();
        if (acceptTypes) {
            for (String acceptedType : this.types) {
                if (types.contains(acceptedType)) {
                    return true;
                }
            }
            return false;
        } else {
            for (String notAcceptedType : this.types) {
                if (types.contains(notAcceptedType)) {
                    return false;
                }
            }
            return true;
        }
    }

}
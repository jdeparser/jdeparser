/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
/* Modified by Red Hat */

package org.jboss.deparser;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a wildcard type like "? super Foo".
 *
 * <p>
 * Instances of this class can be obtained from {@link JClass#wildcard()}
 *
 * <p>
 * Our modeling of types are starting to look really ugly.
 * ideally it should have been done somewhat like APT,
 * but it's too late now.
 *
 * @author Kohsuke Kawaguchi
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
final class JSuperWildcard extends JClass {

    private final JClass primaryBound;

    JSuperWildcard(final JClass primaryBound) {
        super(primaryBound.owner());
        this.primaryBound = primaryBound;
    }

    public String name() {
        return "? super " + primaryBound.name();
    }

    public String fullName() {
        return "? super " + primaryBound.fullName();
    }

    public JPackage _package() {
        return null;
    }

    public JClass _extends() {
        return owner().ref(Object.class);
    }

    public Iterator<JClass> _implements() {
        return Collections.<JClass>emptySet().iterator();
    }

    public boolean isInterface() {
        return false;
    }

    public boolean isAbstract() {
        return false;
    }

    protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
        JClass nb = primaryBound.substituteParams(variables,bindings);
        if(nb == primaryBound) {
            return this;
        } else {
            return new JSuperWildcard(nb);
        }
    }

    public void generate(JFormatter f) {
        f.p("? super ").g(primaryBound);
    }
}

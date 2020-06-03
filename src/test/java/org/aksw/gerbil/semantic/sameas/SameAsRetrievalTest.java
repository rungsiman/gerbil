/**
 * This file is part of General Entity Annotator Benchmark.
 *
 * General Entity Annotator Benchmark is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * General Entity Annotator Benchmark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with General Entity Annotator Benchmark.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.gerbil.semantic.sameas;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aksw.gerbil.semantic.sameas.impl.DomainBasedSameAsRetrieverManager;
import org.aksw.gerbil.semantic.sameas.impl.SimpleDomainExtractor;
import org.aksw.gerbil.semantic.sameas.impl.http.HTTPBasedSameAsRetriever;
import org.aksw.gerbil.test.SameAsRetrieverSingleton4Tests;
import org.aksw.gerbil.web.config.RootConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This class tests the same as retrieval as it is defined in the
 * {@link RootConfig} class.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
@RunWith(Parameterized.class)
public class SameAsRetrievalTest { 

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> testConfigs = new ArrayList<Object[]>();
        testConfigs.add(new Object[] { null, null });
        testConfigs.add(new Object[] { "http://localhost/notInWiki/Peter_Pan", null });
        testConfigs.add(new Object[] { "http://localhost/resource/Kaufland",
                Arrays.asList("http://fr.localhost/resource/Kaufland", "http://de.localhost/resource/Kaufland") });
        testConfigs.add(new Object[] { "http://localhost/resource/Malaysia",
                Arrays.asList("http://de.localhost/resource/Malaysia") });
        testConfigs.add(new Object[] { "http://localhost/resource/People's_Republic_of_China",
                Arrays.asList("http://localhost/resource/China") });
        testConfigs.add(new Object[] { "http://localhost/resource/Home_Depot",
                Arrays.asList("http://localhost/resource/The_Home_Depot") });
        testConfigs.add(new Object[] { "http://localhost/wiki/\"B\"_Movie",
                Arrays.asList("http://localhost/wiki/B_movie") });
        testConfigs.add(new Object[] { "http://en.localhost/resource/Berlin",
                Arrays.asList("http://localhost/resource/Berlin") });
        testConfigs.add(new Object[] { "http://localhost/resource/Gainesville,_Florida",
                Arrays.asList("http://localhost/resource/Gainesville%2C_Florida") });
        testConfigs.add(new Object[] { "http://localhost/resource/Gainesville%2C_Florida",
                Arrays.asList("http://localhost/resource/Gainesville,_Florida") });
        testConfigs.add(new Object[] { "http://localhost/resource/Richard_Taylor_(British_politician)",
                Arrays.asList("http://localhost/resource/Richard_Taylor_%28British_politician%29") });
        testConfigs.add(new Object[] { "http://localhost/resource/Richard_Taylor_%28British_politician%29",
                Arrays.asList("http://localhost/resource/Richard_Taylor_(British_politician)") });
        testConfigs.add(new Object[] { "http://localhost/resource/National_Public_Radio",
                Arrays.asList("http://localhost/resource/NPR") });
        testConfigs.add(new Object[] { "http://localhost/resource/NPR",
                Arrays.asList("http://localhost/resource/National_Public_Radio") });
        return testConfigs;
    }

    private String uri;
    private Set<String> expectedURIs;

    public SameAsRetrievalTest(String uri, Collection<String> expectedURIs) {
        this.uri = uri;
        if (expectedURIs != null) {
            this.expectedURIs = new HashSet<String>();
            this.expectedURIs.addAll(expectedURIs);
        }
    }

    @Test
    public void test() {
        SameAsRetriever retriever = SameAsRetrieverSingleton4Tests.getInstance();
        HTTPBasedSameAsRetriever sameAsRetrieverMock = mock(HTTPBasedSameAsRetriever.class);
        String domain = SimpleDomainExtractor.extractDomain(uri);
        when(sameAsRetrieverMock.retrieveSameURIs(domain, uri)).thenReturn(expectedURIs);
        //register a SameAsRetriever mock for the domain of the current uri
        setMock(retriever, sameAsRetrieverMock, domain);
        
        Set<String> uris = retriever.retrieveSameURIs(uri);
        if (expectedURIs == null) {
            Assert.assertNull(uris);
        } else {
            Assert.assertNotNull(uris);
            Assert.assertTrue(uris.toString() + " does not contain all of " + expectedURIs.toString(),
                    uris.containsAll(expectedURIs));
        }
    }

    protected void setMock(SameAsRetriever retriever, HTTPBasedSameAsRetriever mock, String domain) {
        if (retriever instanceof SameAsRetrieverDecorator) {
            setMock(((SameAsRetrieverDecorator) retriever).getDecorated(), mock, domain);
        }
        if (retriever instanceof DomainBasedSameAsRetrieverManager) {
            ((DomainBasedSameAsRetrieverManager) retriever).addDomainSpecificRetriever(domain, mock);
        }
    }
}

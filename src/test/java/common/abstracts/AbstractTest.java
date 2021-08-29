package common.abstracts;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.factory.TestFactory;

/**
 * Common abstract test class.
 * 
 * @author Sam Butler
 * @since August 28, 2021
 */
@Service
public abstract class AbstractTest {

    @Autowired
    private TestFactory testFactory;

    @Before
    public void setup() throws Exception {
        testFactory.initSchema();
    }
}

package com.channelsoft.cri.test;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

@SuppressWarnings("deprecation")
public class SpringTestCase extends AbstractDependencyInjectionSpringContextTests {
	private static final String DEFAULT_CONTEXT = "classpath*:applicationContext*.xml";

	private static final String DEFAULT_TEST_CONTEXT = "classpath*:applicationCon_test*.xml";

	/**
	 * 
	 * @see org.springframework.test.AbstractSingleSpringContextTests#getConfigLocations
	 *      ()
	 */
	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { DEFAULT_CONTEXT, DEFAULT_TEST_CONTEXT };
	}

}

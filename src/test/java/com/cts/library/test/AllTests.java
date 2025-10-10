package com.cts.library.test;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("junit-jupiter")
@SelectClasses({
    MemberServiceTest.class,
    BookServiceTest.class,
    NotificationServiceTest.class,
    BorrowingTransactionServiceTest.class,
    FineServiceTest.class
})
public class AllTests {
    
}

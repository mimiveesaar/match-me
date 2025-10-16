package tech.kood.match_me.profile.common;

import org.springframework.transaction.annotation.Transactional;
import tech.kood.match_me.common.TestBase;

@Transactional(transactionManager = "matchingTransactionManager")
public abstract class MatchingTestBase extends TestBase {
}
